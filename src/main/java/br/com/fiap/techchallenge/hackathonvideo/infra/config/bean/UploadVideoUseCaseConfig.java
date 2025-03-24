package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.UploadVideoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadVideoUseCaseConfig {

    @Bean
    public UploadVideoUseCaseImpl uploadVideoUseCase() {
        return new UploadVideoUseCaseImpl();
    }
}
