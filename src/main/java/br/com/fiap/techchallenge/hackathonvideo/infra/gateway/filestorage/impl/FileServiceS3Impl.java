package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage.FileService;
import io.awspring.cloud.s3.S3Template;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

@Service
public class FileServiceS3Impl implements FileService {

    private final S3Template s3Template;
    private final S3Presigner s3Presigner;

    public FileServiceS3Impl(S3Template s3Template, S3Presigner s3Presigner) {
        this.s3Template = s3Template;
        this.s3Presigner = s3Presigner;
    }

    @Override
    public InputStream getFile(String bucketName, String key) {
        try {
            return s3Template.download(bucketName, key).getInputStream();
        } catch (IOException e) {
            throw new DoesNotExistException("File not found");
        }
    }

    @Override
    public Boolean uploadFile(String bucketName, String key, InputStream file) {
        var uploaded = s3Template.upload(bucketName, key, file);
        return uploaded.exists();
    }

    @Override
    public String generatePresignedUrl(String bucketName, String key) {
        var presignedRequest = s3Presigner.presignGetObject(
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMillis(300000L))
                        .getObjectRequest(b -> b.bucket(bucketName).key(key))
                        .build()
        );

        return presignedRequest.url().toString();
    }
}