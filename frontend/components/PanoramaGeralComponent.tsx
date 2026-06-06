import { useEffect, useState } from "react";
import { formatarPibEmBilhoes } from "../utils/formatarNumeros";

export default function PanoramaGeralComponent() {

    useEffect(() => {
        getPopulacaoTotalEstado(),
        getPibAgregadoEstado()
    }, []);

    const [referenciaDadosPopulacaoEstadual, setReferenciaDadosPopulacaoEstadual] = useState("");
    const [populacaoTotalEstadual, setPopulacaoTotalEstadual] = useState("");
    const [fonteDadosPopulacaoEstadual, setFonteDadosPopulacaoEstadual] = useState("");
    async function getPopulacaoTotalEstado() {
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/populacaoEstadualRecente`, 
                {
                    next: {
                        revalidate: 60 * 60 * 24, // Revalidar a cada 24 horas
                    }
                }
            );
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                var dadosInternos = resposta?.["Resposta da Requisição"];
                var populacaoEstadual = "--"
                if (dadosInternos) {
                    try {
                        var objetoFormatado = JSON.parse(dadosInternos);
                        populacaoEstadual = objetoFormatado["Quantidade de Pessoas"].toLocaleString("pt-BR");
                        var dataDeColeta = objetoFormatado["Referência dos Dados"];
                        setReferenciaDadosPopulacaoEstadual(dataDeColeta);
                        var fonteDados = objetoFormatado["Fonte dos Dados"];
                        setFonteDadosPopulacaoEstadual(fonteDados);
                    } catch (erro) {
                        console.error("Erro ao converter JSON:", erro);
                    }
                setPopulacaoTotalEstadual(populacaoEstadual);
                }
            }
        } catch (error) {
            console.error("Erro ao buscar população total do estado:", error);
        }
    }

    const [referenciaPibAgregadoEstado, setReferenciaPibAgregadoEstado] = useState("");
    const [pibAgregadoEstado, setPibAgregadoEstado] = useState("");
    const [fontePibAgregadoEstado, setFontePibAgregadoEstado] = useState("");
    async function getPibAgregadoEstado() {
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/economicos/produtoInternoBrutoAgregadoEstadual`, 
                {
                    next: {
                        revalidate: 60 * 60 * 24, // Revalidar a cada 24 horas
                    }
                }
            );
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                var dadosInternos = resposta?.["Resposta da Requisição"];
                var pibAgregado = "--"
                if (dadosInternos) {
                    try {
                        var objetoFormatado = JSON.parse(dadosInternos);
                        pibAgregado = formatarPibEmBilhoes(objetoFormatado["Produto Interno Bruto Agregado"]);
                        var dataDeColeta = objetoFormatado["Referência dos Dados"];
                        setReferenciaPibAgregadoEstado(dataDeColeta);
                        var fonteDados = objetoFormatado["Fonte dos Dados"];
                        setFontePibAgregadoEstado(fonteDados);
                    } catch (erro) {
                        console.error("Erro ao converter JSON:", erro);
                    }
                setPibAgregadoEstado(pibAgregado);
                }
            }
        } catch (error) {
            console.error("Erro ao buscar PIB agregado do estado:", error);
        }
    }

    return (
        <div className="rounded-2xl p-6">
            <div className="group gap-5 flex flex-col rounded-2xl">
                <div className="flex items-center gap-2">
                            <p className="w-1 h-6 rounded bg-sky-600 "></p>
                            <h1 className="text-lg font-bold group-hover:text-sky-800 transition-colors duration-300">Panorama Geral do Estado</h1>
                </div>
                <div className="grid grid-cols-2 gap-3 ">
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        
                        <div className="w-8 h-8 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('total_municipios_icon.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">Total de Municípios</h1>
                        <p className="text-lg font-bold text-gray-800">217</p>
                        <p className="text-xs text-gray-600">Municípios</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-8 h-8 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('populacao_estadual_icon.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">População - {referenciaDadosPopulacaoEstadual}</h1>
                        <p className="text-lg font-bold text-gray-800">{populacaoTotalEstadual}</p>
                        <p className="text-xs text-gray-600">Habitantes</p>
                        <p className="text-xs text-gray-600">Fonte: {fonteDadosPopulacaoEstadual}</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-8 h-8 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('pib_agregado_icon.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">PIB Agregado - {referenciaPibAgregadoEstado}</h1>
                        <p className="text-lg font-bold text-gray-800">R$ {pibAgregadoEstado ? pibAgregadoEstado : "--"}</p>
                        <p className="text-xs text-gray-600">Bilhões</p>
                        <p className="text-xs text-gray-600">Fonte: {fontePibAgregadoEstado}</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-8 h-8 bg-sky-600 md:bg-white group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto"></div>
                        <h1 className="text-xs">IDH Estadual</h1>
                        <p className="text-lg font-bold text-gray-800">--</p>
                        <p className="text-xs text-gray-600">IDH</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-8 h-8 bg-sky-600 md:bg-white group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto"></div>
                        <h1 className="text-xs">Renda per Capita</h1>
                        <p className="text-lg font-bold text-gray-800">R$ --</p>
                        <p className="text-xs text-gray-600">Reais</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-8 h-8 bg-sky-600 md:bg-white group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto"></div>
                        <h1 className="text-xs">Taxa de Desemprego</h1>
                        <p className="text-lg font-bold text-gray-800">--</p>
                        <p className="text-xs text-gray-600">Percentual</p>
                    </div>
                    
                </div>
            </div>
        </div>
    )
}