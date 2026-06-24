import IndicadoresInfraestruturaRendaComponent from "./IndicadoresInfraestruturaRendaComponent";
import IndicadoresPrincipaisComponent from "./IndicadoresPrincipaisComponent";
import IndicadoresSociaisComponent from "./IndicadoresSociaisComponent";

interface AgrupamentoIndicadoresMunicipaisProps {
    municipio: string
    isFiltrando?: boolean
}

export default function AgrupamentoIndicadoresMunicipaisComponent({ municipio, isFiltrando }: AgrupamentoIndicadoresMunicipaisProps) {
    return (
        <div className="col-span-1 min-w-0 md:col-span-3">
            <div className="flex flex-col gap-8 rounded-3xl py-4 sm:p-6 [&_.bg-white]:rounded-3xl [&_.bg-white]:border-slate-200 [&_.bg-white]:shadow-sm [&_.bg-white]:hover:shadow-lg">
                <IndicadoresPrincipaisComponent municipio={municipio} isFiltrando={isFiltrando} />
                <IndicadoresSociaisComponent municipio={municipio} isFiltrando={isFiltrando} />
                <IndicadoresInfraestruturaRendaComponent municipio={municipio} isFiltrando={isFiltrando} />
            </div>
        </div>
    )
}
