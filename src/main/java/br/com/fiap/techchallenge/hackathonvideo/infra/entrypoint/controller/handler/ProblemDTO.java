package br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ProblemDto")
public class ProblemDTO {

	@Schema(example = "Error Message")
	@JsonProperty("message")
	private String message;

	@Schema(example = "2025-03-31T16:46:34.792653300")
	@JsonProperty("dateTime")
	private String dateTime;

	public ProblemDTO(String message, LocalDateTime dateTime) {
		this.message = message;
		this.dateTime = dateTime.toString();
	}

}
