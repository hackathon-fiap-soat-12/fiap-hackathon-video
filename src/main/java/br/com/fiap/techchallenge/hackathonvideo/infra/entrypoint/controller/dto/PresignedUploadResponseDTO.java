package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;

import java.util.UUID;

import static br.com.fiap.techchallenge.hackathonvideo.domain.constants.Constants.PUT_METHOD;

public record PresignedUploadResponseDTO(UUID id,
                                         String url,
                                         String method,
                                         Integer expiresIn) {
    public PresignedUploadResponseDTO(PresignedFile presignedFile) {
        this(presignedFile.getId(), presignedFile.getUrl(), PUT_METHOD, presignedFile.getSecondsToExpire());
    }
}
