package com.example.dto;

import com.example.models.RequestStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para las respuestas de solicitudes de crédito.
 */
@Data
public class RequestResponseDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String city;
    private BigDecimal amount;
    private RequestStatus status;
    private String statusDisplayName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}