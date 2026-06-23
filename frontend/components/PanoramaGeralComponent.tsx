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
                        revalidate: 60 * 60 * 24,
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
        <section className="rounded-3xl border border-slate-200 bg-white p-5 shadow-sm sm:p-6">
            <div className="group gap-5 flex flex-col rounded-2xl">
                <div className="flex items-center gap-3">
                            <span className="flex h-10 w-10 items-center justify-center rounded-2xl bg-sky-100 text-sky-700">
                                <svg viewBox="0 0 24 24" className="h-5 w-5" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
                                    <path d="M4 19V9m6 10V5m6 14v-7m4 7H2" strokeLinecap="round" />
                                </svg>
                            </span>
                            <div>
                                <h1 className="text-lg font-bold text-slate-900">Panorama Geral</h1>
                                <p className="text-xs text-slate-500">Síntese estadual</p>
                            </div>
                </div>
                <div className="grid grid-cols-2 gap-3 ">
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        
                        <span className="rounded bg-slate-100 px-2 py-1 text-xs font-semibold text-slate-800">Estado</span>
                        <h1 className="text-xs">Total de Municípios</h1>
                        <p className="text-lg font-bold text-gray-800">217</p>
                        <p className="text-xs text-gray-600">Municípios</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        <span className="rounded bg-cyan-100 px-2 py-1 text-xs font-semibold text-cyan-800">Demografia</span>
                        <h1 className="text-xs">População - {referenciaDadosPopulacaoEstadual}</h1>
                        <p className="text-lg font-bold text-gray-800">{populacaoTotalEstadual}</p>
                        <p className="text-xs text-gray-600">Habitantes</p>
                        <p className="text-xs text-gray-600">Fonte: {fonteDadosPopulacaoEstadual}</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        <span className="rounded bg-sky-100 px-2 py-1 text-xs font-semibold text-sky-800">Economia</span>
                        <h1 className="text-xs">PIB Agregado - {referenciaPibAgregadoEstado}</h1>
                        <p className="text-lg font-bold text-gray-800">R$ {pibAgregadoEstado ? pibAgregadoEstado : "--"}</p>
                        <p className="text-xs text-gray-600">Bilhões</p>
                        <p className="text-xs text-gray-600">Fonte: {fontePibAgregadoEstado}</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        <span className="rounded bg-sky-100 px-2 py-1 text-xs font-semibold text-sky-800">Economia</span>
                        <h1 className="text-xs">Média municipal do PIB Per Capita - {referenciaPibAgregadoEstado}</h1>
                        <p className="text-lg font-bold text-gray-800">{pibAgregadoEstado ? pibAgregadoEstado : "--"}</p>
                        <p className="text-xs text-gray-600">Bilhões</p>
                        <p className="text-xs text-gray-600">Fonte: {fontePibAgregadoEstado}</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        <span className="rounded bg-emerald-100 px-2 py-1 text-xs font-semibold text-emerald-800">Educação</span>
                        <h1 className="text-xs">Média municipal da taxa de analfabetismo - {referenciaPibAgregadoEstado}</h1>
                        <p className="text-lg font-bold text-gray-800">{pibAgregadoEstado ? pibAgregadoEstado : "--"}</p>
                        <p className="text-xs text-gray-600">Bilhões</p>
                        <p className="text-xs text-gray-600">Fonte: {fontePibAgregadoEstado}</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        <span className="rounded bg-rose-100 px-2 py-1 text-xs font-semibold text-rose-800">Saúde</span>
                        <h1 className="text-xs">Média Municipal da Mortalidade Infantil - {referenciaPibAgregadoEstado}</h1>
                        <p className="text-lg font-bold text-gray-800">{pibAgregadoEstado ? pibAgregadoEstado : "--"}</p>
                        <p className="text-xs text-gray-600">Bilhões</p>
                        <p className="text-xs text-gray-600">Fonte: {fontePibAgregadoEstado}</p>
                    </div>
                    
                </div>
            </div>
        </section>
    )
}
