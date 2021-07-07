package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.HashMap;
import java.util.Map;

public enum RecipeCourseUseCaseFailReason
        implements
        FailReasons {
    NO_COURSE_SELECTED(200);

    private final int id;
    @SuppressLint("UseSparseArrays")
    private static Map<Integer, RecipeCourseUseCaseFailReason> failReasonMap = new HashMap<>();

    RecipeCourseUseCaseFailReason(int id) {
        this.id = id;
    }

    static {
        for (RecipeCourseUseCaseFailReason failReason : RecipeCourseUseCaseFailReason.values()) {
            failReasonMap.put(failReason.id, failReason);
        }
    }

    public static FailReasons fromId(int failReasonId) {
        return failReasonMap.get(failReasonId);
    }

    public int getId() {
        return id;
    }
}
