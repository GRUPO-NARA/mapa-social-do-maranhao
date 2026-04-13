package com.mapasocialma.backend.controller;

import com.mapasocialma.backend.entity.QuantidadeDeMulheresEntity;
import com.mapasocialma.backend.repository.PopulacaoResidenteRepository;
import com.mapasocialma.backend.repository.QuantidadeDeHomensRepository;
import com.mapasocialma.backend.repository.QuantidadeDeMulheresRepository;
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

    @Autowired
    private QuantidadeDeMulheresRepository quantidadeDeMulheresRepository;

    @Autowired
    private QuantidadeDeHomensRepository quantidadeDeHomensRepository;

    @GetMapping("/populacao_residente")
    public ResponseEntity<List<BigInteger>> getPopulacaoResidente(@RequestParam String nomeMunicipio){
        List<BigInteger> populacao_residente = populacaoResidenteRepository.findPopulacaoResidenteByMunicipio(nomeMunicipio);
        return ResponseEntity.ok(populacao_residente);
    }

    @GetMapping("/quantidade_mulheres")
    public ResponseEntity<List<BigInteger>> getQuantidadeDeMulheres(@RequestParam String nomeMunicipio) {
        List<BigInteger> quantidade_de_mulheres = quantidadeDeMulheresRepository.findQuantidadeDeMulheresByMunicipio(nomeMunicipio);
        return ResponseEntity.ok(quantidade_de_mulheres);
    }

    @GetMapping("/quantidade_homens")
    public ResponseEntity<List<BigInteger>> getQuantidadeDeHomens(@RequestParam String nomeMunicipio) {
        List<BigInteger> quantidade_de_homens = quantidadeDeHomensRepository.findQuantidadeDeHomensByMunicipio((nomeMunicipio));
        return ResponseEntity.ok(quantidade_de_homens);
    }

    @GetMapping ("/total_populacao_estado")
    public ResponseEntity<Long> getTotalPopulacaoEstado() {
        return ResponseEntity.ok(populacaoResidenteRepository.findPopulacaoResidenteTotal());
    }
}
