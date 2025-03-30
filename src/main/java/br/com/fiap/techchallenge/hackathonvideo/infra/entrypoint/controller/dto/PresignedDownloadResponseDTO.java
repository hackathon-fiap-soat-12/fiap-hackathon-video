package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

public record PresignedDownloadResponseDTO(String url,
                                           String method) {
    public PresignedDownloadResponseDTO(String url) {
        this(url, "GET");
    }
}
