package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration;

public final class RecipeDurationTimeHelper {
    static int getHours(int totalTime) {
        return totalTime / 60;
    }

    static int getMinutes(int totalTime) {
        return totalTime % 60;
    }

    static int getTotalMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }
}
