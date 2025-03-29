package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedDownloadUseCase;

import java.util.UUID;

public class PresignedDownloadUseCaseImpl implements PresignedDownloadUseCase {

    @Override
    public String presignedDownload(UUID fileId) {
        // recupera o video pelo id
        // retorna a url para download
        return "";
    }
}
