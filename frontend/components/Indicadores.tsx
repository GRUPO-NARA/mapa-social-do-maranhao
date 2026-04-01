import { useEffect, useState } from "react";
import IndicadoresPrincipais from "./IndicadoresPrincipais";
import IndicadoresSociais from "./IndicadoresSociais";
import InfraestruturaERenda from "./InfraestruturaERenda";

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
        <div className="flex justify-center items-center ">
            <div className="w-300 h-200 rounded-2xl shadow-xl/30 shadow-sky-600 overflow-auto border border-sky-600">
                <nav className="">
                    <ul className="">
                        <li className=" bg-sky-600 grid grid-cols-2 divide-x divide-white">
                            <div className="flex justify-center hover:bg-red-600 p-6">
                                <button className="font-bold text-white">Indicadores</button>
                            </div>
                            <div className="flex justify-center hover:bg-red-600 p-6">
                                <a className="font-bold text-white" href="">Gráficos</a>   
                            </div>
                        </li>
                    </ul>
                </nav>
                <IndicadoresPrincipais nomeSelecionado={nomeSelecionado}/>
                <IndicadoresSociais />
                <InfraestruturaERenda />
            </div>
        </div>
    )
}