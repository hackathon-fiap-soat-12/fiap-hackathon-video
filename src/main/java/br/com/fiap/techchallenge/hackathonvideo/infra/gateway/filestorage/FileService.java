package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage;

import java.io.InputStream;
import java.time.Duration;

public interface FileService {

    InputStream getFile(String bucketName, String key);

    Boolean uploadFile(String bucketName, String key, InputStream file);

    String generatePresignedUrl(String bucketName, String key);
}
