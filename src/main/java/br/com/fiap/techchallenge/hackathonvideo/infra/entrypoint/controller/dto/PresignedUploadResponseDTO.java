package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;

public record PresignedUploadResponseDTO(String url,
                                         String method,
                                         Integer expiresIn) {
    public PresignedUploadResponseDTO(PresignedFile presignedFile) {
        this(presignedFile.getUrl(), "PUT", presignedFile.getSecondsToExpire());
    }
}
