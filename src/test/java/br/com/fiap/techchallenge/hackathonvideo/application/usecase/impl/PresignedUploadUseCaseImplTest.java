package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.filestorage.FileService;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.PresignedMethods;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.*;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedUploadRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresignedUploadUseCaseImplTest {

    @Mock
    private VideoPersistence videoPersistence;

    @Mock
    private FileService fileService;

    @InjectMocks
    private PresignedUploadUseCaseImpl useCase;

    private Video video;

    private PresignedUploadRequestDTO dto;

    private PresignedFile expectedPresignedFile;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Generate Presigned URL Successfully")
    void shouldGeneratePresignedUrlSuccessfully() {
        when(videoPersistence.save(any(Video.class))).thenReturn(video);
        when(fileService.generateUploadPresignedUrl(video, dto.fileType())).thenReturn(expectedPresignedFile);

        var result = useCase.presignedUpload(dto, video.getUserId(), video.getUserEmail());

        assertNotNull(result);
        assertEquals(expectedPresignedFile.getId(), result.getId());
        assertEquals(expectedPresignedFile.getUrl(), result.getUrl());
        assertEquals(expectedPresignedFile.getMethod(), result.getMethod());
        assertTrue(result.getSecondsToExpire() > 0);

        verify(videoPersistence).save(any(Video.class));
        verify(fileService).generateUploadPresignedUrl(video, dto.fileType());
    }

    private void buildArranges() {
        video = new Video("video.mp4", new User(UUID.randomUUID(), "test@email.com"));

        dto = new PresignedUploadRequestDTO(video.getVideoName(), "video/mp4");

        expectedPresignedFile = new PresignedFile(
                video.getId(),
                "https://fake-url.com/upload",
                PresignedMethods.PUT,
                Instant.now().plusSeconds(3600)
        );
    }
}
