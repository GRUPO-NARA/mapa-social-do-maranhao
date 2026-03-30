import { useEffect, useState } from "react";

interface IndicadoresProps {
    nomeSelecionado: string;
}

export default function Indicadores({ nomeSelecionado }: IndicadoresProps) {

    const [densidadeDemografica, setDensidadeDemografica] = useState([])

    useEffect(() => {
        getDensidadeDemografica()
    }, [nomeSelecionado])

    async function getDensidadeDemografica() {
        if (!nomeSelecionado) {
            nomeSelecionado = "-"
        } else {
            const resposta = await fetch(`http://localhost:8080/dados_geograficos/densidade_demografica?nomeMunicipio=${nomeSelecionado}`);
            const dados = await resposta.json();
            setDensidadeDemografica(dados);
        }
    }

    return (
        <div className="flex justify-center items-center">
            <div className="w-300 h-200 bg-yellow-200 rounded-2xl shadow-xl/30 shadow-sky-600 overflow-auto">
                <nav className="bg-white">
                    <ul className="">
                        <li className="grid grid-cols-2 bg-sky-600 divide-x ">
                            <a className="text-bold text-white" href="">Indicadores</a>
                            <a className="text-bold text-white" href="">Gráficos</a>   
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    )
}