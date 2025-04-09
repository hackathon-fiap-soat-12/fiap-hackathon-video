package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record FileResponseDTO(@Schema(example = "031193eb-6464-4d18-91f7-d0be91714561") UUID id,
                              @Schema(example = "video.mp4") String videoName,
                              @Schema(example = "NEW") ProcessStatus processingStatus,
                              @Schema(example = "8") Integer qtdFrames,
                              @Schema(example = "876525") Long sizeInBytes) {
    public FileResponseDTO(Video video) {
        this(video.getId(), video.getVideoName(), video.getStatus(), video.getQtdFrames(), video.getSizeInBytes());
    }
}
