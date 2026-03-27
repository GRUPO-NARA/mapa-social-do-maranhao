package com.example.elisabackend.model;

import jakarta.persistence.*; // Importação da anotação @Entity
import lombok.Data; // Importação da anotação @Data do Lombok
import java.math.BigDecimal; // Importação da classe BigDecimal para valores monetários

@Entity
@Table(name="informacoes", schema = "dados_demograficos")//Mapeando o nome exato da tabela
@Data // Utilizando o @Data para a criação de Getters e Setters e Tostring

public class DadosDemograficosModel {
    @Id // definindo campo de chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto incremento do Postgree
    private Long id ;

    @Column(nullable = false) // Campo de município não pode ser nulo
    private String municipio ;

    @Column(nullable = false)
    private Integer ano ;

    @Column(name = "populacao_total") //mapeando o nome da coluna
    private Long populacaoTotal ;

    @Column(name = "taxa_natalidade")
    private Double taxaNatalidade;

    @Column(name="densidade_demografica")
    private Double densidadeDemografica;

}