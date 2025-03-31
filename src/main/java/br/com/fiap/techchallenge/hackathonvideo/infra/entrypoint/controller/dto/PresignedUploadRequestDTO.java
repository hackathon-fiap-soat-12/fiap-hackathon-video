package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PresignedUploadRequestDTO(@Schema(example = "video.mp4") @NotBlank String fileName,
                                        @Schema(example = "video/mp4") @NotBlank String fileType) {
}
