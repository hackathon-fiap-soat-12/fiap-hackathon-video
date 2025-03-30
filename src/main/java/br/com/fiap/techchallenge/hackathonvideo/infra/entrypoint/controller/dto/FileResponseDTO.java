package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;

import java.util.UUID;

public record FileResponseDTO(UUID id,
                              String videoName,
                              ProcessStatus processingStatus) {
    public FileResponseDTO(Video video) {
        this(video.getId(), video.getVideoName(), video.getStatus());
    }
}
