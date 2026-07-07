
interface CardInformativoProps {
    titulo: string;
    descricao: string;
    informacaoAdicional?: string;
}

export default function CardInformativoComponent({ titulo, descricao, informacaoAdicional }: CardInformativoProps){
    return (
        <section className="relative mb-8 overflow-hidden rounded-4xl bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 px-6 py-8 text-white shadow-xl shadow-sky-950/10 sm:px-10 sm:py-10">
            <div className="absolute -right-16 -top-20 h-64 w-64 rounded-full bg-white/10 blur-2xl" aria-hidden="true" />
                <div className="absolute -bottom-24 right-1/3 h-56 w-56 rounded-full bg-cyan-300/10 blur-3xl" aria-hidden="true" />
                    <div className="relative grid items-end gap-8 lg:grid-cols-[1.5fr_1fr]">
                        <div className="max-w-3xl">
                            <span className="mb-5 inline-flex items-center gap-2 rounded-full border border-white/20 bg-white/10 px-3 py-1.5 text-xs font-semibold uppercase tracking-[0.16em] text-sky-100 backdrop-blur-sm">
                            <span className="font-display h-2 w-2 rounded-full bg-cyan-300" />
                                {titulo}
                            </span>
                            <h1 className="font-display text-3xl font-bold leading-tight sm:text-4xl lg:text-5xl">
                                {descricao}
                            </h1>
                            <p className="font-sans mt-4 max-w-2xl text-sm leading-6 text-sky-100 sm:text-base">
                                {informacaoAdicional || "Selecione um município, acompanhe a evolução dos indicadores e compare perfis de desempenho para apoiar análises e decisões públicas."}
                            </p>
                        </div>
                    <div className="grid grid-cols-2 gap-3 sm:max-w-lg lg:justify-self-end">
                    <div className="rounded-2xl border border-white/15 bg-white/10 p-4 backdrop-blur-sm">
                        <p className="text-2xl font-bold">217</p>
                        <p className="mt-1 text-xs text-sky-100">municípios analisáveis</p>
                    </div>
                    <div className="rounded-2xl border border-white/15 bg-white/10 p-4 backdrop-blur-sm">
                        <p className="text-2xl font-bold">Séries</p>
                        <p className="mt-1 text-xs text-sky-100">históricas e comparativas</p>
                    </div>
                </div>
            </div>
        </section>
    )
}