package com.mapasocialma.backend.controller;

import com.mapasocialma.backend.entity.QuantidadeDeHomensEntity;
import com.mapasocialma.backend.service.QuantidadeDeHomensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Vai transformar o return em um JSON
@RequestMapping("/api/QtdHomens")
@CrossOrigin(origins = "*") // Essencial para permitir que o frontend acesse a API sem problemas de CORS
public class QuantidadeDeHomensController {
    @Autowired
    private QuantidadeDeHomensService service;
    /*
    End point para buscar dados da quantidade de homens por município, o frontend vai chamar esse end point passando o código do município

     */
    @GetMapping("/municipio/{codMunicipio}")
    public List<QuantidadeDeHomensEntity>getPorMunicipio(@PathVariable String codMunicipio) {
        // Aqui peguei o valor da url com PathVariable e jogo para a variável 'codMunicipio'
        return service.buscarPorMunicipio(codMunicipio);
    }

}
