package br.com.fiap.techchallenge.hackathonvideo.domain.models;

import java.time.Duration;
import java.time.Instant;

public class PresignedFile {

    private final String url;
    private final Instant expiresIn;

    public PresignedFile(String url, Instant expiresIn) {
        this.url = url;
        this.expiresIn = expiresIn;
    }

    public String getUrl() {
        return url;
    }

    public Integer getSecondsToExpire() {
        return (int) Duration.between(Instant.now(), this.expiresIn).toSeconds();
    }
}
