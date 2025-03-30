package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;

public interface FileService {
    PresignedFile generatePresignedUrl(String bucketName, String key);
}
