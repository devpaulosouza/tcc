package dev.paulosouza.tcc.repository;

import dev.paulosouza.tcc.model.Classroom;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends Neo4jRepository<Classroom, String> {

}
