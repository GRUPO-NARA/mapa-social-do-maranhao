"use client";

import { useEffect, useState } from "react";
import { formatarNomeIndicador } from "@/utils/formatarIndicador";

type TipoGrafico = "linha" | "barra";

interface SeletorIndicadoresProps {
    tipoDoIndicador: string;
    municipioSelecionado: boolean;
    setTipoGraficoAtivo?: (tipo: TipoGrafico | null) => void;
    setIndicadorAtivo?: (indicador: string) => void;
    setIsFiltroAplicado?: (isAplicado: boolean) => void;
    setMunicipiosComparacao?: (municipios: string[]) => void;
    setAnoPrevisao?: (ano: number | null) => void;
}

export default function SeletorIndicadoresComponent({
    tipoDoIndicador,
    municipioSelecionado,
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
    const [listaMunicipios, setListaMunicipios] = useState<string[]>([]);
    const [municipiosSelecionados, setMunicipiosSelecionados] = useState<string[]>([]);
    const [buscaMunicipio, setBuscaMunicipio] = useState("");
    const [isCarregandoMunicipios, setIsCarregandoMunicipios] = useState(true);
    const [erroMunicipios, setErroMunicipios] = useState(false);
    const [anoPrevisaoSelecionado, setAnoPrevisaoSelecionado] = useState(2030);

    const isIndicadorSelecionado = indicadorSelecionado !== "";
    const isComparacaoValida = tipoGraficoSelecionado !== "barra" || municipiosSelecionados.length >= 2;
    const nomeArea = tipoDoIndicador === "educacao"
        ? "educacional"
        : tipoDoIndicador === "saude"
            ? "de saúde"
            : "de assistência social";

    const municipiosFiltrados = listaMunicipios.filter((municipio) =>
        municipio.toLocaleLowerCase("pt-BR").includes(buscaMunicipio.trim().toLocaleLowerCase("pt-BR"))
    );

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

    useEffect(() => {
        let componenteAtivo = true;

        async function buscarMunicipios() {
            try {
                setIsCarregandoMunicipios(true);
                setErroMunicipios(false);
                const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/estadual/municipios`, {
                    cache: "force-cache",
                });

                if (!requisicao.ok) {
                    throw new Error("Não foi possível carregar os municípios");
                }

                const resposta = await requisicao.json();
                if (componenteAtivo) {
                    setListaMunicipios(resposta?.["Resposta da Requisição"] || []);
                }
            } catch (error) {
                console.error("Erro ao buscar municípios para comparação:", error);
                if (componenteAtivo) setErroMunicipios(true);
            } finally {
                if (componenteAtivo) setIsCarregandoMunicipios(false);
            }
        }

        buscarMunicipios();
        return () => {
            componenteAtivo = false;
        };
    }, []);

    function selecionarTipoGrafico(tipo: TipoGrafico) {
        setTipoGraficoSelecionado(tipo);
        setAnoPrevisao?.(null);
        setIsFiltroAplicado?.(false);
        setTipoGraficoAtivo?.(null);
    }

    function alternarMunicipio(municipio: string) {
        setTentouAplicarFiltro(false);
        setMunicipiosSelecionados((atuais) => {
            if (atuais.includes(municipio)) {
                return atuais.filter((item) => item !== municipio);
            }

            if (atuais.length >= 6) return atuais;
            return [...atuais, municipio];
        });
        setIsFiltroAplicado?.(false);
        setTipoGraficoAtivo?.(null);
    }

    function selecionarIndicador(novoIndicador: string) {
        setIndicadorSelecionado(novoIndicador);
        setTentouAplicarFiltro(false);
        setIsFiltroAplicado?.(false);
        setTipoGraficoAtivo?.(null);
        setIndicadorAtivo?.("");
        setAnoPrevisao?.(null);

        if (novoIndicador === "") {
            setTipoGraficoSelecionado(null);
        }
    }

    function aplicarFiltros() {
        setTentouAplicarFiltro(true);
        if (municipioSelecionado && isIndicadorSelecionado && tipoGraficoSelecionado && isComparacaoValida) {
            setTipoGraficoAtivo?.(tipoGraficoSelecionado);
            setIndicadorAtivo?.(indicadorSelecionado);
            setMunicipiosComparacao?.(tipoGraficoSelecionado === "barra" ? municipiosSelecionados : []);
            setIsFiltroAplicado?.(true);
            return;
        }

        setTipoGraficoAtivo?.(null);
        setIndicadorAtivo?.("");
        setIsFiltroAplicado?.(false);
    }

    function limparFiltros() {
        setIndicadorSelecionado("");
        setTentouAplicarFiltro(false);
        setTipoGraficoSelecionado(null);
        setMunicipiosSelecionados([]);
        setBuscaMunicipio("");
        setAnoPrevisaoSelecionado(2030);
        setTipoGraficoAtivo?.(null);
        setIndicadorAtivo?.("");
        setMunicipiosComparacao?.([]);
        setAnoPrevisao?.(null);
        setIsFiltroAplicado?.(false);
    }

    return (
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

            <div className={`grid gap-5 lg:grid-cols-[1.35fr_1fr] ${!municipioSelecionado ? "opacity-55" : ""}`}>
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

                <fieldset className="min-w-0">
                    <legend className="mb-2 text-sm font-semibold text-slate-700">Tipo de visualização</legend>
                    <div className="grid grid-cols-2 gap-3">
                        {(["linha", "barra"] as TipoGrafico[]).map((tipo) => {
                            const selecionado = tipoGraficoSelecionado === tipo;
                            return (
                                <button
                                    key={tipo}
                                    type="button"
                                    disabled={!municipioSelecionado || !isIndicadorSelecionado}
                                    className={`flex min-h-12 items-center justify-center rounded-2xl border px-3 py-3 text-sm font-semibold capitalize transition disabled:cursor-not-allowed disabled:opacity-45 ${selecionado ? "border-sky-700 bg-sky-700 text-white shadow-md shadow-sky-700/20" : "border-slate-200 bg-white text-slate-600 hover:border-sky-300 hover:bg-sky-50"}`}
                                    onClick={() => selecionarTipoGrafico(tipo)}
                                >
                                    {tipo}
                                </button>
                            );
                        })}
                    </div>
                </fieldset>
            </div>

            {isIndicadorSelecionado && tipoGraficoSelecionado === "barra" && (
                <fieldset className="mt-6 rounded-2xl border border-sky-100 bg-sky-50/60 p-4 sm:p-5">
                    <legend className="px-2 text-sm font-bold text-slate-800">Municípios para comparação</legend>
                    <div className="flex flex-col gap-4">
                        <div className="flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
                            <p className="text-sm text-slate-600">Marque de 2 a 6 municípios para comparar no gráfico.</p>
                            <span className="w-fit rounded-full bg-white px-3 py-1 text-xs font-bold text-sky-800 shadow-sm">
                                {municipiosSelecionados.length}/6 selecionados
                            </span>
                        </div>

                        {municipiosSelecionados.length > 0 && (
                            <div className="flex flex-wrap gap-2" aria-label="Municípios selecionados">
                                {municipiosSelecionados.map((municipio) => (
                                    <button
                                        key={municipio}
                                        type="button"
                                        onClick={() => alternarMunicipio(municipio)}
                                        className="inline-flex items-center gap-2 rounded-full bg-sky-700 px-3 py-1.5 text-xs font-semibold text-white transition hover:bg-sky-800"
                                        aria-label={`Remover ${municipio} da comparação`}
                                    >
                                        {municipio}
                                        <span aria-hidden="true">×</span>
                                    </button>
                                ))}
                            </div>
                        )}

                        <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white">
                            <div className="border-b border-slate-100 p-3">
                                <label htmlFor="busca-municipio-comparacao" className="sr-only">Buscar município</label>
                                <input
                                    id="busca-municipio-comparacao"
                                    type="search"
                                    value={buscaMunicipio}
                                    onChange={(event) => setBuscaMunicipio(event.target.value)}
                                    placeholder="Buscar município..."
                                    className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-2.5 text-sm text-slate-800 outline-none transition focus:border-sky-500 focus:bg-white focus:ring-4 focus:ring-sky-100"
                                />
                            </div>

                            <div className="grid max-h-64 gap-1 overflow-y-auto p-2 sm:grid-cols-2 lg:grid-cols-3">
                                {isCarregandoMunicipios ? (
                                    <p className="col-span-full p-4 text-center text-sm text-slate-500">Carregando municípios...</p>
                                ) : erroMunicipios ? (
                                    <p className="col-span-full p-4 text-center text-sm text-rose-600">Não foi possível carregar os municípios.</p>
                                ) : municipiosFiltrados.length === 0 ? (
                                    <p className="col-span-full p-4 text-center text-sm text-slate-500">Nenhum município encontrado.</p>
                                ) : municipiosFiltrados.map((municipio) => {
                                    const selecionado = municipiosSelecionados.includes(municipio);
                                    const bloqueado = !selecionado && municipiosSelecionados.length >= 6;

                                    return (
                                        <label
                                            key={municipio}
                                            className={`flex cursor-pointer items-center gap-3 rounded-xl px-3 py-2.5 text-sm transition ${selecionado ? "bg-sky-50 font-semibold text-sky-900" : "text-slate-700 hover:bg-slate-50"} ${bloqueado ? "cursor-not-allowed opacity-45" : ""}`}
                                        >
                                            <input
                                                type="checkbox"
                                                checked={selecionado}
                                                disabled={bloqueado}
                                                onChange={() => alternarMunicipio(municipio)}
                                                className="sr-only"
                                            />
                                            <span className={`flex h-5 w-5 shrink-0 items-center justify-center rounded-full border-2 transition ${selecionado ? "border-sky-700 bg-sky-700" : "border-slate-300 bg-white"}`} aria-hidden="true">
                                                {selecionado && <span className="h-2 w-2 rounded-full bg-white" />}
                                            </span>
                                            <span className="truncate">{municipio}</span>
                                        </label>
                                    );
                                })}
                            </div>
                        </div>
                    </div>
                </fieldset>
            )}

            {tentouAplicarFiltro && (!municipioSelecionado || !isIndicadorSelecionado || !tipoGraficoSelecionado || !isComparacaoValida) && (
                <div className="mt-5 flex items-start gap-3 rounded-2xl border border-amber-200 bg-amber-50 p-4 text-sm text-amber-800">
                    <span className="mt-0.5 flex h-5 w-5 shrink-0 items-center justify-center rounded-full bg-amber-200 text-xs font-bold">!</span>
                    <p>{!municipioSelecionado ? "Selecione e aplique um município para continuar." : !isIndicadorSelecionado ? "Selecione um indicador para continuar." : !tipoGraficoSelecionado ? "Escolha o tipo de visualização para gerar o gráfico." : "Selecione pelo menos 2 municípios para a comparação."}</p>
                </div>
            )}

            <div className="mt-6 flex flex-col-reverse gap-3 border-t border-slate-100 pt-5 sm:flex-row sm:justify-end">
                <button id="botao-limpar-filtros" className="rounded-xl px-5 py-3 text-sm font-semibold text-slate-600 transition hover:bg-slate-100" onClick={limparFiltros}>
                    Limpar seleção
                </button>
                <button id="botao-aplicar-filtros" className="rounded-xl bg-[#061F56] px-6 py-3 text-sm font-semibold text-white shadow-lg shadow-blue-950/15 transition hover:-translate-y-0.5 hover:bg-[#0A3477] disabled:cursor-not-allowed disabled:opacity-50" onClick={aplicarFiltros} disabled={!municipioSelecionado || isCarregandoIndicadores}>
                    Gerar análise
                </button>
            </div>

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
        </section>
    );
}
