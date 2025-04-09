package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Audit;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Metadata;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable.CustomPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ListFilesUseCaseImplTest {

    private VideoPersistence videoPersistence;
    private ListFilesUseCaseImpl listFilesUseCase;

    @BeforeEach
    void setUp() {
        videoPersistence = mock(VideoPersistence.class);
        listFilesUseCase = new ListFilesUseCaseImpl(videoPersistence);
    }

    @Test
    void shouldReturnCustomPageWhenGetFilesIsCalled() {
        UUID userId = UUID.randomUUID();
        int pageSize = 10;
        String exclusiveStartKey = "startKey";

        User user = new User(userId, "usuario@exemplo.com");
        Audit audit = new Audit(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        Metadata metadata = new Metadata("video1", 10, 100L);

        Video video1 = new Video(
                UUID.randomUUID(), user, "video1.mp4", "frames1.zip",
                ProcessStatus.NEW, audit, metadata
        );
        Video video2 = new Video(
                UUID.randomUUID(), user, "video2.mp4", "frames2.zip",
                ProcessStatus.PROCESSING, audit, metadata
        );

        List<Video> videos = List.of(video1, video2);
        String lastEvaluatedKey = "lastKey";
        CustomPage expectedPage = new CustomPage(videos, lastEvaluatedKey);

        when(videoPersistence.findAllByUserId(userId, pageSize, exclusiveStartKey))
                .thenReturn(expectedPage);

        CustomPage result = listFilesUseCase.getFiles(userId, pageSize, exclusiveStartKey);

        assertEquals(expectedPage, result);
        verify(videoPersistence, times(1)).findAllByUserId(userId, pageSize, exclusiveStartKey);
    }
}
