package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto;

public record PageResponseDTO(Long totalPages,
							  Long totalElements,
							  Long size,
							  Long number,
							  Boolean first,
							  Boolean last,
							  Long numberOfElements,
							  Boolean empty) {

}
