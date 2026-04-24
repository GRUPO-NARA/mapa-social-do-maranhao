package mpma.mapasocial.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import mpma.mapasocial.backend.service.estadual.estadualService;
import java.util.List;

@RestController
@RequestMapping("/estadual")
@Tag(name="Estadual", description = "Endpoints para dados estaduais")
@CrossOrigin(origins = "http://localhost:8080")
public class estadualController {

    @Autowired
    private estadualService estadualService;

    @Operation(summary = "Busca o nome de todos os municípios do Maranhão",
    description = "Retorna uma lista que contêm todos os municípios do estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista retornada com sucesso!"

            ),
            @ApiResponse(responseCode = "204",
                    description = "Lista está vazia",
                    content = @Content
            )
    })
    @GetMapping("/listarMunicipios")
    private ResponseEntity<List<String>> listarMunicipios(){
        List<String> listaDeMunicipios = estadualService.listaDosMunicipiosDoEstado();
        return ResponseEntity.ok(listaDeMunicipios);
    }
}
