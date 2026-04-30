package mpma.mapasocial.backend.service.geograficos;
import mpma.mapasocial.backend.repository.geograficos.AreaTerritorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mpma.mapasocial.backend.repository.geograficos.DensidadeDemograficaRepository;

@Service
public class geograficosService {
    @Autowired
    private AreaTerritorialRepository areaTerritorialRepository;

    @Autowired
    private DensidadeDemograficaRepository densidadeDemograficaRepository;

    public Double areaTerritorialDoMunicipio(String municipio){
        return areaTerritorialRepository.buscarAreaTerritoralPorMunicipio(municipio);
    }

    public Double densidadeDemograficaDoMunicipio(String municipio){
        return densidadeDemograficaRepository.buscarDensidadeDemograficaPorMunicipio(municipio);
    }

}
