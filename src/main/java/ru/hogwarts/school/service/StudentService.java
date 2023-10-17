package ru.hogwarts.school.service;

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

    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student deleteStudent(long id) {
        Student studentForDelete = studentRepository.findById(id).get();
        studentRepository.deleteById(id);
        return studentForDelete;
    }

    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getByAgeBetween(int min, int max) {
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long id) {
        return studentRepository.findById(id).get().getFaculty();
    }

    public Collection<Student> getByFacultyId(Long facultyId) {
        return studentRepository.findByFacultyId(facultyId);
    }

    public List<StudentByCategory> getStudentCount() {
        return studentRepository.getStudentByCount();
    }

    public List<StudentByCategory> getStudentAverageAge() {
        return studentRepository.getAverageStudentAge();
    }

    public List<StudentByCategory> getLastStudents() {
        return studentRepository.getLastStudents();
    }
}
