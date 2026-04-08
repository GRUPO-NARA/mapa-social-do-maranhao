package com.mapasocialma.backend.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quantidade_de_homens", schema = "dados_demograficos")
@Data // Substitui Getters, Setters, Equals e HashCode
@NoArgsConstructor // Construtor padrão exigido pelo Hibernate
@AllArgsConstructor // Construtor para facilitar testes e escrever menos código
public class QuantidadeDeHomensEntity {

    @EmbeddedId
    private QuantidadeDeHomensEntityId id;


}