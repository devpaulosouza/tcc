package dev.paulosouza.tcc.dto.request;

import dev.paulosouza.tcc.model.ClassroomTime;
import lombok.Data;

import java.util.List;

@Data
public class ClassroomRequest {

    private String id;

    private List<ClassroomTime> times;

    private String subjectId;

}
