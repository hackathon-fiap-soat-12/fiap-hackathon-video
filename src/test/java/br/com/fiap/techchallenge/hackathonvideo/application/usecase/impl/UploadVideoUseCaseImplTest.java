package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.producer.process.ProcessProducer;
import br.com.fiap.techchallenge.hackathonvideo.application.producer.process.dto.VideoToProcessDTO;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Audit;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.uploadvideo.dto.UploadVideoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UploadVideoUseCaseImplTest {

    private VideoPersistence videoPersistence;
    private ProcessProducer processProducer;
    private UploadVideoUseCaseImpl uploadVideoUseCase;

    @BeforeEach
    void setUp() {
        videoPersistence = mock(VideoPersistence.class);
        processProducer = mock(ProcessProducer.class);
        uploadVideoUseCase = new UploadVideoUseCaseImpl(videoPersistence, processProducer);
    }

    @Test
    void shouldReceiveVideoAndSendToProcess() {
        UUID videoId = UUID.randomUUID();
        User user = new User(UUID.randomUUID(), "user@example.com");
        Video video = new Video(videoId, user, "videoKey", "framesKey", "video.mp4", ProcessStatus.NEW, new Audit(LocalDateTime.now(), LocalDateTime.now()));
        UploadVideoDTO dto = new UploadVideoDTO(videoId);

        when(videoPersistence.findById(videoId)).thenReturn(Optional.of(video));
        when(videoPersistence.save(any(Video.class))).thenReturn(video);

        uploadVideoUseCase.receiveToProcess(dto);

        verify(videoPersistence).findById(videoId);
        verify(videoPersistence).save(video);

        ArgumentCaptor<VideoToProcessDTO> captor = ArgumentCaptor.forClass(VideoToProcessDTO.class);
        verify(processProducer).sendToProcess(captor.capture());

        VideoToProcessDTO sentDTO = captor.getValue();
        assertEquals(video.getId(), sentDTO.id());
        assertEquals(video.getBucketName(), sentDTO.bucketName());
        assertEquals(video.getVideoKey(), sentDTO.key());
    }

    @Test
    void shouldThrowExceptionWhenVideoNotFound() {
        UUID videoId = UUID.randomUUID();
        UploadVideoDTO dto = new UploadVideoDTO(videoId);

        when(videoPersistence.findById(videoId)).thenReturn(Optional.empty());

        assertThrows(DoesNotExistException.class, () -> uploadVideoUseCase.receiveToProcess(dto));

        verify(videoPersistence, never()).save(any());
        verify(processProducer, never()).sendToProcess(any());
    }
}
