package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl;

import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoRepositoryImplTest {

    @Mock
    private DynamoDbTemplate dynamoDbTemplate;

    @InjectMocks
    private VideoRepositoryImpl videoRepository;

    private VideoEntity videoEntity;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Save Video Entity")
    void shouldSaveVideoEntity() {
        when(dynamoDbTemplate.save(videoEntity)).thenReturn(videoEntity);

        var result = videoRepository.save(videoEntity);

        assertNotNull(result);
        assertEquals(videoEntity.getId(), result.getId());
        verify(dynamoDbTemplate).save(videoEntity);
    }

    @Test
    @DisplayName("Should Find VideoEntity By Id")
    void shouldFindById() {
        when(dynamoDbTemplate.load(any(Key.class), eq(VideoEntity.class))).thenReturn(videoEntity);

        var result = videoRepository.findById(videoEntity.getId());

        assertTrue(result.isPresent());
        assertEquals(videoEntity.getId(), result.get().getId());
        verify(dynamoDbTemplate).load(any(Key.class), eq(VideoEntity.class));
    }

    @Test
    @DisplayName("Should Return Empty Optional When VideoEntity Not Found By Id")
    void shouldReturnEmptyWhenNotFoundById() {
        when(dynamoDbTemplate.load(any(Key.class), eq(VideoEntity.class))).thenReturn(null);

        var result = videoRepository.findById(videoEntity.getId());

        assertTrue(result.isEmpty());
        verify(dynamoDbTemplate).load(any(Key.class), eq(VideoEntity.class));
    }

    @Test
    @DisplayName("Should Find All VideoEntities By UserId Without StartKey")
    void shouldFindAllByUserIdWithoutStartKey() {
        List<VideoEntity> videos = List.of(videoEntity);
        Map<String, AttributeValue> lastKey = Map.of("someKey", AttributeValue.fromS("someValue"));
        Page<VideoEntity> page = Page.builder(VideoEntity.class)
                .items(videos)
                .lastEvaluatedKey(lastKey)
                .build();

        PageIterable<VideoEntity> pageIterable = PageIterable.create(() -> List.of(page).iterator());

        when(dynamoDbTemplate.query(any(), eq(VideoEntity.class), eq("UserCreatedAtIndex")))
                .thenReturn(pageIterable);

        var response = videoRepository.findAllByUserId(videoEntity.getUserId(), 10, null);

        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals(lastKey, response.getLastEvaluatedAttributeValue());

        verify(dynamoDbTemplate).query(any(), eq(VideoEntity.class), eq("UserCreatedAtIndex"));
    }

    @Test
    @DisplayName("Should Find All VideoEntities By UserId With StartKey")
    void shouldFindAllByUserIdWithStartKey() {
        Map<String, AttributeValue> exclusiveStartKey = Map.of("startKey", AttributeValue.fromS("value"));
        List<VideoEntity> videos = List.of(videoEntity);
        Page<VideoEntity> page = Page.builder(VideoEntity.class)
                .items(videos)
                .build();

        PageIterable<VideoEntity> pageIterable = PageIterable.create(() -> List.of(page).iterator());

        when(dynamoDbTemplate.query(any(), eq(VideoEntity.class), eq("UserCreatedAtIndex")))
                .thenReturn(pageIterable);

        var response = videoRepository.findAllByUserId(videoEntity.getUserId(), 5, exclusiveStartKey);

        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertNull(response.getLastEvaluatedAttributeValue());

        verify(dynamoDbTemplate).query(any(), eq(VideoEntity.class), eq("UserCreatedAtIndex"));
    }


    private void buildArranges() {
        videoEntity = new VideoEntity();
        videoEntity.setId(UUID.randomUUID());
        videoEntity.setUserId(UUID.randomUUID());
        videoEntity.setUserEmail("test@example.com");
        videoEntity.setBucketName("bucket");
        videoEntity.setVideoKey("videoKey");
        videoEntity.setFramesKey("framesKey");
        videoEntity.setVideoName("video.mp4");
        videoEntity.setCreatedAt(LocalDateTime.now());
        videoEntity.setUpdatedAt(LocalDateTime.now());
    }
}
