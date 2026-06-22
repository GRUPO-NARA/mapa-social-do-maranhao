"use client";

import {
    BarElement,
    CategoryScale,
    Chart as ChartJS,
    Legend,
    LinearScale,
    Title,
    Tooltip,
} from "chart.js";
import { Bar } from "react-chartjs-2";

ChartJS.register(
    BarElement,
    CategoryScale,
    LinearScale,
    Legend,
    Title,
    Tooltip
);

interface ClusterizacaoEducacaoComponentProps {
    isFiltroAplicado?: boolean;
    indicador: string;
    municipio?: string;
}

export default function ClusterizacaoEducacaoComponent({
    isFiltroAplicado,
    indicador,
    municipio,
}: ClusterizacaoEducacaoComponentProps) {
    const clusters = [
        {
            nome: "Desempenho consolidado",
            faixa: "Acima de 80%",
            media: "84,6%",
            descricao: "Resultados altos e estáveis, com menor distância em relação às metas educacionais.",
            recomendacao: "Monitorar a estabilidade e compartilhar práticas de gestão e aprendizagem.",
            municipios: ["São Luís", "Imperatriz", "Balsas", "Açailândia"],
            cor: "bg-emerald-500",
            fundo: "bg-emerald-50",
            texto: "text-emerald-800",
        },
        {
            nome: "Em desenvolvimento",
            faixa: "Entre 65% e 80%",
            media: "73,8%",
            descricao: "Municípios próximos da média estadual, mas com espaço consistente para evolução.",
            recomendacao: "Priorizar intervenções pedagógicas nos anos e etapas com maior oscilação.",
            municipios: ["Caxias", "Timon", "Bacabal", "Pinheiro", "Codó"],
            cor: "bg-sky-500",
            fundo: "bg-sky-50",
            texto: "text-sky-800",
        },
        {
            nome: "Atenção prioritária",
            faixa: "Abaixo de 65%",
            media: "59,4%",
            descricao: "Resultados abaixo da referência estadual e maior necessidade de acompanhamento.",
            recomendacao: "Investigar causas locais e organizar planos de recuperação com metas periódicas.",
            municipios: ["Santa Inês", "Chapadinha", "Zé Doca"],
            cor: "bg-amber-500",
            fundo: "bg-amber-50",
            texto: "text-amber-800",
        },
    ];

    const dadosDistribuicaoClusters = {
        labels: clusters.map((cluster) => cluster.nome),
        datasets: [
            {
                label: "Quantidade de municípios",
                data: clusters.map((cluster) => cluster.municipios.length),
                backgroundColor: ["#10B981", "#0EA5E9", "#F59E0B"],
                borderColor: ["#059669", "#0284C7", "#D97706"],
                borderWidth: 1,
            },
        ],
    };

    const configuracaoBarrasClusters = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: "top" as const,
            },
            title: {
                display: true,
                text: `Distribuição dos municípios — ${indicador}`,
            },
        },
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    precision: 0,
                },
            },
        },
    };

    const comparacaoMunicipio = [
        {
            label: municipio || "Referência estadual",
            valor: "82%",
        },
        {
            label: "Média do grupo",
            valor: "84,6%",
        },
        {
            label: "Média estadual",
            valor: "74%",
        },
    ];

    if (!isFiltroAplicado) {
        return null;
    }

    return (
        <section className="col-span-1 min-w-0 overflow-hidden rounded-2xl border border-gray-300 bg-white p-4 sm:p-5 md:col-span-3">
            <div className="flex flex-col gap-5">
                <header className="flex flex-col gap-2">
                    <span className="w-fit rounded-full bg-violet-100 px-3 py-1 text-xs font-semibold text-violet-800">
                        Análise de perfis educacionais
                    </span>
                    <h2 className="break-words text-lg font-bold text-[#061F56] sm:text-xl">Agrupamento municipal por desempenho</h2>
                    <p className="text-sm text-gray-600">
                        Classificação dos municípios por faixas de resultado em <strong>{indicador}</strong>
                        {municipio ? `, destacando o recorte de ${municipio}` : " no Maranhão"}.
                    </p>
                    <p className="text-xs text-gray-500">
                        Leitura demonstrativa baseada em faixas de desempenho. Os grupos deverão ser recalculados quando a base analítica estiver conectada à API.
                    </p>
                </header>

                <div className="grid grid-cols-1 gap-4 xl:grid-cols-3">
                    <div className="h-72 min-w-0 rounded-2xl border border-gray-200 p-3 sm:h-80 sm:p-4 xl:col-span-2">
                        <Bar data={dadosDistribuicaoClusters} options={configuracaoBarrasClusters} />
                    </div>
                    <aside className="rounded-2xl border border-gray-200 p-4">
                        <div className="flex flex-col gap-4">
                            <div>
                                <h3 className="font-bold text-gray-800">Posição no recorte</h3>
                                <p className="text-sm text-gray-500">Comparação com o grupo e o conjunto estadual.</p>
                            </div>
                            {comparacaoMunicipio.map((item) => (
                                <div key={item.label} className="flex items-center justify-between border-b border-gray-100 pb-2 last:border-b-0">
                                    <span className="text-sm text-gray-600">{item.label}</span>
                                    <span className="font-bold text-gray-900">{item.valor}</span>
                                </div>
                            ))}
                        </div>
                    </aside>
                </div>

                <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
                    {clusters.map((cluster) => (
                        <article key={cluster.nome} className={`rounded-2xl border border-gray-200 p-4 ${cluster.fundo}`}>
                            <div className="flex h-full flex-col gap-4">
                                <div className="flex flex-wrap items-start justify-between gap-3">
                                    <div className="flex items-center gap-2">
                                        <span className={`h-3 w-3 shrink-0 rounded-full ${cluster.cor}`}></span>
                                        <h3 className={`font-bold ${cluster.texto}`}>{cluster.nome}</h3>
                                    </div>
                                    <span className={`shrink-0 rounded bg-white px-2 py-1 text-xs font-semibold ${cluster.texto}`}>
                                        {cluster.faixa}
                                    </span>
                                </div>

                                <p className="text-sm text-gray-700">{cluster.descricao}</p>

                                <div className="flex items-center justify-between rounded-lg bg-white/80 px-3 py-2">
                                    <span className="text-xs text-gray-500">Média do grupo</span>
                                    <strong className={cluster.texto}>{cluster.media}</strong>
                                </div>

                                <div>
                                    <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-gray-500">Municípios de referência</p>
                                    <div className="flex flex-wrap gap-2">
                                        {cluster.municipios.map((nomeMunicipio) => (
                                            <span key={nomeMunicipio} className="rounded border border-gray-200 bg-white px-2 py-1 text-xs text-gray-700">
                                                {nomeMunicipio}
                                            </span>
                                        ))}
                                    </div>
                                </div>

                                <div className="mt-auto border-t border-gray-200 pt-3">
                                    <p className="text-xs font-semibold uppercase tracking-wide text-gray-500">Direcionamento</p>
                                    <p className="mt-1 text-sm text-gray-700">{cluster.recomendacao}</p>
                                </div>
                            </div>
                        </article>
                    ))}
                </div>
            </div>
        </section>
    );
}
