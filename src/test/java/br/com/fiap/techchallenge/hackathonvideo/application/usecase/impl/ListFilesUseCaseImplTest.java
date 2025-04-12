package br.com.fiap.techchallenge.hackathonvideo.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonvideo.application.persistence.VideoPersistence;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Audit;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Metadata;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable.CustomPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListFilesUseCaseImplTest {

    @Mock
    private VideoPersistence videoPersistence;

    @InjectMocks
    private ListFilesUseCaseImpl listFilesUseCase;

    private Video video1;

    private Video video2;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Return CustomPage When GetFiles Is Called")
    void shouldReturnCustomPageWhenGetFilesIsCalled() {
        int pageSize = 10;
        String exclusiveStartKey = "startKey";


        List<Video> videos = List.of(video1, video2);
        String lastEvaluatedKey = "lastKey";
        CustomPage expectedPage = new CustomPage(videos, lastEvaluatedKey);

        when(videoPersistence.findAllByUserId(video1.getUserId(), pageSize, exclusiveStartKey))
                .thenReturn(expectedPage);

        CustomPage result = listFilesUseCase.getFiles(video1.getUserId(), pageSize, exclusiveStartKey);

        assertEquals(expectedPage, result);
        verify(videoPersistence, times(1)).findAllByUserId(video1.getUserId(), pageSize, exclusiveStartKey);
    }

    private void buildArranges() {
        User user = new User(UUID.randomUUID(), "usuario@exemplo.com");
        Audit audit = new Audit(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        Metadata metadata = new Metadata("video1", 10, 100L);

        video1 = new Video(
                UUID.randomUUID(), user, "video1.mp4", "frames1.zip",
                ProcessStatus.NEW, audit, metadata
        );
        video2 = new Video(
                UUID.randomUUID(), user, "video2.mp4", "frames2.zip",
                ProcessStatus.PROCESSING, audit, metadata
        );
    }
}
