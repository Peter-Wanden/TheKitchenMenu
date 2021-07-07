package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public enum Course {
    COURSE_ZERO(0),
    COURSE_ONE(1),
    COURSE_TWO(2),
    COURSE_THREE(3),
    COURSE_FOUR(4),
    COURSE_FIVE(5),
    COURSE_SIX(6),
    COURSE_SEVEN(7),
    COURSE_EIGHT(8);

    private final int id;
    @SuppressLint("UseSparseArrays")
    private static Map<Integer, Course> courseMap = new HashMap<>();

    Course(int id) {
        this.id = id;
    }

    static {
        for (Course course : Course.values()) courseMap.put(course.id, course);
    }

    public static Course fromId(int courseId) {
        return courseMap.get(courseId);
    }

    public int getId() {
        return id;
    }
}
