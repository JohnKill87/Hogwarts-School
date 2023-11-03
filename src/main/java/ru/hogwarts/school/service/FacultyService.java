package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Service
public class FacultyService {

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;
    private final StudentService studentService;

    public FacultyService(FacultyRepository facultyRepository, StudentService studentService) {
        this.facultyRepository = facultyRepository;
        this.studentService = studentService;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.debug("Creating faculty");
        logger.debug("Faculty created");
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long id) {
        logger.debug("Finding faculty");
        logger.debug("Faculty found");
        return facultyRepository.findById(id).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.debug("Editing faculty");
        logger.debug("faculty edited");
        return facultyRepository.save(faculty);
    }

    public Faculty deleteFaculty(long id) {
        logger.debug("Deleting faculty");
        Faculty facultyForDelete = facultyRepository.findById(id).get();
        facultyRepository.deleteById(id);
        logger.debug("faculty deleted");
        return facultyForDelete;
    }

    public Collection<Faculty> getFacultyByColor(String color) {
        logger.debug("Finding faculty by color");
        logger.debug("faculty by color found");
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Set<Faculty> getFacultyByColorOrName(String param) {
        logger.debug("Finding faculty by color or name");
        Set<Faculty> result = new HashSet<>();
        result.addAll(facultyRepository.findByColorIgnoreCase(param));
        result.addAll(facultyRepository.findByNameIgnoreCase(param));
        logger.debug("faculty by color or name found");
        return result;
    }

    public Collection<Student> getStudentsByFacultyId(Long id) {
        logger.debug("Finding students by faculty id");
        logger.debug("Students by faculty id found");
        return studentService.getByFacultyId(id);
    }

    public String getLongNameOfTheFaculty() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .toString();
    }
//     .max(Comparator.comparingInt(String::length))
}
