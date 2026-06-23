export default function AcessibilidadeComponent() {
    return (
        <div className="a11y-toolbar fixed bottom-4 right-4 z-[100]">
            <p id="a11y-anuncio" className="sr-only" aria-live="polite" />

            <details className="relative">
                <summary
                    className="flex min-h-12 cursor-pointer list-none items-center gap-3 rounded-full bg-[#061F56] px-4 py-3 font-semibold text-white shadow-xl shadow-blue-950/25 transition hover:-translate-y-0.5 hover:bg-[#0A3477] [&::-webkit-details-marker]:hidden"
                    aria-controls="painel-acessibilidade"
                    aria-label="Recursos de acessibilidade"
                >
                    <svg viewBox="0 0 24 24" className="h-6 w-6" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
                        <circle cx="12" cy="4" r="2" />
                        <path d="M5 8h14M12 6v13M8 21l4-7 4 7" strokeLinecap="round" strokeLinejoin="round" />
                    </svg>
                    <span className="hidden sm:inline">Acessibilidade</span>
                </summary>

                <section
                    id="painel-acessibilidade"
                    role="dialog"
                    aria-modal="false"
                    aria-labelledby="titulo-acessibilidade"
                    className="absolute bottom-16 right-0 w-[calc(100vw-2rem)] max-w-sm rounded-3xl border border-slate-200 bg-white p-5 text-slate-800 shadow-2xl shadow-slate-950/20"
                >
                    <div className="flex items-start justify-between gap-4 border-b border-slate-100 pb-4">
                        <div>
                            <p className="text-xs font-bold uppercase tracking-[0.14em] text-sky-700">Acessibilidade</p>
                            <h2 id="titulo-acessibilidade" className="mt-1 text-lg font-bold text-slate-900">Ajuste a visualização</h2>
                        </div>
                        <button type="button" data-a11y-action="close" className="flex h-9 w-9 items-center justify-center rounded-xl bg-slate-100 text-xl text-slate-600 transition hover:bg-slate-200" aria-label="Fechar painel de acessibilidade">×</button>
                    </div>

                    <div className="mt-5">
                        <span className="text-sm font-semibold text-slate-700">Tamanho do texto</span>
                        <div className="mt-2 grid grid-cols-[1fr_auto_1fr] items-center gap-2">
                            <button type="button" data-a11y-action="font-decrease" className="rounded-xl border border-slate-200 px-3 py-2 font-bold transition hover:bg-slate-50 disabled:opacity-40" aria-label="Diminuir tamanho do texto">A−</button>
                            <span data-a11y-font-value className="min-w-14 text-center text-sm font-semibold" aria-label="Tamanho atual 100 por cento">100%</span>
                            <button type="button" data-a11y-action="font-increase" className="rounded-xl border border-slate-200 px-3 py-2 font-bold transition hover:bg-slate-50 disabled:opacity-40" aria-label="Aumentar tamanho do texto">A+</button>
                        </div>
                    </div>

                    <div className="mt-5 flex flex-col gap-2" role="group" aria-label="Opções de acessibilidade">
                        <BotaoPreferencia propriedade="altoContraste" texto="Alto contraste" />
                        <BotaoPreferencia propriedade="fonteLegivel" texto="Fonte mais legível" />
                        <BotaoPreferencia propriedade="linksSublinhados" texto="Sublinhar links" />
                        <BotaoPreferencia propriedade="movimentoReduzido" texto="Reduzir movimentos" />
                    </div>

                    <button type="button" data-a11y-action="reset" className="mt-5 w-full rounded-xl bg-slate-100 px-4 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-200">
                        Restaurar configurações
                    </button>
                </section>
            </details>
        </div>
    );
}

interface BotaoPreferenciaProps {
    propriedade: string;
    texto: string;
}

function BotaoPreferencia({ propriedade, texto }: BotaoPreferenciaProps) {
    return (
        <button type="button" role="switch" aria-checked="false" data-a11y-toggle={propriedade} className="a11y-opcao flex items-center justify-between rounded-xl border border-slate-200 bg-white px-4 py-3 text-left text-sm font-semibold text-slate-700 transition hover:bg-slate-50">
            <span>{texto}</span>
            <span className="a11y-switch relative h-6 w-11 rounded-full bg-slate-300 transition" aria-hidden="true">
                <span className="a11y-switch-thumb absolute left-1 top-1 h-4 w-4 rounded-full bg-white shadow transition" />
            </span>
        </button>
    );
}
