package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;

import java.util.UUID;


public record PresignedUploadResponseDTO(UUID id,
                                         String url,
                                         String method,
                                         Integer expiresIn) {
    public PresignedUploadResponseDTO(PresignedFile presignedFile) {
        this(presignedFile.getId(), presignedFile.getUrl(), presignedFile.getMethod().toString(), presignedFile.getSecondsToExpire());
    }
}
