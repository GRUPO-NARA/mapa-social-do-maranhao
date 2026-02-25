package com.example.elisabackend.model
// Resolvi usar essa biblioteca para fazer a ponte Java-banco
import jakarta.persistence.*;

// Essa escolhi para não precisar escrever Getters e Setters
import lombok.Data;

@Entity // "Esta classe é uma tabela do banco de dados"
@Table(name = "dados_sociais") // Nome EXATO da tabela que está no Docker
@Data // Gerando os Getters e Setters
public class DadosSociaisModel {
    @Id // "Este campo é a chave primária da tabela"
    @GeneratedValue(strategy = GenerationType.IDENTITY) // "O valor deste campo é gerado automaticamente pelo banco de dados"
    private Long id; // "Campo do tipo Long para armazenar o ID"

    @Column(name = "nome") // "Este campo é uma coluna da tabela com o nome 'nome'"
    private String nome; // "Campo do tipo String para armazenar o nome"

    @Column(name = "sobrenome") // "Este campo é uma coluna da tabela com o nome 'sobrenome'"
    private String sobrenome; // "Campo do tipo String para armazenar o sobrenome"

    @Column(name = "email") // "Este campo é uma coluna da tabela com o nome 'email'"
    private String email; // "Campo do tipo String para armazenar o email"

    @Column(name = "telefone") // "Este campo é uma coluna da tabela com o nome 'telefone'"
    private String telefone; // "Campo do tipo String para armazenar o telefone"
}
