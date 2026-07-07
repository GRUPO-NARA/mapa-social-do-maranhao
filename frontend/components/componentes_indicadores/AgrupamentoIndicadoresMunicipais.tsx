import IndicadoresInfraestruturaRendaComponent from "./IndicadoresInfraestruturaRenda";
import IndicadoresPrincipaisComponent from "./IndicadoresPrincipais";
import IndicadoresSociaisComponent from "./IndicadoresSociais";

interface AgrupamentoIndicadoresMunicipaisProps {
    municipio: string
    isFiltrando?: boolean
}

export default function AgrupamentoIndicadoresMunicipaisComponent({ municipio, isFiltrando }: AgrupamentoIndicadoresMunicipaisProps) {
    return (
        <div className="col-span-1 min-w-0 md:col-span-3">
            <div className="flex flex-col gap-8 rounded-3xl py-4 sm:p-6 [&_.bg-white]:rounded-3xl [&_.bg-white]:border-slate-200 [&_.bg-white]:shadow-sm [&_.bg-white]:hover:shadow-lg">
                <aside
                    aria-label="Observação sobre a atualização dos dados"
                    className="flex items-start gap-3 rounded-2xl border border-sky-200 bg-sky-50 px-4 py-3 text-sm text-sky-950 sm:px-5"
                >
                    <span aria-hidden="true" className="mt-0.5 flex h-5 w-5 shrink-0 items-center justify-center rounded-full bg-sky-700 text-xs font-bold text-white">
                        i
                    </span>
                    <p className="leading-6">
                        <span className="font-semibold">Observação:</span> os dados apresentados nesta seção correspondem aos dados mais recentes disponíveis para cada indicador.
                    </p>
                </aside>
                <IndicadoresPrincipaisComponent municipio={municipio} isFiltrando={isFiltrando} />
                <IndicadoresSociaisComponent municipio={municipio} isFiltrando={isFiltrando} />
                <IndicadoresInfraestruturaRendaComponent municipio={municipio} isFiltrando={isFiltrando} />
            </div>
        </div>
    )
}
