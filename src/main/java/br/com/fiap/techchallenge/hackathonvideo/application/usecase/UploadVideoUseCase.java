package br.com.fiap.techchallenge.hackathonvideo.application.usecase;

import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.uploadvideo.dto.UploadVideoDTO;

public interface UploadVideoUseCase {

    void receiveToProcess(UploadVideoDTO dto);
}
