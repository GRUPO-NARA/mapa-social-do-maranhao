"use client"
import { useEffect, useState } from "react";
import IndicadoresPrincipais from "./IndicadoresPrincipais";
import IndicadoresSociais from "./IndicadoresSociais";
import InfraestruturaERenda from "./InfraestruturaERenda";

interface IndicadoresProps {
    municipioSelecionado: string;
}

export default function Indicadores({ municipioSelecionado }: IndicadoresProps) {
    const [abaAtiva, setAbaAtiva] = useState<"indicadores" | "graficos">("indicadores");

    return (
        <div className="flex justify-center items-center ">
            <div className="w-300 rounded-2xl shadow-xl/30  shadow-sky-900 border border-gray-300 hover:border-sky-600 transition-colors duration-300 overflow-hidden">
                <nav className="">
                    <ul className="">
                        <li className=" bg-sky-800 grid grid-cols-2 divide-x divide-white">
                            <div className={`flex justify-center transition-colors duration-300 ${abaAtiva === "indicadores" ? "bg-red-600" : "hover:bg-red-600"}`}>
                                <button 
                                    className="font-bold text-white py-4 px-6 w-full"
                                    onClick={() => setAbaAtiva("indicadores")}
                                >
                                    Indicadores
                                </button>
                            </div>
                            <div className={`flex justify-center transition-colors duration-300 ${abaAtiva === "graficos" ? "bg-red-600" : "hover:bg-red-600"}`}>
                                <button 
                                    className="font-bold text-white py-4 px-6 w-full"
                                    onClick={() => setAbaAtiva("graficos")}
                                >
                                    Gráficos
                                </button>   
                            </div>
                        </li>
                    </ul>
                </nav>
                
                {abaAtiva === "indicadores" ? (
                    <div>
                        <IndicadoresPrincipais municipioSelecionado={municipioSelecionado}/>
                        <IndicadoresSociais />
                        <InfraestruturaERenda />
                    </div>
                ) : (
                    <div className="bg-white h-96 flex items-center justify-center">
                        <p className="text-gray-400">Gráficos em desenvolvimento...</p>
                    </div>
                )}
            </div>
        </div>
    )
}