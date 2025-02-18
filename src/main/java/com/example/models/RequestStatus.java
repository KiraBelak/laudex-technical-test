package com.example.models;

/**
 * Enum que representa los posibles estados de una solicitud de crédito.
 */
public enum RequestStatus {
    PENDING("Pendiente"),
    APPROVED("Aprobado"),
    REJECTED("Rechazado"),
    IN_REVIEW("En revisión");

    private final String displayName;

    RequestStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}