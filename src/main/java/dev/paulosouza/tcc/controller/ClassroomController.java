package dev.paulosouza.tcc.controller;

import dev.paulosouza.tcc.dto.request.ClassroomRequest;
import dev.paulosouza.tcc.dto.response.ClassroomResponse;
import dev.paulosouza.tcc.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService service;

    @PostMapping
    public ResponseEntity<List<ClassroomResponse>> create(@RequestBody List<ClassroomRequest> classrooms) {
        List<ClassroomResponse> response = this.service.create(classrooms);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
