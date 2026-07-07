"use client";

import { useEffect, useState } from "react";
import {
    BarElement,
    CategoryScale,
    Chart as ChartJS,
    Filler,
    Legend,
    LineElement,
    LinearScale,
    PointElement,
    Title,
    Tooltip,
} from "chart.js";
import { Bar, Line } from "react-chartjs-2";
import { formatarNomeIndicador } from "@/tratamento/formatarIndicador";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, BarElement, Filler, Tooltip, Legend, Title);

type TipoGrafico = "linha" | "barra";

interface GraficosComponentProps {
    tipoGrafico?: TipoGrafico | null;
    isFiltroAplicado?: boolean;
    indicador?: string;
    tipoDoIndicador?: string;
    municipio?: string;
    anoPrevisao?: number;
    municipiosComparacao?: string[];
}

interface PontoIndicador {
    Referência: string | number;
    Valor: number;
}

interface PontoComparacao extends PontoIndicador {
    Município: string;
}

interface ModeloPredicao {
    modelo: string;
    mae: number;
    r2: number | null;
}

interface RespostaPredicao {
    modelo: string;
    predicao: number;
    modelosAvaliados: ModeloPredicao[];
}

function valorNumericoValido(valor: unknown) {
    const numero = Number(valor);
    return Number.isFinite(numero) && numero >= 0 ? numero : null;
}

