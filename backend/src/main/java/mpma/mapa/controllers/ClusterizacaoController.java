package mpma.mapa.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import mpma.mapa.dto.ClusterizacaoDTO;
import mpma.mapa.service.ClusterizacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/clusterizacoes")
public class ClusterizacaoController {

    private final ClusterizacaoService clusterizacaoService;

    public ClusterizacaoController(ClusterizacaoService clusterizacaoService) {
        this.clusterizacaoService = clusterizacaoService;
    }

    @GetMapping
    public ResponseEntity<ClusterizacaoDTO.Resposta> clusterizar(
            @RequestParam
            @NotBlank
            @Pattern(regexp = "[a-zA-Z0-9_]+")
            String schema,

            @RequestParam
            @NotBlank
            @Pattern(regexp = "[a-zA-Z0-9_]+")
            String indicador
    ) {
        return ResponseEntity.ok(clusterizacaoService.clusterizar(schema, indicador));
    }
}
