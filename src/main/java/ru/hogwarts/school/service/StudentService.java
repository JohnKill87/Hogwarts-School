package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.StudentByCategory;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.debug("Creating student");
        logger.debug("Student created");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.debug("Finding student");
        logger.debug("Student found");
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        logger.debug("Editing student");
        logger.debug("Student edited");
        return studentRepository.save(student);
    }

    public Student deleteStudent(long id) {
        logger.debug("Deleting student");
        Student studentForDelete = studentRepository.findById(id).get();
        studentRepository.deleteById(id);
        logger.debug("Student deleted");
        return studentForDelete;
    }

    public Collection<Student> findByAge(int age) {
        logger.debug("Finding student by age");
        logger.debug("Student by age found");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getByAgeBetween(int min, int max) {
        logger.debug("Finding student by age between");
        logger.debug("Student by age between found");
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long id) {
        logger.debug("Finding faculty by student id");
        logger.debug("Faculty by student id found");
        return studentRepository.findById(id).get().getFaculty();
    }

    public Collection<Student> getByFacultyId(Long facultyId) {
        logger.debug("Finding student by faculty id");
        logger.debug("Student by faculty id found");
        return studentRepository.findByFacultyId(facultyId);
    }

    public List<StudentByCategory> getStudentCount() {
        logger.debug("Finding student count");
        logger.debug("Student count found");
        return studentRepository.getStudentByCount();
    }

    public List<StudentByCategory> getStudentAverageAge() {
        logger.debug("Finding student average age");
        logger.debug("Student average age found");
        return studentRepository.getAverageStudentAge();
    }

    public List<StudentByCategory> getLastStudents() {
        logger.debug("Finding last students");
        logger.debug("Last students found");
        return studentRepository.getLastStudents();
    }
}
