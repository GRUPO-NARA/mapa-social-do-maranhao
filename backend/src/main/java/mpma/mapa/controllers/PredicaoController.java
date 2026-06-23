package mpma.mapa.controllers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import mpma.mapa.dto.PredicaoDTO;
import mpma.mapa.service.PredicaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/predicoes")
public class PredicaoController {

    private final PredicaoService predicaoService;

    public PredicaoController(PredicaoService predicaoService) {
        this.predicaoService = predicaoService;
    }

    @GetMapping
    public ResponseEntity<PredicaoDTO.Resposta> prever(
            @RequestParam
            @NotBlank
            @Pattern(regexp = "[a-zA-Z0-9_]+")
            String schema,

            @RequestParam
            @NotBlank
            @Pattern(regexp = "[a-zA-Z0-9_]+")
            String indicador,

            @RequestParam
            @NotBlank
            @Size(max = 100)
            String municipio,

            @RequestParam
            @Min(1900)
            @Max(2200)
            Integer ano
    ) {
        return ResponseEntity.ok(
                predicaoService.prever(schema, indicador, municipio, ano)
        );
    }
}
