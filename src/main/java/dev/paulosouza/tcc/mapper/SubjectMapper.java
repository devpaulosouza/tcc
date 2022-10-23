package dev.paulosouza.tcc.mapper;

import dev.paulosouza.tcc.dto.request.SubjectRequest;
import dev.paulosouza.tcc.dto.response.SubjectResponse;
import dev.paulosouza.tcc.model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectMapper {

    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    @Mapping(target = "preRequisites", ignore = true)
    @Mapping(target = "coRequisites", ignore = true)
    @Mapping(target = "color", ignore = true)
    @Mapping(target = "colors", ignore = true)
    Subject toEntity(SubjectRequest request);
    List<Subject> toEntity(List<SubjectRequest> request);

    SubjectResponse toResponse(Subject entity);
    List<SubjectResponse> toResponse(List<Subject> entity);
}
