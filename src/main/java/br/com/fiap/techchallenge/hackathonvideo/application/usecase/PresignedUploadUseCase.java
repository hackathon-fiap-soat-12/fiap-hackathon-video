package br.com.fiap.techchallenge.hackathonvideo.application.usecase;

import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedUploadRequestDTO;

public interface PresignedUploadUseCase {
    String presignedUpload(PresignedUploadRequestDTO dto, String userId, String email);
}
