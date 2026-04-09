package com.mapasocialma.backend.controller;

import com.mapasocialma.backend.repository.AreaTerritorialRepository;
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

    @Autowired
    private AreaTerritorialRepository areaTerritorialRepository;

    @GetMapping("/densidade_demografica")
    public ResponseEntity<List<Double>> getDensidadeDemografica(@RequestParam String nomeMunicipio){
        List<Double> densidade_demografica = densidadeDemograficaRepository.findDensidadeDemograficaByMunicipio(nomeMunicipio);
        return ResponseEntity.ok(densidade_demografica);
    }

    @GetMapping("/area_territorial")
    public ResponseEntity<List<Double>> getAreaTerritorial(@RequestParam String nomeMunicipio){
        List<Double> area_territorial = areaTerritorialRepository.findAreaTerritorialByMunicipio(nomeMunicipio);
        return ResponseEntity.ok(area_territorial);
    }

}
