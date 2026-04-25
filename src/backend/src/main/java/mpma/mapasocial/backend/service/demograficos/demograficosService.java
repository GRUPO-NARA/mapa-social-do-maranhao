package mpma.mapasocial.backend.service.demograficos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import mpma.mapasocial.backend.repository.demograficos.QuantidadeDeHomensRepository;
import mpma.mapasocial.backend.repository.demograficos.PopulacaoResidenteRepository;
import mpma.mapasocial.backend.repository.demograficos.QuantidadeDeMulheresRepository;
import java.util.List;

@Service
public class demograficosService {

    @Autowired
    private PopulacaoResidenteRepository populacaoResidenteRepository;

    @Autowired
    private QuantidadeDeHomensRepository quantidadeDeHomensRepository;

    @Autowired
    private QuantidadeDeMulheresRepository quantidadeDeMulheresRepository;

    public Long populacaoTotalDoMunicipio(@Param("municipio") String municipio){
        return populacaoResidenteRepository.buscarPopulacaoResidente(municipio);
    }

    public Long quantidadeDeHomensDoMunicipio(@Param("municipio") String municipio){
        return quantidadeDeHomensRepository.buscarQuantidadeDeHomensPorMunicipio(municipio);
    }

    public Long quantidadeDeMulheresDoMunicipio(@Param("municipio") String municipio){
        return quantidadeDeMulheresRepository.buscarQuantidadeDeMulheresPorMunicipio(municipio);
    }
}
