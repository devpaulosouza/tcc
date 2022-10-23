package dev.paulosouza.tcc.controller;

import dev.paulosouza.tcc.dto.request.GridRequest;
import dev.paulosouza.tcc.dto.response.GridResponse;
import dev.paulosouza.tcc.service.GridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("grids")
public class GridController {

    @Autowired
    private GridService service;

    @GetMapping
    public ResponseEntity<GridResponse> generate(GridRequest request) {
        GridResponse response = this.service.generate(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
