package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.PresignedDownloadUseCaseImpl;
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
class PresignedDownloadUseCaseConfigTest {

    @Mock
    private VideoPersistenceImpl videoPersistence;

    @Mock
    private FileServiceS3Impl fileService;

    @InjectMocks
    private PresignedDownloadUseCaseConfig presignedDownloadUseCaseConfig;

    @Test
    @DisplayName("Should Create a Singleton Instance Of PresignedDownloadUseCaseImpl")
    void shouldCreateSingletonInstanceOfPresignedDownloadUseCaseImpl() {
        var presignedDownloadUseCaseImpl = presignedDownloadUseCaseConfig.presignedDownloadUseCase(videoPersistence, fileService);

        assertNotNull(presignedDownloadUseCaseImpl);
        assertNotNull(videoPersistence);
        assertNotNull(fileService);
        assertInstanceOf(PresignedDownloadUseCaseImpl.class, presignedDownloadUseCaseImpl);
    }
}