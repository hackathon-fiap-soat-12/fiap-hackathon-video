package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.UpdateVideoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateVideoUseCaseConfig {

    @Bean
    public UpdateVideoUseCaseImpl updateVideoUseCase() {
        return new UpdateVideoUseCaseImpl();
    }
}
