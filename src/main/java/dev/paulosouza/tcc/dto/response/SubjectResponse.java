package dev.paulosouza.tcc.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SubjectResponse {

    private String id;

    private int period;

    private String name;

    private List<SubjectResponse> preRequisites;

    private List<SubjectResponse> coRequisites;
}
