package br.com.fiap.techchallenge.hackathonvideo.application.usecase;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable.CustomPage;
import java.util.UUID;

public interface ListFilesUseCase {
    CustomPage getFiles(UUID userId, Integer pageSize, String exclusiveStartKey);
}
