package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.notification;

import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.notification.dto.PushNotificationDTO;

public interface PushNotificationProducer {
    void sendToPushNotification(PushNotificationDTO dto);
}
