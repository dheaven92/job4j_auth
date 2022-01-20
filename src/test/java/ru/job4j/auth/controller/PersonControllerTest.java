package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.Job4jAuthApplication;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = Job4jAuthApplication.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @Test
    public void testFindAll() throws Exception {
        List<Person> people = List.of(
                new Person(1, "user1", "123"),
                new Person(2, "user2", "123")
        );
        when(personService.findAll()).thenReturn(people);
        mockMvc.perform(get("/person/"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(people)));
    }

    @Test
    public void testFindById() throws Exception {
        Person person = new Person(1, "user1", "123");
        when(personService.findById(1)).thenReturn(Optional.of(person));
        mockMvc.perform(get("/person/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(person)));
    }

    @Test
    public void testCreate() throws Exception {
        Person person = new Person(1, "user1", "123");
        when((personService.save(person))).thenReturn(person);
        String body = objectMapper.writeValueAsString(person);
        mockMvc.perform(post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().json(body));
    }

    @Test
    public void testUpdate() throws Exception {
        Person person = new Person(1, "user1", "123");
        when((personService.save(person))).thenReturn(person);
        mockMvc.perform(put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/person/1"))
                .andExpect(status().isOk());
    }
}