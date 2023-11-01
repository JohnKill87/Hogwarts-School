package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.generator.FacultyGenerator;
import ru.hogwarts.school.model.Faculty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.hogwarts.school.generator.FacultyGenerator.*;

class FacultyServiceTest {

    private FacultyService facultyService;

    @Test
    void addFaculty_success() {

//        Входные данные.

        Faculty faculty = new Faculty(NAME, COLOR);

//        Ожидаемый результат.

        Faculty expected = FacultyGenerator.getFaculty();

//        Тест.

        Faculty actual = facultyService.addFaculty(faculty);
        assertEquals(expected, actual);
    }

    @Test
    void getFaculty_success() {

//        Входные данные.

        Faculty faculty = new Faculty(NAME, COLOR);
        Long id = 1L;

//        Ожидаемый результат.

        facultyService.addFaculty(faculty);
        Faculty expected = FacultyGenerator.getFaculty();

//        Тест.

        Faculty actual = facultyService.getFaculty(id);
        assertEquals(expected, actual);
    }

    @Test
    void editFaculty_success() {

//        Входные данные.

        Faculty faculty = new Faculty(NAME, COLOR);

//        Ожидаемый результат.

        Faculty expected = null;

//        Тест.

        Faculty actual = facultyService.editFaculty(faculty);
        assertEquals(expected, actual);
    }

    @Test
    void deleteFaculty_success() {

//        Входные данные.

        Long id = 1L;

//        Тест.

        assertThrows(RuntimeException.class, () -> facultyService.deleteFaculty(id));

    }

    @Test
    void getFacultyByColor_success() {

//        Входные данные.

        Faculty faculty = new Faculty(NAME, COLOR);
        String color = COLOR;

//        Ожидаемый результат.

        facultyService.addFaculty(faculty);
        ArrayList<Faculty> expected = new ArrayList<>(List.of(FacultyGenerator.getFaculty()));

//        Тест.

        Collection<Faculty> actual = facultyService.getFacultyByColor(color);
        assertEquals(expected, actual);
    }
}