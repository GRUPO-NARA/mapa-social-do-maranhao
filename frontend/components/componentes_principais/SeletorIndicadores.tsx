"use client";

import { useEffect, useState } from "react";
import { formatarNomeIndicador } from "@/tratamento/formatarIndicador";

type TipoGrafico = "linha" | "barra";

const INDICADORES_SEM_SERIE_HISTORICA = new Set([
    "idade_mediana",
    "percentual_de_envelhecimento",
    "pessoas_de_2_anos_ou_mais_com_deficiencia",
]);

interface SeletorIndicadoresProps {
    tipoDoIndicador: string;
    municipioSelecionado: boolean;
    municipioAtual?: string;
    setTipoGraficoAtivo?: (tipo: TipoGrafico | null) => void;
    setIndicadorAtivo?: (indicador: string) => void;
    setIsFiltroAplicado?: (isAplicado: boolean) => void;
    setMunicipiosComparacao?: (municipios: string[]) => void;
    setAnoPrevisao?: (ano: number | null) => void;
}

export default function SeletorIndicadoresComponent({
    tipoDoIndicador,
    municipioSelecionado,
    municipioAtual = "",
    setTipoGraficoAtivo,
    setIndicadorAtivo,
    setIsFiltroAplicado,
    setMunicipiosComparacao,
    setAnoPrevisao,
}: SeletorIndicadoresProps) {
    const [tentouAplicarFiltro, setTentouAplicarFiltro] = useState(false);
    const [indicadorSelecionado, setIndicadorSelecionado] = useState("");
    const [tipoGraficoSelecionado, setTipoGraficoSelecionado] = useState<TipoGrafico | null>(null)
    const [listaIndicadores, setListaIndicadores] = useState<string[]>([]);
    const [isCarregandoIndicadores, setIsCarregandoIndicadores] = useState(false);
    const [erroIndicadores, setErroIndicadores] = useState(false);
    const [anoPrevisaoSelecionado, setAnoPrevisaoSelecionado] = useState(2030);

    const isIndicadorSelecionado = indicadorSelecionado !== "";
    const indicadorSemSerieHistorica = INDICADORES_SEM_SERIE_HISTORICA.has(indicadorSelecionado);
    const municipioPrincipal = municipioSelecionado ? municipioAtual.trim() : "";
    const nomeArea = tipoDoIndicador === "educacao"
        ? "educacional"
        : tipoDoIndicador === "saude"
            ? "de saúde"
            : "de assistência social";

    useEffect(() => {
        if (!municipioSelecionado) return;

        let componenteAtivo = true;

        async function buscarIndicadores() {
            try {
                setIsCarregandoIndicadores(true);
                setErroIndicadores(false);
                const requisicao = await fetch(
                    `${process.env.NEXT_PUBLIC_API_URL}/informacoes/tabelasDoSchema?schema=${tipoDoIndicador}`
                );

                if (!requisicao.ok) {
                    throw new Error("Não foi possível carregar os indicadores");
                }

                const resposta = await requisicao.json();
                if (componenteAtivo) {
                    setListaIndicadores(resposta?.["Resposta da Requisição"] || []);
                }
            } catch (error) {
                console.error("Erro ao buscar indicadores:", error);
                if (componenteAtivo) {
                    setErroIndicadores(true);
                }
            } finally {
                if (componenteAtivo) {
                    setIsCarregandoIndicadores(false);
                }
            }
        }

        buscarIndicadores();
        return () => {
            componenteAtivo = false;
        };
    }, [tipoDoIndicador, municipioSelecionado]);


    function selecionarIndicador(novoIndicador: string) {
        setIndicadorSelecionado(novoIndicador);
        setTentouAplicarFiltro(false);
        setIsFiltroAplicado?.(false);
        setTipoGraficoAtivo?.(null);
        setIndicadorAtivo?.("");
        setAnoPrevisao?.(null);

        if (novoIndicador === "") {
            setTipoGraficoSelecionado(null);
        } else if (INDICADORES_SEM_SERIE_HISTORICA.has(novoIndicador) && tipoGraficoSelecionado === "linha") {
            setTipoGraficoSelecionado(null);
        }
    }

    function gerarAnalise(tipo: TipoGrafico) {
        setTentouAplicarFiltro(true);

        if (tipo === "linha" && indicadorSemSerieHistorica) {
            setTipoGraficoSelecionado(null);
            setTipoGraficoAtivo?.(null);
            setIsFiltroAplicado?.(false);
            return;
        }

        if (municipioSelecionado && isIndicadorSelecionado) {
            setTipoGraficoSelecionado(tipo);
            setTipoGraficoAtivo?.(tipo);
            setIndicadorAtivo?.(indicadorSelecionado);
            setMunicipiosComparacao?.(tipo === "barra" && municipioPrincipal ? [municipioPrincipal] : []);
            setAnoPrevisao?.(null);
            setIsFiltroAplicado?.(true);
            return;
        }

        setTipoGraficoSelecionado(null);
        setTipoGraficoAtivo?.(null);
        setIndicadorAtivo?.("");
        setIsFiltroAplicado?.(false);
    }

    function limparFiltros() {
        setIndicadorSelecionado("");
        setTentouAplicarFiltro(false);
        setTipoGraficoSelecionado(null);
        setAnoPrevisaoSelecionado(2030);
        setTipoGraficoAtivo?.(null);
        setIndicadorAtivo?.("");
        setMunicipiosComparacao?.([]);
        setAnoPrevisao?.(null);
        setIsFiltroAplicado?.(false);
    }

    return (
        <div className="flex min-w-0 flex-col gap-5">
        <section className="min-w-0 rounded-3xl border border-slate-200 bg-white p-5 shadow-sm sm:p-7">
            <div className="mb-6 flex items-start gap-4">
                <div className="flex h-11 w-11 shrink-0 items-center justify-center rounded-2xl bg-sky-100 text-sky-700">
                    <svg viewBox="0 0 24 24" className="h-5 w-5" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
                        <path d="M4 6h16M7 12h10M10 18h4" strokeLinecap="round" />
                    </svg>
                </div>
                <div>
                    <h3 className="text-lg font-bold text-slate-900">Indicador e visualização</h3>
                    <p className="mt-1 text-sm text-slate-500">Combine um indicador {nomeArea} com o formato de gráfico mais útil para sua análise.</p>
                </div>
            </div>

            {!municipioSelecionado && (
                <div className="mb-6 flex items-start gap-3 rounded-2xl border border-sky-200 bg-sky-50 p-4 text-sm text-sky-900">
                    <span className="flex h-6 w-6 shrink-0 items-center justify-center rounded-full bg-sky-800 text-xs font-bold text-white">1</span>
                    <div>
                        <p className="font-bold">Selecione primeiro um município</p>
                        <p className="mt-1 text-sky-700">Escolha e aplique um município na Etapa 1 para liberar os indicadores.</p>
                    </div>
                </div>
            )}

            <div className={`flex min-w-0 flex-col gap-5 ${!municipioSelecionado ? "opacity-55" : ""}`}>
                <div className="flex min-w-0 flex-col gap-2">
                    <label className="text-sm font-semibold text-slate-700" htmlFor="seletor-indicador">Indicador {nomeArea}</label>
                    <div className="relative">
                        <select
                            id="seletor-indicador"
                            className="w-full min-w-0 appearance-none rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3.5 pr-10 text-sm text-slate-800 outline-none transition focus:border-sky-500 focus:bg-white focus:ring-4 focus:ring-sky-100 disabled:cursor-wait disabled:text-slate-400 sm:text-base"
                            value={indicadorSelecionado}
                            onChange={(e) => selecionarIndicador(e.target.value)}
                            disabled={!municipioSelecionado || isCarregandoIndicadores}
                        >
                            <option value="">{isCarregandoIndicadores ? "Carregando indicadores..." : "Selecione um indicador"}</option>
                            {listaIndicadores.map((indicador) => (
                                <option key={indicador} value={indicador}>{formatarNomeIndicador(indicador)}</option>
                            ))}
                        </select>
                        <svg viewBox="0 0 24 24" className="pointer-events-none absolute right-4 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-500" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
                            <path d="m7 10 5 5 5-5" strokeLinecap="round" strokeLinejoin="round" />
                        </svg>
                    </div>
                    {erroIndicadores && <p className="text-xs text-rose-600">Não foi possível carregar os indicadores. Verifique a conexão com a API.</p>}
                </div>

                <div className="grid min-w-0 gap-4">
                    <article className={`rounded-2xl border p-4 transition ${tipoGraficoSelecionado === "linha" ? "border-sky-500 bg-sky-50" : "border-slate-200 bg-white"}`}>
                        <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
                            <div>
                                <h4 className="font-bold text-slate-900">Evolução histórica</h4>
                                <p className="mt-1 text-sm leading-6 text-slate-500">Mostra a série temporal do município selecionado e libera a projeção por ano.</p>
                            </div>
                            <button
                                type="button"
                                disabled={!municipioSelecionado || !isIndicadorSelecionado || indicadorSemSerieHistorica}
                                onClick={() => gerarAnalise("linha")}
                                className="rounded-xl border border-sky-200 bg-white px-5 py-3 text-sm font-semibold text-sky-800 transition hover:border-sky-400 hover:bg-sky-100 disabled:cursor-not-allowed disabled:opacity-50"
                            >
                                Gerar evolução
                            </button>
                        </div>
                    </article>

                    <article className={`rounded-2xl border p-4 transition ${tipoGraficoSelecionado === "barra" ? "border-sky-500 bg-sky-50" : "border-slate-200 bg-white"}`}>
                        <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
                            <div>
                                <h4 className="font-bold text-slate-900">Comparação territorial</h4>
                                <p className="mt-1 text-sm leading-6 text-slate-500">Compara o município atual com outros municípios em um gráfico de barras.</p>
                            </div>
                            <button
                                type="button"
                                disabled={!municipioSelecionado || !isIndicadorSelecionado}
                                onClick={() => gerarAnalise("barra")}
                                className="rounded-xl bg-[#061F56] px-5 py-3 text-sm font-semibold text-white shadow-lg shadow-blue-950/15 transition hover:-translate-y-0.5 hover:bg-[#0A3477] disabled:cursor-not-allowed disabled:opacity-50"
                            >
                                Gerar comparação
                            </button>
                        </div>
                    </article>
                </div>

                <button id="botao-limpar-filtros" className="rounded-xl px-5 py-3 text-sm font-semibold text-slate-600 bg-slate-100 transition hover:bg-slate-200" onClick={limparFiltros}>
                    Limpar seleção
                </button>

            </div>

            {indicadorSemSerieHistorica && (
                <p className="mt-3 rounded-2xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800">
                    Este indicador tem apenas uma referência disponível. Use o gráfico de barras para comparar municípios.
                </p>
            )}

        </section>

            {tentouAplicarFiltro && (!municipioSelecionado || !isIndicadorSelecionado || !tipoGraficoSelecionado) && (
                <div className="flex items-start gap-3 rounded-2xl border border-amber-200 bg-amber-50 p-4 text-sm text-amber-800">
                    <span className="mt-0.5 flex h-5 w-5 shrink-0 items-center justify-center rounded-full bg-amber-200 text-xs font-bold">!</span>
                    <p>{!municipioSelecionado ? "Selecione e aplique um município para continuar." : !isIndicadorSelecionado ? "Selecione um indicador para continuar." : "Escolha o tipo de visualização para gerar o gráfico."}</p>
                </div>
            )}

            {setAnoPrevisao && isIndicadorSelecionado && tentouAplicarFiltro && tipoGraficoSelecionado === "linha" && (
                <div className="mt-6 rounded-2xl border border-sky-100 bg-sky-50/70 p-4 sm:p-5">
                    <div className="flex flex-col gap-4 sm:flex-row sm:items-center">
                        <div className="flex-1">
                            <div className="flex items-center justify-between gap-3">
                                <label className="text-sm font-semibold text-slate-700" htmlFor="ano-previsao">Projeção até o ano</label>
                                <span className="rounded-lg bg-white px-3 py-1 text-sm font-bold text-sky-800 shadow-sm">{anoPrevisaoSelecionado}</span>
                            </div>
                            <input id="ano-previsao" type="range" min="2026" max="2040" step="1" value={anoPrevisaoSelecionado} onChange={(e) => setAnoPrevisaoSelecionado(Number(e.target.value))} className="mt-3 w-full accent-sky-700" />
                        </div>
                        <button type="button" onClick={() => setAnoPrevisao(anoPrevisaoSelecionado)} className="rounded-xl border border-sky-200 bg-white px-5 py-3 text-sm font-semibold text-sky-800 transition hover:border-sky-400 hover:bg-sky-100">
                            Visualizar projeção
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}
