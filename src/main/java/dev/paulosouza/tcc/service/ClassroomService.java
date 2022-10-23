package dev.paulosouza.tcc.service;

import dev.paulosouza.tcc.dto.request.ClassroomRequest;
import dev.paulosouza.tcc.dto.response.ClassroomResponse;
import dev.paulosouza.tcc.mapper.ClassroomMapper;
import dev.paulosouza.tcc.model.Classroom;
import dev.paulosouza.tcc.repository.ClassroomRepository;
import dev.paulosouza.tcc.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository repository;

    @Autowired
    private SubjectRepository subjectRepository;

    public List<ClassroomResponse> create(List<ClassroomRequest> request) {
        List<Classroom> entity = ClassroomMapper.INSTANCE.toEntity(request);

        this.repository.deleteAll();
        this.repository.saveAll(entity);

        return ClassroomMapper.INSTANCE.toResponse(entity);
    }

}
