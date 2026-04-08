/*
package com.mapasocialma.backend.repository;

import com.mapasocialma.backend.entity.QuantidadeDeHomensEntity;
import com.mapasocialma.backend.entity.QuantidadeDeHomensEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuantidadeDeHomensRepository extends JpaRepository<QuantidadeDeHomensEntity, QuantidadeDeHomensEntityId> {
     /* Aqui vamos buscar os dados da Quantidade de homens de acordo com o código do
    município.

    Não vai ser preciso definir a forma que vai pegar esses dados com "SELECT * FROM"
    e blá blá blá, com o Spring Data JPA, basta definir o
    método de busca seguindo a convenção de nomenclatura.

    */
    /*
    List<QuantidadeDeHomensEntity>findByIdCodMunicipio(String codMunicipio);

}
*/