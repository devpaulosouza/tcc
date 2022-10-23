package dev.paulosouza.tcc.controller;

import dev.paulosouza.tcc.dto.request.SubjectRequest;
import dev.paulosouza.tcc.dto.response.SubjectResponse;
import dev.paulosouza.tcc.model.Subject;
import dev.paulosouza.tcc.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("subjects")
public class SubjectController {

    @Autowired
    private SubjectService service;

    @GetMapping
    public ResponseEntity<List<Subject>> getSubjects(@RequestParam(name = "period", defaultValue = "1") short period) {
        return ResponseEntity.ok(this.service.findAll(period));
    }

    @PostMapping
    public ResponseEntity<List<SubjectResponse>> createSubjects(@RequestBody List<SubjectRequest> subjects) {
        List<SubjectResponse> response = this.service.create(subjects);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
