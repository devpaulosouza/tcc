package dev.paulosouza.tcc.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GridRequest {

    private int period = 0;

    private List<String> pendingSubjectIds = new ArrayList<>();

    private List<String> doneSubjectIds = new ArrayList<>();

}
