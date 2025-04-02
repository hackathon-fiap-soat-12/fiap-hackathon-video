package br.com.fiap.techchallenge.hackathonvideo.application.usecase;

import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo.dto.UpdateVideoDTO;

public interface UpdateVideoUseCase {
    void updateVideo(UpdateVideoDTO updateVideoDto);
}
