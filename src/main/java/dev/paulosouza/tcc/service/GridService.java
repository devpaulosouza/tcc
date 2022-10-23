package dev.paulosouza.tcc.service;

import dev.paulosouza.tcc.dto.request.GridRequest;
import dev.paulosouza.tcc.dto.response.ClassroomResponse;
import dev.paulosouza.tcc.dto.response.GridResponse;
import dev.paulosouza.tcc.mapper.ClassroomMapper;
import dev.paulosouza.tcc.model.Classroom;
import dev.paulosouza.tcc.model.ClassroomTime;
import dev.paulosouza.tcc.model.Subject;
import dev.paulosouza.tcc.repository.ClassroomRepository;
import dev.paulosouza.tcc.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GridService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    public GridResponse generate(GridRequest request) {
        GridResponse response = new GridResponse();

        List<Subject> pendingSubjects = subjectRepository.findByPeriodGreaterThanEqualOrIdIn(request.getPeriod(), request.getPendingSubjectIds());
        List<String> pendingSubjectIds = pendingSubjects.stream().map(Subject::getId).collect(Collectors.toList());

        response.setPendingSubjects(pendingSubjectIds);
        response.setPendingSubjectsCount(pendingSubjectIds.size());

        response.setGrid(this.generate(pendingSubjects, request));


        return response;
    }

    private Map<Integer, List<ClassroomResponse>> generate(List<Subject> pendingSubjects, GridRequest request) {
        Map<Integer, List<ClassroomResponse>> groupByColor = new HashMap<>();
        Map<Integer, List<ClassroomResponse>> grid = new HashMap<>();
        Map<String, List<Classroom>> classroomMap = new HashMap<>();
        List<Classroom> lateSubjectsClassrooms = new ArrayList<>();

        List<Classroom> classrooms = classroomRepository.findAll();
        classrooms.forEach(cr -> {
            List<Classroom> classroomList = classroomMap.get(cr.getSubjectId());

            if (classroomList == null) {
                classroomList = new ArrayList<>();
            }
            classroomList.add(cr);
            classroomMap.put(cr.getSubjectId(), classroomList);
        });

        List<Subject> subjects = cloneAndSortDesc(pendingSubjects);

        this.colore(subjects);

        int maxColor = subjects.stream()
                .map(Subject::getColor)
                .max(Integer::compareTo)
                .orElse(0);

        for (int i = 0; i < maxColor + 1; ++i) {
            int color = i;

            List<Classroom> cr = subjects.stream()
                    .filter(subject -> subject.getColor() == color)
                    .map(Subject::getId)
                    .flatMap(id -> classroomMap.get(id).stream())
                    .collect(Collectors.toList());


            groupByColor.put(i + 1, ClassroomMapper.INSTANCE.toResponse(cr));
        }


        List<Subject> lateSubjects = new ArrayList<>();

        for (Subject subject : subjects) {
            int period = subject.getPeriod();

            if (period < request.getPeriod()) {
                lateSubjects.add(subject);
                lateSubjectsClassrooms.addAll(classroomMap.get(subject.getId()));
            }
        }
        subjects.removeAll(lateSubjects);


        grid.put(0, ClassroomMapper.INSTANCE.toResponse(lateSubjectsClassrooms));

        int semester = request.getPeriod();

        while (!subjects.isEmpty()) {
            int finalSemester = semester;

            List<Subject> currentSemesterSubjects = subjects.stream()
                    .filter(subject -> subject.getPeriod() <= finalSemester)
                    .filter(subject -> {
                        List<Classroom> subjectClassrooms = classroomMap.get(subject.getId());
                        List<ClassroomTime> subjectTimes = new ArrayList<>();

                        subjectClassrooms.forEach(sc -> subjectTimes.addAll(sc.getTimes()));

                        List<ClassroomTime> lateTimes = lateSubjectsClassrooms.stream()
                                .flatMap(classroom -> classroom.getTimes().stream())
                                .collect(Collectors.toList());

                        return finalSemester != request.getPeriod() || Collections.disjoint(lateTimes, subjectTimes);
                    })
                    .collect(Collectors.toList());
            List<Classroom> currentSemesterClassrooms = currentSemesterSubjects
                    .stream()
                    .flatMap(subject -> classroomMap.get(subject.getId()).stream())
                    .collect(Collectors.toList());



            grid.put(semester - request.getPeriod(), ClassroomMapper.INSTANCE.toResponse(currentSemesterClassrooms));
            subjects.removeAll(currentSemesterSubjects);

            semester++;
        }


        return grid;
    }

    private static List<Subject> cloneAndSortDesc(List<Subject> pendingSubjects) {
        List<Subject> subjects = pendingSubjects.stream()
                .map(Subject::clone)
                .collect(Collectors.toList());

        subjects.forEach(
                s -> s.getPreRequisites()
                    .removeIf(s1 -> pendingSubjects.stream().map(Subject::getId).noneMatch(id -> s1.getId().equals(id)))
        );

        subjects.sort(Comparator.comparingInt(Subject::getDegree).reversed());

        return subjects;
    }

    /**
     * Welsh & Powell algorithm
     * @param subjects vertices to colore
     */
    private void colore(List<Subject> subjects) {
        int maxDegree = subjects.stream()
                .map(Subject::getPreRequisites)
                .map(List::size)
                .max(Integer::compareTo)
                .orElse(1);

        for (int i = 0; i < subjects.size(); ++i) {
            List<Integer> colors = new ArrayList<>();

            for (int j = 0; j <= i && j <= maxDegree; j++) {
                colors.add(j);
            }

            subjects.get(i).setColors(colors);
        }

        subjects.forEach(subject -> {
            Integer color = subject.getColors().get(0);

            subject.setColor(color);
            subject.getPreRequisites()
                    .stream()
                    .map(Subject::getId)
                    .forEach(preRequisiteId -> removeColors(subjects, color, preRequisiteId));
        });

    }

    private static void removeColors(List<Subject> subjects, Integer color, String preRequisiteId) {
        List<Subject> preRequisites = subjects.stream()
                .filter(s -> s.getId().equals(preRequisiteId))
                .collect(Collectors.toList());

        if (!preRequisites.isEmpty()) {
            Subject preRequisite = preRequisites.get(0);
            preRequisite.getColors().remove(color);
        }
    }

}
