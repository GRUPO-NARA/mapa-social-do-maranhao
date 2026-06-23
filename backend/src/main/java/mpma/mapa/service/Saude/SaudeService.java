package mpma.mapa.service.Saude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mpma.mapa.repository.Saude.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class SaudeService {

    @Autowired
    private IdadeMedianaRepository idadeMedianaRepository;

    @Autowired
    private CasosHanseniaseRepository casosHanseniaseRepository;

    @Autowired
    private CasosTuberculoseRepository casosTuberculoseRepository;

    @Autowired
    private CoberturaDaEstrategiaDeSaudeFamiliarRepository coberturaDaEstrategiaDeSaudeFamiliarRepository;

    @Autowired
    private CoberturaDeAgentesComunitariosDeSaudeRepository coberturaDeAgentesComunitariosDeSaudeRepository;

    @Autowired
    private CoberturaVacinaRepository coberturaVacinaRepository;

    public String idadeMedianaMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {
        return idadeMedianaRepository.buscarIdadeMedianaDoMunicipio(municipio);
    }

    public String casosHanseniaseMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {

        return casosHanseniaseRepository.buscarCasosHanseniaseDoMunicipio(municipio);
    }

    public String casosTuberculoseMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {

        return casosTuberculoseRepository.buscarCasosTuberculoseDoMunicipio(municipio);
    }

    public String coberturaDaEstrategiaDeSaudeFamiliarMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {

        return coberturaDaEstrategiaDeSaudeFamiliarRepository.buscarCoberturaDaEstrategiaDeSaudeFamiliarDoMunicipio(municipio);
    }
    
    public String coberturaDeAgentesComunitariosDeSaudeMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {

        return coberturaDeAgentesComunitariosDeSaudeRepository.buscarCoberturaDeAgentesComunitariosDeSaudeDoMunicipio(municipio);
    }

    public String coberturaVacinaMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return coberturaVacinaRepository.buscarCoberturaVacinalDoMunicipio(municipio);
    }
}