export default function GraficosComponent({
    tipoGrafico,
    isFiltroAplicado,
    indicador,
    tipoDoIndicador,
    municipio,
    municipiosComparacao = [],
    anoPrevisao,
}: GraficosComponentProps) {
    const [legendas, setLegendas] = useState<string[]>([]);
    const [valores, setValores] = useState<number[]>([]);
    const [valorDaPrevisao, setValorDaPrevisao] = useState<number | null>(null);
    const [dadosDaPrevisao, setDadosDaPrevisao] = useState<RespostaPredicao | null>(null);
    const [isCarregandoPrevisao, setIsCarregandoPrevisao] = useState(false);
    const [erroPrevisao, setErroPrevisao] = useState(false);
    const [mensagemPrevisao, setMensagemPrevisao] = useState("Não foi possível gerar a projeção para este recorte.");
    const [referenciaComparacao, setReferenciaComparacao] = useState<string | number | null>(null);
    const [isCarregando, setIsCarregando] = useState(false);
    const [erro, setErro] = useState(false);

    useEffect(() => {
        const controller = new AbortController();

        async function buscarPrevisao() {
        if (!isFiltroAplicado || tipoGrafico !== "linha" || !indicador || !municipio || !anoPrevisao) {
            setValorDaPrevisao(null);
            setDadosDaPrevisao(null);
            return;
        }

        try {
            setIsCarregandoPrevisao(true);
            setErroPrevisao(false);
            setMensagemPrevisao("Não foi possível gerar a projeção para este recorte.");
            setDadosDaPrevisao(null);
            const parametros = new URLSearchParams({
                schema: tipoDoIndicador || "educacao",
                indicador: indicador,
                municipio: municipio || "",
                ano: String(anoPrevisao)
            });

            const requisicao = await fetch(
                `${process.env.NEXT_PUBLIC_API_URL}/predicoes?${parametros.toString()}`,
                { signal: controller.signal }
            );

            if (!requisicao.ok) {
                let mensagem = "Não há dados históricos suficientes para gerar uma projeção para este indicador.";
                try {
                    const respostaErro = await requisicao.json();
                    mensagem = respostaErro?.["Mensagem da Requisição"]
                        || respostaErro?.["Resposta da Requisição"]
                        || respostaErro?.message
                        || mensagem;
                } catch {
                    // Mantém a mensagem padrão quando a API não retorna JSON.
                }

                setMensagemPrevisao(mensagem);
                setErroPrevisao(true);
                setValorDaPrevisao(null);
                setDadosDaPrevisao(null);
                return;
            }

            const resposta: RespostaPredicao = await requisicao.json();
            setValorDaPrevisao(Number(resposta.predicao));
            setDadosDaPrevisao(resposta);
        }catch (error) {
            if ((error as Error).name !== "AbortError") {
            setMensagemPrevisao("Não foi possível carregar a projeção agora. Tente novamente em instantes.");
            setErroPrevisao(true);
            setValorDaPrevisao(null);
            setDadosDaPrevisao(null);
            }
        } finally {
            setIsCarregandoPrevisao(false);
        }
        }

        buscarPrevisao();
        return () => controller.abort();
    }, [isFiltroAplicado, tipoGrafico, indicador, tipoDoIndicador, municipio, anoPrevisao]);

    useEffect(() => {
        if (!isFiltroAplicado || !tipoGrafico || !indicador) {
            return;
        }

        const controller = new AbortController();

        async function buscarDadosGrafico() {
            try {
                setIsCarregando(true);
                setErro(false);
                setLegendas([]);
                setValores([]);
                setReferenciaComparacao(null);
                const parametros = new URLSearchParams({
                    schema: tipoDoIndicador || "educacao",
                    indicador: indicador || "",
                });
                let rota = "/informacoes/evolucaoDoIndicador";

                if (tipoGrafico === "barra") {
                    rota = "/informacoes/comparacaoTerritorial";
                    parametros.set("municipios", municipiosComparacao.join(","));
                } else {
                    parametros.set("municipio", municipio || "");
                }

                const requisicao = await fetch(
                    `${process.env.NEXT_PUBLIC_API_URL}${rota}?${parametros.toString()}`,
                    { signal: controller.signal }
                );

                if (!requisicao.ok) {
                    throw new Error("Falha ao buscar dados do gráfico");
                }

                const resposta = await requisicao.json();
                const itens = resposta?.["Resposta da Requisição"] || [];
                const dados: PontoIndicador[] = itens.map((item: string | PontoIndicador) =>
                    typeof item === "string" ? JSON.parse(item) : item
                );

                if (tipoGrafico === "barra") {
                    const dadosComparacao = (dados as PontoComparacao[])
                        .map((dado) => ({
                            ...dado,
                            Valor: valorNumericoValido(dado.Valor),
                        }))
                        .filter((dado): dado is PontoComparacao & { Valor: number } => dado.Valor !== null);
                    setLegendas(dadosComparacao.map((dado) => dado.Município));
                    setValores(dadosComparacao.map((dado) => dado.Valor));
                    setReferenciaComparacao(dadosComparacao[0]?.Referência ?? null);
                } else {
                    const dadosValidos = dados
                        .map((dado) => ({
                            ...dado,
                            Valor: valorNumericoValido(dado.Valor),
                        }))
                        .filter((dado): dado is PontoIndicador & { Valor: number } => dado.Valor !== null);
                    setLegendas(dadosValidos.map((dado) => String(dado.Referência)));
                    setValores(dadosValidos.map((dado) => dado.Valor));
                }
            } catch (error) {
                if ((error as Error).name !== "AbortError") {
                    console.error("Erro ao buscar dados do gráfico:", error);
                    setErro(true);
                }
            } finally {
                setIsCarregando(false);
            }
        }

        buscarDadosGrafico();
        return () => controller.abort();
    }, [isFiltroAplicado, tipoGrafico, indicador, tipoDoIndicador, municipio, municipiosComparacao]);

    const possuiPrevisao = anoPrevisao !== undefined && valorDaPrevisao !== null;
    const legendasLinha = possuiPrevisao ? [...legendas, String(anoPrevisao)] : legendas;
    const valoresObservados: Array<number | null> = possuiPrevisao ? [...valores, null] : valores;
    const valoresProjetados: Array<number | null> = possuiPrevisao
        ? [...Array(Math.max(legendas.length - 1, 0)).fill(null), valores.at(-1) ?? null, valorDaPrevisao]
        : [];

    const dadosLinha = {
        labels: legendasLinha,
        datasets: [
            {
                label: "Valor observado",
                data: valoresObservados,
                borderColor: "#0369A1",
                backgroundColor: "rgba(14, 165, 233, 0.14)",
                pointBackgroundColor: "#FFFFFF",
                pointBorderColor: "#0369A1",
                pointBorderWidth: 2,
                pointRadius: 4,
                pointHoverRadius: 6,
                borderWidth: 3,
                tension: 0.34,
                fill: true,
            },
            {
                label: "Previsão",
                data: valoresProjetados,
                borderColor: "#1D4ED8",
                backgroundColor: "rgba(37, 99, 235, 0.12)",
                pointBackgroundColor: "#FFFFFF",
                pointBorderColor: "#1D4ED8",
                pointBorderWidth: 2,
                pointRadius: 4,
                pointHoverRadius: 6,
                borderWidth: 3,
                tension: 0.34,
                borderDash: [7, 5],
                fill: false,
            }
        ],
    };

    const coresBarras = ["#075985", "#0284C7", "#0EA5E9", "#38BDF8", "#0369A1", "#7DD3FC"];

    const dadosBarra = {
        labels: legendas,
        datasets: [
            {
                label: referenciaComparacao ? `Comparação municipal — ${referenciaComparacao}` : "Comparação municipal",
                data: valores,
                backgroundColor: valores.map((_, indice) => coresBarras[indice % coresBarras.length]),
                borderRadius: 8,
                borderSkipped: false,
            },
        ],
    };

    const configuracao = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: "top" as const,
                align: "end" as const,
                labels: {
                    usePointStyle: true,
                    boxWidth: 8,
                    color: "#475569",
                },
            },
            title: { display: false },
        },
        scales: {
            x: {
                grid: { display: false },
                ticks: { color: "#64748B" },
                border: { display: false },
            },
            y: {
                beginAtZero: tipoGrafico === "barra",
                grid: { color: "rgba(148, 163, 184, 0.18)" },
                ticks: { color: "#64748B" },
                border: { display: false },
            },
        },
    };

    if (!isFiltroAplicado || !tipoGrafico) {
        return (
            <div className="flex min-h-72 min-w-0 flex-col items-center justify-center rounded-3xl border border-dashed border-slate-300 bg-white px-6 py-12 text-center shadow-sm">
                <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-sky-50 text-sky-700">
                    <svg viewBox="0 0 24 24" className="h-8 w-8" fill="none" stroke="currentColor" strokeWidth="1.7" aria-hidden="true">
                        <path d="M4 19V9m6 10V5m6 14v-7m4 7H2" strokeLinecap="round" />
                    </svg>
                </div>
                <h3 className="mt-5 text-lg font-bold text-slate-800">Sua análise aparecerá aqui</h3>
                <p className="mt-2 max-w-md text-sm leading-6 text-slate-500">Selecione um indicador, escolha o tipo de gráfico e clique em “Gerar análise” para visualizar os dados.</p>
            </div>
        );
    }

    const tituloIndicador = formatarNomeIndicador(indicador);

    return (
        <section className="min-w-0 overflow-hidden rounded-3xl border border-slate-200 bg-white p-4 shadow-sm sm:p-7">
            <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
                <div>
                    <span className="inline-flex rounded-full bg-sky-100 px-3 py-1 text-xs font-bold uppercase tracking-wide text-sky-800">
                        {tipoGrafico === "linha" ? "Evolução histórica" : "Comparação territorial"}
                    </span>
                    <h3 className="mt-3 text-xl font-bold text-slate-900">{tituloIndicador}</h3>
                    <p className="mt-1 text-sm text-slate-500">
                        {tipoGrafico === "barra"
                            ? `${municipiosComparacao.length} municípios selecionados`
                            : municipio ? `Recorte municipal: ${municipio}` : "Recorte estadual do Maranhão"}
                    </p>
                </div>
                <div className="flex w-fit items-center gap-2 rounded-xl bg-slate-50 px-3 py-2 text-xs font-medium text-slate-600">
                    <span className={`h-2 w-2 rounded-full ${erro ? "bg-rose-500" : isCarregando ? "animate-pulse bg-amber-400" : "bg-sky-500"}`} />
                    {erro ? "Dados indisponíveis" : isCarregando ? "Atualizando dados" : "Dados carregados"}
                </div>
            </div>

            <div className="relative h-72 w-full min-w-0 rounded-2xl bg-slate-50/60 p-2 sm:h-96 sm:p-4">
                {isCarregando ? (
                    <div className="flex h-full items-center justify-center">
                        <div className="h-10 w-10 animate-spin rounded-full border-4 border-sky-100 border-t-sky-600" aria-label="Carregando gráfico" />
                    </div>
                ) : erro || valores.length === 0 ? (
                    <div className="flex h-full flex-col items-center justify-center text-center">
                        <p className="font-semibold text-slate-700">Nenhum dado encontrado para este recorte</p>
                        <p className="mt-1 text-sm text-slate-500">Tente selecionar outro município ou indicador.</p>
                    </div>
                ) : tipoGrafico === "linha" ? (
                    <Line data={dadosLinha} options={configuracao} />
                ) : (
                    <Bar
                        data={dadosBarra}
                        options={configuracao}
                        role="img"
                        aria-label={`Gráfico de barras comparando ${tituloIndicador} entre ${legendas.join(", ")}`}
                    />
                )}
            </div>
            {tipoGrafico === "linha" && anoPrevisao && (
                <div className={`mt-3 rounded-xl px-4 py-3 text-sm ${erroPrevisao ? "bg-rose-50 text-rose-700" : "border border-sky-100 bg-sky-50 text-sky-900"}`}>
                    {isCarregandoPrevisao ? (
                        "Calculando projeção..."
                    ) : erroPrevisao ? (
                        mensagemPrevisao
                    ) : valorDaPrevisao !== null ? (
                        <>
                            <p className="font-semibold">
                                Projeção estimada para {anoPrevisao}: {valorDaPrevisao.toLocaleString("pt-BR", { minimumFractionDigits: 3, maximumFractionDigits: 3 })}
                            </p>
                            {dadosDaPrevisao && (
                                <div className="mt-3 border-t border-sky-200 pt-3">
                                    <p className="text-xs font-bold uppercase tracking-wide text-sky-700">
                                        Algoritmos utilizados
                                    </p>
                                    <div className="mt-2 flex flex-wrap gap-2">
                                        {dadosDaPrevisao.modelosAvaliados.map((modelo) => (
                                            <span
                                                key={modelo.modelo}
                                                className={`rounded-full px-3 py-1 text-xs font-semibold ${modelo.modelo === dadosDaPrevisao.modelo ? "bg-sky-800 text-white" : "bg-white text-sky-800 ring-1 ring-sky-200"}`}
                                            >
                                                {modelo.modelo}
                                            </span>
                                        ))}
                                    </div>
                                    <p className="mt-2 text-xs leading-5 text-sky-700">
                                        O algoritmo escolhido para esta projeção foi <strong>{dadosDaPrevisao.modelo}</strong>, por apresentar o menor erro nos testes com os dados históricos.
                                    </p>
                                </div>
                            )}
                        </>
                    ) : (
                        "Selecione um município para gerar a projeção."
                    )}
                </div>
            )}
            {tipoGrafico === "barra" && referenciaComparacao && !isCarregando && !erro && (
                <p className="mt-3 text-right text-xs font-medium text-slate-500">
                    Dados referentes a {referenciaComparacao}
                </p>
            )}
        </section>
    );
}
