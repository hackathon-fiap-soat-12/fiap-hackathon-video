package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.process.dto;

import java.util.UUID;

public record VideoToProcessDTO(UUID id,
                                String bucketName,
                                String key) {
}
