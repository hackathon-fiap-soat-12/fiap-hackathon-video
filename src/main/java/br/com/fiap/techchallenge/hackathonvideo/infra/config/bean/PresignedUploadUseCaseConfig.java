package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.PresignedUploadUseCaseImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl.VideoPersistenceImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage.FileServiceS3Impl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PresignedUploadUseCaseConfig {

    @Bean
    public PresignedUploadUseCaseImpl presignedUploadUseCase(VideoPersistenceImpl videoPersistence, FileServiceS3Impl fileService) {
        return new PresignedUploadUseCaseImpl(videoPersistence, fileService);
    }

}
