package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;

public interface FileService {
    PresignedFile generateUploadPresignedUrl(String bucketName, String key);

    PresignedFile generateDownloadPresignedUrl(String bucketName, String key);
}
