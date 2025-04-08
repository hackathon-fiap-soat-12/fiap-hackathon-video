package br.com.fiap.techchallenge.hackathonvideo.infra.config.bean;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl.ListFilesUseCaseImpl;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl.VideoPersistenceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ListFilesUseCaseConfigTest {

    @Mock
    private VideoPersistenceImpl videoPersistence;

    @InjectMocks
    private ListFilesUseCaseConfig listFilesUseCaseConfig;

    @Test
    @DisplayName("Should Create a Singleton Instance Of ListFilesUseCaseImpl")
    void shouldCreateSingletonInstanceOfListFilesUseCaseImpl() {
        var listFilesUseCaseImpl = listFilesUseCaseConfig.listFilesUseCase(videoPersistence);

        assertNotNull(listFilesUseCaseImpl);
        assertNotNull(videoPersistence);
        assertInstanceOf(ListFilesUseCaseImpl.class, listFilesUseCaseImpl);
    }
}