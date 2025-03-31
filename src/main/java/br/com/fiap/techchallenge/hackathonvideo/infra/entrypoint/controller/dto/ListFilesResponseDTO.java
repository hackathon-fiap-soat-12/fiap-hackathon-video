package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable.CustomPage;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ListFilesResponseDTO(List<FileResponseDTO> files,
                                   @Schema(example = "{\\\"createdAt\\\":\\\"2025-03-31T12:28:09.775653700\\\",\\\"id\\\":\\\"031193eb-6464-4d18-91f7-d0be91714561\\\",\\\"userId\\\":\\\"4f2da442-81d6-47d9-bfbb-3b525c6f0606\\\"}") String lastEvaluatedKey) {

    public ListFilesResponseDTO(CustomPage customPage) {
        this(customPage.videos().stream().map(FileResponseDTO::new).toList(), customPage.lastEvaluatedKey());
    }
}
