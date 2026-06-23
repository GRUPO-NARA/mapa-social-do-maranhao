"use client";

import { useEffect, useState } from "react";
import {
    ArcElement,
    BarElement,
    CategoryScale,
    Chart as ChartJS,
    Legend,
    LinearScale,
    Tooltip,
} from "chart.js";
import { Bar, Pie } from "react-chartjs-2";
import { formatarNomeIndicador } from "@/utils/formatarIndicador";

ChartJS.register(ArcElement, BarElement, CategoryScale, LinearScale, Legend, Tooltip);

export interface ClusterMunicipal {
    nome: string;
    quantidadeMunicipios: number;
    faixa: string;
    media: number;
    valorMinimo?: number;
    valorMaximo?: number;
    municipios: string[];
}

interface MetricaModelo {
    algoritmo: string;
    quantidadeClusters: number;
    silhouette: number;
}

interface RespostaClusterizacao {
    algoritmoSelecionado: string;
    quantidadeClusters: number;
    silhouette: number;
    referencia: number;
    totalMunicipios: number;
    modelosAvaliados: MetricaModelo[];
    clusters: ClusterMunicipal[];
}

interface ClusterizacaoComponentProps {
    isFiltroAplicado?: boolean;
    indicador: string;
    tipoDoIndicador: string;
    municipio?: string;
    dadosClusters?: ClusterMunicipal[];
    isCarregando?: boolean;
}

const CORES_CLUSTERS = ["#0C4A6E", "#075985", "#0369A1", "#0284C7", "#38BDF8"];

function explicarSeparacaoDosGrupos(silhouette: number) {
    if (silhouette >= 0.7) return "Os grupos encontrados são muito bem definidos.";
    if (silhouette >= 0.5) return "Os grupos encontrados estão bem separados.";
    if (silhouette >= 0.25) return "Os grupos têm diferenças moderadas entre si.";
    return "Os municípios apresentam resultados bastante parecidos entre si.";
}

