package dev.paulosouza.tcc.repository;

import dev.paulosouza.tcc.model.Classroom;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends Neo4jRepository<Classroom, String> {

    List<Classroom> findBySubjectId(String id);

}
