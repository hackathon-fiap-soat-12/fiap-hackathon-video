package br.com.fiap.techchallenge.hackathonvideo.application.producer.notification.dto;

import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;

public record PushNotificationDTO(String email,
                                  ProcessStatus status,
                                  String videoName) {
}
