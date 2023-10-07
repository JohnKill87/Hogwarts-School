package ru.hogwarts.school.generator;

import ru.hogwarts.school.model.Faculty;

public class FacultyGenerator {

    public static final String NAME = "Илья";
    public static final String COLOR = "Пурпурный";

    public static Faculty getFaculty() {
        return new Faculty();
    }
}
