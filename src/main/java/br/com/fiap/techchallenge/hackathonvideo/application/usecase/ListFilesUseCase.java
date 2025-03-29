package br.com.fiap.techchallenge.hackathonvideo.application.usecase;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;

import java.util.List;
import java.util.UUID;

public interface ListFilesUseCase {
    List<Video> getFiles(UUID userId);
}
