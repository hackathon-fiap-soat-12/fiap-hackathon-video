package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage;

import br.com.fiap.techchallenge.hackathonvideo.application.filestorage.FileService;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static br.com.fiap.techchallenge.hackathonvideo.domain.enums.PresignedMethods.GET;
import static br.com.fiap.techchallenge.hackathonvideo.domain.enums.PresignedMethods.PUT;

@Service
public class FileServiceS3Impl implements FileService {

    private final S3Presigner s3Presigner;
    private static final int MINUTES = 5;

    public FileServiceS3Impl(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    @Override
    public PresignedFile generateUploadPresignedUrl(Video video, String fileType) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", fileType);
        metadata.put("x-amz-meta-id", video.getId().toString());

        var presignedPutRequest = s3Presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(MINUTES))
                        .putObjectRequest(b -> b.bucket(video.getBucketName()).key(video.getVideoKey()).metadata(metadata))
                        .build()
        );

        return new PresignedFile(video.getId(), presignedPutRequest.url().toString(), PUT, presignedPutRequest.expiration());
    }

    @Override
    public PresignedFile generateDownloadPresignedUrl(Video video) {
        var presignedGetRequest = s3Presigner.presignGetObject(
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(MINUTES))
                        .getObjectRequest(b -> b.bucket(video.getBucketName()).key(video.getFramesKey()))
                        .build()
        );

        return new PresignedFile(video.getId(), presignedGetRequest.url().toString(), GET, presignedGetRequest.expiration());
    }
}