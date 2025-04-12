package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller;

import br.com.fiap.techchallenge.hackathonvideo.application.usecase.ListFilesUseCase;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedDownloadUseCase;
import br.com.fiap.techchallenge.hackathonvideo.application.usecase.PresignedUploadUseCase;
import br.com.fiap.techchallenge.hackathonvideo.domain.enums.PresignedMethods;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.PresignedFile;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable.CustomPage;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedUploadRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoControllerTest {

    @Mock
    private PresignedUploadUseCase presignedUploadUseCase;

    @Mock
    private PresignedDownloadUseCase presignedDownloadUseCase;

    @Mock
    private ListFilesUseCase listFilesUseCase;

    @InjectMocks
    private VideoController controller;

    private UUID userId;

    private PresignedFile presignedFile;

    private PresignedUploadRequestDTO requestDTO;

    private CustomPage page;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Return Presigned Upload Successfully")
    void shouldReturnPresignedUploadSuccessfully() {
        var email = page.videos().getFirst().getUserEmail();
        when(presignedUploadUseCase.presignedUpload(requestDTO, userId, email)).thenReturn(presignedFile);

        var response = controller.presignedUpload(requestDTO, userId, email);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(presignedUploadUseCase).presignedUpload(requestDTO, userId, email);
    }

    @Test
    @DisplayName("Should Return Presigned Download Successfully")
    void shouldReturnPresignedDownloadSuccessfully() {
        when(presignedDownloadUseCase.presignedDownload(presignedFile.getId())).thenReturn(presignedFile);

        var response = controller.presignedDownload(presignedFile.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(presignedDownloadUseCase).presignedDownload(presignedFile.getId());
    }

    @Test
    @DisplayName("Should Return List Files Successfully")
    void shouldReturnListFilesSuccessfully() {
        when(listFilesUseCase.getFiles(userId, 10, null)).thenReturn(page);

        var response = controller.getFiles(userId, 10, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(listFilesUseCase).getFiles(userId, 10, null);
    }

    private void buildArranges() {
        userId = UUID.randomUUID();

        presignedFile = new PresignedFile(UUID.randomUUID(), "https://example.com", PresignedMethods.PUT, Instant.now().plusSeconds(300));

        requestDTO = new PresignedUploadRequestDTO("video.mp4", "video/mp4");

        var video = new Video("video.mp4", new User(userId, "user@example.com"));
        page = new CustomPage(List.of(video), "abc123");

    }
}
