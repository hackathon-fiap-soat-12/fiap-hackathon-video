package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record PresignedUploadRequestDTO(@NotBlank String fileName,
                                        @NotBlank String fileType) {
}
