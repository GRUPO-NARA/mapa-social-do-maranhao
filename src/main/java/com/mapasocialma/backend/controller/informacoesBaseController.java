package com.mapasocialma.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mapasocialma.backend.repository.informacoesMunicipaisRepository;

import java.util.List;

@RestController()
@RequestMapping("/api")
@CrossOrigin("*")
public class informacoesBaseController {
    @Autowired
    private informacoesMunicipaisRepository informacoesMunicipaisRepository;

    @GetMapping("/nomesMunicipios")
    public ResponseEntity<List<String>> nomesMunicipios(){
        List<String> nomes = informacoesMunicipaisRepository.findALLNomesMunicipios();
        return ResponseEntity.ok(nomes);
    }
}
