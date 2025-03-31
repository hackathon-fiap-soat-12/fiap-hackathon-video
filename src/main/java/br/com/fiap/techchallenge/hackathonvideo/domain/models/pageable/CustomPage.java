package br.com.fiap.techchallenge.hackathonvideo.domain.models.pageable;

import br.com.fiap.techchallenge.hackathonvideo.domain.models.Video;

import java.util.List;

public record CustomPage(List<Video> videos, String lastEvaluatedKey) {
}
