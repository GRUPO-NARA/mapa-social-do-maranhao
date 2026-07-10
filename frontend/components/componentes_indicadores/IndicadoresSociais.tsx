"use client";

import { useEffect, useState } from "react";
import CartaoIndicadorComponent  from "@/components/cards/CardIndicador";

interface IndicadoresSociaisProps {
    municipio: string;
    isFiltrando?: boolean;
}

export default function IndicadoresSociaisComponent({ municipio, isFiltrando }: IndicadoresSociaisProps) {
    const [taxaAnalfabetismo, setTaxaAnalfabetismo] = useState<number>();
    const [referenciaTaxaAnalfabetismo, setReferenciaTaxaAnalfabetismo] = useState("");
    const [fonteTaxaAnalfabetismo, setFonteTaxaAnalfabetismo] = useState("");
    const [aprovacaoEnsinoFundamental, setAprovacaoEnsinoFundamental] = useState<number>();
    const [referenciaAprovacaoFundamental, setReferenciaAprovacaoFundamental] = useState("");
    const [fonteAprovacaoFundamental, setFonteAprovacaoFundamental] = useState("");
    const [aprovacaoEnsinoMedio, setAprovacaoEnsinoMedio] = useState<number>();
    const [referenciaAprovacaoMedio, setReferenciaAprovacaoMedio] = useState("");
    const [fonteAprovacaoMedio, setFonteAprovacaoMedio] = useState("");
    const [mortalidadeInfantil, setMortalidadeInfantil] = useState<number>();
    const [referenciaMortalidadeInfantil, setReferenciaMortalidadeInfantil] = useState("");
    const [fonteMortalidadeInfantil, setFonteMortalidadeInfantil] = useState("");
    const [nascidosVivosMaesAdolescentes, setNascidosVivosMaesAdolescentes] = useState<number>();
    const [referenciaNascidosVivos, setReferenciaNascidosVivos] = useState("");
    const [fonteNascidosVivos, setFonteNascidosVivos] = useState("");
    const [razaoMortalidadeMaterna, setRazaoMortalidadeMaterna] = useState<number>();
    const [referenciaMortalidadeMaterna, setReferenciaMortalidadeMaterna] = useState("");
    const [fonteMortalidadeMaterna, setFonteMortalidadeMaterna] = useState("");

    useEffect(() => {
        if (municipio !== "") {
            buscarTaxaAnalfabetismo();
            buscarAprovacaoEnsinoFundamental();
            buscarAprovacaoEnsinoMedio();
            buscarMortalidadeInfantil();
            buscarNascidosVivosMaesAdolescentes();
            buscarRazaoMortalidadeMaterna();
        }
    }, [municipio]);

    async function buscarTaxaAnalfabetismo() {
        try {
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/educacao/taxaAnalfabetismo15AnosOuMais?municipio=${municipio}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setTaxaAnalfabetismo(objetoFormatado["Taxa de Analfabetismo 15 anos ou mais"]);
                    setReferenciaTaxaAnalfabetismo(objetoFormatado["Referência dos Dados"]);
                    setFonteTaxaAnalfabetismo(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar a taxa de analfabetismo!", error);
        }
    }

    async function buscarAprovacaoEnsinoFundamental() {
        try {
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/educacao/taxaAprovacaoEnsinoFundamental?municipio=${municipio}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setAprovacaoEnsinoFundamental(objetoFormatado["Taxa de Aprovação no Ensino Fundamental"]);
                    setReferenciaAprovacaoFundamental(objetoFormatado["Referência dos Dados"]);
                    setFonteAprovacaoFundamental(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar a aprovação no Ensino Fundamental!", error);
        }
    }

    async function buscarAprovacaoEnsinoMedio() {
        try {
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/educacao/taxaAprovacaoEnsinoMedio?municipio=${municipio}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setAprovacaoEnsinoMedio(objetoFormatado["Taxa de Aprovação no Ensino Médio"]);
                    setReferenciaAprovacaoMedio(objetoFormatado["Referência dos Dados"]);
                    setFonteAprovacaoMedio(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar a aprovação no Ensino Médio!", error);
        }
    }

    async function buscarMortalidadeInfantil() {
        try {
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/saude/taxaMortalidadeInfantil?municipio=${municipio}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setMortalidadeInfantil(objetoFormatado["Taxa de Mortalidade Infantil"]);
                    setReferenciaMortalidadeInfantil(objetoFormatado["Referência dos Dados"]);
                    setFonteMortalidadeInfantil(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar a mortalidade infantil!", error);
        }
    }

    async function buscarNascidosVivosMaesAdolescentes() {
        try {
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/saude/nascidosVivosMaesAdolescentes?municipio=${municipio}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setNascidosVivosMaesAdolescentes(objetoFormatado["Nascidos Vivos de Mães Adolescentes"]);
                    setReferenciaNascidosVivos(objetoFormatado["Referência dos Dados"]);
                    setFonteNascidosVivos(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar nascidos vivos de mães adolescentes!", error);
        }
    }

    async function buscarRazaoMortalidadeMaterna() {
        try {
            const parametros = new URLSearchParams({
                schema: "saude",
                indicador: "razao_mortalidade_materna",
                municipio,
            });
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/informacoes/resumoDoIndicador?${parametros.toString()}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = typeof dadosInternos === "string" ? JSON.parse(dadosInternos) : dadosInternos;
                    setRazaoMortalidadeMaterna(objetoFormatado["VALOR MAIS RECENTE"]);
                    setReferenciaMortalidadeMaterna(objetoFormatado["Referência"]);
                    setFonteMortalidadeMaterna(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar a razão de mortalidade materna!", error);
        }
    }

    return (
        <section className="group">
            <div className="flex flex-col gap-6">
                <div className="flex items-center gap-2">
                    <span className="h-6 w-1 rounded bg-sky-600" />
                    <h2 className="text-lg font-bold transition-colors duration-300 group-hover:text-sky-800">Indicadores Sociais</h2>
                </div>

                <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
                    <CartaoIndicadorComponent
                        titulo="Analfabetismo 15+" descricao="Percentual de pessoas com 15 anos ou mais que não sabem ler e escrever." 
                        detalhes={isFiltrando ? `Referência: ${referenciaTaxaAnalfabetismo || "--"} - Fonte: ${fonteTaxaAnalfabetismo || "--"}` : undefined} 
                        valor={taxaAnalfabetismo !== undefined && isFiltrando ? `${taxaAnalfabetismo.toLocaleString("pt-BR")}%` : "--"} />
                    <CartaoIndicadorComponent 
                        titulo="Aprovação no Ensino Fundamental" descricao="Percentual de alunos aprovados ao final do ensino fundamental." 
                        detalhes={isFiltrando ? `Referência: ${referenciaAprovacaoFundamental || "--"} - Fonte: ${fonteAprovacaoFundamental || "--"}` : undefined} 
                        valor={aprovacaoEnsinoFundamental !== undefined && isFiltrando ? `${aprovacaoEnsinoFundamental.toLocaleString("pt-BR")}%` : "--"} />
                    <CartaoIndicadorComponent 
                        titulo="Aprovação no Ensino Médio" descricao="Percentual de alunos aprovados ao final do ensino médio." 
                        detalhes={isFiltrando ? `Referência: ${referenciaAprovacaoMedio || "--"} - Fonte: ${fonteAprovacaoMedio || "--"}` : undefined} 
                        valor={aprovacaoEnsinoMedio !== undefined && isFiltrando ? `${aprovacaoEnsinoMedio.toLocaleString("pt-BR")}%` : "--"} />
                    <CartaoIndicadorComponent 
                        titulo="Mortalidade Infantil" descricao="Óbitos de crianças menores de um ano por mil nascidos vivos." 
                        detalhes={isFiltrando ? `Referência: ${referenciaMortalidadeInfantil || "--"} - Fonte: ${fonteMortalidadeInfantil || "--"}` : undefined} 
                        valor={mortalidadeInfantil !== undefined && isFiltrando ? mortalidadeInfantil.toLocaleString("pt-BR") + "%" : "--"} />
                    <CartaoIndicadorComponent 
                        titulo="Nascidos Vivos de Mães Adolescentes" descricao="Nascimentos registrados de mães com idade entre 10 e 19 anos." 
                        detalhes={isFiltrando ? `Referência: ${referenciaNascidosVivos || "--"} - Fonte: ${fonteNascidosVivos || "--"}` : undefined} 
                        valor={nascidosVivosMaesAdolescentes !== undefined && isFiltrando ? nascidosVivosMaesAdolescentes.toLocaleString("pt-BR") + "%" : "--"} />
                    <CartaoIndicadorComponent 
                        titulo="Razão de Mortalidade Materna" descricao="Óbitos maternos por 100 mil nascidos vivos." 
                        detalhes={isFiltrando ? `Referência: ${referenciaMortalidadeMaterna || "--"} - Fonte: ${fonteMortalidadeMaterna || "--"}` : undefined} 
                        valor={razaoMortalidadeMaterna !== undefined && isFiltrando ? razaoMortalidadeMaterna.toLocaleString("pt-BR") + "%" : "--"} />
                </div>
            </div>
        </section>
    );
}
