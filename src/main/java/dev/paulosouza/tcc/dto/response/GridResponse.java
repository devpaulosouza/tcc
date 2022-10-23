package dev.paulosouza.tcc.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GridResponse {

    private List<String> pendingSubjects;

    private int pendingSubjectsCount;

    private Map<Integer, List<ClassroomResponse>> grid;

}
