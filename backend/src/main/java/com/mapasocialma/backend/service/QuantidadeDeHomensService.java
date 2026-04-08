package com.mapasocialma.backend.service;

import com.mapasocialma.backend.entity.QuantidadeDeHomensEntity;
import com.mapasocialma.backend.entity.QuantidadeDeHomensEntityId;
import com.mapasocialma.backend.repository.QuantidadeDeHomensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantidadeDeHomensService {
    @Autowired
    private QuantidadeDeHomensRepository repository;

    public List<QuantidadeDeHomensEntity> buscarPorMunicipio(String codMunicipio){
        return repository.findByIdCodMunicipio(codMunicipio);
    }
}