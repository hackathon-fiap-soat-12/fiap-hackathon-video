package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.PresignedDownloadUseCaseImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl.VideoPersistenceImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage.impl.FileServiceS3Impl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PresignedDownloadUseCaseConfig {

    @Bean
    public PresignedDownloadUseCaseImpl presignedDownloadUseCase(VideoPersistenceImpl videoPersistence, FileServiceS3Impl fileService) {
        return new PresignedDownloadUseCaseImpl(videoPersistence, fileService);
    }
}