export default function ClusterizacaoComponent({
    isFiltroAplicado,
    indicador,
    tipoDoIndicador,
    municipio,
    dadosClusters: dadosClustersRecebidos,
    isCarregando = false,
}: ClusterizacaoComponentProps) {
    const [resposta, setResposta] = useState<RespostaClusterizacao | null>(null);
    const [isCarregandoApi, setIsCarregandoApi] = useState(false);
    const [erro, setErro] = useState(false);
    const [tipoGrafico, setTipoGrafico] = useState<"pizza" | "barras">("pizza");

    useEffect(() => {
        if (!isFiltroAplicado || !indicador || dadosClustersRecebidos !== undefined) {
            return;
        }

        const controller = new AbortController();

        async function buscarClusters() {
            try {
                setIsCarregandoApi(true);
                setErro(false);
                setResposta(null);

                const parametros = new URLSearchParams({
                    schema: tipoDoIndicador,
                    indicador,
                });
                const requisicao = await fetch(
                    `${process.env.NEXT_PUBLIC_API_URL}/clusterizacoes?${parametros.toString()}`,
                    { signal: controller.signal }
                );

                if (!requisicao.ok) {
                    throw new Error("Falha ao buscar os dados de clusterização");
                }

                setResposta(await requisicao.json());
            } catch (error) {
                if ((error as Error).name !== "AbortError") {
                    console.error("Erro ao buscar dados de clusterização:", error);
                    setErro(true);
                }
            } finally {
                if (!controller.signal.aborted) {
                    setIsCarregandoApi(false);
                }
            }
        }

        buscarClusters();
        return () => controller.abort();
    }, [isFiltroAplicado, indicador, tipoDoIndicador, dadosClustersRecebidos]);

    if (!isFiltroAplicado) {
        return null;
    }

    const dadosClusters = dadosClustersRecebidos ?? resposta?.clusters ?? [];
    const carregando = isCarregando || isCarregandoApi;
    const tituloIndicador = formatarNomeIndicador(indicador);
    const possuiDados = dadosClusters.length > 0;
    const totalMunicipios = resposta?.totalMunicipios ?? dadosClusters.reduce(
        (total, cluster) => total + cluster.quantidadeMunicipios,
        0
    );
    const melhoresPorAlgoritmo = resposta?.modelosAvaliados.reduce<MetricaModelo[]>(
        (melhores, metrica) => {
            const indice = melhores.findIndex(
                (item) => item.algoritmo === metrica.algoritmo
            );

            if (indice === -1) {
                return [...melhores, metrica];
            }

            if (metrica.silhouette > melhores[indice].silhouette) {
                const atualizados = [...melhores];
                atualizados[indice] = metrica;
                return atualizados;
            }

            return melhores;
        },
        []
    ) ?? [];
    const indiceClusterMunicipio = municipio
        ? dadosClusters.findIndex((cluster) =>
            cluster.municipios.some(
                (item) => item.localeCompare(municipio, "pt-BR", { sensitivity: "base" }) === 0
            )
        )
        : -1;
    const clusterMunicipio = indiceClusterMunicipio >= 0
        ? dadosClusters[indiceClusterMunicipio]
        : null;
    const posicaoMunicipio = indiceClusterMunicipio === 0
        ? "entre os municípios com os valores mais baixos"
        : indiceClusterMunicipio === dadosClusters.length - 1
            ? "entre os municípios com os valores mais altos"
            : "entre os municípios com valores intermediários";

    const dadosGrafico = {
        labels: dadosClusters.map((cluster) => cluster.nome),
        datasets: [
            {
                label: "Municípios",
                data: dadosClusters.map((cluster) => cluster.quantidadeMunicipios),
                backgroundColor: dadosClusters.map(
                    (_, indice) => CORES_CLUSTERS[indice % CORES_CLUSTERS.length]
                ),
                borderColor: "#FFFFFF",
                borderWidth: 3,
                borderRadius: 8,
                borderSkipped: false,
            },
        ],
    };

    const configuracaoGrafico = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { display: false },
        },
        scales: {
            x: {
                grid: { display: false },
                border: { display: false },
                ticks: { color: "#64748B" },
            },
            y: {
                beginAtZero: true,
                grid: { color: "rgba(148, 163, 184, 0.18)" },
                border: { display: false },
                ticks: { color: "#64748B", precision: 0 },
            },
        },
    };

    const configuracaoPizza = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: "bottom" as const,
                labels: {
                    usePointStyle: true,
                    boxWidth: 10,
                    padding: 16,
                    color: "#475569",
                },
            },
            tooltip: {
                callbacks: {
                    label: (contexto: { label?: string; raw: unknown }) => {
                        const quantidade = Number(contexto.raw);
                        const percentual = totalMunicipios > 0
                            ? Math.round((quantidade / totalMunicipios) * 100)
                            : 0;
                        return `${contexto.label}: ${quantidade} municípios (${percentual}%)`;
                    },
                },
            },
        },
    };

    return (
        <section className="min-w-0 overflow-hidden rounded-3xl border border-slate-200 bg-white p-5 shadow-sm sm:p-7">
            <header className="flex flex-col gap-2 border-b border-slate-100 pb-5 sm:flex-row sm:items-start sm:justify-between">
                <div>
                    <span className="text-xs font-bold uppercase tracking-[0.14em] text-sky-700">Comparação entre municípios</span>
                    <h2 className="mt-1 text-xl font-bold text-slate-900">Municípios com resultados semelhantes</h2>
                    <p className="mt-1 text-sm text-slate-500">
                        {tituloIndicador} — Maranhão
                    </p>
                    <p className="mt-2 text-sm text-slate-600">
                        Veja como os municípios se distribuem entre resultados mais baixos, intermediários e mais altos.
                    </p>
                </div>
                <div className="flex flex-wrap gap-2">
                    {resposta && (
                        <span className="w-fit rounded-full bg-sky-50 px-3 py-1.5 text-xs font-semibold text-sky-700">
                            Referência {resposta.referencia}
                        </span>
                    )}
                    {possuiDados && (
                        <span className="w-fit rounded-full bg-blue-50 px-3 py-1.5 text-xs font-semibold text-blue-800">
                            {totalMunicipios} municípios
                        </span>
                    )}
                </div>
            </header>

            {carregando ? (
                <div className="flex min-h-64 items-center justify-center">
                    <div className="h-10 w-10 animate-spin rounded-full border-4 border-sky-100 border-t-sky-700" aria-label="Carregando grupos" />
                </div>
            ) : erro || !possuiDados ? (
                <div className="flex min-h-64 flex-col items-center justify-center px-4 py-10 text-center">
                    <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-sky-50 text-sky-700">
                        <svg viewBox="0 0 24 24" className="h-7 w-7" fill="none" stroke="currentColor" strokeWidth="1.8" aria-hidden="true">
                            <circle cx="7" cy="7" r="3" />
                            <circle cx="17" cy="7" r="3" />
                            <circle cx="12" cy="17" r="3" />
                            <path d="m9.5 9 1.2 5M14.5 9l-1.2 5M10 7h4" />
                        </svg>
                    </div>
                    <h3 className="mt-4 font-bold text-slate-800">Clusterização indisponível</h3>
                    <p className="mt-2 max-w-md text-sm leading-6 text-slate-500">
                        Não foi possível formar grupos para o indicador e a referência selecionados.
                    </p>
                </div>
            ) : (
                <>
                    {clusterMunicipio && municipio && (
                        <div className="mt-5 rounded-2xl border border-sky-200 bg-sky-50/70 p-5">
                            <div className="flex items-start gap-4">
                                <span className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-sky-800 text-lg font-bold text-white" aria-hidden="true">
                                    ✓
                                </span>
                                <div>
                                    <p className="text-xs font-bold uppercase tracking-wide text-sky-800">Entenda o resultado</p>
                                    <h3 className="mt-1 text-lg font-bold text-slate-900">
                                        {municipio} está {posicaoMunicipio} neste indicador.
                                    </h3>
                                    <p className="mt-2 text-sm leading-6 text-slate-600">
                                        Seu resultado faz parte do grupo “{clusterMunicipio.nome}”, cuja média é {" "}
                                        <strong>{clusterMunicipio.media.toLocaleString("pt-BR", { maximumFractionDigits: 3 })}</strong>.
                                        Esse grupo reúne {clusterMunicipio.quantidadeMunicipios} municípios, ou {" "}
                                        {Math.round((clusterMunicipio.quantidadeMunicipios / totalMunicipios) * 100)}% do total analisado.
                                    </p>
                                    <p className="mt-2 text-xs leading-5 text-slate-500">
                                        Atenção: estar no grupo de valores mais altos não significa necessariamente ter o melhor desempenho. A interpretação depende do indicador — em mortalidade, por exemplo, valores menores são mais favoráveis.
                                    </p>
                                </div>
                            </div>
                        </div>
                    )}

                    {resposta && (
                        <div className="border-b border-slate-100 py-5">
                            <div className="rounded-2xl bg-slate-50 p-4">
                                <p className="font-semibold text-slate-800">
                                    O sistema comparou duas formas de agrupamento e encontrou {resposta.quantidadeClusters} grupos.
                                </p>
                                <p className="mt-1 text-sm text-slate-600">
                                    {explicarSeparacaoDosGrupos(resposta.silhouette)} Os municípios de cada grupo possuem resultados semelhantes para este indicador.
                                </p>
                                <details className="mt-3 text-xs text-slate-500">
                                    <summary className="cursor-pointer font-semibold text-sky-700">Como essa análise foi feita?</summary>
                                    <p className="mt-2 leading-5">
                                        Foram testados os métodos K-Means e Hierárquico Aglomerativo. O método selecionado foi {resposta.algoritmoSelecionado}, com índice silhouette {resposta.silhouette.toLocaleString("pt-BR", { maximumFractionDigits: 3 })}.
                                    </p>
                                    <div className="mt-2 flex flex-wrap gap-2">
                                        {melhoresPorAlgoritmo.map((metrica) => (
                                            <span key={metrica.algoritmo} className="rounded-full bg-white px-3 py-1 ring-1 ring-slate-200">
                                                {metrica.algoritmo}: {metrica.quantidadeClusters} grupos
                                            </span>
                                        ))}
                                    </div>
                                </details>
                            </div>
                        </div>
                    )}

                    <div className="grid gap-5 pt-6 lg:grid-cols-[1.4fr_1fr]">
                        <div className="min-w-0 rounded-2xl bg-slate-50 p-3 sm:p-5">
                            <div className="mb-3 flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
                                <div>
                                    <h3 className="text-sm font-bold text-slate-700">Como os municípios estão distribuídos</h3>
                                    <p className="text-xs text-slate-500">
                                        {tipoGrafico === "pizza"
                                            ? "Cada fatia representa a participação de um grupo no total."
                                            : "Quanto maior a barra, mais municípios fazem parte do grupo."}
                                    </p>
                                </div>
                                <div className="flex w-fit rounded-xl bg-white p-1 ring-1 ring-slate-200" aria-label="Escolher tipo de gráfico">
                                    <button
                                        type="button"
                                        onClick={() => setTipoGrafico("pizza")}
                                        className={`rounded-lg px-3 py-1.5 text-xs font-semibold transition ${tipoGrafico === "pizza" ? "bg-sky-800 text-white" : "text-slate-600 hover:bg-slate-100"}`}
                                    >
                                        Pizza
                                    </button>
                                    <button
                                        type="button"
                                        onClick={() => setTipoGrafico("barras")}
                                        className={`rounded-lg px-3 py-1.5 text-xs font-semibold transition ${tipoGrafico === "barras" ? "bg-sky-800 text-white" : "text-slate-600 hover:bg-slate-100"}`}
                                    >
                                        Barras
                                    </button>
                                </div>
                            </div>
                            <div className="h-72">
                                {tipoGrafico === "pizza" ? (
                                    <Pie
                                        data={dadosGrafico}
                                        options={configuracaoPizza}
                                        role="img"
                                        aria-label="Gráfico de pizza com a distribuição dos municípios entre os grupos"
                                    />
                                ) : (
                                    <Bar
                                        data={dadosGrafico}
                                        options={configuracaoGrafico}
                                        role="img"
                                        aria-label="Gráfico de barras com a quantidade de municípios em cada grupo"
                                    />
                                )}
                            </div>
                        </div>

                        <div className="flex flex-col gap-3">
                            {dadosClusters.map((cluster, indice) => {
                                const percentual = totalMunicipios > 0
                                    ? Math.round((cluster.quantidadeMunicipios / totalMunicipios) * 100)
                                    : 0;
                                const contemMunicipio = municipio
                                    ? cluster.municipios.some(
                                        (item) => item.localeCompare(municipio, "pt-BR", { sensitivity: "base" }) === 0
                                    )
                                    : false;

                                return (
                                    <article
                                        key={cluster.nome}
                                        className={`rounded-2xl border p-4 ${contemMunicipio ? "border-sky-400 bg-sky-50/60" : "border-slate-200"}`}
                                    >
                                        <div className="flex items-start justify-between gap-3">
                                            <div className="flex items-start gap-3">
                                                <span
                                                    className="mt-1.5 h-3 w-3 shrink-0 rounded-full"
                                                    style={{ backgroundColor: CORES_CLUSTERS[indice % CORES_CLUSTERS.length] }}
                                                />
                                                <div>
                                                    <h3 className="font-semibold text-slate-800">{cluster.nome}</h3>
                                                    <p className="mt-0.5 text-xs text-slate-500">{cluster.faixa}</p>
                                                    {contemMunicipio && (
                                                        <p className="mt-1 text-xs font-bold text-sky-700">Este é o grupo de {municipio}</p>
                                                    )}
                                                </div>
                                            </div>
                                            <div className="text-right">
                                                <strong className="block text-lg text-slate-900">{cluster.quantidadeMunicipios}</strong>
                                                <span className="text-xs text-slate-500">municípios</span>
                                            </div>
                                        </div>
                                        <div className="mt-3 h-2 overflow-hidden rounded-full bg-slate-100">
                                            <div
                                                className="h-full rounded-full"
                                                style={{
                                                    width: `${percentual}%`,
                                                    backgroundColor: CORES_CLUSTERS[indice % CORES_CLUSTERS.length],
                                                }}
                                            />
                                        </div>
                                        <div className="mt-3 flex justify-between border-t border-slate-100 pt-3 text-xs">
                                            <span className="text-slate-500">{percentual}% do total</span>
                                            <span className="font-semibold text-slate-700">
                                                Valor médio: {cluster.media.toLocaleString("pt-BR", { maximumFractionDigits: 3 })}
                                            </span>
                                        </div>
                                    </article>
                                );
                            })}
                        </div>
                    </div>
                </>
            )}
        </section>
    );
}
