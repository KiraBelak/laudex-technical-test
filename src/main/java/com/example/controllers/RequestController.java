package com.example.controllers;

import com.example.models.Request;
import com.example.services.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/solicitudes")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<Request> list() {
        return requestService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Request> get(@PathVariable Long id) {
        Optional<Request> request = requestService.getById(id);
        return request.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Request create(@RequestBody Request request) {
        return requestService.create(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Request> update(@PathVariable Long id, @RequestBody Request request) {
        return ResponseEntity.ok(requestService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        requestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
