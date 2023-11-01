package ru.hogwarts.school.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.controller.StudentControllerWebMvcTest;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private StudentControllerWebMvcTest studentController;

    @Test
    public void postFacultyTest() throws Exception {

        final long id = 3;
        final String name = "Gryffindor";
        final String color = "Red";

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void testFindFaculty() throws Exception {

            final long id = 3L;
            final String name = "Gryffindor";
            final String color = "Red";

            Faculty faculty = new Faculty();
            faculty.setId(id);
            faculty.setName(name);
            faculty.setColor(color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/faculty/by-id")
                                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(faculty.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(faculty.getColor()));

        verify(facultyRepository).findById(id);
        verifyNoMoreInteractions(facultyRepository);
    }

    @Test
    public void testFindFacultyByColor() throws Exception {

        final String color = "Red";

        Faculty faculty1 = new Faculty("Gryffindor", "Red");
        Faculty faculty2 = new Faculty("Ravenclaw", "Blue");

        List<Faculty> faculties = Arrays.asList(faculty1, faculty2);

        when(facultyRepository.findByColorIgnoreCase(color)).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/by-color")
                        .param("color", String.valueOf(color)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(faculties.size()));
    }

    @Test
    public void testFindFacultyByNameOrColor() throws Exception {

        final String color = "Red";
        final String name = "Gryffindor";

        Faculty faculty1 = new Faculty("Gryffindor", "Red");
        Faculty faculty2 = new Faculty("Ravenclaw", "Blue");

        List<Faculty> faculties = Arrays.asList(faculty1, faculty2);

        when(facultyRepository.findByColorIgnoreCase(color)).thenReturn(faculties);
        when(facultyRepository.findByNameIgnoreCase(name)).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/by-name-or-color")
                        .param("name", String.valueOf(name))
                        .param("color", String.valueOf(color)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(faculties.size()));

        verify(facultyRepository).findByNameIgnoreCase(name);
        verify(facultyRepository).findByColorIgnoreCase(color);
        verifyNoMoreInteractions(facultyRepository);
    }

    @Test
    void testFindStudentsByFacultyId() throws Exception {

        final long id = 5;

        List<Student> list = new ArrayList<>(List.of(
                new Student("name1", 20),
                new Student("name2", 25),
                new Student("name3", 20)
        ));

        when(studentRepository.findByFacultyId(id)).thenReturn(list);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/students-by-faculty-id")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andReturn();

        List<Student> actualStudents = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Student>>() {
        });
        assertEquals(list, actualStudents);
    }

    @Test
    void testDeleteFaculty() throws Exception {
        long id = 1l;

        Faculty faculty = new Faculty("name1", "red");
        faculty.setId(id);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        doNothing().when(facultyRepository).deleteById(id);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/faculty")
                                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(faculty.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(faculty.getColor()));

        verify(facultyRepository).findById(id);
        verify(facultyRepository).deleteById(id);
        verifyNoMoreInteractions(facultyRepository);
    }

    @Test
    public void putFacultyTest() throws Exception {

        final long id = 3;
        final String name = "Gryffindor";
        final String color = "Red";

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }
}
