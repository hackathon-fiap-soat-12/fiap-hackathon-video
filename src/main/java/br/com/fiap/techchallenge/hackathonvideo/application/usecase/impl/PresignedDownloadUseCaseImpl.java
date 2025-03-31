package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedDownloadUseCase;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PresignedDownloadUseCaseImpl implements PresignedDownloadUseCase {

    private static final Logger logger = LoggerFactory.getLogger(PresignedDownloadUseCaseImpl.class);

    private final VideoPersistence videoPersistence;
    private final FileService fileService;

    public PresignedDownloadUseCaseImpl(VideoPersistence videoPersistence, FileService fileService) {
        this.videoPersistence = videoPersistence;
        this.fileService = fileService;
    }

    @Override
    public PresignedFile presignedDownload(UUID id) {
        var video = videoPersistence.findById(id)
                .orElseThrow(() -> new DoesNotExistException("Zip File not found"));

        logger.info("Presigned Download video id {} requested by user id {}", video.getId(), video.getUserId());

        return fileService.generateDownloadPresignedUrl(video);
    }
}
