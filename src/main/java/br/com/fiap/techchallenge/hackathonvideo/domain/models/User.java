package br.com.fiap.techchallenge.hackathonvideo.domain.models;

import java.util.UUID;

public class User {
    private final UUID id;

    private final String email;

    public User(UUID id, String email) {
        this.id = id;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
