import { formatarNomeIndicador } from "@/tratamento/formatarIndicador";
import { useState } from "react";
import { useEffect } from "react";

interface CardsResumoComponentProps {
    tipo_do_indicador?: string;
    municipio?: string;
    isFiltroAplicado?: boolean;
    indicador?: string;
}

function formatarVariacaoNoPeriodo(valor: unknown) {
    if (valor === null || valor === undefined || valor === "") return "--";

    const correspondencia = String(valor).match(/[+-]?\d+(?:[.,]\d+)?/);
    if (!correspondencia) return String(valor);

    const numero = Number(correspondencia[0].replace(",", "."));
    if (!Number.isFinite(numero)) return String(valor);

    const numeroFormatado = Math.abs(numero).toLocaleString("pt-BR", {
        minimumFractionDigits: 3,
        maximumFractionDigits: 3,
    });
    const sinal = numero > 0 ? "+" : numero < 0 ? "−" : "";

    return `${sinal}${numeroFormatado} p.p.`;
}

function indicadorQuantoMenorMelhor(indicador?: string) {
    if (!indicador) return false;

    return [
        "analfabetismo",
        "abandono",
        "distorcao",
        "distorção",
        "evasao",
        "evasão",
        "mortalidade",
        "obito",
        "óbito",
        "reprovacao",
        "reprovação",
    ].some((termo) => indicador.toLocaleLowerCase("pt-BR").includes(termo));
}

function indicadorPercentual(indicador?: string) {
    if (!indicador) return false;

    return [
        "cobertura",
        "domicilios_com_agua_encanada",
        "domicilios_com_energia_eletrica",
        "percentual",
        "programa_auxilio_gas",
        "taxa",
    ].some((termo) => indicador.toLocaleLowerCase("pt-BR").includes(termo));
}

function formatarPercentual(valor: unknown) {
    if (valor === null || valor === undefined || valor === "") return "--";
    const texto = String(valor);
    return texto.includes("%") ? texto : `${texto}%`;
}



