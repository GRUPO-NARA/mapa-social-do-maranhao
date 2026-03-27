package com.mapasocialma.backend.controller;

import com.mapasocialma.backend.repository.DensidadeDemograficaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/dados_geograficos")
@CrossOrigin("*")
public class dadosGeograficosController {
    @Autowired
    private DensidadeDemograficaRepository densidadeDemograficaRepository;

    @GetMapping("/densidade_demografica")
    public ResponseEntity<List<Double>> getDensidadeDemografica(@RequestParam String nomeMunicipio){
        List<Double> densidade_demografica = densidadeDemograficaRepository.findDensidadeDemograficaByMunicipio(nomeMunicipio);
        return ResponseEntity.ok(densidade_demografica);
    }
}
