package edu.midterm;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc
class StudentApiTest {
 @Autowired MockMvc mvc; @Autowired StudentRepository repo; @Autowired ObjectMapper json;
 @BeforeEach void clean() { repo.deleteAll(); }
 String body(String name,String email) { return "{\"name\":\""+name+"\",\"email\":\""+email+"\",\"course\":\"DevOps\"}"; }
 @Test void crudLifecycle() throws Exception {
  String content=mvc.perform(post("/api/students").contentType("application/json").content(body("Test Student","test@example.org"))).andExpect(status().isCreated()).andExpect(header().exists("Location")).andReturn().getResponse().getContentAsString();
  long id=json.readTree(content).get("id").asLong();
  mvc.perform(get("/api/students")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
  mvc.perform(get("/api/students/"+id)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Test Student"));
  mvc.perform(put("/api/students/"+id).contentType("application/json").content(body("Updated Student","updated@example.org"))).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Updated Student"));
  mvc.perform(delete("/api/students/"+id)).andExpect(status().isNoContent());
  mvc.perform(get("/api/students/"+id)).andExpect(status().isNotFound());
 }
 @Test void invalidInput() throws Exception {
  mvc.perform(post("/api/students").contentType("application/json").content(body("","invalid"))).andExpect(status().isBadRequest());
 }
 @Test void duplicateEmail() throws Exception {
  mvc.perform(post("/api/students").contentType("application/json").content(body("First","same@example.org"))).andExpect(status().isCreated());
  mvc.perform(post("/api/students").contentType("application/json").content(body("Second","same@example.org"))).andExpect(status().isConflict());
 }
 @Test void missingUpdateAndDelete() throws Exception {
  mvc.perform(put("/api/students/999999").contentType("application/json").content(body("Test","test@example.org"))).andExpect(status().isNotFound());
  mvc.perform(delete("/api/students/999999")).andExpect(status().isNotFound());
 }
 @Test void nameFilter() throws Exception {
  mvc.perform(post("/api/students").contentType("application/json").content(body("Tajibayev Assylbek","author@example.org"))).andExpect(status().isCreated());
  mvc.perform(get("/api/students").param("name","TAJIBAYEV")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
  mvc.perform(get("/api/students").param("name","absent")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
 }
 @Test void controlledFailure() { Assertions.assertFalse(Boolean.getBoolean("midterm.fail"), "Controlled failure demonstration"); }
}
