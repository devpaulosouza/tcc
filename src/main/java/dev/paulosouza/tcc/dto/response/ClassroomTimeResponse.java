package dev.paulosouza.tcc.dto.response;

import lombok.Data;

import java.time.DayOfWeek;

@Data
public class ClassroomTimeResponse {

    private DayOfWeek dayOfWeek;

    private int hour;

}
