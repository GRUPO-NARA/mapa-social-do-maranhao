"use client";

import { useId } from "react";

interface AjudaIndicadorProps {
    titulo: string;
    texto: string;
    variante?: "clara" | "escura";
    alinhamento?: "inicio" | "centro" | "fim";
}

export default function AjudaIndicadorComponent({ titulo, texto, variante = "clara", alinhamento = "centro" }: AjudaIndicadorProps) {
    const tooltipId = useId();
    const coresDoBotao = variante === "escura"
        ? "border-white/70 text-white hover:bg-white/15 focus-visible:bg-white/15"
        : "border-sky-300 text-sky-700 hover:bg-sky-100 focus-visible:bg-sky-100";
    const posicaoTooltip = alinhamento === "inicio" ? "left-0" : alinhamento === "fim" ? "right-0" : "left-1/2 -translate-x-1/2";
    const posicaoSeta = alinhamento === "inicio" ? "left-2" : alinhamento === "fim" ? "right-2" : "left-1/2 -translate-x-1/2";

    return (
        <span className="group/info relative inline-flex shrink-0">
            <button
                type="button"
                aria-label={`O que significa ${titulo}`}
                aria-describedby={tooltipId}
                className={`flex h-5 w-5 items-center justify-center rounded-full border text-[11px] font-bold transition-colors ${coresDoBotao}`}
            >
                i
            </button>
            <span
                id={tooltipId}
                role="tooltip"
                className={`pointer-events-none invisible absolute bottom-full z-10000 mb-2 w-60 max-w-[calc(100vw-2rem)] rounded-xl bg-slate-900 px-3 py-2 text-left text-xs font-normal leading-5 text-white opacity-0 shadow-xl transition-opacity group-hover/info:visible group-hover/info:opacity-100 group-focus-within/info:visible group-focus-within/info:opacity-100 ${posicaoTooltip}`}
            >
                {texto}
                <span aria-hidden="true" className={`absolute top-full border-4 border-transparent border-t-slate-900 ${posicaoSeta}`} />
            </span>
        </span>
    );
}
