package br.com.fiap.techchallenge.hackathonvideo.infra.config.openapi;

import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.FileResponseDTO;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.ListFilesResponseDTO;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedDownloadResponseDTO;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.dto.PresignedUploadResponseDTO;
import br.com.fiap.techchallenge.hackathonvideo.infra.entrypoint.controller.handler.ProblemDTO;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SpringDocConfigurations {


	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Hackathon FIAP API").version("v1").description("""
				API Rest for Hackathon of Master's Degree in Software Architecture 
				Developed by:
				 - Alexandre Miranda - RM357321
				 - Diego Ceccon - RM357437
				 - Jéssica Rodrigues - RM357218
				 - Rodrigo Sartori - RM358002
				 - Wilton Souza - RM357991
				""").contact(new Contact().name("SOAT 8 Group").email("soat-group@gmail.com")))
			.components(new Components().schemas(this.generateSchemas()));
	}

	@SuppressWarnings("rawtypes")
	private Map<String, Schema> generateSchemas() {
		final Map<String, Schema> schemaMap = new HashMap<>();

		Map<String, Schema> problemSchema = ModelConverters.getInstance().read(ProblemDTO.class);
		Map<String, Schema> fileResponseDto = ModelConverters.getInstance().read(FileResponseDTO.class);
		Map<String, Schema> listFilesResponseDto = ModelConverters.getInstance().read(ListFilesResponseDTO.class);
		Map<String, Schema> presignedDownloadResponseDTO = ModelConverters.getInstance().read(PresignedDownloadResponseDTO.class);
		Map<String, Schema> presignedUploadResponseDTO = ModelConverters.getInstance().read(PresignedUploadResponseDTO.class);

		Schema errorsValidateDataArraySchema = new ArraySchema()
			.items(new Schema<>().$ref("#/components/schemas/ErrorsValidateData"));

		schemaMap.putAll(problemSchema);
		schemaMap.putAll(fileResponseDto);
		schemaMap.putAll(listFilesResponseDto);
		schemaMap.putAll(presignedDownloadResponseDTO);
		schemaMap.putAll(presignedUploadResponseDTO);
		schemaMap.put("ErrorsValidateDataList", errorsValidateDataArraySchema);

		return schemaMap;
	}

}