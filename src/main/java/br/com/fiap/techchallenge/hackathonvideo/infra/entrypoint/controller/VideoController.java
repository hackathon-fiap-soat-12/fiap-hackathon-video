package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller;

import br.com.fiap.techchallenge.hackathonvideo.enums.ProcessStatus;
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


    @PostMapping("/presigned-upload")
    public ResponseEntity<PresignedUploadResponseDTO> presignedUpload(@RequestBody @Valid PresignedUploadRequestDTO dto,
                                                                      @RequestHeader("user_id") String userId,
                                                                      @RequestHeader("email") String email){

        return ResponseEntity.status(HttpStatus.CREATED).body(new PresignedUploadResponseDTO("url", "PUT", 3000));
    }

    @GetMapping("/files/{fileId}/presigned-download")
    public ResponseEntity<PresignedDownloadResponseDTO> presignedDownload(@PathVariable("fileId") String fileId){

        return ResponseEntity.status(HttpStatus.OK).body(new PresignedDownloadResponseDTO("url", "GET"));
    }

    @GetMapping("/files")
    public ResponseEntity<?> getFiles(){

        return ResponseEntity.status(HttpStatus.OK).body(new ListFilesResponseDTO(List.of(new FileResponseDTO(UUID.randomUUID(), "videoName", ProcessStatus.PROCESSING)),
                new PageResponseDTO(1L,1L,1L, 1L,true, true,1L,true)));
    }
}
