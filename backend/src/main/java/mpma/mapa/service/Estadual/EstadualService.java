package mpma.mapa.service.Estadual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mpma.mapa.repository.Estadual.ReferenciasCodigosMunicipaisRepository;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Service
public class EstadualService {

    @Autowired
    private ReferenciasCodigosMunicipaisRepository referenciasCodigosMunicipaisRepository;

    public List<String> municipiosDoEstado(){
        return referenciasCodigosMunicipaisRepository.buscarMunicipiosDoEstado();
    }
}
