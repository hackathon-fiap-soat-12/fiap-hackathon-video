package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedUploadUseCase;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedUploadRequestDTO;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PresignedUploadUseCaseImpl implements PresignedUploadUseCase {

    private static final Logger logger = LoggerFactory.getLogger(PresignedUploadUseCaseImpl.class);

    private final VideoPersistence videoPersistence;
    private final FileService fileService;

    public PresignedUploadUseCaseImpl(VideoPersistence videoPersistence, FileService fileService) {
        this.videoPersistence = videoPersistence;
        this.fileService = fileService;
    }

    @Override
    public PresignedFile presignedUpload(PresignedUploadRequestDTO dto, UUID userId, String email) {
        var video = videoPersistence.save(new Video(dto.fileName(), new User(userId, email)));

        logger.info("Presigned Upload video id {} requested by user id {}", video.getId(), userId);

        return fileService.generateUploadPresignedUrl(video, dto.fileType());
    }
}
