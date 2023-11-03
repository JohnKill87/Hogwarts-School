package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.StudentByCategory;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("/by-id")
    public Student getStudent(@RequestParam Long id) {
        return studentService.findStudent(id);
    }

    @PutMapping
    public Student editStudent(@RequestBody Student student) {
        return studentService.editStudent(student);
    }

    @DeleteMapping
    public Student deleteStudent(@RequestParam Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/by-age")
    public Collection<Student> getStudentByAge(@RequestParam int age) {
        return studentService.findByAge(age);
    }

    @GetMapping("by-age-between")
    public Collection<Student> findStudentByAgeBetween(@RequestParam int min,
                                                       @RequestParam int max) {
        return studentService.getByAgeBetween(min, max);
    }

    @GetMapping("/faculty-by-student-id")
    public Faculty getFacultyByStudentId(@RequestParam Long id) {
        return studentService.getFacultyByStudentId(id);
    }

    @GetMapping("/student-Count")
    public List<StudentByCategory> getStudentCount() {
        return studentService.getStudentCount();
    }

    @GetMapping("/student-Average-Age")
    public List<StudentByCategory> getStudentAverageAge() {
        return studentService.getStudentAverageAge();
    }

    @GetMapping("/last-Students")
    public List<StudentByCategory> getLastStudents() {
        return studentService.getLastStudents();
    }

    @GetMapping("/students-Starting-From-A")
    public List<String> getStudentsStartingFromA() {
        return studentService.getStudentsStartingFromA();
    }

    @GetMapping("/student-Average-Age-With-Stream")
    public Double getStudentAverageAgeWithStream() {
        return studentService.getStudentAverageAgeWithStream();
    }

    @GetMapping("/fast-Integer-Value")
    public Integer getFastIntegerValue() {
        int sum;
        List<Integer> listOfInt = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .collect(Collectors.toList());
        sum = listOfInt.stream()
                .parallel()
                .mapToInt(a -> a)
                .sum();
        return sum;
//        Результат: 237 ms
    }

    @GetMapping("/all-Students-With-Thread")
    public void getAllStudentsWithThread() {
        studentService.getAllStudentsWithThread();
    }

    @GetMapping("/all-Students-With-Synchronized-Thread")
    public void getAllStudentsWithSynchronizedThread() {
        studentService.getAllStudentsWithSynchronizedThread();
    }
}
