package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.producer.notification.PushNotificationProducer;
import br.com.fiap.techchallenge.hackathonvideo.application.producer.notification.dto.PushNotificationDTO;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Audit;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo.dto.UpdateVideoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateVideoUseCaseImplTest {

    private VideoPersistence videoPersistence;
    private PushNotificationProducer pushNotificationProducer;
    private UpdateVideoUseCaseImpl updateVideoUseCase;

    @BeforeEach
    void setUp() {
        videoPersistence = mock(VideoPersistence.class);
        pushNotificationProducer = mock(PushNotificationProducer.class);
        updateVideoUseCase = new UpdateVideoUseCaseImpl(videoPersistence, pushNotificationProducer);
    }

    @Test
    void shouldUpdateVideoAndSendNotificationWhenStatusIsNotProcessing() {
        UUID videoId = UUID.randomUUID();
        ProcessStatus newStatus = ProcessStatus.RECEIVED;
        User user = new User(UUID.randomUUID(), "usuario@teste.com");
        Video video = new Video(videoId, user, "videoKey", "framesKey", "meuVideo.mp4", ProcessStatus.NEW, new Audit(LocalDateTime.now(), LocalDateTime.now()));
        UpdateVideoDTO dto = new UpdateVideoDTO(videoId, newStatus);

        when(videoPersistence.findById(videoId)).thenReturn(Optional.of(video));

        updateVideoUseCase.updateVideo(dto);

        verify(videoPersistence).update(video);
        ArgumentCaptor<PushNotificationDTO> captor = ArgumentCaptor.forClass(PushNotificationDTO.class);
        verify(pushNotificationProducer).sendToPushNotification(captor.capture());

        PushNotificationDTO sentNotification = captor.getValue();
        assertEquals("usuario@teste.com", sentNotification.email());
        assertEquals(ProcessStatus.RECEIVED, sentNotification.status());
        assertEquals("meuVideo.mp4", sentNotification.videoName());
    }

    @Test
    void shouldNotSendNotificationWhenStatusIsProcessing() {
        UUID videoId = UUID.randomUUID();
        ProcessStatus newStatus = ProcessStatus.PROCESSING;
        User user = new User(UUID.randomUUID(), "usuario@teste.com");
        Video video = new Video(videoId, user, "videoKey", "framesKey", "video.mp4", ProcessStatus.NEW, new Audit(LocalDateTime.now(), LocalDateTime.now()));
        UpdateVideoDTO dto = new UpdateVideoDTO(videoId, newStatus);

        when(videoPersistence.findById(videoId)).thenReturn(Optional.of(video));

        updateVideoUseCase.updateVideo(dto);

        verify(videoPersistence).update(video);
        verify(pushNotificationProducer, never()).sendToPushNotification(any());
    }

    @Test
    void shouldThrowExceptionWhenVideoIsNotFound() {
        UUID videoId = UUID.randomUUID();
        UpdateVideoDTO dto = new UpdateVideoDTO(videoId, ProcessStatus.RECEIVED);

        when(videoPersistence.findById(videoId)).thenReturn(Optional.empty());

        assertThrows(DoesNotExistException.class, () -> updateVideoUseCase.updateVideo(dto));

        verify(videoPersistence, never()).update(any());
        verify(pushNotificationProducer, never()).sendToPushNotification(any());
    }
}
