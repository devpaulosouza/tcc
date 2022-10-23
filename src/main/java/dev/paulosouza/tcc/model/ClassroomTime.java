package dev.paulosouza.tcc.model;

import lombok.Getter;

import java.time.DayOfWeek;

@Getter
public enum ClassroomTime {
    MONDAY_1(DayOfWeek.MONDAY, 1),
    MONDAY_2(DayOfWeek.MONDAY, 2),
    MONDAY_3(DayOfWeek.MONDAY, 3),
    MONDAY_4(DayOfWeek.MONDAY, 4),
    MONDAY_5(DayOfWeek.MONDAY, 5),
    MONDAY_6(DayOfWeek.MONDAY, 6),
    MONDAY_7(DayOfWeek.MONDAY, 7),
    MONDAY_8(DayOfWeek.MONDAY, 8),
    MONDAY_9(DayOfWeek.MONDAY, 9),
    TUESDAY_1(DayOfWeek.TUESDAY, 1),
    TUESDAY_2(DayOfWeek.TUESDAY, 2),
    TUESDAY_3(DayOfWeek.TUESDAY, 3),
    TUESDAY_4(DayOfWeek.TUESDAY, 4),
    TUESDAY_5(DayOfWeek.TUESDAY, 5),
    TUESDAY_6(DayOfWeek.TUESDAY, 6),
    TUESDAY_7(DayOfWeek.TUESDAY, 7),
    TUESDAY_8(DayOfWeek.TUESDAY, 8),
    TUESDAY_9(DayOfWeek.TUESDAY, 9),
    WEDNESDAY_1(DayOfWeek.WEDNESDAY, 1),
    WEDNESDAY_2(DayOfWeek.WEDNESDAY, 2),
    WEDNESDAY_3(DayOfWeek.WEDNESDAY, 3),
    WEDNESDAY_4(DayOfWeek.WEDNESDAY, 4),
    WEDNESDAY_5(DayOfWeek.WEDNESDAY, 5),
    WEDNESDAY_6(DayOfWeek.WEDNESDAY, 6),
    WEDNESDAY_7(DayOfWeek.WEDNESDAY, 7),
    WEDNESDAY_8(DayOfWeek.WEDNESDAY, 8),
    WEDNESDAY_9(DayOfWeek.WEDNESDAY, 9),
    THURSDAY_1(DayOfWeek.THURSDAY, 1),
    THURSDAY_2(DayOfWeek.THURSDAY, 2),
    THURSDAY_3(DayOfWeek.THURSDAY, 3),
    THURSDAY_4(DayOfWeek.THURSDAY, 4),
    THURSDAY_5(DayOfWeek.THURSDAY, 5),
    THURSDAY_6(DayOfWeek.THURSDAY, 6),
    THURSDAY_7(DayOfWeek.THURSDAY, 7),
    THURSDAY_8(DayOfWeek.THURSDAY, 8),
    THURSDAY_9(DayOfWeek.THURSDAY, 9),
    FRIDAY_1(DayOfWeek.FRIDAY, 1),
    FRIDAY_2(DayOfWeek.FRIDAY, 2),
    FRIDAY_3(DayOfWeek.FRIDAY, 3),
    FRIDAY_4(DayOfWeek.FRIDAY, 4),
    FRIDAY_5(DayOfWeek.FRIDAY, 5),
    FRIDAY_6(DayOfWeek.FRIDAY, 6),
    FRIDAY_7(DayOfWeek.FRIDAY, 7),
    FRIDAY_8(DayOfWeek.FRIDAY, 8),
    FRIDAY_9(DayOfWeek.FRIDAY, 9),
    SATURDAY_1(DayOfWeek.SATURDAY, 1),
    SATURDAY_2(DayOfWeek.SATURDAY, 2),
    SATURDAY_3(DayOfWeek.SATURDAY, 3),
    SATURDAY_4(DayOfWeek.SATURDAY, 4),
    SATURDAY_5(DayOfWeek.SATURDAY, 5),
    SATURDAY_6(DayOfWeek.SATURDAY, 6),
    SATURDAY_7(DayOfWeek.SATURDAY, 7),
    SATURDAY_8(DayOfWeek.SATURDAY, 8),
    SATURDAY_9(DayOfWeek.SATURDAY, 9);

    private final DayOfWeek dayOfWeek;
    private final int hour;
    ClassroomTime(DayOfWeek dayOfWeek, int hour) {
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
    }
}
