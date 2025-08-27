package com.example.backend.users.controller.dto;

import lombok.Data;

/**
 * DTO utilizado para representar una solicitud de envío de email.
 * Contiene únicamente la dirección de correo electrónico.
 */
@Data
public class EmailRequest {

    /**
     * Dirección de correo electrónico del destinatario.
     */
    private String email;
}
