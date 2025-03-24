package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.PresignedUploadUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PresignedUploadUseCaseConfig {

    @Bean
    public PresignedUploadUseCaseImpl presignedUploadUseCase() {
        return new PresignedUploadUseCaseImpl();
    }

}
