package com.example.controllers;

import com.example.dto.RequestDTO;
import com.example.dto.RequestResponseDTO;
import com.example.mappers.RequestMapper;
import com.example.models.Request;
import com.example.models.RequestStatus;
import com.example.services.RequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/solicitudes")
public class RequestController {

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    public RequestController(RequestService requestService, RequestMapper requestMapper) {
        this.requestService = requestService;
        this.requestMapper = requestMapper;
    }

    @GetMapping
    public ResponseEntity<List<RequestResponseDTO>> list() {
        List<RequestResponseDTO> requests = requestService.getAll()
                .stream()
                .map(requestMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestResponseDTO> get(@PathVariable Long id) {
        return requestService.getById(id)
                .map(request -> ResponseEntity.ok(requestMapper.toResponseDto(request)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RequestDTO requestDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(result));
        }

        Request request = requestMapper.toEntity(requestDTO);
        Request savedRequest = requestService.create(request);
        return ResponseEntity.ok(requestMapper.toResponseDto(savedRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody RequestDTO requestDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(result));
        }

        Request request = requestMapper.toEntity(requestDTO);
        try {
            Request updatedRequest = requestService.update(id, request);
            return ResponseEntity.ok(requestMapper.toResponseDto(updatedRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {
        try {
            Request request = requestService.getById(id)
                    .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
            request.setStatus(status);
            Request updatedRequest = requestService.update(id, request);
            return ResponseEntity.ok(requestMapper.toResponseDto(updatedRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            requestService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Map<String, String> getValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}
