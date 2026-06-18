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
}

export default function GraficoLinhaComponent({ tipoGrafico }: GraficoLinhaComponentProps){
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
        plugins: {
            legend: {
                position: "top" as const,
            },
            title: {
                display: true,
                text: tipoGrafico === "barra" ? "Grafico de Barras - Comparacao" : "Grafico de Linha - Evolucao"
            }
        },
    };

    if (!tipoGrafico) {
        return (
            <div className="p-4 bg-white md:col-span-3 rounded-2xl border-gray-700 border flex min-h-64 items-center justify-center">
                <p className="text-sm text-gray-500">Selecione um indicador e o tipo de grafico.</p>
            </div>
        )
    }

    return (
        <div className="p-4 bg-white md:col-span-3 rounded-2xl border-gray-700 border">
            {tipoGrafico === "linha" ? (
                <Line data={dadosLinha} options={configuracao} />
            ) : (
                <Bar data={dadosBarra} options={configuracao} />
            )}
        </div>
    )
}
