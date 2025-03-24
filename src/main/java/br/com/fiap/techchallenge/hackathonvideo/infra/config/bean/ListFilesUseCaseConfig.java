package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.ListFilesUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListFilesUseCaseConfig {

    @Bean
    public ListFilesUseCaseImpl listFilesUseCase() {
        return new ListFilesUseCaseImpl();
    }
}
