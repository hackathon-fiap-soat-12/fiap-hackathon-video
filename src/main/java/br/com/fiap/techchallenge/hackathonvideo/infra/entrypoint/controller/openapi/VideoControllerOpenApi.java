package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.openapi;

import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.ListFilesResponseDTO;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedDownloadResponseDTO;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedUploadRequestDTO;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedUploadResponseDTO;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.handler.ErrorsValidateData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Video")
public interface VideoControllerOpenApi {

    @Operation(summary = "Get a Presigned Upload URL")
    @ApiResponse(responseCode = "201", description = "Created Response",
            content = @Content(mediaType = "application/json", schema = @Schema(ref = "PresignedUploadResponseDTO")))
    @ApiResponse(responseCode = "400", description = "Bad Request Response",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ErrorsValidateData.class))))
    @ApiResponse(responseCode = "500", description = "Internal Server Error Response",
            content = @Content(mediaType = "application/json", schema = @Schema(ref = "ProblemDto")))
    ResponseEntity<PresignedUploadResponseDTO> presignedUpload(PresignedUploadRequestDTO dto,
                                                               @Schema(example = "4f2da442-81d6-47d9-bfbb-3b525c6f0606") UUID userId,
                                                               @Schema(example = "email@email.com") String email);

    @Operation(summary = "Get a Presigned Download URL")
    @ApiResponse(responseCode = "200", description = "Ok Response",
            content = @Content(mediaType = "application/json", schema = @Schema(ref = "PresignedDownloadResponseDTO")))
    @ApiResponse(responseCode = "404", description = "Not Found Response",
            content = @Content(mediaType = "application/json", schema = @Schema(ref = "ProblemDto")))
    @ApiResponse(responseCode = "500", description = "Internal Server Error Response",
            content = @Content(mediaType = "application/json", schema = @Schema(ref = "ProblemDto")))
    ResponseEntity<PresignedDownloadResponseDTO> presignedDownload(@Schema(example = "4f2da442-81d6-47d9-bfbb-3b525c6f0606")  UUID id);

    @Operation(summary = "Find a List of files by user id")
    @ApiResponse(responseCode = "200", description = "Ok Response",
            content = @Content(mediaType = "application/json", schema = @Schema(ref = "ListFilesResponseDTO")))
    @ApiResponse(responseCode = "404", description = "Not Found Response",
            content = @Content(mediaType = "application/json", schema = @Schema(ref = "ProblemDto")))
    @ApiResponse(responseCode = "500", description = "Internal Server Error Response",
            content = @Content(mediaType = "application/json", schema = @Schema(ref = "ProblemDto")))
    ResponseEntity<ListFilesResponseDTO> getFiles(@Schema(example = "4f2da442-81d6-47d9-bfbb-3b525c6f0606") UUID userId,
                                                  @Schema(example = "3") Integer pageSize,
                                                  @Schema(example = "{\"createdAt\":\"2025-03-31T12:28:09.775653700\",\"id\":\"031193eb-6464-4d18-91f7-d0be91714561\",\"userId\":\"4f2da442-81d6-47d9-bfbb-3b525c6f0606\"}") String exclusiveStartKey);
}
