"use client"
import { useEffect, useState } from "react";

interface ResumoGeralProps {
    municipioSelecionado: string;
}

export default function ResumoGeral({ municipioSelecionado }: ResumoGeralProps) {
    const [populacaoTotal, setPopulacaoTotal] = useState([]);
    const [densidadeDemografica, setDensidadeDemografica] = useState([])

    useEffect(() => {
        getPopulacaoTotal(),
        getDensidadeDemografica()
    }, [municipioSelecionado])

    async function getPopulacaoTotal(){
        if(!municipioSelecionado){
            setPopulacaoTotal([])
        }else{
            const resposta = await fetch(`http://localhost:8080/dados_demograficos/populacao_residente?nomeMunicipio=${municipioSelecionado}`);
            const dados = await resposta.json();
            setPopulacaoTotal(dados);
        }
        
    }
    async function getDensidadeDemografica() {
        if (!municipioSelecionado) {
            municipioSelecionado = "-"
        } else {
            const resposta = await fetch(`http://localhost:8080/dados_geograficos/densidade_demografica?nomeMunicipio=${municipioSelecionado}`);
            const dados = await resposta.json();
            setDensidadeDemografica(dados);
        }
    }

    return (
        <div className="flex justify-center items-center">
            <div className="group flex flex-col gap-5 w-300 p-6 rounded-2xl shadow-xl/30 shadow-sky-900 border border-gray-300 hover:border-sky-600 transition-colors duration-300">
                <div className="flex items-center gap-2">
                    <p className="w-1 h-6 rounded bg-sky-600"></p>
                    <h1 className="text-xl font-bold group-hover:text-sky-800 transition-colors duration-300">Resumo Geral do Município</h1>
                </div>
                <div className="grid sm:grid-cols-1 md:grid-cols-3 xl:grid-cols-3 gap-4 ">
                    <div className="bg-white rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300">
                        <div className="flex flex-col gap-2 p-6">
                            <div>
                                <h1 className="text-gray-600 font-bold">População Total</h1>
                                <p className="text-gray-400 text-sm">habitantes no município</p>
                            </div>
                            <h1 className="text-sky-600 font-bold text-2xl">
                                {populacaoTotal.length > 0 ? populacaoTotal[0].toLocaleString('pt-BR') : '--'}
                            </h1>
                        </div>
                    </div>
                    <div className="bg-white rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300">
                        <div className="flex flex-col gap-2 p-6">
                            <div>
                                <h1 className="text-gray-600 font-bold">Densidade Demográfica</h1>
                                <p className="text-gray-400 text-sm">habitantes por km²</p>
                            </div>
                            <h1 className="text-sky-600 font-bold text-2xl">
                                {densidadeDemografica.length > 0 ? densidadeDemografica[0] : '--'}
                            </h1>
                        </div>
                    </div>
                    <div className="bg-white rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300">
                        <div className="flex flex-col gap-2 p-6">
                            <h1 className="text-gray-600">PIB per capita</h1>
                            <h1 className="text-sky-600 font-bold text-2xl">--</h1>
                        </div>
                    </div>
                    <div className="bg-white rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300">
                        <div className="flex flex-col gap-2 p-6">
                            <h1 className="text-gray-600">Razão de Sexo</h1>
                            <h1 className="text-sky-600 font-bold text-2xl">--</h1>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}