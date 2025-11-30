package com.example.fms.common.dto;

public record EmailEvent(
        String to,
        String subject,
        String body,
        String eventType,
        String pnr
) {
}
