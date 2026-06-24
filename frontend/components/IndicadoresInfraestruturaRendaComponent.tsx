"use client";

import { useEffect, useState } from "react";
import CartaoIndicadorComponent from "./CartaoIndicadorComponent";

interface IndicadoresInfraestruturaRendaProps {
    municipio: string;
    isFiltrando?: boolean;
}

export default function IndicadoresInfraestruturaRendaComponent({ municipio, isFiltrando }: IndicadoresInfraestruturaRendaProps) {
    const [domiciliosAguaEncanada, setDomiciliosAguaEncanada] = useState<number>();
    const [referenciaAguaEncanada, setReferenciaAguaEncanada] = useState("");
    const [fonteAguaEncanada, setFonteAguaEncanada] = useState("");
    const [domiciliosEnergiaEletrica, setDomiciliosEnergiaEletrica] = useState<number>();
    const [referenciaEnergiaEletrica, setReferenciaEnergiaEletrica] = useState("");
    const [fonteEnergiaEletrica, setFonteEnergiaEletrica] = useState("");
    const [familiasCadastroUnico, setFamiliasCadastroUnico] = useState<number>();
    const [referenciaCadastroUnico, setReferenciaCadastroUnico] = useState("");
    const [fonteCadastroUnico, setFonteCadastroUnico] = useState("");
    const [familiasSituacaoRua, setFamiliasSituacaoRua] = useState<number>();
    const [referenciaSituacaoRua, setReferenciaSituacaoRua] = useState("");
    const [fonteSituacaoRua, setFonteSituacaoRua] = useState("");
    const [familiasTrabalhoInfantil, setFamiliasTrabalhoInfantil] = useState<number>();
    const [referenciaTrabalhoInfantil, setReferenciaTrabalhoInfantil] = useState("");
    const [fonteTrabalhoInfantil, setFonteTrabalhoInfantil] = useState("");
    const [auxilioGas, setAuxilioGas] = useState<number>();
    const [referenciaAuxilioGas, setReferenciaAuxilioGas] = useState("");
    const [fonteAuxilioGas, setFonteAuxilioGas] = useState("");

    useEffect(() => {
        if (municipio !== "") {
            buscarDomiciliosAguaEncanada();
            buscarDomiciliosEnergiaEletrica();
            buscarFamiliasCadastroUnico();
            buscarFamiliasSituacaoRua();
            buscarFamiliasTrabalhoInfantil();
            buscarAuxilioGas();
        }
    }, [municipio]);

    async function buscarDomiciliosAguaEncanada() {
        try {
            const parametros = new URLSearchParams({ schema: "assistencia_social", indicador: "domicilios_com_agua_encanada", municipio });
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/informacoes/resumoDoIndicador?${parametros.toString()}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = typeof dadosInternos === "string" ? JSON.parse(dadosInternos) : dadosInternos;
                    setDomiciliosAguaEncanada(objetoFormatado["VALOR MAIS RECENTE"]);
                    setReferenciaAguaEncanada(objetoFormatado["Referência"]);
                    setFonteAguaEncanada(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar domicílios com água encanada!", error);
        }
    }

    async function buscarDomiciliosEnergiaEletrica() {
        try {
            const parametros = new URLSearchParams({ schema: "assistencia_social", indicador: "domicilios_com_energia_eletrica", municipio });
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/informacoes/resumoDoIndicador?${parametros.toString()}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = typeof dadosInternos === "string" ? JSON.parse(dadosInternos) : dadosInternos;
                    setDomiciliosEnergiaEletrica(objetoFormatado["VALOR MAIS RECENTE"]);
                    setReferenciaEnergiaEletrica(objetoFormatado["Referência"]);
                    setFonteEnergiaEletrica(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar domicílios com energia elétrica!", error);
        }
    }

    async function buscarFamiliasCadastroUnico() {
        try {
            const parametros = new URLSearchParams({ schema: "assistencia_social", indicador: "total_de_familias_inscritas_no_cadastro_unico", municipio });
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/informacoes/resumoDoIndicador?${parametros.toString()}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = typeof dadosInternos === "string" ? JSON.parse(dadosInternos) : dadosInternos;
                    setFamiliasCadastroUnico(objetoFormatado["VALOR MAIS RECENTE"]);
                    setReferenciaCadastroUnico(objetoFormatado["Referência"]);
                    setFonteCadastroUnico(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar famílias do Cadastro Único!", error);
        }
    }

    async function buscarFamiliasSituacaoRua() {
        try {
            const parametros = new URLSearchParams({ schema: "assistencia_social", indicador: "familias_em_situacao_de_rua_inscritas_cadastro_unico", municipio });
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/informacoes/resumoDoIndicador?${parametros.toString()}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = typeof dadosInternos === "string" ? JSON.parse(dadosInternos) : dadosInternos;
                    setFamiliasSituacaoRua(objetoFormatado["VALOR MAIS RECENTE"]);
                    setReferenciaSituacaoRua(objetoFormatado["Referência"]);
                    setFonteSituacaoRua(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar famílias em situação de rua!", error);
        }
    }

    async function buscarFamiliasTrabalhoInfantil() {
        try {
            const parametros = new URLSearchParams({ schema: "assistencia_social", indicador: "familias_em_situacao_de_trabalho_infantil_cadastro_unico", municipio });
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/informacoes/resumoDoIndicador?${parametros.toString()}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = typeof dadosInternos === "string" ? JSON.parse(dadosInternos) : dadosInternos;
                    setFamiliasTrabalhoInfantil(objetoFormatado["VALOR MAIS RECENTE"]);
                    setReferenciaTrabalhoInfantil(objetoFormatado["Referência"]);
                    setFonteTrabalhoInfantil(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar famílias com trabalho infantil!", error);
        }
    }

    async function buscarAuxilioGas() {
        try {
            const parametros = new URLSearchParams({ schema: "assistencia_social", indicador: "programa_auxilio_gas_para_brasileiros", municipio });
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/informacoes/resumoDoIndicador?${parametros.toString()}`);
            if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if (dadosInternos) {
                    const objetoFormatado = typeof dadosInternos === "string" ? JSON.parse(dadosInternos) : dadosInternos;
                    setAuxilioGas(objetoFormatado["VALOR MAIS RECENTE"]);
                    setReferenciaAuxilioGas(objetoFormatado["Referência"]);
                    setFonteAuxilioGas(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Ocorreu um erro ao buscar famílias beneficiárias do Auxílio Gás!", error);
        }
    }

    return (
        <section className="group">
            <div className="flex flex-col gap-6">
                <div className="flex items-center gap-2">
                    <span className="h-6 w-1 rounded bg-sky-600" />
                    <h2 className="text-lg font-bold transition-colors duration-300 group-hover:text-sky-800">Infraestrutura e Assistência Social</h2>
                </div>

                <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
                    <CartaoIndicadorComponent titulo="Domicílios com Água Encanada" descricao="Percentual de domicílios atendidos" detalhes={isFiltrando ? `Referência: ${referenciaAguaEncanada || "--"} - Fonte: ${fonteAguaEncanada || "--"}` : undefined} categoria="Infraestrutura" categoriaClassName="bg-teal-100 text-teal-800" valor={domiciliosAguaEncanada !== undefined && isFiltrando ? `${domiciliosAguaEncanada.toLocaleString("pt-BR")}%` : "--"} />
                    <CartaoIndicadorComponent titulo="Domicílios com Energia Elétrica" descricao="Percentual de domicílios atendidos" detalhes={isFiltrando ? `Referência: ${referenciaEnergiaEletrica || "--"} - Fonte: ${fonteEnergiaEletrica || "--"}` : undefined} categoria="Infraestrutura" categoriaClassName="bg-teal-100 text-teal-800" valor={domiciliosEnergiaEletrica !== undefined && isFiltrando ? `${domiciliosEnergiaEletrica.toLocaleString("pt-BR")}%` : "--"} />
                    <CartaoIndicadorComponent titulo="Famílias no Cadastro Único" descricao="Total de famílias inscritas" detalhes={isFiltrando ? `Referência: ${referenciaCadastroUnico || "--"} - Fonte: ${fonteCadastroUnico || "--"}` : undefined} categoria="Assistência" categoriaClassName="bg-amber-100 text-amber-800" valor={familiasCadastroUnico !== undefined && isFiltrando ? familiasCadastroUnico.toLocaleString("pt-BR") : "--"} />
                    <CartaoIndicadorComponent titulo="Famílias em Situação de Rua" descricao="Famílias inscritas no Cadastro Único" detalhes={isFiltrando ? `Referência: ${referenciaSituacaoRua || "--"} - Fonte: ${fonteSituacaoRua || "--"}` : undefined} categoria="Assistência" categoriaClassName="bg-amber-100 text-amber-800" valor={familiasSituacaoRua !== undefined && isFiltrando ? familiasSituacaoRua.toLocaleString("pt-BR") : "--"} />
                    <CartaoIndicadorComponent titulo="Famílias com Trabalho Infantil" descricao="Famílias inscritas no Cadastro Único" detalhes={isFiltrando ? `Referência: ${referenciaTrabalhoInfantil || "--"} - Fonte: ${fonteTrabalhoInfantil || "--"}` : undefined} categoria="Assistência" categoriaClassName="bg-amber-100 text-amber-800" valor={familiasTrabalhoInfantil !== undefined && isFiltrando ? familiasTrabalhoInfantil.toLocaleString("pt-BR") : "--"} />
                    <CartaoIndicadorComponent titulo="Auxílio Gás para Brasileiros" descricao="Percentual de famílias beneficiárias" detalhes={isFiltrando ? `Referência: ${referenciaAuxilioGas || "--"} - Fonte: ${fonteAuxilioGas || "--"}` : undefined} categoria="Assistência" categoriaClassName="bg-amber-100 text-amber-800" valor={auxilioGas !== undefined && isFiltrando ? `${auxilioGas.toLocaleString("pt-BR")}%` : "--"} />
                </div>
            </div>
        </section>
    );
}
