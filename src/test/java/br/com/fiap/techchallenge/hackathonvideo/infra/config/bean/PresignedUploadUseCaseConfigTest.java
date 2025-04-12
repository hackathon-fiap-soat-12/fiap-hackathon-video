package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.PresignedUploadUseCaseImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl.VideoPersistenceImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.filestorage.FileServiceS3Impl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PresignedUploadUseCaseConfigTest {

    @Mock
    private VideoPersistenceImpl videoPersistence;

    @Mock
    private FileServiceS3Impl fileService;

    @InjectMocks
    private PresignedUploadUseCaseConfig presignedUploadUseCaseConfig;

    @Test
    @DisplayName("Should Create a Singleton Instance Of PresignedUploadUseCaseImpl")
    void shouldCreateSingletonInstanceOfPresignedUploadUseCaseImpl() {
        var presignedUploadUseCaseImpl = presignedUploadUseCaseConfig.presignedUploadUseCase(videoPersistence, fileService);

        assertNotNull(presignedUploadUseCaseImpl);
        assertNotNull(videoPersistence);
        assertNotNull(fileService);
        assertInstanceOf(PresignedUploadUseCaseImpl.class, presignedUploadUseCaseImpl);
    }
}