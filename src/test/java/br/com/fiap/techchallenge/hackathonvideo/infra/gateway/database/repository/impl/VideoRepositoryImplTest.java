package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl;

import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VideoRepositoryImplTest {

    @Mock
    private DynamoDbTemplate dynamoDbTemplate;

    @InjectMocks
    private VideoRepositoryImpl videoRepository;

    private AutoCloseable closeable;

    private UUID videoId;
    private UUID userId;
    private VideoEntity videoEntity;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        videoId = UUID.randomUUID();
        userId = UUID.randomUUID();

        videoEntity = new VideoEntity();
        videoEntity.setId(videoId);
        videoEntity.setUserId(userId);
        videoEntity.setUserEmail("test@example.com");
        videoEntity.setBucketName("bucket");
        videoEntity.setVideoKey("videoKey");
        videoEntity.setFramesKey("framesKey");
        videoEntity.setVideoName("video.mp4");
        videoEntity.setCreatedAt(LocalDateTime.now());
        videoEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void shouldSaveVideoEntity() {
        when(dynamoDbTemplate.save(videoEntity)).thenReturn(videoEntity);

        var result = videoRepository.save(videoEntity);

        assertNotNull(result);
        assertEquals(videoId, result.getId());
        verify(dynamoDbTemplate).save(videoEntity);
    }

    @Test
    void shouldFindById() {
        when(dynamoDbTemplate.load(any(Key.class), eq(VideoEntity.class))).thenReturn(videoEntity);

        var result = videoRepository.findById(videoId);

        assertTrue(result.isPresent());
        assertEquals(videoId, result.get().getId());
        verify(dynamoDbTemplate).load(any(Key.class), eq(VideoEntity.class));
    }

    @Test
    void shouldReturnEmptyWhenNotFoundById() {
        when(dynamoDbTemplate.load(any(Key.class), eq(VideoEntity.class))).thenReturn(null);

        var result = videoRepository.findById(videoId);

        assertTrue(result.isEmpty());
        verify(dynamoDbTemplate).load(any(Key.class), eq(VideoEntity.class));
    }

    @Test
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

        var response = videoRepository.findAllByUserId(userId, 10, null);

        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals(lastKey, response.getLastEvaluatedAttributeValue());

        verify(dynamoDbTemplate).query(any(), eq(VideoEntity.class), eq("UserCreatedAtIndex"));
    }

    @Test
    void shouldFindAllByUserIdWithStartKey() {
        Map<String, AttributeValue> exclusiveStartKey = Map.of("startKey", AttributeValue.fromS("value"));
        List<VideoEntity> videos = List.of(videoEntity);
        Page<VideoEntity> page = Page.builder(VideoEntity.class)
                .items(videos)
                .build();

        PageIterable<VideoEntity> pageIterable = PageIterable.create(() -> List.of(page).iterator());

        when(dynamoDbTemplate.query(any(), eq(VideoEntity.class), eq("UserCreatedAtIndex")))
                .thenReturn(pageIterable);

        var response = videoRepository.findAllByUserId(userId, 5, exclusiveStartKey);

        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertNull(response.getLastEvaluatedAttributeValue());

        verify(dynamoDbTemplate).query(any(), eq(VideoEntity.class), eq("UserCreatedAtIndex"));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
