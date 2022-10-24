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
        Map<Integer, List<ClassroomResponse>> response = new HashMap<>();

        // busca map de salas de aula
        Map<String, List<Classroom>> classrooms = this.getClassrooms();

        // ordenação decrescente pelo grau dos vértices
        List<Subject> subjects = this.cloneAndSortDesc(pendingSubjects);

        // coloração pelo algoritmo Welsh e Powell
        this.colore(subjects);

        // disciplinas atrasadas
        List<Subject> lateSubjects  = this.getLateSubjects(request, subjects);
        List<Classroom> lateClassrooms = this.getLateSubjectClassrooms(lateSubjects, classrooms);
        response.put(0, ClassroomMapper.INSTANCE.toResponse(lateClassrooms));

        int semester = request.getPeriod();

        // itera até alocar todas as disciplinas
        while (!subjects.isEmpty()) {
            int finalSemester = semester;

            // busca disciplinas do semestre atual
            List<Subject> currentSubjects =
                    getCurrentSemesterSubjects(request, classrooms, subjects, lateClassrooms, finalSemester);

            // busca salas de aula do semestre atual
            List<Classroom> currentClassrooms = getCurrentSemesterClassrooms(classrooms, currentSubjects);

            List<ClassroomResponse> classroomResponses = response.get(semester - request.getPeriod());
            if (classroomResponses != null) {
                classroomResponses.addAll(ClassroomMapper.INSTANCE.toResponse(currentClassrooms));
            }
            response.put(semester - request.getPeriod(), classroomResponses);

            // remove as disciplinas que já foram alocadas
            subjects.removeAll(currentSubjects);

            // incrementa para o próximo semestre
            semester++;
        }


        return response;
    }

    private static List<Classroom> getCurrentSemesterClassrooms(Map<String, List<Classroom>> classroomMap, List<Subject> currentSemesterSubjects) {
        return currentSemesterSubjects
                .stream()
                .flatMap(subject -> classroomMap.get(subject.getId()).stream())
                .collect(Collectors.toList());
    }

    private static List<Subject> getCurrentSemesterSubjects(
            GridRequest request,
            Map<String, List<Classroom>> classroomMap,
            List<Subject> subjects,
            List<Classroom> lateSubjectsClassrooms,
            int semester
    ) {
        return subjects.stream()
                .filter(subject -> subject.getPeriod() <= semester)
                .filter(subject -> {
                    List<Classroom> subjectClassrooms = classroomMap.get(subject.getId());
                    List<ClassroomTime> subjectTimes = new ArrayList<>();

                    subjectClassrooms.forEach(sc -> subjectTimes.addAll(sc.getTimes()));

                    List<ClassroomTime> lateTimes = lateSubjectsClassrooms.stream()
                            .flatMap(classroom -> classroom.getTimes().stream())
                            .collect(Collectors.toList());

                    return semester != request.getPeriod() || Collections.disjoint(lateTimes, subjectTimes);
                })
                .collect(Collectors.toList());
    }

    private List<Subject> getLateSubjects(
            GridRequest request,
            List<Subject> subjects
    ) {
        List<Subject> lateSubjects = new ArrayList<>();
        for (Subject subject : subjects) {
            int period = subject.getPeriod();

            if (period < request.getPeriod()) {
                lateSubjects.add(subject);
            }
        }

        subjects.removeAll(lateSubjects);

        return lateSubjects;
    }

    private List<Classroom> getLateSubjectClassrooms(List<Subject> lateSubjects, Map<String, List<Classroom>> classroomMap) {
        List<Classroom> lateSubjectsClassrooms = new ArrayList<>();

        lateSubjects.forEach(subject -> lateSubjectsClassrooms.addAll(classroomMap.get(subject.getId())));

        return lateSubjectsClassrooms;
    }

    private Map<String, List<Classroom>> getClassrooms() {
        List<Classroom> classrooms = classroomRepository.findAll();

        Map<String, List<Classroom>> classroomMap = new HashMap<>();
        classrooms.forEach(cr -> {
            List<Classroom> classroomList = classroomMap.get(cr.getSubjectId());

            if (classroomList == null) {
                classroomList = new ArrayList<>();
            }
            classroomList.add(cr);
            classroomMap.put(cr.getSubjectId(), classroomList);
        });

        return classroomMap;
    }

    private List<Subject> cloneAndSortDesc(List<Subject> pendingSubjects) {
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
