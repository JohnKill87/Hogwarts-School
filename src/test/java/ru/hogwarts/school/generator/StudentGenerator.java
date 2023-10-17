package ru.hogwarts.school.generator;

import ru.hogwarts.school.model.Student;

public class StudentGenerator {

    public static final String NAME = "Илья";
    public static final int AGE = 16;

    public static Student getStudent() {
        return new Student(NAME, AGE);
    }
}
