package mpma.mapa.service.Educacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mpma.mapa.repository.Educacao.TaxaDeAnalfabetismo15AnosOuMaisRepository;
import mpma.mapa.repository.Educacao.TaxaDeAprovacaoNoEnsinoFundamentalRepository;
import mpma.mapa.repository.Educacao.TaxaDeAprovacaoNoEnsinoMedioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class EducacaoService {

    @Autowired
    private TaxaDeAnalfabetismo15AnosOuMaisRepository taxaDeAnalfabetismo15AnosOuMaisRepository;

    @Autowired
    private TaxaDeAprovacaoNoEnsinoFundamentalRepository taxaDeAprovacaoNoEnsinoFundamentalRepository;

    @Autowired
    private TaxaDeAprovacaoNoEnsinoMedioRepository taxaDeAprovacaoNoEnsinoMedioRepository;

    public String taxaDeAnalfabetismo15AnosOuMaisMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {

        return taxaDeAnalfabetismo15AnosOuMaisRepository.buscarMediaDaTaxaDeAnalfabetismoDoMunicipio(municipio);
    }
    
    public String taxaDeAprovacaoNoEnsinoFundamentalMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {

        return taxaDeAprovacaoNoEnsinoFundamentalRepository.buscarTaxaDeAprovacaoNoEnsinoFundamentalDoMunicipio(municipio);
    }

    public String taxaDeAprovacaoNoEnsinoMedioMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {

        return taxaDeAprovacaoNoEnsinoMedioRepository.buscarTaxaDeAprovacaoNoEnsinoMedioDoMunicipio(municipio);
    }
}