package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.UploadVideoUseCaseImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl.VideoPersistenceImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.producer.process.ProcessProducerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UploadVideoUseCaseConfigTest {

    @Mock
    private VideoPersistenceImpl videoPersistence;

    @Mock
    private ProcessProducerImpl processProducer;

    @InjectMocks
    private UploadVideoUseCaseConfig uploadVideoUseCaseConfig;

    @Test
    @DisplayName("Should Create a Singleton Instance Of UploadVideoUseCaseImpl")
    void shouldCreateSingletonInstanceOfUploadVideoUseCaseImpl() {
        var uploadVideoUseCaseImpl = uploadVideoUseCaseConfig.uploadVideoUseCase(videoPersistence, processProducer);

        assertNotNull(uploadVideoUseCaseImpl);
        assertNotNull(videoPersistence);
        assertNotNull(processProducer);
        assertInstanceOf(UploadVideoUseCaseImpl.class, uploadVideoUseCaseImpl);
    }

}