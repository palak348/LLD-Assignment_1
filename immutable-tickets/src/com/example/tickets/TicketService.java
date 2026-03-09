package com.example.tickets;

import java.util.Collections;

/**
 * Service layer that creates and updates tickets.
 * Uses immutable IncidentTicket + Builder; never mutates after creation.
 */
public class TicketService {

    /**
     * Creates a new ticket with sensible defaults.
     */
    public IncidentTicket createTicket(String id, String reporterEmail, String title) {
        return IncidentTicket.builder()
                .id(id)
                .reporterEmail(reporterEmail)
                .title(title)
                .description("")
                .priority("MEDIUM")
                .source("CLI")
                .customerVisible(false)
                .tags(Collections.singletonList("NEW"))
                .build();
    }

    /**
     * Returns a NEW ticket with priority = CRITICAL and tag "ESCALATED" added.
     */
    public IncidentTicket escalateToCritical(IncidentTicket ticket) {
        return ticket.toBuilder()
                .priority("CRITICAL")
                .addTag("ESCALATED")
                .build();
    }

    /**
     * Returns a NEW ticket with the given assignee.
     */
    public IncidentTicket assign(IncidentTicket ticket, String assigneeEmail) {
        return ticket.toBuilder()
                .assigneeEmail(assigneeEmail)
                .build();
    }
}
