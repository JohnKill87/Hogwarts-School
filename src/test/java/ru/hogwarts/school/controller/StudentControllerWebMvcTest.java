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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

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
    public void postStudentTest() throws Exception {

        final String name = "Gaben";
        final int age = 3;
        final long id = 3;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void testAgeFilter() throws Exception {

        final int ageOne = 18;
        final int ageTwo = 20;
        final int ageThree = 25;

        List<Student> list = new ArrayList<>(List.of(
                new Student("name1", ageOne),
                new Student("name2", ageTwo),
                new Student("name3", ageThree)
        ));

        when(studentRepository.findAllByAgeBetween(ageOne, ageThree)).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/by-age-between")
                        .param("min", String.valueOf(ageOne))
                        .param("max", String.valueOf(ageThree)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(list.size()));

        verify(studentRepository).findAllByAgeBetween(ageOne, ageThree);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void testFindStudent() throws Exception {

        final long id = 3L;
        final String name = "Гарри Поттер";
        final int age = 18;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/student/by-id")
                                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(student.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(student.getAge()));

        verify(studentRepository).findById(id);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void testFindFacultyByStudentId() throws Exception {

        final long id = 3;

        List<Faculty> list = new ArrayList<>(List.of(
                new Faculty("name1", "red"),
                new Faculty("name2", "blue"),
                new Faculty("name3", "green")
        ));

        when(facultyRepository.findByStudentId(id)).thenReturn(list);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/faculty-by-student-id")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andReturn();

        List<Faculty> actualFaculties = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Faculty>>() {
        });
        assertEquals(list, actualFaculties);
    }

    @Test
    void testDeleteStudent() throws Exception {
        long id = 1l;

        Student studentWithId = new Student("name1", 20);
        studentWithId.setId(id);

        when(studentRepository.findById(id)).thenReturn(Optional.of(studentWithId));
        doNothing().when(studentRepository).deleteById(id);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/student")
                                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(studentWithId.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(studentWithId.getAge()));

        verify(studentRepository).findById(id);
        verify(studentRepository).deleteById(id);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    public void putStudentTest() throws Exception {

        final String name = "Gaben";
        final int age = 3;
        final long id = 3;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }
}
