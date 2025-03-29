package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.UploadVideoUseCaseImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl.VideoPersistenceImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.process.impl.ProcessProducerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadVideoUseCaseConfig {

    @Bean
    public UploadVideoUseCaseImpl uploadVideoUseCase(VideoPersistenceImpl videoPersistence, ProcessProducerImpl processProducer) {
        return new UploadVideoUseCaseImpl(videoPersistence, processProducer);
    }
}
