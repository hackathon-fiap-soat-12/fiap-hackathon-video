package br.com.fiap.techchallenge.hackathonvideo.application.usecase;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;

import java.util.UUID;

public interface PresignedDownloadUseCase {
    PresignedFile presignedDownload(UUID id);
}
