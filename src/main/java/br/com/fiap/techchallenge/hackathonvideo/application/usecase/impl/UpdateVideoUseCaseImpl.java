package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.UpdateVideoUseCase;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo.dto.UpdateVideoDTO;

public class UpdateVideoUseCaseImpl implements UpdateVideoUseCase {

    @Override
    public void updateVideo(UpdateVideoDTO updateVideoDto) {
        // recupera video do banco
        // atualiza o status
        // salva
        // caso status for finalizado, envia notificação
    }
}
