package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;

import java.util.List;

public record ListFilesResponseDTO(List<FileResponseDTO> files, PageResponseDTO page) {
    public ListFilesResponseDTO(List<Video> videos) {
        this(videos.stream().map(FileResponseDTO::new).toList(), null);
    }
}
