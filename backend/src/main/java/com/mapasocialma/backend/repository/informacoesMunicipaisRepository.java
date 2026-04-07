package com.mapasocialma.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.mapasocialma.backend.entity.informacoesMunicipais;

import java.util.List;

@Repository
public interface informacoesMunicipaisRepository extends JpaRepository<informacoesMunicipais, String> {
    @Query(value="SELECT nome_municipio FROM dados_gerais.informacoes", nativeQuery = true)
    List<String> findALLNomesMunicipios();
}
