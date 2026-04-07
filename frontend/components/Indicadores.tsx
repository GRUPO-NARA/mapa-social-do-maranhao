"use client"
import { useEffect, useState } from "react";
import IndicadoresPrincipais from "./IndicadoresPrincipais";
import IndicadoresSociais from "./IndicadoresSociais";
import InfraestruturaERenda from "./InfraestruturaERenda";

interface IndicadoresProps {
    municipioSelecionado: string;
}

export default function Indicadores({ municipioSelecionado }: IndicadoresProps) {
    return (
        <div className="flex justify-center items-center ">
            <div className="w-300 rounded-2xl shadow-xl/30  shadow-sky-900 border border-gray-300 hover:border-sky-600 transition-colors duration-300 overflow-hidden">
                <nav className="">
                    <ul className="">
                        <li className=" bg-sky-800 grid grid-cols-2 divide-x divide-white">
                            <div className="flex justify-center hover:bg-red-600">
                                <button className="font-bold text-white">Indicadores</button>
                            </div>
                            <div className="flex justify-center hover:bg-red-600 p-6">
                                <a className="font-bold text-white" href="">Gráficos</a>   
                            </div>
                        </li>
                    </ul>
                </nav>
                <IndicadoresPrincipais municipioSelecionado={municipioSelecionado}/>
                <IndicadoresSociais />
                <InfraestruturaERenda />
            </div>
        </div>
    )
}