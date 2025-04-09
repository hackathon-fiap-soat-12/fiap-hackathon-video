package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;

import java.util.UUID;

public record UpdateVideoDTO(UUID id,
                             ProcessStatus status,
                             Integer qtdFrames,
                             Long sizeInBytes) {
}
