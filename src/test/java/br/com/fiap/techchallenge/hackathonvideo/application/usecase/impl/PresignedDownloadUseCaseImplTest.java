package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.filestorage.FileService;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.PresignedMethods;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresignedDownloadUseCaseImplTest {

    @Mock
    private VideoPersistence videoPersistence;

    @Mock
    private FileService fileService;

    @InjectMocks
    private PresignedDownloadUseCaseImpl useCase;

    private Video video;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    private void buildArranges() {
        var user = new User(UUID.randomUUID(), "test@email.com");
        var audit = new Audit(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        var metadata = new Metadata("video.mp4", 10, 100L);

        video = new Video(
                UUID.randomUUID(), user, "video1.mp4", "frames1.zip",
                ProcessStatus.PROCESSED, audit, metadata
        );
    }

    @Test
    @DisplayName("Should return presigned file when video is processed")
    void shouldReturnPresignedFileWhenVideoIsProcessed() {
        var presignedFile = new PresignedFile(video.getId(), "https://example.com", PresignedMethods.GET, Instant.now().plusSeconds(3600));

        when(videoPersistence.findById(video.getId())).thenReturn(Optional.of(video));
        when(fileService.generateDownloadPresignedUrl(video)).thenReturn(presignedFile);

        var result = useCase.presignedDownload(video.getId());

        assertNotNull(result);
        assertEquals(presignedFile.getUrl(), result.getUrl());
        verify(videoPersistence).findById(video.getId());
        verify(fileService).generateDownloadPresignedUrl(video);
    }

    @Test
    @DisplayName("Should throw exception when video not found")
    void shouldThrowExceptionWhenVideoNotFound() {
        when(videoPersistence.findById(video.getId())).thenReturn(Optional.empty());

        var ex = assertThrows(Exception.class,
                () -> useCase.presignedDownload(video.getId()));

        assertEquals("Zip File not found", ex.getMessage());
        verify(videoPersistence).findById(video.getId());
        verify(fileService, never()).generateDownloadPresignedUrl(any());
    }

    @Test
    @DisplayName("Should throw exception when video status is FAILED")
    void shouldThrowExceptionWhenVideoStatusIsFailed() {
        assertThrowsByStatus(ProcessStatus.FAILED, "An error happened when processing the video, try processing again");
    }

    @Test
    @DisplayName("Should throw exception when video status is NEW")
    void shouldThrowExceptionWhenVideoStatusIsNew() {
        assertThrowsByStatus(ProcessStatus.NEW, "The video has not yet been sent");
    }

    @Test
    @DisplayName("Should throw exception when video status is RECEIVED")
    void shouldThrowExceptionWhenVideoStatusIsReceived() {
        assertThrowsByStatus(ProcessStatus.RECEIVED, "The video is in processing");
    }

    @Test
    @DisplayName("Should throw exception when video status is PROCESSING")
    void shouldThrowExceptionWhenVideoStatusIsProcessing() {
        assertThrowsByStatus(ProcessStatus.PROCESSING, "The video is in processing");
    }

    private void assertThrowsByStatus(ProcessStatus status, String expectedMessage) {
        video = new Video(UUID.randomUUID(), new User(UUID.randomUUID(), "test@email.com"),
                "videoKey", "framesKey", status,
                new Audit(LocalDateTime.now().minusDays(1), LocalDateTime.now()),
                new Metadata("video.mp4", 10, 100L));

        when(videoPersistence.findById(video.getId())).thenReturn(Optional.of(video));

        var ex = assertThrows(Exception.class,
                () -> useCase.presignedDownload(video.getId()));

        assertEquals(expectedMessage, ex.getMessage());
        verify(videoPersistence).findById(video.getId());
        verify(fileService, never()).generateDownloadPresignedUrl(any());
    }
}
