package mpma.mapasocial.backend.service.estadual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mpma.mapasocial.backend.repository.estadual.referenciasCodigosMunicipaisRepository;

import java.util.List;


@Service
public class estadualService {
    @Autowired
    private referenciasCodigosMunicipaisRepository referenciasCodigosMunicipaisRepository;

    public List<String> listaDosMunicipiosDoEstado(){
        List<String> listaDeMunicipios = referenciasCodigosMunicipaisRepository.getMunicipios();

        if (listaDeMunicipios.isEmpty()){
            return null;
        }

        return listaDeMunicipios;
    }
}
