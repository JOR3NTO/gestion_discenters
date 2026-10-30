package com.sistema.examenes.sistema_examenes_backend;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Pruebas de integración de la API REST contra H2 en memoria. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GestionApiTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    private long crear(String url, String cuerpo) throws Exception {
        String resp = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return json.readTree(resp).get("id").asLong();
    }

    private long salon(String nombre, int capacidad) throws Exception {
        return crear("/api/salones", "{\"nombre\":\"" + nombre + "\",\"capacidad\":" + capacidad + "}");
    }

    private long horario(String dia, String inicio, String fin) throws Exception {
        return crear("/api/horarios",
                "{\"diaSemana\":\"" + dia + "\",\"horaInicio\":\"" + inicio + "\",\"horaFin\":\"" + fin + "\"}");
    }

    private long estudiante(String matricula) throws Exception {
        return crear("/api/estudiantes", "{\"nombre\":\"Ana\",\"apellido\":\"Gil\",\"email\":\"" + matricula
                + "@mail.com\",\"matricula\":\"" + matricula + "\"}");
    }

    private long asignacion(long salonId, long horarioId) throws Exception {
        return crear("/api/asignaciones", "{\"salonId\":" + salonId + ",\"horarioId\":" + horarioId + "}");
    }

    @Test
    void estudianteDuplicadoYValidacion() throws Exception {
        estudiante("M1");
        mvc.perform(post("/api/estudiantes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"X\",\"apellido\":\"Y\",\"email\":\"otro@mail.com\",\"matricula\":\"M1\"}"))
                .andExpect(status().isConflict());
        mvc.perform(post("/api/estudiantes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"\",\"email\":\"malo\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalle.nombre").exists());
        mvc.perform(get("/api/estudiantes/99999")).andExpect(status().isNotFound());
    }

    @Test
    void horarioConRangoInvalidoSeRechaza() throws Exception {
        mvc.perform(post("/api/horarios").contentType(MediaType.APPLICATION_JSON)
                .content("{\"diaSemana\":\"LUNES\",\"horaInicio\":\"10:00\",\"horaFin\":\"09:00\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void salonNoAdmiseHorariosCruzados() throws Exception {
        long s = salon("A-101", 30);
        long h1 = horario("LUNES", "08:00", "10:00");
        long h2 = horario("LUNES", "09:00", "11:00");
        long h3 = horario("LUNES", "10:00", "12:00");
        asignacion(s, h1);
        mvc.perform(post("/api/asignaciones").contentType(MediaType.APPLICATION_JSON)
                .content("{\"salonId\":" + s + ",\"horarioId\":" + h2 + "}")).andExpect(status().isConflict());
        asignacion(s, h3); // 10:00-12:00 no se cruza con 08:00-10:00
    }

    @Test
    void inscripcionRespetaCapacidadYCruces() throws Exception {
        long s = salon("B-201", 1);
        long s2 = salon("B-202", 5);
        long h1 = horario("MARTES", "08:00", "10:00");
        long h2 = horario("MARTES", "09:00", "11:00");
        long a1 = asignacion(s, h1);
        long a2 = asignacion(s2, h2);
        long e1 = estudiante("E1");
        long e2 = estudiante("E2");

        mvc.perform(post("/api/asignaciones/" + a1 + "/estudiantes/" + e1)).andExpect(status().isOk())
                .andExpect(jsonPath("$.estudiantes", hasSize(1)));
        // repetido
        mvc.perform(post("/api/asignaciones/" + a1 + "/estudiantes/" + e1)).andExpect(status().isConflict());
        // cupo lleno
        mvc.perform(post("/api/asignaciones/" + a1 + "/estudiantes/" + e2)).andExpect(status().isConflict());
        // e1 ya tiene clase que se cruza
        mvc.perform(post("/api/asignaciones/" + a2 + "/estudiantes/" + e1)).andExpect(status().isConflict());
        mvc.perform(get("/api/asignaciones/estudiante/" + e1)).andExpect(jsonPath("$", hasSize(1)));

        mvc.perform(delete("/api/asignaciones/" + a1 + "/estudiantes/" + e1)).andExpect(status().isOk())
                .andExpect(jsonPath("$.estudiantes", hasSize(0)));
        mvc.perform(post("/api/asignaciones/" + a2 + "/estudiantes/" + e1)).andExpect(status().isOk());
    }

    @Test
    void quizSeCalificaYNoExponeRespuestasCorrectas() throws Exception {
        long est = estudiante("Q1");
        String quiz = """
                {"titulo":"Java básico","preguntas":[
                  {"enunciado":"2+2","opciones":[{"texto":"3"},{"texto":"4","correcta":true}]},
                  {"enunciado":"Capital de Colombia","opciones":[{"texto":"Bogotá","correcta":true},{"texto":"Cali"}]}
                ]}""";
        long quizId = crear("/api/quizzes", quiz);

        String detalle = mvc.perform(get("/api/quizzes/" + quizId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.preguntas", hasSize(2)))
                .andExpect(jsonPath("$.preguntas[0].opciones[0].correcta").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        // Se responde bien la primera y mal la segunda.
        JsonNode p = json.readTree(detalle).get("preguntas");
        long p1 = p.get(0).get("id").asLong();
        long p2 = p.get(1).get("id").asLong();
        long op1 = p.get(0).get("opciones").get(1).get("id").asLong(); // "4"
        long op2 = p.get(1).get("opciones").get(1).get("id").asLong(); // "Cali"

        mvc.perform(post("/api/quizzes/" + quizId + "/responder").contentType(MediaType.APPLICATION_JSON)
                .content("{\"estudianteId\":" + est + ",\"respuestas\":{\"" + p1 + "\":" + op1 + ",\"" + p2 + "\":"
                        + op2 + "}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correctas", is(1)))
                .andExpect(jsonPath("$.total", is(2)))
                .andExpect(jsonPath("$.puntaje", is(2.5)));

        mvc.perform(get("/api/quizzes/" + quizId + "/intentos")).andExpect(jsonPath("$", hasSize(1)));
        // con intentos ya no se puede eliminar
        mvc.perform(delete("/api/quizzes/" + quizId)).andExpect(status().isConflict());
    }

    @Test
    void quizConPreguntaSinRespuestaCorrectaSeRechaza() throws Exception {
        mvc.perform(post("/api/quizzes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"X\",\"preguntas\":[{\"enunciado\":\"?\",\"opciones\":[{\"texto\":\"a\"},{\"texto\":\"b\"}]}]}"))
                .andExpect(status().isConflict());
    }

    @Test
    void usuarioSeCreaConRolYSinExponerPassword() throws Exception {
        mvc.perform(get("/api/roles")).andExpect(jsonPath("$", hasSize(3)));
        String cuerpo = """
                {"username":"jorge","password":"secreto123","nombre":"Jorge","email":"j@mail.com","rolIds":[1]}""";
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.usuarioRoles[0].rol.nombre", is("ADMIN")))
                .andExpect(jsonPath("$.usuarioRoles[0].usuario").doesNotExist());
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isConflict());
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"x\",\"password\":\"123\",\"nombre\":\"X\",\"email\":\"x@mail.com\"}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.mensaje", not(nullValue())));
    }
}