export default function CardsResumoComponent({ isFiltroAplicado, indicador, municipio, tipo_do_indicador }: CardsResumoComponentProps) {
    
    const [valorMaisRecente, setValorMaisRecente] = useState<string | number | null>(null);
    const [referenciaValorMaisRecente, setReferenciaValorMaisRecente] = useState<string | number | null>(null);
    const [anoMelhorResultado, setAnoMelhorResultado] = useState<string | number | null>(null);
    const [melhorResultado, setMelhorResultado] = useState<string | number | null>(null);
    const [anoPontoDeAtencao, setAnoPontoDeAtencao] = useState<string | number | null>(null);
    const [pontoDeAtencao, setPontoDeAtencao] = useState<string | number | null>(null);
    const [variacaoNoPeriodo, setVariacaoNoPeriodo] = useState<string | null>(null);
    const [evolucaoHistorica, setEvolucaoHistorica] = useState<string | number | null>(null);

    async function buscarResumoIndicador(){
        const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/informacoes/resumoDoIndicador?schema=${tipo_do_indicador}&indicador=${indicador}&municipio=${municipio}`);
        const resposta = await requisicao.json();
        const dadosResumo = resposta?.['Resposta da Requisição'];

        if (!dadosResumo) {
            setValorMaisRecente('--');
            setReferenciaValorMaisRecente('--');
            setAnoMelhorResultado('--');
            setMelhorResultado('--');
            setAnoPontoDeAtencao('--');
            setPontoDeAtencao('--');
            setVariacaoNoPeriodo('--');
            setEvolucaoHistorica('--');
            return;
        }
        
        if (indicadorPercentual(indicador)){
            dadosResumo['VALOR MAIS RECENTE'] = formatarPercentual(dadosResumo['VALOR MAIS RECENTE']);
            dadosResumo['Maior valor'] = formatarPercentual(dadosResumo['Maior valor']);
            dadosResumo['Menor valor'] = formatarPercentual(dadosResumo['Menor valor']);
        }

        setValorMaisRecente(dadosResumo?.['VALOR MAIS RECENTE'] || '--');
        setReferenciaValorMaisRecente(dadosResumo?.['Referência'] || '--');

        setAnoMelhorResultado(dadosResumo?.['MELHOR RESULTADO'] || '--');
        setMelhorResultado(dadosResumo?.['Maior valor'] || '--');

        setAnoPontoDeAtencao(dadosResumo?.['PONTO DE ATENÇÃO'] || '--');
        setPontoDeAtencao(dadosResumo?.['Menor valor'] || '--');

        setVariacaoNoPeriodo(formatarVariacaoNoPeriodo(
            dadosResumo?.['VARIAÇÃO NO PERÍODO']
        ));
        setEvolucaoHistorica(dadosResumo?.['Evolução'] || '--');
    }

    useEffect(() => {
        if (isFiltroAplicado && indicador) {
            buscarResumoIndicador();
        }
    }, [isFiltroAplicado, indicador, municipio]);

    if (!isFiltroAplicado) {
        return null;
    }

    const menorValorEhMelhor = indicadorQuantoMenorMelhor(indicador);

    const cardsResumo = [
        {
            titulo: "Valor mais recente",
            valor: valorMaisRecente,
            detalhe: `Referência: ${referenciaValorMaisRecente}`,
            cor: "bg-sky-700",
            fundo: "bg-sky-50",
            texto: "text-sky-700",
        },
        {
            titulo: "Melhor resultado",
            valor: menorValorEhMelhor ? anoPontoDeAtencao : anoMelhorResultado,
            detalhe: menorValorEhMelhor ? `Menor valor: ${pontoDeAtencao}` : `Maior valor: ${melhorResultado}`,
            cor: "bg-sky-700",
            fundo: "bg-sky-50",
            texto: "text-sky-700",
        },
        {
            titulo: "Ponto de atenção",
            valor: menorValorEhMelhor ? anoMelhorResultado : anoPontoDeAtencao,
            detalhe: menorValorEhMelhor ? `Maior valor: ${melhorResultado}` : `Menor valor: ${pontoDeAtencao}`,
            cor: "bg-sky-700",
            fundo: "bg-sky-50",
            texto: "text-sky-700",
        },
        {
            titulo: "Variação no período",
            valor: variacaoNoPeriodo,
            detalhe: `Evolução: ${evolucaoHistorica}`,
            descricao: menorValorEhMelhor
                ? "Diferença entre o primeiro e o último resultado da série. Para este indicador, redução representa melhora."
                : "Diferença entre o primeiro e o último resultado da série, medida em pontos percentuais.",
            cor: "bg-sky-700",
            fundo: "bg-sky-50",
            texto: "text-sky-700",
        },
    ];

    return (
        <section aria-label={`Resumo de ${formatarNomeIndicador(indicador)}`}>
            <div className="mb-3 flex items-center justify-between gap-3">
                <h3 className="text-sm font-bold text-slate-700">Resumo do período</h3>
                <span className="max-w-[65%] truncate rounded-full bg-slate-100 px-3 py-1 text-xs font-medium text-slate-600">
                    {formatarNomeIndicador(indicador)}
                </span>
            </div>
            <div className="grid min-w-0 grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
                {cardsResumo.map((card) => (
                    <article key={card.titulo} className="group relative overflow-hidden rounded-2xl border border-slate-200 bg-white p-5 shadow-sm transition duration-300 hover:-translate-y-1 hover:shadow-lg">
                        <span className={`absolute inset-y-0 left-0 w-1 ${card.cor}`} aria-hidden="true" />
                        <div className="flex items-start justify-between gap-3">
                            <div>
                                <p className="text-xs font-semibold uppercase tracking-wide text-slate-500">{card.titulo}</p>
                                <p className="mt-3 text-2xl font-bold text-slate-900">{card.valor}</p>
                            </div>
                            <span className={`flex h-9 w-9 items-center justify-center rounded-xl ${card.fundo} ${card.texto}`}>
                                <span className={`h-2.5 w-2.5 rounded-full ${card.cor}`} />
                            </span>
                        </div>
                        <p className="mt-3 text-xs text-slate-500">{card.detalhe}</p>
                        {card.descricao && (
                            <p className="mt-2 text-xs leading-5 text-slate-500">{card.descricao}</p>
                        )}
                    </article>
                ))}
            </div>
        </section>
    );
}
