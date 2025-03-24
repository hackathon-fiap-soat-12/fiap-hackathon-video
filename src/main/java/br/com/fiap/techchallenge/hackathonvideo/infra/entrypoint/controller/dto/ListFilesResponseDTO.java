package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import java.util.List;

public record ListFilesResponseDTO(List<FileResponseDTO> files, PageResponseDTO page) {
}
