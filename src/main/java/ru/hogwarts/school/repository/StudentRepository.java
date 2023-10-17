package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.StudentByCategory;

import java.util.Collection;
import java.util.List;

import static org.hibernate.loader.Loader.SELECT;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAge (int age);

    Collection<Student> findAllByAgeBetween(int min, int max);

    Collection<Student> findByFacultyId(Long facultyId);

    @Query(value = "SELECT COUNT(*) as amountOfStudents FROM student", nativeQuery = true)
    List<StudentByCategory> getStudentByCount();

    @Query(value = "SELECT AVG(age) as amountOfStudents FROM student", nativeQuery = true)
    List<StudentByCategory> getAverageStudentAge();

    @Query(value = "SELECT COUNT(*) as amountOfStudents FROM student  HAVING COUNT(*) > 2", nativeQuery = true)
    List<StudentByCategory> getLastStudents();
}
