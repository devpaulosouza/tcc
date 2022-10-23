package dev.paulosouza.tcc.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

@Node("Classroom")
@Data
public class Classroom {

    @Id
    private String id;

    @Property("time")
    private List<ClassroomTime> times;

    @Property("subjectId")
    private String subjectId;

}
