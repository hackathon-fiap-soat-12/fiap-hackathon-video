package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.filestorage.FileService;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.PresignedMethods;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedUploadRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PresignedUploadUseCaseImplTest {

    private VideoPersistence videoPersistence;
    private FileService fileService;
    private PresignedUploadUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        videoPersistence = mock(VideoPersistence.class);
        fileService = mock(FileService.class);
        useCase = new PresignedUploadUseCaseImpl(videoPersistence, fileService);
    }

    @Test
    void shouldGeneratePresignedUrlSuccessfully() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String email = "test@example.com";
        String fileName = "video.mp4";
        String fileType = "video/mp4";

        PresignedUploadRequestDTO dto = new PresignedUploadRequestDTO(fileName, fileType);

        Video video = new Video(fileName, new User(userId, email));
        when(videoPersistence.save(any(Video.class))).thenReturn(video);

        PresignedFile expectedPresignedFile = new PresignedFile(
                video.getId(),
                "https://fake-url.com/upload",
                PresignedMethods.PUT,
                Instant.now().plusSeconds(3600)
        );
        when(fileService.generateUploadPresignedUrl(video, fileType)).thenReturn(expectedPresignedFile);

        // Act
        PresignedFile result = useCase.presignedUpload(dto, userId, email);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPresignedFile.getId(), result.getId());
        assertEquals(expectedPresignedFile.getUrl(), result.getUrl());
        assertEquals(expectedPresignedFile.getMethod(), result.getMethod());
        assertTrue(result.getSecondsToExpire() > 0);

        // Verifica se os mocks foram chamados com os argumentos esperados
        verify(videoPersistence).save(any(Video.class));
        verify(fileService).generateUploadPresignedUrl(video, fileType);
    }
}
