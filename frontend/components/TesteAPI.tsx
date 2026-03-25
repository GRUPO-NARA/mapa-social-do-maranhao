"use client"

import { useState } from "react"

export default function Teste() {
    const [dadosPokemon, setDadosPokemon] = useState(null) // "Guarda os dados do pokemon encontrado"
    const [pokemonDigitado, setPokemonDigitado] = useState("") // Guarda o nome do pokemon digitado

    async function getPokemon() {
        const resposta = await fetch(`https://pokeapi.co/api/v2/pokemon/${pokemonDigitado.toLowerCase()}`); // Envia a requisição para o servidor
        const dados = await resposta.json(); // Resposta do servidor
        setDadosPokemon(dados); // Atualiza os dados do pokemon
    }
    return (
        <div style={{ padding: '20px' }}>
            <input
                type="text"
                placeholder="Digite: ditto"
                onChange={(e) => setPokemonDigitado(e.target.value)}
            />
            <button onClick={getPokemon}>Buscar</button>

            {/* Se o pokemon existir, mostra o nome e a foto */}
            {dadosPokemon && (
                <div>
                    <h1>{dadosPokemon.name}</h1>
                    <img src={dadosPokemon.sprites.front_default} alt="pokemon" />
                </div>
            )}
        </div>
    );

}