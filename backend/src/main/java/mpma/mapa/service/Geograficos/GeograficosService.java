package mpma.mapa.service.Geograficos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mpma.mapa.repository.Geograficos.AreaTerritorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mpma.mapa.repository.Geograficos.DensidadeDemograficaRepository;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class GeograficosService {

    @Autowired
    private AreaTerritorialRepository areaTerritorialRepository;

    @Autowired
    private DensidadeDemograficaRepository densidadeDemograficaRepository;

    public String areaTerritorialMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){
        return areaTerritorialRepository.buscarAreaTerritoralDoMunicipio(municipio);
    }

    public String densidadeDemograficaMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){
        return densidadeDemograficaRepository.buscarDensidadeDemograficaDoMunicipio(municipio);
    }

}
