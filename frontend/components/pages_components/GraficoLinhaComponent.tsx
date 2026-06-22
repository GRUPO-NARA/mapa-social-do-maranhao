import { 
  Chart as ChartJS, 
  ArcElement, 
  Tooltip, 
  Legend, 
  Title, 
  CategoryScale, 
  LinearScale,  
  PointElement,  
  LineElement,
  BarElement
} from "chart.js";
import { Bar, Line } from "react-chartjs-2";

ChartJS.register(
  ArcElement, 
  Tooltip, 
  Legend, 
  Title, 
  CategoryScale, 
  LinearScale, 
  PointElement, 
  LineElement,
  BarElement
);

type TipoGrafico = "linha" | "barra";

interface GraficoLinhaComponentProps {
    tipoGrafico?: TipoGrafico | null;
    isFiltroAplicado?: boolean;
    indicador?: string;
    municipio?: string;
}

export default function GraficoLinhaComponent({
    tipoGrafico,
    isFiltroAplicado,
    indicador,
    municipio,
}: GraficoLinhaComponentProps){
    const legendas = ["2009", "2010", "2011", "2012", "2013", "2014", "2015"];
    const dadosLinha = {
        labels: legendas,
        datasets: [
            {
                label: "Dados reais",
                data: [72, 75, 74, 78, 80, 82, 84],
                borderColor: "#0284C7",
                backgroundColor: "#0284C7",
            },
            {
                label: "Previsao",
                data: [null, null, null, null, null, 82, 86],
                borderColor: "#16A34A",
                backgroundColor: "#16A34A",
            }
        ]
    };

    const dadosBarra = {
        labels: ["Sao Luis", "Imperatriz", "Caxias", "Timon", "Bacabal"],
        datasets: [
            {
                label: "Comparacao municipal",
                data: [82, 76, 71, 69, 73],
                backgroundColor: "#0284C7",
                borderColor: "#0369A1",
                borderWidth: 1,
            }
        ]
    };

    const configuracao = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: "top" as const,
            },
            title: {
                display: true,
                text: tipoGrafico === "barra"
                    ? `${indicador} — comparação municipal`
                    : `${indicador} — evolução${municipio ? ` em ${municipio}` : " no Maranhão"}`
            }
        },
    };

    if (!isFiltroAplicado || !tipoGrafico) {
        return (
            <div className="col-span-1 flex min-h-48 min-w-0 items-center justify-center rounded-2xl border border-gray-300 bg-white p-4 text-center md:col-span-3 md:min-h-64">
                <p className="max-w-md text-sm text-gray-500">Selecione um indicador, escolha o tipo de grafico e aplique os filtros.</p>
            </div>
        )
    }

    return (
        <section className="col-span-1 min-w-0 overflow-hidden rounded-2xl border border-gray-300 bg-white p-4 sm:p-5 md:col-span-3">
            <div className="mb-4 flex flex-col gap-1">
                <span className="text-xs font-semibold uppercase tracking-wide text-sky-700">
                    {tipoGrafico === "linha" ? "Evolução histórica" : "Comparação territorial"}
                </span>
                <h2 className="text-lg font-bold text-[#061F56]">{indicador}</h2>
                <p className="text-sm text-gray-500">
                    {municipio ? `Recorte municipal: ${municipio}.` : "Recorte estadual do Maranhão."}
                </p>
            </div>
            <div className="relative h-72 w-full min-w-0 sm:h-96">
                {tipoGrafico === "linha" ? (
                    <Line data={dadosLinha} options={configuracao} />
                ) : (
                    <Bar data={dadosBarra} options={configuracao} />
                )}
            </div>
        </section>
    )
}
