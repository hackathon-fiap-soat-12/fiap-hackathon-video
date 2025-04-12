package br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.impl;

import br.com.fiap.techchallenge.hackathonvideo.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Audit;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Metadata;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.User;
import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.entities.VideoEntity;
import br.com.fiap.techchallenge.hackathonvideo.infra.gateway.database.repository.VideoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoPersistenceImplTest {

    @Mock
    private VideoRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private VideoPersistenceImpl videoPersistence;

    private Video video;

    private VideoEntity entity;

    @BeforeEach
    void setup() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Save Video")
    void shouldSaveVideo() {
        when(repository.save(any(VideoEntity.class))).thenReturn(entity);

        var videoSaved = videoPersistence.save(video);

        assertNotNull(videoSaved);
        assertEquals(video.getId(), videoSaved.getId());
        verify(repository).save(any(VideoEntity.class));
    }

    @Test
    @DisplayName("Should Update Video")
    void shouldUpdateVideo() {
        var updatedEntity = entity.update(video);
        when(repository.save(any(VideoEntity.class))).thenReturn(updatedEntity);

        var result = videoPersistence.update(video);

        assertNotNull(result);
        assertEquals(video.getId(), result.getId());
        verify(repository).save(any(VideoEntity.class));
    }

    @Test
    @DisplayName("Should Find Video By ID When Present")
    void shouldFindVideoByIdWhenPresent() {
        when(repository.findById(video.getId())).thenReturn(Optional.of(entity));

        Optional<Video> result = videoPersistence.findById(video.getId());

        assertTrue(result.isPresent());
        assertEquals(video.getId(), result.get().getId());
        verify(repository).findById(video.getId());
    }

    @Test
    @DisplayName("Should Return Empty When Video Not Found By ID")
    void shouldReturnEmptyWhenVideoNotFoundById() {
        when(repository.findById(video.getId())).thenReturn(Optional.empty());

        Optional<Video> result = videoPersistence.findById(video.getId());

        assertFalse(result.isPresent());
        verify(repository).findById(video.getId());
    }

    @Test
    @DisplayName("Should Find All Videos By User ID")
    void shouldFindAllVideosByUserId() throws JsonProcessingException {
        Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
        lastEvaluatedKey.put("id", AttributeValue.builder().s("some-id").build());

        PaginatedResponse<VideoEntity> response = PaginatedResponse.<VideoEntity>builder()
                .items(List.of(entity))
                .lastEvaluatedKey(lastEvaluatedKey)
                .build();

        when(repository.findAllByUserId(eq(video.getUserId()), anyInt(), any())).thenReturn(response);
        when(objectMapper.writeValueAsString(any())).thenReturn("json-string");

        var page = videoPersistence.findAllByUserId(video.getUserId(), 10, null);

        assertNotNull(page);
        assertEquals(1, page.videos().size());
        assertNotNull(page.lastEvaluatedKey());
    }

    @Test
    @DisplayName("Should Throw Exception When ExclusiveStartKey Is Invalid JSON")
    void shouldThrowExceptionWhenExclusiveStartKeyIsInvalidJson() {
        assertThrows(Exception.class, () ->
                videoPersistence.findAllByUserId(UUID.randomUUID(), 10, "invalid-json"));
    }

    @Test
    @DisplayName("Should Convert LastEvaluatedKey To JSON String")
    void shouldConvertLastEvaluatedKeyToJsonString() throws JsonProcessingException {
        Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
        lastEvaluatedKey.put("key", AttributeValue.builder().s("value").build());

        when(objectMapper.writeValueAsString(any())).thenReturn("value");

        var json = videoPersistence.convertLastEvaluatedKeyToString(lastEvaluatedKey);

        assertNotNull(json);
        assertTrue(json.contains("value"));
    }

    @Test
    @DisplayName("Should Return Null When LastEvaluatedKey Map Is Null")
    void shouldReturnNullWhenLastEvaluatedKeyMapIsNull() {
        assertNull(videoPersistence.convertLastEvaluatedKeyToString(null));
    }

    @Test
    @DisplayName("Should Return Null When LastEvaluatedKey Map Is Empty")
    void shouldReturnNullWhenLastEvaluatedKeyMapIsEmpty() {
        assertNull(videoPersistence.convertLastEvaluatedKeyToString(Collections.emptyMap()));
    }

    @Test
    @DisplayName("Should Throw Exception When Converting LastEvaluatedKey To String Fails")
    void shouldThrowExceptionWhenConvertingLastEvaluatedKeyToStringFails() throws Exception {
        Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
        lastEvaluatedKey.put("key", AttributeValue.builder().s("value").build());

        when(objectMapper.writeValueAsString(any()))
                .thenThrow(new RuntimeException("Simulated serialization error"));

        assertThrows(Exception.class, () ->
                videoPersistence.convertLastEvaluatedKeyToString(lastEvaluatedKey));
    }

    @Test
    @DisplayName("Should Parse ExclusiveStartKey From Valid JSON")
    void shouldParseExclusiveStartKeyFromValidJson() throws Exception {
        String json = "{\"key1\":\"value1\",\"key2\":\"value2\"}";

        Method method = VideoPersistenceImpl.class.getDeclaredMethod("parseExclusiveStartKey", String.class);
        method.setAccessible(true);

        Map<String, String> expectedMap = Map.of("key1", "value1", "key2", "value2");

        when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(expectedMap);

        @SuppressWarnings("unchecked")
        Map<String, AttributeValue> result = (Map<String, AttributeValue>) method.invoke(videoPersistence, json);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1").s());
        assertEquals("value2", result.get("key2").s());
    }

    private void buildArranges() {
        video = new Video(
                UUID.randomUUID(),
                new User(UUID.randomUUID(), "test@email.com"),
                "videoKey",
                "framesKey",
                ProcessStatus.NEW,
                new Audit(LocalDateTime.now(), LocalDateTime.now()),
                new Metadata("video.mp4", 10, 100L)
        );

        entity = new VideoEntity(video);
    }
}
