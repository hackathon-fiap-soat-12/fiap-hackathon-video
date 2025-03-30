package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage.impl;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage.FileService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Service
public class FileServiceS3Impl implements FileService {

    private final S3Presigner s3Presigner;

    public FileServiceS3Impl(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    @Override
    public PresignedFile generatePresignedUrl(String bucketName, String key) {
        var presignedRequest = s3Presigner.presignGetObject(
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMillis(300000L))
                        .getObjectRequest(b -> b.bucket(bucketName).key(key))
                        .build()
        );

        return new PresignedFile(presignedRequest.url().toString(), presignedRequest.expiration());
    }
}