package mpma.mapasocial.backend.service.geograficos;
import mpma.mapasocial.backend.repository.geograficos.AreaTerritorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class geograficosService {
    @Autowired
    private AreaTerritorialRepository areaTerritorialRepository;

    public Double areaTerritorialDoMunicipio(String municipio){
        return areaTerritorialRepository.buscarAreaTerritoralPorMunicipio(municipio);
    }

}
