package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.producer.process.ProcessProducer;
import br.com.fiap.techchallenge.hackathonvideo.application.producer.process.dto.VideoToProcessDTO;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Audit;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Metadata;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.uploadvideo.dto.UploadVideoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadVideoUseCaseImplTest {

    @Mock
    private VideoPersistence videoPersistence;

    @Mock
    private ProcessProducer processProducer;

    @InjectMocks
    private UploadVideoUseCaseImpl uploadVideoUseCase;

    private Video video;

    private UploadVideoDTO dto;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Receive Video And Send To Process")
    void shouldReceiveVideoAndSendToProcess() {
        when(videoPersistence.findById(video.getId())).thenReturn(Optional.of(video));
        when(videoPersistence.save(any(Video.class))).thenReturn(video);

        uploadVideoUseCase.receiveToProcess(dto);

        verify(videoPersistence).findById(video.getId());
        verify(videoPersistence).save(video);

        ArgumentCaptor<VideoToProcessDTO> captor = ArgumentCaptor.forClass(VideoToProcessDTO.class);
        verify(processProducer).sendToProcess(captor.capture());

        var sentDTO = captor.getValue();
        assertEquals(video.getId(), sentDTO.id());
        assertEquals(video.getBucketName(), sentDTO.bucketName());
        assertEquals(video.getVideoKey(), sentDTO.key());
    }

    @Test
    @DisplayName("Should Throw Exception When Video Not Found")
    void shouldThrowExceptionWhenVideoNotFound() {
        when(videoPersistence.findById(video.getId())).thenReturn(Optional.empty());

        assertThrows(DoesNotExistException.class, () -> uploadVideoUseCase.receiveToProcess(dto));

        verify(videoPersistence, never()).save(any());
        verify(processProducer, never()).sendToProcess(any());
    }

    private void buildArranges() {
        var user = new User(UUID.randomUUID(), "user@example.com");
        var audit = new Audit(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        var metadata = new Metadata("video.mp4", 10, 100L);

        video = new Video(
                UUID.randomUUID(), user, "video1.mp4", "frames1.zip",
                ProcessStatus.PROCESSED, audit, metadata
        );

        dto = new UploadVideoDTO(video.getId());
    }
}
