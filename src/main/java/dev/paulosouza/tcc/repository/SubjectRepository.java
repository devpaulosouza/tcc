package dev.paulosouza.tcc.repository;

import dev.paulosouza.tcc.model.Subject;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends Neo4jRepository<Subject, String> {

    List<Subject> findByPeriod(short period);

    List<Subject> findByPeriodGreaterThanEqualOrIdIn(int period, List<String> pendingSubjectIds);

}
