package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable.CustomPage;

import java.util.List;

public record ListFilesResponseDTO(List<FileResponseDTO> files, String lastEvaluatedKey) {

    public ListFilesResponseDTO(CustomPage customPage) {
        this(customPage.videos().stream().map(FileResponseDTO::new).toList(), customPage.lastEvaluatedKey());
    }
}
