package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage;

import br.com.fiap.techchallenge.hackathonvideo.domain.enums.PresignedMethods;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceS3ImplTest {

    @Mock
    private S3Presigner s3Presigner;

    @Mock
    private PresignedPutObjectRequest presignedPutObjectRequest;

    @Mock
    private PresignedGetObjectRequest presignedGetObjectRequest;

    @InjectMocks
    private FileServiceS3Impl fileService;

    private Video video;

    private String fileType;

    private URL fakeUrl;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Generate Upload Presigned URL")
    void shouldGenerateUploadPresignedUrl() {
        when(presignedPutObjectRequest.url()).thenReturn(fakeUrl);
        when(presignedPutObjectRequest.expiration()).thenReturn(java.time.Instant.now().plusSeconds(300));
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class))).thenReturn(presignedPutObjectRequest);

        var result = fileService.generateUploadPresignedUrl(video, fileType);

        assertEquals(video.getId(), result.getId());
        assertEquals(fakeUrl.toString(), result.getUrl());
        assertEquals(299, result.getSecondsToExpire());
        assertEquals(PresignedMethods.PUT, result.getMethod());
    }

    @Test
    @DisplayName("Should Generate Download Presigned URL")
    void shouldGenerateDownloadPresignedUrl() {
        when(presignedGetObjectRequest.url()).thenReturn(fakeUrl);
        when(presignedGetObjectRequest.expiration()).thenReturn(java.time.Instant.now().plusSeconds(300));
        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedGetObjectRequest);

        var result = fileService.generateDownloadPresignedUrl(video);

        assertEquals(video.getId(), result.getId());
        assertEquals(fakeUrl.toString(), result.getUrl());
        assertEquals(299, result.getSecondsToExpire());
        assertEquals(PresignedMethods.GET, result.getMethod());
    }

    private void buildArranges() {
        video = new Video("video.mp4", new User(UUID.randomUUID(), "email@email.com"));
        fileType = "video/mp4";
        fakeUrl = generateFakeUrl();
    }

    private URL generateFakeUrl() {
        try {
            return URI.create("https://example.com/fake-url").toURL();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create fake URL");
        }
    }
}
