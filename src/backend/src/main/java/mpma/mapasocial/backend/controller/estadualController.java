package mpma.mapasocial.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@CrossOrigin(origins = "http://localhost:3000/")
public class estadualController {

    @Autowired
    private estadualService estadualService;

    @Operation(summary = "Busca o nome de todos os municípios do Maranhão",
    description = "Retorna uma lista que contêm todos os municípios do estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista retornada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de requisição bem sucessida para a lista de municípios do estado do Maranhão",
                                            value = "[\n" +
                                                    "  \"Açailândia\",\n" +
                                                    "  \"Afonso Cunha\",\n" +
                                                    "  \"Água Doce do Maranhão\",\n" +
                                                    "  \"Alcântara\",\n" +
                                                    "  \"Aldeias Altas\",\n" +
                                                    "  \"Altamira do Maranhão\",\n" +
                                                    "  \"Alto Alegre do Maranhão\",\n" +
                                                    "  \"Alto Alegre do Pindaré\",\n" +
                                                    "  \"Alto Parnaíba\",\n" +
                                                    "  \"Amapá do Maranhão\",\n" +
                                                    "  \"Amarante do Maranhão\",\n" +
                                                    "  \"Anajatuba\",\n" +
                                                    "  \"Anapurus\",\n" +
                                                    "  \"Apicum-Açu\",\n" +
                                                    "  \"Araguanã\",\n" +
                                                    "  \"Araioses\",\n" +
                                                    "  \"Arame\",\n" +
                                                    "  \"Arari\",\n" +
                                                    "  \"Axixá\",\n" +
                                                    "  \"Bacabal\",\n" +
                                                    "  \"Bacabeira\",\n" +
                                                    "  \"Bacuri\",\n" +
                                                    "  \"Bacurituba\",\n" +
                                                    "  \"Balsas\",\n" +
                                                    "  \"Barão de Grajaú\",\n" +
                                                    "  \"Barra do Corda\",\n" +
                                                    "  \"Barreirinhas\",\n" +
                                                    "  \"Belágua\",\n" +
                                                    "  \"Bela Vista do Maranhão\",\n" +
                                                    "  \"Benedito Leite\",\n" +
                                                    "  \"Bequimão\",\n" +
                                                    "  \"Bernardo do Mearim\",\n" +
                                                    "  \"Boa Vista do Gurupi\",\n" +
                                                    "  \"Bom Jardim\",\n" +
                                                    "  \"Bom Jesus das Selvas\",\n" +
                                                    "  \"Bom Lugar\",\n" +
                                                    "  \"Brejo\",\n" +
                                                    "  \"Brejo de Areia\",\n" +
                                                    "  \"Buriti\",\n" +
                                                    "  \"Buriti Bravo\",\n" +
                                                    "  \"Buriticupu\",\n" +
                                                    "  \"Buritirana\",\n" +
                                                    "  \"Cachoeira Grande\",\n" +
                                                    "  \"Cajapió\",\n" +
                                                    "  \"Cajari\",\n" +
                                                    "  \"Campestre do Maranhão\",\n" +
                                                    "  \"Cândido Mendes\",\n" +
                                                    "  \"Cantanhede\",\n" +
                                                    "  \"Capinzal do Norte\",\n" +
                                                    "  \"Carolina\",\n" +
                                                    "  \"Carutapera\",\n" +
                                                    "  \"Caxias\",\n" +
                                                    "  \"Cedral\",\n" +
                                                    "  \"Central do Maranhão\",\n" +
                                                    "  \"Centro do Guilherme\",\n" +
                                                    "  \"Centro Novo do Maranhão\",\n" +
                                                    "  \"Chapadinha\",\n" +
                                                    "  \"Cidelândia\",\n" +
                                                    "  \"Codó\",\n" +
                                                    "  \"Coelho Neto\",\n" +
                                                    "  \"Colinas\",\n" +
                                                    "  \"Conceição do Lago-Açu\",\n" +
                                                    "  \"Coroatá\",\n" +
                                                    "  \"Cururupu\",\n" +
                                                    "  \"Davinópolis\",\n" +
                                                    "  \"Dom Pedro\",\n" +
                                                    "  \"Duque Bacelar\",\n" +
                                                    "  \"Esperantinópolis\",\n" +
                                                    "  \"Estreito\",\n" +
                                                    "  \"Feira Nova do Maranhão\",\n" +
                                                    "  \"Fernando Falcão\",\n" +
                                                    "  \"Formosa da Serra Negra\",\n" +
                                                    "  \"Fortaleza dos Nogueiras\",\n" +
                                                    "  \"Fortuna\",\n" +
                                                    "  \"Godofredo Viana\",\n" +
                                                    "  \"Gonçalves Dias\",\n" +
                                                    "  \"Governador Archer\",\n" +
                                                    "  \"Governador Edison Lobão\",\n" +
                                                    "  \"Governador Eugênio Barros\",\n" +
                                                    "  \"Governador Luiz Rocha\",\n" +
                                                    "  \"Governador Newton Bello\",\n" +
                                                    "  \"Governador Nunes Freire\",\n" +
                                                    "  \"Graça Aranha\",\n" +
                                                    "  \"Grajaú\",\n" +
                                                    "  \"Guimarães\",\n" +
                                                    "  \"Humberto de Campos\",\n" +
                                                    "  \"Icatu\",\n" +
                                                    "  \"Igarapé do Meio\",\n" +
                                                    "  \"Igarapé Grande\",\n" +
                                                    "  \"Imperatriz\",\n" +
                                                    "  \"Itaipava do Grajaú\",\n" +
                                                    "  \"Itapecuru Mirim\",\n" +
                                                    "  \"Itinga do Maranhão\",\n" +
                                                    "  \"Jatobá\",\n" +
                                                    "  \"Jenipapo dos Vieiras\",\n" +
                                                    "  \"João Lisboa\",\n" +
                                                    "  \"Joselândia\",\n" +
                                                    "  \"Junco do Maranhão\",\n" +
                                                    "  \"Lago da Pedra\",\n" +
                                                    "  \"Lago do Junco\",\n" +
                                                    "  \"Lago Verde\",\n" +
                                                    "  \"Lagoa do Mato\",\n" +
                                                    "  \"Lago dos Rodrigues\",\n" +
                                                    "  \"Lagoa Grande do Maranhão\",\n" +
                                                    "  \"Lajeado Novo\",\n" +
                                                    "  \"Lima Campos\",\n" +
                                                    "  \"Loreto\",\n" +
                                                    "  \"Luís Domingues\",\n" +
                                                    "  \"Magalhães de Almeida\",\n" +
                                                    "  \"Maracaçumé\",\n" +
                                                    "  \"Marajá do Sena\",\n" +
                                                    "  \"Maranhãozinho\",\n" +
                                                    "  \"Mata Roma\",\n" +
                                                    "  \"Matinha\",\n" +
                                                    "  \"Matões\",\n" +
                                                    "  \"Matões do Norte\",\n" +
                                                    "  \"Milagres do Maranhão\",\n" +
                                                    "  \"Mirador\",\n" +
                                                    "  \"Miranda do Norte\",\n" +
                                                    "  \"Mirinzal\",\n" +
                                                    "  \"Monção\",\n" +
                                                    "  \"Montes Altos\",\n" +
                                                    "  \"Morros\",\n" +
                                                    "  \"Nina Rodrigues\",\n" +
                                                    "  \"Nova Colinas\",\n" +
                                                    "  \"Nova Iorque\",\n" +
                                                    "  \"Nova Olinda do Maranhão\",\n" +
                                                    "  \"Olho d'Água das Cunhãs\",\n" +
                                                    "  \"Olinda Nova do Maranhão\",\n" +
                                                    "  \"Paço do Lumiar\",\n" +
                                                    "  \"Palmeirândia\",\n" +
                                                    "  \"Paraibano\",\n" +
                                                    "  \"Parnarama\",\n" +
                                                    "  \"Passagem Franca\",\n" +
                                                    "  \"Pastos Bons\",\n" +
                                                    "  \"Paulino Neves\",\n" +
                                                    "  \"Paulo Ramos\",\n" +
                                                    "  \"Pedreiras\",\n" +
                                                    "  \"Pedro do Rosário\",\n" +
                                                    "  \"Penalva\",\n" +
                                                    "  \"Peri Mirim\",\n" +
                                                    "  \"Peritoró\",\n" +
                                                    "  \"Pindaré-Mirim\",\n" +
                                                    "  \"Pinheiro\",\n" +
                                                    "  \"Pio XII\",\n" +
                                                    "  \"Pirapemas\",\n" +
                                                    "  \"Poção de Pedras\",\n" +
                                                    "  \"Porto Franco\",\n" +
                                                    "  \"Porto Rico do Maranhão\",\n" +
                                                    "  \"Presidente Dutra\",\n" +
                                                    "  \"Presidente Juscelino\",\n" +
                                                    "  \"Presidente Médici\",\n" +
                                                    "  \"Presidente Sarney\",\n" +
                                                    "  \"Presidente Vargas\",\n" +
                                                    "  \"Primeira Cruz\",\n" +
                                                    "  \"Raposa\",\n" +
                                                    "  \"Riachão\",\n" +
                                                    "  \"Ribamar Fiquene\",\n" +
                                                    "  \"Rosário\",\n" +
                                                    "  \"Sambaíba\",\n" +
                                                    "  \"Santa Filomena do Maranhão\",\n" +
                                                    "  \"Santa Helena\",\n" +
                                                    "  \"Santa Inês\",\n" +
                                                    "  \"Santa Luzia\",\n" +
                                                    "  \"Santa Luzia do Paruá\",\n" +
                                                    "  \"Santa Quitéria do Maranhão\",\n" +
                                                    "  \"Santa Rita\",\n" +
                                                    "  \"Santana do Maranhão\",\n" +
                                                    "  \"Santo Amaro do Maranhão\",\n" +
                                                    "  \"Santo Antônio dos Lopes\",\n" +
                                                    "  \"São Benedito do Rio Preto\",\n" +
                                                    "  \"São Bento\",\n" +
                                                    "  \"São Bernardo\",\n" +
                                                    "  \"São Domingos do Azeitão\",\n" +
                                                    "  \"São Domingos do Maranhão\",\n" +
                                                    "  \"São Félix de Balsas\",\n" +
                                                    "  \"São Francisco do Brejão\",\n" +
                                                    "  \"São Francisco do Maranhão\",\n" +
                                                    "  \"São João Batista\",\n" +
                                                    "  \"São João do Carú\",\n" +
                                                    "  \"São João do Paraíso\",\n" +
                                                    "  \"São João do Soter\",\n" +
                                                    "  \"São João dos Patos\",\n" +
                                                    "  \"São José de Ribamar\",\n" +
                                                    "  \"São José dos Basílios\",\n" +
                                                    "  \"São Luís\",\n" +
                                                    "  \"São Luís Gonzaga do Maranhão\",\n" +
                                                    "  \"São Mateus do Maranhão\",\n" +
                                                    "  \"São Pedro da Água Branca\",\n" +
                                                    "  \"São Pedro dos Crentes\",\n" +
                                                    "  \"São Raimundo das Mangabeiras\",\n" +
                                                    "  \"São Raimundo do Doca Bezerra\",\n" +
                                                    "  \"São Roberto\",\n" +
                                                    "  \"São Vicente Ferrer\",\n" +
                                                    "  \"Satubinha\",\n" +
                                                    "  \"Senador Alexandre Costa\",\n" +
                                                    "  \"Senador La Rocque\",\n" +
                                                    "  \"Serrano do Maranhão\",\n" +
                                                    "  \"Sítio Novo\",\n" +
                                                    "  \"Sucupira do Norte\",\n" +
                                                    "  \"Sucupira do Riachão\",\n" +
                                                    "  \"Tasso Fragoso\",\n" +
                                                    "  \"Timbiras\",\n" +
                                                    "  \"Timon\",\n" +
                                                    "  \"Trizidela do Vale\",\n" +
                                                    "  \"Tufilândia\",\n" +
                                                    "  \"Tuntum\",\n" +
                                                    "  \"Turiaçu\",\n" +
                                                    "  \"Turilândia\",\n" +
                                                    "  \"Tutóia\",\n" +
                                                    "  \"Urbano Santos\",\n" +
                                                    "  \"Vargem Grande\",\n" +
                                                    "  \"Viana\",\n" +
                                                    "  \"Vila Nova dos Martírios\",\n" +
                                                    "  \"Vitória do Mearim\",\n" +
                                                    "  \"Vitorino Freire\",\n" +
                                                    "  \"Zé Doca\"\n" +
                                                    "]"
                                    )
                            }
                    )

            ),
            @ApiResponse(responseCode = "204",
                    description = "Lista está vazia",
                    content = @Content
            ),
            @ApiResponse(responseCode = "400",
                    description = "Erro na requisição dos dados",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404",
                    description = "Endpoint não encontrado",
                    content = @Content
            ),
            @ApiResponse(responseCode = "500",
                    description = "Erro na requisição dos dados",
                    content = @Content)
    })
    @GetMapping("/listarMunicipios")
    private ResponseEntity<List<String>> listarMunicipios(){
        List<String> listaDeMunicipios = estadualService.listaDosMunicipiosDoEstado();

        if (listaDeMunicipios.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listaDeMunicipios);
    }
}
