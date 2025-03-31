package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.ListFilesUseCase;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedDownloadUseCase;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedUploadUseCase;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable.CustomPage;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.*;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl.PaginatedResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
public class VideoController {

    private final PresignedUploadUseCase presignedUploadUseCase;
    private final PresignedDownloadUseCase presignedDownloadUseCase;
    private final ListFilesUseCase listFilesUseCase;

    public VideoController(PresignedUploadUseCase presignedUploadUseCase,
                           PresignedDownloadUseCase presignedDownloadUseCase,
                           ListFilesUseCase listFilesUseCase) {
        this.presignedUploadUseCase = presignedUploadUseCase;
        this.presignedDownloadUseCase = presignedDownloadUseCase;
        this.listFilesUseCase = listFilesUseCase;
    }


    @PostMapping("/presigned-upload")
    public ResponseEntity<PresignedUploadResponseDTO> presignedUpload(@RequestBody @Valid PresignedUploadRequestDTO dto,
                                                                      @RequestHeader("user_id") UUID userId,
                                                                      @RequestHeader("email") String email){

        var presignedFile = presignedUploadUseCase.presignedUpload(dto, userId, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(new PresignedUploadResponseDTO(presignedFile));
    }

    @GetMapping("/files/{id}/presigned-download")
    public ResponseEntity<PresignedDownloadResponseDTO> presignedDownload(@PathVariable("id") UUID id){

        var presignedFile = presignedDownloadUseCase.presignedDownload(id);

        return ResponseEntity.status(HttpStatus.OK).body(new PresignedDownloadResponseDTO(presignedFile));
    }

    @GetMapping("/files")
    public ResponseEntity<ListFilesResponseDTO> getFiles(@RequestParam(value = "user_id") UUID userId,
                                               @RequestParam Integer pageSize,
                                               @RequestParam(required = false) String exclusiveStartKey){

        var customPage = listFilesUseCase.getFiles(userId, pageSize, exclusiveStartKey);

        return ResponseEntity.status(HttpStatus.OK).body(new ListFilesResponseDTO(customPage));
    }
}
