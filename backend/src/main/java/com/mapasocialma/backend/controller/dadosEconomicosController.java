package com.mapasocialma.backend.controller;

import com.mapasocialma.backend.repository.ProdutoInternoBrutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController()
@RequestMapping("/dados_economicos")
@CrossOrigin("*")
public class dadosEconomicosController {
    @Autowired
    private ProdutoInternoBrutoRepository produtoInternoBrutoRepository;

    @GetMapping("/produto_interno_bruto")
    public ResponseEntity<List<Double>> getPIB(@RequestParam String nomeMunicipio){
        List<Double> produto_interno_bruto = produtoInternoBrutoRepository.findAllProdutoInternoBrutoByMunicipio(nomeMunicipio);
        return ResponseEntity.ok(produto_interno_bruto);
    }

    @GetMapping("/total_pib_estado")
    public ResponseEntity<BigDecimal>getPibTotal(){
        return ResponseEntity.ok(BigDecimal.valueOf(produtoInternoBrutoRepository.findTotalPibEstado()));
    }
}
