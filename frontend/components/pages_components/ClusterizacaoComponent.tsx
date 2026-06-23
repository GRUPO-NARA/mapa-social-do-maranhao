"use client";

import {
    BarElement,
    CategoryScale,
    Chart as ChartJS,
    Legend,
    LinearScale,
    Tooltip,
} from "chart.js";
import { Bar } from "react-chartjs-2";
import { formatarNomeIndicador } from "@/utils/formatarIndicador";

ChartJS.register(BarElement, CategoryScale, LinearScale, Legend, Tooltip);

export interface ClusterEducacao {
    nome: string;
    quantidadeMunicipios: number;
    faixa?: string;
    media?: number;
}

export const DADOS_CLUSTERS_FICTICIOS: ClusterEducacao[] = [
    {
        nome: "Resultado alto",
        quantidadeMunicipios: 54,
        faixa: "80 pontos ou mais",
        media: 86,
    },
    {
        nome: "Resultado médio",
        quantidadeMunicipios: 108,
        faixa: "De 60 a 79 pontos",
        media: 69,
    },
    {
        nome: "Precisa de atenção",
        quantidadeMunicipios: 55,
        faixa: "Abaixo de 60 pontos",
        media: 48,
    },
];

interface ClusterizacaoComponentProps {
    isFiltroAplicado?: boolean;
    indicador: string;
    municipio?: string;
    dadosClusters?: ClusterEducacao[];
    isCarregando?: boolean;
}

const CORES_CLUSTERS = ["#0284C7", "#10B981", "#F59E0B", "#8B5CF6", "#F43F5E"];

export default function ClusterizacaoComponent({
    isFiltroAplicado,
    indicador,
    municipio,
    dadosClusters: dadosClustersRecebidos,
    isCarregando = false,
}: ClusterizacaoComponentProps) {
    if (!isFiltroAplicado) {
        return null;
    }

    const usandoDadosFicticios = dadosClustersRecebidos === undefined;
    const dadosClusters = dadosClustersRecebidos ?? DADOS_CLUSTERS_FICTICIOS;
    const tituloIndicador = formatarNomeIndicador(indicador);
    const possuiDados = dadosClusters.length > 0;
    const totalMunicipios = dadosClusters.reduce(
        (total, cluster) => total + cluster.quantidadeMunicipios,
        0
    );

    const dadosGrafico = {
        labels: dadosClusters.map((cluster) => cluster.nome),
        datasets: [
            {
                label: "Municípios",
                data: dadosClusters.map((cluster) => cluster.quantidadeMunicipios),
                backgroundColor: dadosClusters.map((_, indice) => CORES_CLUSTERS[indice % CORES_CLUSTERS.length]),
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

    return (
        <section className="min-w-0 overflow-hidden rounded-3xl border border-slate-200 bg-white p-5 shadow-sm sm:p-7">
            <header className="flex flex-col gap-2 border-b border-slate-100 pb-5 sm:flex-row sm:items-start sm:justify-between">
                <div>
                    <span className="text-xs font-bold uppercase tracking-[0.14em] text-violet-700">Clusterização</span>
                    <h2 className="mt-1 text-xl font-bold text-slate-900">Grupos de desempenho</h2>
                    <p className="mt-1 text-sm text-slate-500">
                        {tituloIndicador}{municipio ? ` — ${municipio}` : " — Maranhão"}
                    </p>
                    <p className="mt-2 text-sm text-slate-600">
                        Cada barra mostra quantos municípios ficaram em uma faixa de resultado.
                    </p>
                </div>
                <div className="flex flex-wrap gap-2">
                    {usandoDadosFicticios && (
                        <span className="w-fit rounded-full bg-amber-50 px-3 py-1.5 text-xs font-semibold text-amber-700">
                            Dados fictícios
                        </span>
                    )}
                    {possuiDados && (
                        <span className="w-fit rounded-full bg-violet-50 px-3 py-1.5 text-xs font-semibold text-violet-700">
                            {totalMunicipios} municípios
                        </span>
                    )}
                </div>
            </header>

            {isCarregando ? (
                <div className="flex min-h-64 items-center justify-center">
                    <div className="h-10 w-10 animate-spin rounded-full border-4 border-violet-100 border-t-violet-600" aria-label="Carregando clusters" />
                </div>
            ) : !possuiDados ? (
                <div className="flex min-h-64 flex-col items-center justify-center px-4 py-10 text-center">
                    <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-violet-50 text-violet-700">
                        <svg viewBox="0 0 24 24" className="h-7 w-7" fill="none" stroke="currentColor" strokeWidth="1.8" aria-hidden="true">
                            <circle cx="7" cy="7" r="3" />
                            <circle cx="17" cy="7" r="3" />
                            <circle cx="12" cy="17" r="3" />
                            <path d="m9.5 9 1.2 5M14.5 9l-1.2 5M10 7h4" />
                        </svg>
                    </div>
                    <h3 className="mt-4 font-bold text-slate-800">Aguardando dados de clusterização</h3>
                    <p className="mt-2 max-w-md text-sm leading-6 text-slate-500">
                        Os grupos e a distribuição dos municípios serão exibidos aqui quando os dados da análise estiverem disponíveis.
                    </p>
                </div>
            ) : (
                <div className="grid gap-5 pt-6 lg:grid-cols-[1.4fr_1fr]">
                    <div className="min-w-0 rounded-2xl bg-slate-50 p-3 sm:p-5">
                        <div className="mb-3">
                            <h3 className="text-sm font-bold text-slate-700">Municípios por grupo</h3>
                            <p className="text-xs text-slate-500">Quanto maior a barra, mais municípios fazem parte do grupo.</p>
                        </div>
                        <div className="h-64">
                            <Bar data={dadosGrafico} options={configuracaoGrafico} />
                        </div>
                    </div>

                    <div className="flex flex-col gap-3">
                        {dadosClusters.map((cluster, indice) => {
                            const percentual = totalMunicipios > 0
                                ? Math.round((cluster.quantidadeMunicipios / totalMunicipios) * 100)
                                : 0;

                            return (
                            <article key={cluster.nome} className="rounded-2xl border border-slate-200 p-4">
                                <div className="flex items-start justify-between gap-3">
                                    <div className="flex items-start gap-3">
                                        <span
                                            className="mt-1.5 h-3 w-3 shrink-0 rounded-full"
                                            style={{ backgroundColor: CORES_CLUSTERS[indice % CORES_CLUSTERS.length] }}
                                        />
                                        <div>
                                            <h3 className="font-semibold text-slate-800">{cluster.nome}</h3>
                                            {cluster.faixa && <p className="mt-0.5 text-xs text-slate-500">{cluster.faixa}</p>}
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
                                    {cluster.media !== undefined && (
                                        <span className="font-semibold text-slate-700">
                                            Média: {cluster.media.toLocaleString("pt-BR")} pontos
                                        </span>
                                    )}
                                </div>
                            </article>
                            );
                        })}
                    </div>
                </div>
            )}
        </section>
    );
}
