package mpma.mapa.service.Demograficos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mpma.mapa.repository.Demograficos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class DemograficosService {

    @Autowired
    private PopulacaoResidenteRepository populacaoResidenteRepository;

    @Autowired
    private QuantidadeDeHomensRepository quantidadeDeHomensRepository;

    @Autowired
    private QuantidadeDeMulheresRepository quantidadeDeMulheresRepository;

    @Autowired
    private QuantidadeDeResidentesRuraisRepository quantidadeDeResidentesRuraisRepository;

    @Autowired
    private IndiceDeDesenvolvimentoHumanoRepository indiceDeDesenvolvimentoHumanoRepository;

    public String populacaoMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){
        return populacaoResidenteRepository.buscarPopulacaoTotalDoMunicipio(municipio);
    }

    public String populacaoEstadualRecente () {
        return populacaoResidenteRepository.buscarPopulacaoEstadualRecente();
    }
    
    public String quantidadeDeHomensMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){
        return quantidadeDeHomensRepository.buscarQuantidadeDeHomensDoMunicipio(municipio);
    }

    public String quantidadeDeMulheresMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){
        return quantidadeDeMulheresRepository.buscarQuantidadeDeMulheresDoMunicipio(municipio);
    }

    public String quantidadeDeResidentesRuraisMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){
        return quantidadeDeResidentesRuraisRepository.buscarQuantidadeDeResidentesRuraisDoMunicipio(municipio);
    }

    public String indiceDeDesenvolvimentoHumanoMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){
        return indiceDeDesenvolvimentoHumanoRepository.buscarIndiceDeDesenvolvimentoHumanoDoMunicipio(municipio);
    }

    public List<String> evolucaoDoIndiceDeDesenvolvimentoHumanoMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){
        return indiceDeDesenvolvimentoHumanoRepository.buscarEvolucaoIndiceDeDesenvolvimentoHumanoDoMunicipio(municipio);
    }
}
