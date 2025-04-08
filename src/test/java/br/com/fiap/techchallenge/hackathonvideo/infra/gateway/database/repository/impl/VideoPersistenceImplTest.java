package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl;

import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Audit;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable.CustomPage;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.VideoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VideoPersistenceImplTest {

    private VideoRepository repository;
    private VideoPersistenceImpl videoPersistence;

    @BeforeEach
    void setup() {
        repository = mock(VideoRepository.class);
        videoPersistence = new VideoPersistenceImpl(new ObjectMapper(), repository);
    }

    @Test
    void testSave() {
        Video video = buildVideo();
        VideoEntity entity = new VideoEntity(video);
        when(repository.save(any(VideoEntity.class))).thenReturn(entity);

        Video saved = videoPersistence.save(video);

        assertNotNull(saved);
        assertEquals(video.getId(), saved.getId());
        verify(repository).save(any(VideoEntity.class));
    }

    @Test
    void testUpdate() {
        Video video = buildVideo();
        VideoEntity updatedEntity = new VideoEntity().update(video);
        when(repository.save(any(VideoEntity.class))).thenReturn(updatedEntity);

        Video result = videoPersistence.update(video);

        assertNotNull(result);
        assertEquals(video.getId(), result.getId());
        verify(repository).save(any(VideoEntity.class));
    }

    @Test
    void testFindByIdFound() {
        Video video = buildVideo();
        VideoEntity entity = new VideoEntity(video);
        when(repository.findById(video.getId())).thenReturn(Optional.of(entity));

        Optional<Video> result = videoPersistence.findById(video.getId());

        assertTrue(result.isPresent());
        assertEquals(video.getId(), result.get().getId());
        verify(repository).findById(video.getId());
    }

    @Test
    void testFindByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Video> result = videoPersistence.findById(id);

        assertFalse(result.isPresent());
        verify(repository).findById(id);
    }

    @Test
    void testFindAllByUserId() {
        UUID userId = UUID.randomUUID();
        Video video = buildVideo();
        VideoEntity entity = new VideoEntity(video);
        Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
        lastEvaluatedKey.put("id", AttributeValue.builder().s("some-id").build());


        PaginatedResponse<VideoEntity> response =PaginatedResponse.<VideoEntity>builder()
                .items(List.of(entity))
                .lastEvaluatedKey(lastEvaluatedKey)
                .build();

        when(repository.findAllByUserId(eq(userId), anyInt(), any())).thenReturn(response);

        CustomPage page = videoPersistence.findAllByUserId(userId, 10, null);

        assertNotNull(page);
        assertEquals(1, page.videos().size());
        assertNotNull(page.lastEvaluatedKey());
    }

    @Test
    void testParseExclusiveStartKey_InvalidJson_ThrowsException() {
        String invalidJson = "invalid-json";

        Exception ex = assertThrows(Exception.class, () ->
                videoPersistence.findAllByUserId(UUID.randomUUID(), 10, invalidJson));

        assertTrue(ex.getMessage().contains("Erro ao converter exclusiveStartKey"));
    }

    @Test
    void testConvertLastEvaluatedKeyToString() {
        Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
        lastEvaluatedKey.put("key", AttributeValue.builder().s("value").build());

        String json = videoPersistence.convertLastEvaluatedKeyToString(lastEvaluatedKey);

        assertNotNull(json);
        assertTrue(json.contains("value"));
    }

    @Test
    void testConvertLastEvaluatedKeyToString_NullMap_ReturnsNull() {
        assertNull(videoPersistence.convertLastEvaluatedKeyToString(null));
    }

    @Test
    void testConvertLastEvaluatedKeyToString_EmptyMap_ReturnsNull() {
        assertNull(videoPersistence.convertLastEvaluatedKeyToString(Collections.emptyMap()));
    }

    @Test
    void testConvertLastEvaluatedKeyToString_ThrowsException() throws Exception {
        Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
        lastEvaluatedKey.put("key", AttributeValue.builder().s("value").build());

        ObjectMapper mockedObjectMapper = mock(ObjectMapper.class);
        when(mockedObjectMapper.writeValueAsString(any()))
                .thenThrow(new RuntimeException("Simulated serialization error"));

        VideoPersistenceImpl videoPersistenceWithMock = new VideoPersistenceImpl(mockedObjectMapper, repository);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                videoPersistenceWithMock.convertLastEvaluatedKeyToString(lastEvaluatedKey));

        assertTrue(ex.getMessage().contains("Erro ao converter LastEvaluatedKey para String"));
    }

    @Test
    void testParseExclusiveStartKey_Success() throws Exception {
        String json = "{\"key1\":\"value1\",\"key2\":\"value2\"}";

        Method method = VideoPersistenceImpl.class.getDeclaredMethod("parseExclusiveStartKey", String.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, AttributeValue> result = (Map<String, AttributeValue>) method.invoke(videoPersistence, json);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1").s());
        assertEquals("value2", result.get("key2").s());
    }



    private Video buildVideo() {
        return new Video(
                UUID.randomUUID(),
                new User(UUID.randomUUID(), "test@email.com"),
                "videoKey",
                "framesKey",
                "videoName.mp4",
                ProcessStatus.NEW,
                new Audit(LocalDateTime.now(), LocalDateTime.now())
        );
    }
}
