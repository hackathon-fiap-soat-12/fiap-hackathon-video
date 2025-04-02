package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;


public record PresignedUploadResponseDTO(@Schema(example = "fa90993a-823c-4848-ae75-b01690867d4b") UUID id,
                                         @Schema(example = "https://videofiles.localhost.localstack.cloud:4566/videofiles/videos/marvel.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250331T194746Z&X-Amz-SignedHeaders=host%3Bx-amz-meta-content-type&X-Amz-Credential=fakeAccessKey%2F20250331%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Expires=300&X-Amz-Signature=ce6970fca66633079244e06182f17563c0317c9af3cde5edea434365223391ef")
                                         String url,
                                         @Schema(example = "PUT") String method,
                                         @Schema(example = "299") Integer expiresIn) {
    public PresignedUploadResponseDTO(PresignedFile presignedFile) {
        this(presignedFile.getId(), presignedFile.getUrl(), presignedFile.getMethod().toString(), presignedFile.getSecondsToExpire());
    }
}
