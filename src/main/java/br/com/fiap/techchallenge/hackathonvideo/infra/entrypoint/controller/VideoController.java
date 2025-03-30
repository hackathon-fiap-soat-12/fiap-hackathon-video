package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.ListFilesUseCase;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedDownloadUseCase;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedUploadUseCase;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

        var url = presignedDownloadUseCase.presignedDownload(id);

        return ResponseEntity.status(HttpStatus.OK).body(new PresignedDownloadResponseDTO(url));
    }

    @GetMapping("/files")
    public ResponseEntity<ListFilesResponseDTO> getFiles(@RequestParam(value = "user_id") UUID userId){
        var response = listFilesUseCase.getFiles(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ListFilesResponseDTO(List.of(new FileResponseDTO(UUID.randomUUID(), "videoName", ProcessStatus.PROCESSING)),
                new PageResponseDTO(1L,1L,1L, 1L,true, true,1L,true)));
    }
}
