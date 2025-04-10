package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedDownloadUseCase;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import br.com.fiap.techchallenge.hackathonvideo.application.filestorage.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PresignedDownloadUseCaseImpl implements PresignedDownloadUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(PresignedDownloadUseCaseImpl.class);

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

        LOGGER.info("Presigned Download video id {} requested by user id {}", video.getId(), video.getUserId());

        this.validateStatus(video.getStatus());

        return fileService.generateDownloadPresignedUrl(video);
    }

    private void validateStatus(ProcessStatus status) {
        if(ProcessStatus.FAILED.equals(status)){
            throw new DoesNotExistException("An error happened when processing the video, try processing again");
        }

        if(ProcessStatus.NEW.equals(status)){
            throw new DoesNotExistException("The video has not yet been sent");
        }

        if(ProcessStatus.RECEIVED.equals(status) || ProcessStatus.PROCESSING.equals(status)){
            throw new DoesNotExistException("The video is in processing");
        }
    }
}
