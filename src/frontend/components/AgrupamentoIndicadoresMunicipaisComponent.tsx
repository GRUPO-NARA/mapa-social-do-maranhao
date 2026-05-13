import IndicadoresInfraestruturaRendaComponent from "./IndicadoresInfraestruturaRendaComponent";
import IndicadoresPrincipaisComponent from "./IndicadoresPrincipaisComponent";
import IndicadoresSociaisComponent from "./IndicadoresSociaisComponent";

interface AgrupamentoIndicadoresMunicipaisProps {
    municipio: String
}

export default function AgrupamentoIndicadoresMunicipaisComponent({ municipio }: AgrupamentoIndicadoresMunicipaisProps) {
    return (
        <div className="col-span-1 md:col-span-3">
            <div className="flex flex-col gap-8 rounded-2xl p-6 ">
                <IndicadoresPrincipaisComponent municipio={municipio} />
                <IndicadoresSociaisComponent />
                <IndicadoresInfraestruturaRendaComponent />
            </div>
        </div>
    )
}