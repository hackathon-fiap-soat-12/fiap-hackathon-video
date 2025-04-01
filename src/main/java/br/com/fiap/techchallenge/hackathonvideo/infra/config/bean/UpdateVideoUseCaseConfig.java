package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.UpdateVideoUseCaseImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl.VideoPersistenceImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.notification.PushNotificationProducerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateVideoUseCaseConfig {

    @Bean
    public UpdateVideoUseCaseImpl updateVideoUseCase(VideoPersistenceImpl videoPersistence, PushNotificationProducerImpl pushNotificationProducer) {
        return new UpdateVideoUseCaseImpl(videoPersistence, pushNotificationProducer);
    }
}
