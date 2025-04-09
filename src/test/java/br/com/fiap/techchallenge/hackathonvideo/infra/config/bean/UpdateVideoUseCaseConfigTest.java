package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.UpdateVideoUseCaseImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl.VideoPersistenceImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.notification.PushNotificationProducerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UpdateVideoUseCaseConfigTest {

    @Mock
    private VideoPersistenceImpl videoPersistence;

    @Mock
    private PushNotificationProducerImpl pushNotificationProducer;

    @InjectMocks
    private UpdateVideoUseCaseConfig updateVideoUseCaseConfig;

    @Test
    @DisplayName("Should Create a Singleton Instance Of UpdateVideoUseCaseImpl")
    void shouldCreateSingletonInstanceOfUpdateVideoUseCaseImpl() {
        var updateVideoUseCaseImpl = updateVideoUseCaseConfig.updateVideoUseCase(videoPersistence, pushNotificationProducer);

        assertNotNull(updateVideoUseCaseImpl);
        assertNotNull(videoPersistence);
        assertNotNull(pushNotificationProducer);
        assertInstanceOf(UpdateVideoUseCaseImpl.class, updateVideoUseCaseImpl);
    }
}