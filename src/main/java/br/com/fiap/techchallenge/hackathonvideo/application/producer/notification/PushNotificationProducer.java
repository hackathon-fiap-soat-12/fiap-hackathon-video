package br.com.fiap.techchallenge.hackathonvideo.application.producer.notification;

import br.com.fiap.techchallenge.hackathonvideo.application.producer.notification.dto.PushNotificationDTO;

public interface PushNotificationProducer {
    void sendToPushNotification(PushNotificationDTO dto);
}
