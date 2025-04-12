package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.producer.notification.PushNotificationProducer;
import br.com.fiap.techchallenge.hackathonvideo.application.producer.notification.dto.PushNotificationDTO;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Audit;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Metadata;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo.dto.UpdateVideoDTO;
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
class UpdateVideoUseCaseImplTest {

    @Mock
    private VideoPersistence videoPersistence;

    @Mock
    private PushNotificationProducer pushNotificationProducer;

    @InjectMocks
    private UpdateVideoUseCaseImpl updateVideoUseCase;

    private Video video;

    private UpdateVideoDTO dto;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Update Video And Send Notification When Status Is Not Processing")
    void shouldUpdateVideoAndSendNotificationWhenStatusIsNotProcessing() {
        when(videoPersistence.findById(video.getId())).thenReturn(Optional.of(video));

        updateVideoUseCase.updateVideo(dto);

        verify(videoPersistence).update(video);
        ArgumentCaptor<PushNotificationDTO> captor = ArgumentCaptor.forClass(PushNotificationDTO.class);
        verify(pushNotificationProducer).sendToPushNotification(captor.capture());

        var sentNotification = captor.getValue();
        assertEquals("usuario@teste.com", sentNotification.email());
        assertEquals(ProcessStatus.RECEIVED, sentNotification.status());
        assertEquals("meuVideo.mp4", sentNotification.videoName());
    }

    @Test
    @DisplayName("Should Not Send Notification When Status Is Processing")
    void shouldNotSendNotificationWhenStatusIsProcessing() {
        dto = new UpdateVideoDTO(video.getId(), ProcessStatus.PROCESSING, video.getQtdFrames(), video.getSizeInBytes());

        when(videoPersistence.findById(video.getId())).thenReturn(Optional.of(video));

        updateVideoUseCase.updateVideo(dto);

        verify(videoPersistence).update(video);
        verify(pushNotificationProducer, never()).sendToPushNotification(any());
    }

    @Test
    @DisplayName("Should Throw Exception When Video Is Not Found")
    void shouldThrowExceptionWhenVideoIsNotFound() {
        dto = new UpdateVideoDTO(video.getId(), ProcessStatus.RECEIVED, video.getQtdFrames(), video.getSizeInBytes());

        when(videoPersistence.findById(video.getId())).thenReturn(Optional.empty());

        assertThrows(DoesNotExistException.class, () -> updateVideoUseCase.updateVideo(dto));

        verify(videoPersistence, never()).update(any());
        verify(pushNotificationProducer, never()).sendToPushNotification(any());
    }

    private void buildArranges() {
        video = new Video(UUID.randomUUID(), new User(UUID.randomUUID(), "usuario@teste.com"),
                "videoKey", "framesKey", ProcessStatus.NEW,
                new Audit(LocalDateTime.now(), LocalDateTime.now()),
                new Metadata("meuVideo.mp4", 10, 100L));

        dto = new UpdateVideoDTO(video.getId(), ProcessStatus.RECEIVED, video.getQtdFrames(), video.getSizeInBytes());
    }
}
