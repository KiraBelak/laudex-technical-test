package com.example.mappers;

import com.example.dto.RequestDTO;
import com.example.dto.RequestResponseDTO;
import com.example.models.Request;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre DTOs y entidad Request.
 */
@Component
public class RequestMapper {

    public Request toEntity(RequestDTO dto) {
        Request entity = new Request();
        entity.setName(dto.getName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setCity(dto.getCity());
        entity.setAmount(dto.getAmount());
        return entity;
    }

    public RequestResponseDTO toResponseDto(Request entity) {
        RequestResponseDTO dto = new RequestResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setCity(entity.getCity());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setStatusDisplayName(entity.getStatus().getDisplayName());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}