import type { ReactNode } from "react";
import AjudaIndicadorComponent from "./AjudaIndicadorComponent";

interface CartaoIndicadorProps {
    titulo: string;
    descricao?: string;
    detalhes?: string;
    categoria: string;
    categoriaClassName: string;
    valor: ReactNode;
    acao?: ReactNode;
}

export default function CartaoIndicadorComponent({
    titulo,
    descricao,
    detalhes,
    categoria,
    categoriaClassName,
    valor,
    acao,
}: CartaoIndicadorProps) {
    return (
        <article className="group/card flex h-full min-h-48 flex-col justify-between rounded-2xl border border-gray-300 bg-white p-7 transition-all duration-300 hover:-translate-y-1 hover:border-sky-600 hover:shadow-lg">
            <div className="flex items-start justify-between gap-4">
                <div className="min-w-0">
                    <div className="flex w-fit items-center gap-2 rounded bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 p-2">
                        <h3 className="text-sm font-bold text-white">{titulo}</h3>
                        {descricao && <AjudaIndicadorComponent titulo={titulo} texto={descricao} variante="escura" />}
                    </div>
                    {descricao && <p className="mt-1 text-sm text-gray-600">{descricao}</p>}
                    <div className="mt-1 h-1 w-full rounded-2xl bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600" />
                    {detalhes && <p className="mt-2 text-xs leading-5 text-gray-500">{detalhes}</p>}
                </div>
                <span className={`h-fit w-fit shrink-0 rounded p-2 text-xs font-semibold ${categoriaClassName}`}>
                    {categoria}
                </span>
            </div>

            <div className="mt-8 flex items-end justify-between gap-3">
                <p className="text-2xl font-bold text-gray-800 transition-colors duration-300 group-hover/card:text-sky-600">
                    {valor}
                </p>
                {acao}
            </div>
        </article>
    );
}
