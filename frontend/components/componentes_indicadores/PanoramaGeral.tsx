import { useEffect, useState } from "react";
import { formatarPibEmBilhoes } from "../../tratamento/formatarNumeros";
import CartaoIndicadorPanoramaGeral from "../cards/CardsPanoramaGeral";


export default function PanoramaGeralComponent() {

    useEffect(() => {
        buscarPopulacaoTotalEstado();
        buscarPibAgregadoEstado();
        buscarProdutoInternoBrutoPerCapitaEstadual();
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
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if(dadosInternos) {
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setPopulacaoTotalEstadual(objetoFormatado["Quantidade de Pessoas"].toLocaleString("pt-BR"));
                    setReferenciaDadosPopulacaoEstadual(objetoFormatado["Referência dos Dados"]);
                    setFonteDadosPopulacaoEstadual(objetoFormatado["Fonte dos Dados"]);
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
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if(dadosInternos) {
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setPibAgregadoEstado(formatarPibEmBilhoes(objetoFormatado["Produto Interno Bruto Agregado"]));
                    setReferenciaPibAgregadoEstado(objetoFormatado["Referência dos Dados"]);
                    setFontePibAgregadoEstado(objetoFormatado["Fonte dos Dados"]);
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
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if(dadosInternos) {
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setProdutoInternoBrutoPerCapitaEstadual(objetoFormatado["Produto Interno Bruto per Capita"].toLocaleString("pt-BR", { style: 'currency', currency: 'BRL' }));
                    setReferenciaProdutoInternoBrutoPerCapitaEstadual(objetoFormatado["Referência dos Dados"]);
                    setFonteProdutoInternoBrutoPerCapitaEstadual(objetoFormatado["Fonte dos Dados"]);
                }
            }
        } catch (error) {
            console.error("Erro ao buscar Produto Interno Bruto per Capita do estado:", error);
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
                    <CartaoIndicadorPanoramaGeral
                        titulo="População"
                        descricao="Total de pessoas residentes no estado"
                        categoria="Demografia"
                        texto="Total de pessoas residentes no estado."
                        valor={populacaoTotalEstadual}
                        referencia={referenciaDadosPopulacaoEstadual}
                        fonte={fonteDadosPopulacaoEstadual}
                        alinhamento="fim"
                    />
                    <CartaoIndicadorPanoramaGeral
                        titulo="PIB Agregado"
                        descricao="Bens e serviços produzidos no estado"
                        categoria="Economia"
                        texto="Soma dos bens e serviços produzidos no estado em um ano."
                        valor={`R$ ${pibAgregadoEstado ? `${pibAgregadoEstado} Bilhões` : "--"}`}
                        referencia={referenciaPibAgregadoEstado}
                        fonte={fontePibAgregadoEstado}
                        alinhamento="inicio"
                    />   
                    <CartaoIndicadorPanoramaGeral
                        titulo="Média municipal do PIB per capita"
                        descricao="Média dos municípios do estado"
                        categoria="Economia"
                        texto="Média simples do PIB per capita dos municípios do Maranhão no ano de referência."
                        valor={produtoInternoBrutoPerCapitaEstadual ? produtoInternoBrutoPerCapitaEstadual : "--"}
                        referencia={referenciaProdutoInternoBrutoPerCapitaEstadual}
                        fonte={fonteProdutoInternoBrutoPerCapitaEstadual}
                        alinhamento="fim"
                    />
                </div>
            </div>
        </section>
    )
}
