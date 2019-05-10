package com.example.peter.thekitchenmenu.utils;

import java.util.GregorianCalendar;

/**
 * Calculates dates and days
 * Attribution: Udacity - Intro to Java - Lesson 7:4 Fundamental data types: Section 14.
 *
 * Example of use:
 * // To find day of week from a given date, first give the date to find.
 * DateCalculator dayOfWeekToFind = new DateCalculator(1983, 6, 18);
 * // Then provide a reference date.
 * DateCalculator referenceDay = new DateCalculator(1900, 1, 1);
 * int referenceWeekDay = 1;
 * int daysBetween = dayOfWeekToFind.daysFrom(referenceDay);
 * int uncorrectedWeekDay = referenceWeekDay + daysBetween;
 * int weekDay = uncorrectedWeekDay % 7; // % provides the remainder
 *
 * System.out.print("Weekday: ");
 * System.out.println(weekDay);
 *
 */
public class DateCalculator {

    private int year;
    private int month;
    private int date;

    private static final int[] DAYS_PER_MONTH
            = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static final int GREGORIAN_START_YEAR = 1582;
    private static final int GREGORIAN_START_MONTH = 10;
    private static final int GREGORIAN_START_DAY = 15;
    private static final int JULIAN_END_DAY = 4;

    private static final int JANUARY = 1;
    private static final int FEBRUARY = 2;
    private static final int DECEMBER = 12;

    /**
     Constructs a day object representing today's date.
     */
    public DateCalculator() {

        GregorianCalendar today = new GregorianCalendar();

        year = today.get(GregorianCalendar.YEAR);
        month = today.get(GregorianCalendar.MONTH) + 1;
        date = today.get(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
     Constructs a day with a given year, month, and day
     of the Julian/Gregorian calendar. The Julian calendar
     is used for all days before October 15, 1582
     @param year a year (any number other than 0)
     @param month a month between 1 and 12
     @param dayOfMonth a day of the month between 1 and 31
     */
    public DateCalculator( int year, int month, int dayOfMonth) {

        this.year = year;
        this.month = month;
        this.date = dayOfMonth;
    }

    /**
     Returns the year of this day.
     @return the year
     */
    public int getYear () {
        return year;
    }

    /**
     Returns the month of this day.
     @return the month
     */
    public int getMonth () {
        return month;
    }

    /**
     Returns the day of the month of this day.
     @return the day of the month
     */
    public int getDayOfMonth () {
        return date;
    }

    /**
     Changes to a date that is a certain number of days away from
     the current date.
     @param numberOfDays the number of days, can be negative
     */
    public void addDays ( int numberOfDays) {

        while (numberOfDays > 0) {

            nextDay();
            numberOfDays--;
        }

        while (numberOfDays < 0) {

            previousDay();
            numberOfDays++;
        }
    }

    /**
     Returns the number of days between this day and another
     day.
     @param other the other day
     @return the number of days that this day is away from
     the other (>0 if this day comes later than <code>other</code>)
     */
    public int daysFrom (DateCalculator other) {

        int n = 0;
        DateCalculator d = new DateCalculator(year, month, date);

        while (d.compareTo(other) > 0) {
            d.previousDay();
            n++;
        }

        while (d.compareTo(other) < 0) {
            d.nextDay();
            n--;
        }

        return n;
    }

    public String toString () {
        return String.format("%04d-%02d-%02d", year, month, date);
    }

    /**
     Compares this day with another day.
     @param other the other day
     @return a positive number if this day comes after the
     other day, a negative number if this day comes before
     the other day, and zero if the days are the same
     */
    private int compareTo (DateCalculator other) {

        if (year > other.year) return 1;
        if (year < other.year) return -1;
        if (month > other.month) return 1;
        if (month < other.month) return -1;

        return date - other.date;
    }

    /**
     Advances to the next day.
     */
    private void nextDay () {

        if (year == GREGORIAN_START_YEAR && month == GREGORIAN_START_MONTH && date == JULIAN_END_DAY)

            date = GREGORIAN_START_DAY;

        else if (date < daysPerMonth(year, month))

            date++;

        else {

            date = 1;
            month++;

            if (month > DECEMBER) {

                month = JANUARY;
                year++;
                if (year == 0) year++;
            }
        }
    }

    /**
     Moves to the previous day.
     */
    private void previousDay () {

        if (year == GREGORIAN_START_YEAR && month == GREGORIAN_START_MONTH && date == GREGORIAN_START_DAY)

            date = JULIAN_END_DAY;

        else if (date > 1)

            date--;

        else {

            month--;

            if (month < JANUARY) {

                month = DECEMBER;
                year--;
                if (year == 0) year--;
            }

            date = daysPerMonth(year, month);
        }
    }

    /**
     Gets the days in a given month.
     @param y the year
     @param m the month
     @return the last day in the given month
     */
    private static int daysPerMonth (int y, int m) {

        int days = DAYS_PER_MONTH[m - 1];

        if (m == FEBRUARY && isLeapYear(y))

            days++;

        return days;
    }

    /**
     Tests if a year is a leap year.
     @param y the year
     @return true if y is a leap year
     */
    private static boolean isLeapYear (int y) {

        if (y % 4 != 0) return false;

        if (y < GREGORIAN_START_YEAR) return true;

        return (y % 100 != 0) || (y % 400 == 0);
    }
}