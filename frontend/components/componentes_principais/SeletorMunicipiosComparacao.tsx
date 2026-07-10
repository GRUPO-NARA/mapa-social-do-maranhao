"use client";

import { useEffect, useMemo, useState } from "react";

interface SeletorMunicipiosComparacaoProps {
    municipioAtual: string;
    municipiosSelecionados: string[];
    setMunicipiosComparacao: (municipios: string[]) => void;
}

export default function SeletorMunicipiosComparacao({
    municipioAtual,
    municipiosSelecionados,
    setMunicipiosComparacao,
}: SeletorMunicipiosComparacaoProps) {
    const [listaMunicipios, setListaMunicipios] = useState<string[]>([]);
    const [buscaMunicipio, setBuscaMunicipio] = useState("");
    const [isCarregandoMunicipios, setIsCarregandoMunicipios] = useState(true);
    const [erroMunicipios, setErroMunicipios] = useState(false);

    const municipioPrincipal = municipioAtual.trim();
    const municipiosComparacao = useMemo(() => {
        return municipioPrincipal
            ? Array.from(new Set([municipioPrincipal, ...municipiosSelecionados])).slice(0, 6)
            : municipiosSelecionados.slice(0, 6);
    }, [municipioPrincipal, municipiosSelecionados]);

    const municipiosDisponiveis = listaMunicipios.filter((municipio) =>
        !municipiosComparacao.some((selecionado) =>
            selecionado.localeCompare(municipio, "pt-BR", { sensitivity: "base" }) === 0
        )
    );
    const sugestoesMunicipios = municipiosDisponiveis
        .filter((municipio) =>
            municipio.toLocaleLowerCase("pt-BR").includes(buscaMunicipio.trim().toLocaleLowerCase("pt-BR"))
        )
        .slice(0, 12);

    useEffect(() => {
        if (!municipioPrincipal) return;
        if (municipiosSelecionados.includes(municipioPrincipal)) return;
        setMunicipiosComparacao(municipiosComparacao);
    }, [municipioPrincipal, municipiosComparacao, municipiosSelecionados, setMunicipiosComparacao]);

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
                    throw new Error("Nao foi possivel carregar os municipios");
                }

                const resposta = await requisicao.json();
                if (componenteAtivo) {
                    setListaMunicipios(resposta?.["Resposta da Requisição"] || resposta?.["Resposta da RequisiÃ§Ã£o"] || []);
                }
            } catch (error) {
                console.error("Erro ao buscar municipios para comparacao:", error);
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

    function alternarMunicipio(municipio: string) {
        if (municipio.localeCompare(municipioPrincipal, "pt-BR", { sensitivity: "base" }) === 0) {
            return;
        }

        if (municipiosComparacao.includes(municipio)) {
            setMunicipiosComparacao(municipiosComparacao.filter((item) => item !== municipio));
            return;
        }

        if (municipiosComparacao.length >= 6) return;
        setMunicipiosComparacao([...municipiosComparacao, municipio]);
    }

    function adicionarMunicipio(municipio: string) {
        if (!municipio || municipiosComparacao.length >= 6) return;
        setMunicipiosComparacao([...municipiosComparacao, municipio]);
        setBuscaMunicipio("");
    }

    return (
        <fieldset className="min-h-0 min-w-0 rounded-3xl border border-slate-200 bg-white p-5 shadow-sm sm:p-7">
            <legend className="px-2 text-sm font-bold text-slate-800">Municípios para comparação</legend>
            <div className="flex flex-col gap-4">
                <div className="flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
                    <p className="text-sm text-slate-600">Agora selecione mais municípios para comparar com {municipioPrincipal}.</p>
                    <span className="w-fit rounded-full bg-sky-50 px-3 py-1 text-xs font-bold text-sky-800 shadow-sm">
                        {municipiosComparacao.length}/6 selecionados
                    </span>
                </div>

                {municipiosComparacao.length < 2 && (
                    <div className="flex items-start gap-3 rounded-2xl border border-amber-200 bg-amber-50 p-4 text-sm text-amber-800">
                        <span className="mt-0.5 flex h-5 w-5 shrink-0 items-center justify-center rounded-full bg-amber-200 text-xs font-bold">!</span>
                        <p>Selecione pelo menos mais 1 município para gerar o gráfico de comparação.</p>
                    </div>
                )}

                <div className="flex flex-wrap gap-2" aria-label="Municípios selecionados">
                    {municipiosComparacao.map((municipio) => {
                        const isPrincipal = municipio.localeCompare(municipioPrincipal, "pt-BR", { sensitivity: "base" }) === 0;

                        return (
                            <button
                                key={municipio}
                                type="button"
                                onClick={() => alternarMunicipio(municipio)}
                                disabled={isPrincipal}
                                className="inline-flex items-center gap-2 rounded-full bg-sky-700 px-3 py-1.5 text-xs font-semibold text-white transition hover:bg-sky-800 disabled:cursor-default disabled:bg-sky-900"
                                aria-label={isPrincipal ? `${municipio} e o municipio principal` : `Remover ${municipio} da comparacao`}
                            >
                                {municipio}
                                <span aria-hidden="true">{isPrincipal ? "principal" : "x"}</span>
                            </button>
                        );
                    })}
                </div>

                <div className="rounded-2xl border border-slate-200 bg-slate-50 p-4">
                    <label htmlFor="municipio-comparacao" className="text-sm font-semibold text-slate-700">
                        Buscar e adicionar municípios
                    </label>
                    <input
                        id="municipio-comparacao"
                        type="search"
                        value={buscaMunicipio}
                        disabled={isCarregandoMunicipios || erroMunicipios || municipiosComparacao.length >= 6}
                        onChange={(event) => setBuscaMunicipio(event.target.value)}
                        placeholder={
                            isCarregandoMunicipios
                                ? "Carregando municípios..."
                                : erroMunicipios
                                    ? "Não foi possível carregar os municípios"
                                    : municipiosComparacao.length >= 6
                                        ? "Limite de 6 municípios atingido"
                                        : "Digite para filtrar municípios"
                        }
                        className="mt-2 w-full rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-800 outline-none transition focus:border-sky-500 focus:ring-4 focus:ring-sky-100 disabled:cursor-not-allowed disabled:bg-slate-100 disabled:text-slate-400"
                    />

                    {!isCarregandoMunicipios && !erroMunicipios && municipiosComparacao.length < 6 && (
                        <div className="mt-3 flex max-h-40 flex-wrap gap-2 overflow-y-auto overscroll-contain pr-1">
                            {sugestoesMunicipios.length > 0 ? (
                                sugestoesMunicipios.map((municipio) => (
                                    <button
                                        key={municipio}
                                        type="button"
                                        onClick={() => adicionarMunicipio(municipio)}
                                        className="rounded-full border border-slate-200 bg-white px-3 py-1.5 text-xs font-semibold text-slate-700 transition hover:border-sky-300 hover:bg-sky-50 hover:text-sky-800"
                                    >
                                        {municipio}
                                    </button>
                                ))
                            ) : (
                                <p className="py-2 text-sm text-slate-500">Nenhum município encontrado.</p>
                            )}
                        </div>
                    )}
                </div>
            </div>
        </fieldset>
    );
}
