package dev.paulosouza.tcc.service;

import dev.paulosouza.tcc.dto.request.SubjectRequest;
import dev.paulosouza.tcc.dto.response.SubjectResponse;
import dev.paulosouza.tcc.mapper.SubjectMapper;
import dev.paulosouza.tcc.model.Subject;
import dev.paulosouza.tcc.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository repository;

    public List<Subject> findAll(short period) {
        return this.repository.findByPeriod(period);
    }

    public List<SubjectResponse> create(List<SubjectRequest> request) {
        List<Subject> entity = SubjectMapper.INSTANCE.toEntity(request);

        this.repository.deleteAll(entity);

        setRelationships(request, entity);

        this.repository.saveAll(entity);

        return SubjectMapper.INSTANCE.toResponse(entity);
    }

    private void setRelationships(List<SubjectRequest> request, List<Subject> entity) {
        for (int i = 0; i < entity.size(); ++i) {
            Subject subject = entity.get(i);
            SubjectRequest subjectRequest = request.get(i);

            List<Subject> preRequisites = this.filterPreRequisites(subjectRequest, entity);
            List<Subject> coRequisites = this.filterCoRequisites(subjectRequest, entity);

            subject.setPreRequisites(preRequisites);
            subject.setCoRequisites(coRequisites);
        }
    }

    private List<Subject> filterPreRequisites(SubjectRequest request, List<Subject> entity) {
        return entity.stream()
                .filter(item -> request.getPreRequisiteIds().contains(item.getId()))
                .collect(Collectors.toList());
    }

    private List<Subject> filterCoRequisites(SubjectRequest request, List<Subject> entity) {
        return entity.stream()
                .filter(item -> request.getCoRequisiteIds().contains(item.getId()))
                .collect(Collectors.toList());
    }

}
