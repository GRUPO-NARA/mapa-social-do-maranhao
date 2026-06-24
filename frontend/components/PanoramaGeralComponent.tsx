import { useEffect, useState } from "react";
import { formatarPibEmBilhoes } from "../utils/formatarNumeros";
import AjudaIndicadorComponent from "./AjudaIndicadorComponent";


export default function PanoramaGeralComponent() {

    useEffect(() => {
        buscarPopulacaoTotalEstado(),
        buscarPibAgregadoEstado(),
        buscarProdutoInternoBrutoPerCapitaEstadual(),
        buscarTaxaAnalfabetismo15AnosOuMaisEstadual(),
        buscarMortalidadeInfantilEstadual()
    }, []);

    const [referenciaDadosPopulacaoEstadual, setReferenciaDadosPopulacaoEstadual] = useState("");
    const [populacaoTotalEstadual, setPopulacaoTotalEstadual] = useState("");
    const [fonteDadosPopulacaoEstadual, setFonteDadosPopulacaoEstadual] = useState("");
    async function buscarPopulacaoTotalEstado() {
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/populacaoEstadualRecente`, 
                {
                    cache: "force-cache" 
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
    async function buscarPibAgregadoEstado() {
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/economicos/produtoInternoBrutoAgregadoEstadual`, 
                {
                    cache: "force-cache" 
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

    const [produtoInternoBrutoPerCapitaEstadual, setProdutoInternoBrutoPerCapitaEstadual] = useState("");
    const [referenciaProdutoInternoBrutoPerCapitaEstadual, setReferenciaProdutoInternoBrutoPerCapitaEstadual] = useState("");
    const [fonteProdutoInternoBrutoPerCapitaEstadual, setFonteProdutoInternoBrutoPerCapitaEstadual] = useState("");
    async function buscarProdutoInternoBrutoPerCapitaEstadual() {
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/economicos/pibPerCapitaEstadual`,
                {
                    cache: "force-cache" 
                }
            );
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                var dadosInternos = resposta?.["Resposta da Requisição"];
                var pibPerCapita = "--"
                if (dadosInternos) {
                    try {
                        var objetoFormatado = JSON.parse(dadosInternos);
                        pibPerCapita = objetoFormatado["Produto Interno Bruto per Capita"].toLocaleString("pt-BR", { style: 'currency', currency: 'BRL' });
                        var dataDeColeta = objetoFormatado["Referência dos Dados"];
                        var fonte = objetoFormatado["Fonte dos Dados"];

                        setProdutoInternoBrutoPerCapitaEstadual(pibPerCapita);
                        setReferenciaProdutoInternoBrutoPerCapitaEstadual(dataDeColeta);
                        setFonteProdutoInternoBrutoPerCapitaEstadual(fonte);
                    } catch (error) {
                        console.error("Erro ao analisar os dados do Produto Interno Bruto per Capita:", error);
                    }
                }
            }
        } catch (error) {
            console.error("Erro ao buscar Produto Interno Bruto per Capita do estado:", error);
        }
    }

    const [taxaAnalfabetismo15AnosOuMaisEstadual, setTaxaAnalfabetismo15AnosOuMaisEstadual] = useState("");
    const [referenciaTaxaAnalfabetismo15AnosOuMaisEstadual, setReferenciaTaxaAnalfabetismo15AnosOuMaisEstadual] = useState("");
    const [fonteTaxaAnalfabetismo15AnosOuMaisEstadual, setFonteTaxaAnalfabetismo15AnosOuMaisEstadual] = useState("");
    async function buscarTaxaAnalfabetismo15AnosOuMaisEstadual() {
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/educacao/taxaAnalfabetismo15AnosOuMaisEstadual`,
                {
                    cache: "force-cache" 
                }
            );
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                var dadosInternos = resposta?.["Resposta da Requisição"];
                var taxaAnalfabetismo = "--"
                if (dadosInternos) {
                    try {
                        var objetoFormatado = JSON.parse(dadosInternos);
                        taxaAnalfabetismo = objetoFormatado["Taxa de Analfabetismo 15 anos ou mais"].toLocaleString("pt-BR", { style: 'percent', minimumFractionDigits: 2 });
                        var dataDeColeta = objetoFormatado["Referência dos Dados"];
                        var fonte = objetoFormatado["Fonte dos Dados"];

                        setTaxaAnalfabetismo15AnosOuMaisEstadual(taxaAnalfabetismo);
                        setReferenciaTaxaAnalfabetismo15AnosOuMaisEstadual(dataDeColeta);
                        setFonteTaxaAnalfabetismo15AnosOuMaisEstadual(fonte);
                    } catch (error) {
                        console.error("Erro ao analisar os dados da taxa de analfabetismo 15 anos ou mais do estado:", error);
                    }
                }
            }
        } catch (error) {
            console.error("Erro ao buscar taxa de analfabetismo 15 anos ou mais do estado:", error);
        }
    }

    const [taxaMortalidadeInfantilEstadual, setMortalidadeInfantilEstadual] = useState("");
    const [referenciaTaxaMortalidadeInfantilEstadual, setReferenciaMortalidadeInfantilEstadual] = useState("");
    const [fonteTaxaMortalidadeInfantilEstadual, setFonteMortalidadeInfantilEstadual] = useState("");
    async function buscarMortalidadeInfantilEstadual() {
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/saude/taxaMortalidadeInfantilEstadual`,
                {
                    cache: "force-cache" 
                }
            );
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                var dadosInternos = resposta?.["Resposta da Requisição"];
                var mortalidadeInfantil = "--"
                if (dadosInternos) {
                    try {
                        var objetoFormatado = JSON.parse(dadosInternos);
                        mortalidadeInfantil = objetoFormatado["Taxa de Mortalidade Infantil"].toLocaleString("pt-BR", { style: 'percent', minimumFractionDigits: 2 });
                        var dataDeColeta = objetoFormatado["Referência dos Dados"];
                        var fonte = objetoFormatado["Fonte dos Dados"];

                        setMortalidadeInfantilEstadual(mortalidadeInfantil);
                        setReferenciaMortalidadeInfantilEstadual(dataDeColeta);
                        setFonteMortalidadeInfantilEstadual(fonte);
                    } catch (error) {
                        console.error("Erro ao analisar os dados da mortalidade infantil do estado:", error);
                    }
                }
            }
        } catch (error) {
            console.error("Erro ao buscar mortalidade infantil do estado:", error);
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
                                <p className="text-xs text-slate-500">Dados Estaduais</p>
                            </div>
                </div>
                <div className="grid grid-cols-2 gap-3 ">
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        
                        <span className="rounded bg-slate-100 px-2 py-1 text-xs font-semibold text-slate-800">Estado</span>
                        <div className="flex items-center justify-center gap-1.5">
                            <h1 className="text-xs">Total de Municípios</h1>
                            <AjudaIndicadorComponent titulo="Total de Municípios" texto="Quantidade de municípios que compõem o Maranhão." alinhamento="inicio" />
                        </div>
                        <p className="text-lg font-bold text-gray-800">217</p>
                        <p className="text-xs text-gray-600">Municípios</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        <span className="rounded bg-cyan-100 px-2 py-1 text-xs font-semibold text-cyan-800">Demografia</span>
                        <div className="flex items-center justify-center gap-1.5">
                            <h1 className="text-xs">População - {referenciaDadosPopulacaoEstadual}</h1>
                            <AjudaIndicadorComponent titulo="População" texto="Total de pessoas residentes no estado." alinhamento="fim" />
                        </div>
                        <p className="text-lg font-bold text-gray-800">{populacaoTotalEstadual}</p>
                        <p className="text-xs text-gray-600">Habitantes</p>
                        <p className="text-xs text-gray-600">Fonte: {fonteDadosPopulacaoEstadual}</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        <span className="rounded bg-sky-100 px-2 py-1 text-xs font-semibold text-sky-800">Economia</span>
                        <div className="flex items-center justify-center gap-1.5">
                            <h1 className="text-xs">PIB Agregado - {referenciaPibAgregadoEstado}</h1>
                            <AjudaIndicadorComponent titulo="PIB Agregado" texto="Soma dos bens e serviços produzidos no estado em um ano." alinhamento="inicio" />
                        </div>
                        <p className="text-lg font-bold text-gray-800">R$ {pibAgregadoEstado ? pibAgregadoEstado : "--"}</p>
                        <p className="text-xs text-gray-600">Bilhões</p>
                        <p className="text-xs text-gray-600">Fonte: {fontePibAgregadoEstado}</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        <span className="rounded bg-sky-100 px-2 py-1 text-xs font-semibold text-sky-800">Economia</span>
                        <div className="flex items-center justify-center gap-1.5">
                            <h1 className="text-xs">Produto Interno Bruto per Capita - {referenciaProdutoInternoBrutoPerCapitaEstadual}</h1>
                            <AjudaIndicadorComponent titulo="Produto Interno Bruto per Capita" texto="PIB estadual dividido pelo número de habitantes." alinhamento="fim" />
                        </div>
                        <p className="text-lg font-bold text-gray-800">{produtoInternoBrutoPerCapitaEstadual}</p>
                        <p className="text-xs text-gray-600">Bilhões</p>
                        <p className="text-xs text-gray-600">Fonte: {fonteProdutoInternoBrutoPerCapitaEstadual}</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        <span className="rounded bg-emerald-100 px-2 py-1 text-xs font-semibold text-emerald-800">Educação</span>
                        <div className="flex items-center justify-center gap-1.5">
                            <h1 className="text-xs">Taxa de Analfabetismo 15 anos ou mais - {referenciaTaxaAnalfabetismo15AnosOuMaisEstadual}</h1>
                            <AjudaIndicadorComponent titulo="Taxa de Analfabetismo" texto="Percentual de pessoas com 15 anos ou mais que não sabem ler e escrever." alinhamento="inicio" />
                        </div>
                        <p className="text-lg font-bold text-gray-800">{taxaAnalfabetismo15AnosOuMaisEstadual}</p>
                        <p className="text-xs text-gray-600">Percentual</p>
                        <p className="text-xs text-gray-600">Fonte: {fonteTaxaAnalfabetismo15AnosOuMaisEstadual}</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center items-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                        <span className="rounded bg-rose-100 px-2 py-1 text-xs font-semibold text-rose-800">Saúde</span>
                        <div className="flex items-center justify-center gap-1.5">
                            <h1 className="text-xs">Taxa de Mortalidade Infantil - {referenciaTaxaMortalidadeInfantilEstadual}</h1>
                            <AjudaIndicadorComponent titulo="Taxa de Mortalidade Infantil" texto="Óbitos de crianças menores de um ano por mil nascidos vivos." alinhamento="fim" />
                        </div>
                        <p className="text-lg font-bold text-gray-800">{taxaMortalidadeInfantilEstadual}</p>
                        <p className="text-xs text-gray-600">Percentual</p>
                        <p className="text-xs text-gray-600">Fonte: {fonteTaxaMortalidadeInfantilEstadual}</p>
                    </div>
                    
                </div>
            </div>
        </section>
    )
}
