package com.example.elisabackend.repository;

import com.example.elisabackend.model.DadosDemograficosModel; // Importando a classe de modelo para o repository
import org.springframework.data.jpa.repository.JpaRepository; // A base do repository do Spring Data JPA
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Começando a elaborar o repository, que é a camada de acesso a dados.
@Repository
public interface DadosDemograficosRepository {
    // Criação de um query para buscar os dados.
    Optional<DadosDemograficosModel> findByMunicipioAndAno(String municipio, Integer ano); //Acha por município e ano.
}
