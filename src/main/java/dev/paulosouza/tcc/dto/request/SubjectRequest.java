package dev.paulosouza.tcc.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SubjectRequest {

    private String id;

    private int period;

    private String name;

    private List<String> preRequisiteIds;

    private List<String> coRequisiteIds;

}
