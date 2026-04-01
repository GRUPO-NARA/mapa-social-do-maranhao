package com.mapasocialma.backend.controller;

import com.mapasocialma.backend.repository.PopulacaoResidenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController()
@RequestMapping("/dados_demograficos")
@CrossOrigin("*")
public class dadosDemograficosController {

    @Autowired
    private PopulacaoResidenteRepository populacaoResidenteRepository;

    @GetMapping("/populacao_residente")
    public ResponseEntity<List<BigInteger>> getPopulacaoResidente(@RequestParam String nomeMunicipio){
        List<BigInteger> populacao_residente = populacaoResidenteRepository.findPopulacaoResidenteByMunicipio(nomeMunicipio);
        return ResponseEntity.ok(populacao_residente);
    }
}
