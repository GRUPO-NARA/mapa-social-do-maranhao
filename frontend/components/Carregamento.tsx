"use client"
import "../styles/Carregamento.css"
import { useEffect, useState } from "react";

export default function Carregamento() {
    const [visivel, setVisivel] = useState(true);

    useEffect(() => {
        const timer = setTimeout(() => setVisivel(false), 2000); // 2 segundos
        return () => clearTimeout(timer);
    }, []);

    if (!visivel) return null;

    return (
        <div id="tela-carregamento" className="tela-carregamento">
            <img src="logo.png" alt="" id="logo-carregamento" />
            <h1 id="titulo-carregamento">Mapa Social do Maranhão</h1>
        </div>
    );
}


