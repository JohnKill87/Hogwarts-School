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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    private volatile Integer count = 0;

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

    public List<String> getStudentsStartingFromA() {
        logger.debug("Finding students starting from A");
        logger.debug("Students starting from A found");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.toUpperCase().startsWith("А"))
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getStudentAverageAgeWithStream() {
        logger.debug("Finding student average age with stream");
        logger.debug("Student average age with stream found");
        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(Double.NaN);
    }

    public void getAllStudentsWithThread() {
        List<Student> studentList = studentRepository.findAll();

        System.out.println(studentList.get(0).getName());
        System.out.println(studentList.get(1).getName());

        new Thread(() -> {
            System.out.println(studentList.get(2).getName());
            System.out.println(studentList.get(3).getName());
        }).start();

        new Thread(() -> {
            System.out.println(studentList.get(4).getName());
            System.out.println(studentList.get(5).getName());
        }).start();
    }

    private synchronized void synchronizedStudentName(List<Student> studentList) {
        System.out.println(studentList.get(count).getName());
        count++;
    }

    public void getAllStudentsWithSynchronizedThread() {
        List<Student> studentList = studentRepository.findAll();

        synchronizedStudentName(studentList);
        synchronizedStudentName(studentList);

        new Thread(() -> {
            synchronizedStudentName(studentList);
            synchronizedStudentName(studentList);
        }).start();

        new Thread(() -> {
            synchronizedStudentName(studentList);
            synchronizedStudentName(studentList);
        }).start();
    }
}
