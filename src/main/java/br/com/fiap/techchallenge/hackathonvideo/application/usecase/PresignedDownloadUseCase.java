package br.com.fiap.techchallenge.hackathonvideo.application.usecase;

import java.util.UUID;

public interface PresignedDownloadUseCase {
    String presignedDownload(UUID id);
}
