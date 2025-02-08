package com.cht.network.monitoring.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;

import java.time.Instant;

public class CommentDto {
    private Long id;

    private String comment;

    @Transient
    @JsonProperty("full_name")
    private String fullName;

    private UserDto user;

    @JsonProperty("created_at")
    private Instant createdAt;

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
