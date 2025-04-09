package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonvideo.application.filestorage.FileService;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PresignedDownloadUseCaseImplTest {

    private VideoPersistence videoPersistence;
    private FileService fileService;
    private PresignedDownloadUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        videoPersistence = mock(VideoPersistence.class);
        fileService = mock(FileService.class);
        useCase = new PresignedDownloadUseCaseImpl(videoPersistence, fileService);
    }

    @Test
    void testPresignedDownload_Success() {
        UUID videoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String email = "test@email.com";

        User user = new User(userId, email);
        Video video = new Video(videoId, user, "videoKey", "framesKey",
                ProcessStatus.PROCESSED, new Audit(Instant.now().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(), Instant.now().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()), new Metadata("video.mp4", 10, 100L));

        PresignedFile presignedFile = new PresignedFile(videoId, "https://example.com", null, Instant.now().plusSeconds(3600));

        when(videoPersistence.findById(videoId)).thenReturn(Optional.of(video));
        when(fileService.generateDownloadPresignedUrl(video)).thenReturn(presignedFile);

        PresignedFile result = useCase.presignedDownload(videoId);

        assertNotNull(result);
        assertEquals(presignedFile.getUrl(), result.getUrl());
        verify(videoPersistence).findById(videoId);
        verify(fileService).generateDownloadPresignedUrl(video);
    }

    @Test
    void testPresignedDownload_VideoNotFound() {
        UUID videoId = UUID.randomUUID();
        when(videoPersistence.findById(videoId)).thenReturn(Optional.empty());

        DoesNotExistException ex = assertThrows(DoesNotExistException.class,
                () -> useCase.presignedDownload(videoId));

        assertEquals("Zip File not found", ex.getMessage());
        verify(videoPersistence).findById(videoId);
        verify(fileService, never()).generateDownloadPresignedUrl(any());
    }

    @Test
    void testPresignedDownload_VideoStatusFailed() {
        assertThrowsByStatus(ProcessStatus.FAILED, "An error happened when processing the video, try processing again");
    }

    @Test
    void testPresignedDownload_VideoStatusNew() {
        assertThrowsByStatus(ProcessStatus.NEW, "The video has not yet been sent");
    }

    @Test
    void testPresignedDownload_VideoStatusReceived() {
        assertThrowsByStatus(ProcessStatus.RECEIVED, "The video is in processing");
    }

    @Test
    void testPresignedDownload_VideoStatusProcessing() {
        assertThrowsByStatus(ProcessStatus.PROCESSING, "The video is in processing");
    }

    private void assertThrowsByStatus(ProcessStatus status, String expectedMessage) {
        UUID videoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String email = "test@email.com";
        User user = new User(userId, email);

        Video video = new Video(videoId, user, "videoKey", "framesKey",
                status, new Audit(Instant.now().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(), Instant.now().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()), new Metadata("video.mp4", 10, 100L));

        when(videoPersistence.findById(videoId)).thenReturn(Optional.of(video));

        DoesNotExistException ex = assertThrows(DoesNotExistException.class,
                () -> useCase.presignedDownload(videoId));

        assertEquals(expectedMessage, ex.getMessage());
        verify(videoPersistence).findById(videoId);
        verify(fileService, never()).generateDownloadPresignedUrl(any());
    }
}
