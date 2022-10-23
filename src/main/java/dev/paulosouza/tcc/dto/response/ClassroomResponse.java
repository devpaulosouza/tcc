package dev.paulosouza.tcc.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class ClassroomResponse {

    private String id;

    @JsonIgnore
    List<ClassroomTimeResponse> times;

    @JsonIgnore
    private String subjectId;

}
