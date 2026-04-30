package mpma.mapasocial.backend.service.demograficos;

import mpma.mapasocial.backend.repository.demograficos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class demograficosService {

    @Autowired
    private PopulacaoResidenteRepository populacaoResidenteRepository;

    @Autowired
    private QuantidadeDeHomensRepository quantidadeDeHomensRepository;

    @Autowired
    private QuantidadeDeMulheresRepository quantidadeDeMulheresRepository;

    @Autowired
    private QuantidadeDeResidentesRuraisRepository quantidadeDeResidentesRuraisRepository;

    @Autowired
    private IndiceDeDesenvolvimentoHumanoRepository indiceDeDesenvolvimentoHumanoRepository;

    public Long populacaoTotalDoMunicipio(@Param("municipio") String municipio){
        return populacaoResidenteRepository.buscarPopulacaoResidente(municipio);
    }

    public Long quantidadeDeHomensDoMunicipio(@Param("municipio") String municipio){
        return quantidadeDeHomensRepository.buscarQuantidadeDeHomensPorMunicipio(municipio);
    }

    public Long quantidadeDeMulheresDoMunicipio(@Param("municipio") String municipio){
        return quantidadeDeMulheresRepository.buscarQuantidadeDeMulheresPorMunicipio(municipio);
    }

    public Long quantidadeDeResidentesRuraisDoMunicipio(@Param("municipio") String municipio){
        return quantidadeDeResidentesRuraisRepository.buscarQuantidadeDeResidentesRuraisPorMunicipio(municipio);
    }

    public Double indiceDeDesenvolvimentoHumanoDoMunicipio(@Param("ano") Integer ano, @Param("municipio") String municipio){
        return indiceDeDesenvolvimentoHumanoRepository.buscarIndiceDeDesenvolvimentoHumanoPorMunicipio(ano, municipio);
    }
}
