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

    // novo: repository para população residente em favela
    @Autowired
    private PopulacaoResidenteEmFavelaRepository populacaoResidenteEmFavelaRepository;

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

    /* ===========================
       Métodos adicionados para População Residente em Favelas
       =========================== */

    public String populacaoResidenteEmFavelaMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {
        return populacaoResidenteEmFavelaRepository.buscarPopulacaoResidenteEmFavelaDoMunicipio(municipio);
    }

    public List<String> evolucaoPopulacaoResidenteEmFavelaMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {
        return populacaoResidenteEmFavelaRepository.buscarEvolucaoPopulacaoResidenteEmFavelaDoMunicipio(municipio);
    }

    public String populacaoResidenteEmFavelaMunicipalPorAno(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio,
            @Size(min = 1, max = 10, message = "Referência deve conter entre 1 e 10 caracteres")
            @NotBlank(message = "Referência não pode ser em branco")
            String referencia) {
        return populacaoResidenteEmFavelaRepository.buscarPopulacaoResidenteEmFavelaDoMunicipioPorAno(municipio, referencia);
    }
}
