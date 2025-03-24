package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedUploadUseCase;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedUploadRequestDTO;

public class PresignedUploadUseCaseImpl implements PresignedUploadUseCase {

    @Override
    public String presignedUpload(PresignedUploadRequestDTO dto, String userId, String email) {
        return "";
    }
}
