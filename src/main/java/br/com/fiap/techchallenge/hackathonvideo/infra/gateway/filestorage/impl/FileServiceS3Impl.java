package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage.impl;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage.FileService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
public class FileServiceS3Impl implements FileService {

    private final S3Presigner s3Presigner;

    public FileServiceS3Impl(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    @Override
    public PresignedFile generateUploadPresignedUrl(String bucketName, String key) {
        var presignedPutRequest = s3Presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMillis(300000L))
                        .putObjectRequest(b -> b.bucket(bucketName).key(key))
                        .build()
        );

        return new PresignedFile(presignedPutRequest.url().toString(), presignedPutRequest.expiration());
    }

    @Override
    public PresignedFile generateDownloadPresignedUrl(String bucketName, String key) {
        var presignedGetRequest = s3Presigner.presignGetObject(
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMillis(300000L))
                        .getObjectRequest(b -> b.bucket(bucketName).key(key))
                        .build()
        );

        return new PresignedFile(presignedGetRequest.url().toString(), presignedGetRequest.expiration());
    }
}