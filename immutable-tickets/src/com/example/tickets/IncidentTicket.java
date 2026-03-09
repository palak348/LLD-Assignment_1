package com.example.tickets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Immutable IncidentTicket with Builder pattern.
 * - All fields are private final.
 * - No public setters.
 * - Tags list is defensively copied and unmodifiable.
 * - Validation is centralized in Builder.build().
 */
public final class IncidentTicket {

    private final String id;
    private final String reporterEmail;
    private final String title;
    private final String description;
    private final String priority;
    private final List<String> tags;
    private final String assigneeEmail;
    private final boolean customerVisible;
    private final Integer slaMinutes;
    private final String source;

    private IncidentTicket(Builder builder) {
        this.id = builder.id;
        this.reporterEmail = builder.reporterEmail;
        this.title = builder.title;
        this.description = builder.description;
        this.priority = builder.priority;
        this.tags = Collections.unmodifiableList(new ArrayList<>(builder.tags));
        this.assigneeEmail = builder.assigneeEmail;
        this.customerVisible = builder.customerVisible;
        this.slaMinutes = builder.slaMinutes;
        this.source = builder.source;
    }

    // --- Getters (no setters) ---

    public String getId() {
        return id;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public boolean isCustomerVisible() {
        return customerVisible;
    }

    public Integer getSlaMinutes() {
        return slaMinutes;
    }

    public String getSource() {
        return source;
    }

    // --- toBuilder: create a Builder pre-populated with this ticket's values ---

    public Builder toBuilder() {
        Builder b = new Builder();
        b.id = this.id;
        b.reporterEmail = this.reporterEmail;
        b.title = this.title;
        b.description = this.description;
        b.priority = this.priority;
        b.tags = new ArrayList<>(this.tags);
        b.assigneeEmail = this.assigneeEmail;
        b.customerVisible = this.customerVisible;
        b.slaMinutes = this.slaMinutes;
        b.source = this.source;
        return b;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "IncidentTicket{" +
                "id='" + id + '\'' +
                ", reporterEmail='" + reporterEmail + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", tags=" + tags +
                ", assigneeEmail='" + assigneeEmail + '\'' +
                ", customerVisible=" + customerVisible +
                ", slaMinutes=" + slaMinutes +
                ", source='" + source + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        IncidentTicket that = (IncidentTicket) o;
        return customerVisible == that.customerVisible &&
                Objects.equals(id, that.id) &&
                Objects.equals(reporterEmail, that.reporterEmail) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(priority, that.priority) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(assigneeEmail, that.assigneeEmail) &&
                Objects.equals(slaMinutes, that.slaMinutes) &&
                Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reporterEmail, title, description, priority,
                tags, assigneeEmail, customerVisible, slaMinutes, source);
    }

    // ========================= Builder =========================

    public static class Builder {
        private String id;
        private String reporterEmail;
        private String title;
        private String description;
        private String priority;
        private List<String> tags = new ArrayList<>();
        private String assigneeEmail;
        private boolean customerVisible;
        private Integer slaMinutes;
        private String source;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder reporterEmail(String e) {
            this.reporterEmail = e;
            return this;
        }

        public Builder title(String t) {
            this.title = t;
            return this;
        }

        public Builder description(String d) {
            this.description = d;
            return this;
        }

        public Builder priority(String p) {
            this.priority = p;
            return this;
        }

        public Builder assigneeEmail(String e) {
            this.assigneeEmail = e;
            return this;
        }

        public Builder customerVisible(boolean v) {
            this.customerVisible = v;
            return this;
        }

        public Builder slaMinutes(Integer m) {
            this.slaMinutes = m;
            return this;
        }

        public Builder source(String s) {
            this.source = s;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags.clear();
            if (tags != null) {
                this.tags.addAll(tags.stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .collect(Collectors.toList()));
            }
            return this;
        }

        public Builder addTag(String tag) {
            if (tag != null && !tag.trim().isEmpty()) {
                this.tags.add(tag.trim());
            }
            return this;
        }

        /**
         * Validates all fields and returns an immutable IncidentTicket.
         * ALL validation is centralized here.
         */
        public IncidentTicket build() {
            // Required fields
            Validation.requireTicketId(id);
            Validation.requireEmail(reporterEmail, "reporterEmail");
            Validation.requireNonBlank(title, "title");
            Validation.requireMaxLen(title, 80, "title");

            // Optional fields
            Validation.requireOneOf(priority, "priority",
                    "LOW", "MEDIUM", "HIGH", "CRITICAL");
            Validation.requireRange(slaMinutes, 5, 7200, "slaMinutes");
            if (assigneeEmail != null) {
                Validation.requireEmail(assigneeEmail, "assigneeEmail");
            }

            return new IncidentTicket(this);
        }
    }
}
