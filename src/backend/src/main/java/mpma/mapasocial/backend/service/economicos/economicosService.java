package mpma.mapasocial.backend.service.economicos;

import mpma.mapasocial.backend.repository.economicos.ProdutoInternoBrutoMunicipalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class economicosService {
    @Autowired
    private ProdutoInternoBrutoMunicipalRepository produtoInternoBrutoMunicipalRepository;

    public Double produtoInternoBrutoMunicipal(@Param("municipio") String municipio){
        return produtoInternoBrutoMunicipalRepository.buscarPIBPorMunicipio(municipio);
    }
}
