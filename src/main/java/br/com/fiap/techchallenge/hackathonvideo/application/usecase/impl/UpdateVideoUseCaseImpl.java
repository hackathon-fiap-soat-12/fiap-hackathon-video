package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.UpdateVideoUseCase;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.consumer.updatevideo.dto.UpdateVideoDTO;
import br.com.fiap.techchallenge.hackathonvideo.application.producer.notification.PushNotificationProducer;
import br.com.fiap.techchallenge.hackathonvideo.application.producer.notification.dto.PushNotificationDTO;

public class UpdateVideoUseCaseImpl implements UpdateVideoUseCase {

    private final VideoPersistence videoPersistence;
    private final PushNotificationProducer pushNotificationProducer;

    public UpdateVideoUseCaseImpl(VideoPersistence videoPersistence, PushNotificationProducer pushNotificationProducer) {
        this.videoPersistence = videoPersistence;
        this.pushNotificationProducer = pushNotificationProducer;
    }

    @Override
    public void updateVideo(UpdateVideoDTO updateVideoDto) {
        var video = videoPersistence.findById(updateVideoDto.id())
                .orElseThrow(() -> new DoesNotExistException("Video not found"));

        video.setStatus(updateVideoDto.status());

        videoPersistence.update(video);

        if(!ProcessStatus.PROCESSING.equals(updateVideoDto.status())){
            pushNotificationProducer.sendToPushNotification(new PushNotificationDTO(video.getUserEmail(), updateVideoDto.status(), video.getVideoName()));
        }
    }
}
