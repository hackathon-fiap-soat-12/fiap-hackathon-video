package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.notification.dto;

import br.com.fiap.techchallenge.hackathonvideo.enums.ProcessStatus;

public record PushNotificationDTO(String email,
                                  ProcessStatus status,
                                  String videoName) {
}
