package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo.dto;

import br.com.fiap.techchallenge.hackathonvideo.enums.ProcessStatus;

import java.util.UUID;

public record UpdateVideoDTO(UUID id,
                             ProcessStatus status) {
}
