package dev.paulosouza.tcc.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node("Subject")
@Data
public class Subject {

    @Id
    private String id;

    @Property("period")
    private int period;

    @Property("name")
    private String name;

    @Relationship(type = "PRE_REQUISITE", direction = Relationship.Direction.INCOMING)
    private List<Subject> preRequisites;

    @Relationship(type = "CO_REQUISITE", direction = Relationship.Direction.OUTGOING)
    private List<Subject> coRequisites;

    private int color;

    private List<Integer> colors;

    public int getDegree() {
        return this.preRequisites.size();
    }

    public Subject clone() {
        Subject subject = new Subject();

        subject.setId(this.id);
        subject.setName(this.name);
        subject.setPreRequisites(this.preRequisites);
        subject.setCoRequisites(this.coRequisites);
        subject.setPeriod(this.period);

        return subject;
    }

}
