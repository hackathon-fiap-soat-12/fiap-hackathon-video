package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;

import java.util.UUID;

public interface FileService {
    PresignedFile generateUploadPresignedUrl(Video video, String fileType);

    PresignedFile generateDownloadPresignedUrl(Video video);
}
