package br.com.fiap.techchallenge.hackathonvideo.application.usecase;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedUploadRequestDTO;

import java.util.UUID;

public interface PresignedUploadUseCase {
    PresignedFile presignedUpload(PresignedUploadRequestDTO dto, UUID userId, String email);
}
