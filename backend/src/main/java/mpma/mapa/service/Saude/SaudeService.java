package mpma.mapa.service.Saude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mpma.mapa.repository.Saude.IdadeMedianaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Validated
public class SaudeService {
    @Autowired
    private IdadeMedianaRepository idadeMedianaRepository;

    public String idadeMedianaMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {
        return idadeMedianaRepository.buscarIdadeMedianaDoMunicipio(municipio);
    }
}
