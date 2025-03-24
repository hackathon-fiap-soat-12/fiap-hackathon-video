package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import br.com.fiap.techchallenge.hackathonvideo.enums.ProcessStatus;

import java.util.UUID;

public record FileResponseDTO(UUID id,
                              String videoName,
                              ProcessStatus processingStatus) {
}
