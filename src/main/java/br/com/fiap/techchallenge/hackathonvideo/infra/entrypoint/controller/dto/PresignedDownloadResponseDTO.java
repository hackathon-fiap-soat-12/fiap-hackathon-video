package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;

public record PresignedDownloadResponseDTO(String url,
                                           String method,
                                           Integer expiresIn) {
    public PresignedDownloadResponseDTO(PresignedFile presignedFile) {
        this(presignedFile.getUrl(), presignedFile.getMethod().toString(), presignedFile.getSecondsToExpire());
    }
}
