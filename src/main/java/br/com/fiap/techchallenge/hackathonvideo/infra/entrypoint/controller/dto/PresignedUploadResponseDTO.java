package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

public record PresignedUploadResponseDTO(String url,
                                         String method,
                                         Integer expiresIn) {
}
