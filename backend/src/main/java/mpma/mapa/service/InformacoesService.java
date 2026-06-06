package mpma.mapa.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mpma.mapa.repository.InformacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Service
@Validated
public class InformacoesService {

    @Autowired
    private InformacoesRepository informacoesRepository;

    public String principaisInformacoesMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {
        return informacoesRepository.buscarDadosPrincipaisDoMunicipio(municipio);
    }

}
