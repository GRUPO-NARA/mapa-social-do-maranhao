interface CardsResumoEducacaoComponentProps {
    isFiltroAplicado?: boolean;
    indicador?: string;
}

export default function CardsResumoEducacaoComponent({ isFiltroAplicado, indicador }: CardsResumoEducacaoComponentProps) {
    const cardsResumo = [
        {
            titulo: "Valor mais recente",
            valor: "84%",
            detalhe: "Referencia: 2015",
        },
        {
            titulo: "Melhor ano",
            valor: "2015",
            detalhe: "Maior valor: 84%",
        },
        {
            titulo: "Pior ano",
            valor: "2009",
            detalhe: "Menor valor: 72%",
        },
        {
            titulo: "Variacao no periodo",
            valor: "+12 p.p.",
            detalhe: "De 2009 a 2015",
        },
    ];

    if (!isFiltroAplicado) {
        return null;
    }

    return (
        <section className="col-span-1 grid min-w-0 grid-cols-1 gap-4 sm:grid-cols-2 md:col-span-3 xl:grid-cols-4" aria-label={`Resumo de ${indicador}`}>
            {cardsResumo.map((card) => (
                <div key={card.titulo} className="bg-white rounded-2xl border border-gray-300 p-5 hover:border-sky-600 transition-colors duration-300">
                    <div className="flex flex-col gap-3">
                        <span className="rounded bg-sky-100 px-2 py-1 text-xs font-semibold text-sky-800 w-fit">
                            Resumo
                        </span>
                        <div>
                            <h2 className="text-sm font-semibold text-gray-700">{card.titulo}</h2>
                            <p className="text-2xl font-bold text-gray-900">{card.valor}</p>
                        </div>
                        <p className="text-xs text-gray-500">{card.detalhe}</p>
                    </div>
                </div>
            ))}
        </section>
    );
}
