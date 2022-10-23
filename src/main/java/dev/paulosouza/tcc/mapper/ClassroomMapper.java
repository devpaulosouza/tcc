package dev.paulosouza.tcc.mapper;

import dev.paulosouza.tcc.dto.request.ClassroomRequest;
import dev.paulosouza.tcc.dto.response.ClassroomResponse;
import dev.paulosouza.tcc.dto.response.ClassroomTimeResponse;
import dev.paulosouza.tcc.model.Classroom;
import dev.paulosouza.tcc.model.ClassroomTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ClassroomMapper {

    ClassroomMapper INSTANCE = Mappers.getMapper(ClassroomMapper.class);

    Classroom toEntity(ClassroomRequest request);
    List<Classroom> toEntity(List<ClassroomRequest> request);

    default ClassroomResponse toResponse(Classroom entity) {
        ClassroomResponse response = new ClassroomResponse();
        List<ClassroomTimeResponse> times = entity.getTimes().stream().map(this::toResponse).collect(Collectors.toList());

        response.setId(entity.getId());
        response.setTimes(times);
        response.setSubjectId(entity.getSubjectId());

        return response;
    }

    default ClassroomTimeResponse toResponse(ClassroomTime time) {
        ClassroomTimeResponse timeResponse = new ClassroomTimeResponse();

        timeResponse.setHour(time.getHour());
        timeResponse.setDayOfWeek(time.getDayOfWeek());

        return timeResponse;
    }

    List<ClassroomResponse> toResponse(List<Classroom> entity);
}
