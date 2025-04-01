package br.com.fiap.techchallenge.hackathonvideo.application.filestorage;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;


public interface FileService {
    PresignedFile generateUploadPresignedUrl(Video video, String fileType);

    PresignedFile generateDownloadPresignedUrl(Video video);
}
