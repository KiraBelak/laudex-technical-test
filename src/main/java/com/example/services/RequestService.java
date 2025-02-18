package com.example.services;

import com.example.models.Request;
import com.example.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    private final RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public List<Request> getAll() {
        return requestRepository.findAll();
    }

    public Optional<Request> getById(Long id) {
        return requestRepository.findById(id);
    }

    public Request create(Request request) {
        return requestRepository.save(request);
    }

    public Request update(Long id, Request updatedRequest) {
        return requestRepository.findById(id).map(request -> {
            request.setName(updatedRequest.getName());
            request.setLastName(updatedRequest.getLastName());
            request.setEmail(updatedRequest.getEmail());
            request.setCity(updatedRequest.getCity());
            request.setAmount(updatedRequest.getAmount());
            request.setStatus(updatedRequest.getStatus());
            return requestRepository.save(request);
        }).orElseThrow(() -> new RuntimeException("Request not found"));
    }

    public void delete(Long id) {
        requestRepository.deleteById(id);
    }
}
