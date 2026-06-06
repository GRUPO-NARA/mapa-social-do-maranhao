"use client";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Line } from 'react-chartjs-2';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

interface GraficoCompativoProps {
    municipio?: String
    isVisualizacaoGrafica: boolean
    dadosEvolucaoIndicadorMunicipal?: any
    legendaDoGrafico?: string
    fecharGrafico?: () => void
}

const LineChart = ({ dadosEvolucao, legenda }: { dadosEvolucao: any, legenda?: string }) => {

  if (!dadosEvolucao) return null;

  const indicador = dadosEvolucao?.["Indicador da Requisição"];
  const stringsInternas = dadosEvolucao?.["Resposta da Requisição"] || [];

  const dadosInternos = stringsInternas.map((item: string) => {
    try {
      return JSON.parse(item);
    } catch (e) {
      console.error("Erro ao converter string JSON:", e);
      return null;
    }
  }).filter(Boolean); 

  if (dadosInternos.length === 0) return null;

  const primeiroItem = dadosInternos[0];
  const chaveIndicador = Object.keys(primeiroItem).find(
    (key) => key !== "Referência" && key !== "Fonte dos Dados"
  ) || "";

  const referencias = dadosInternos.map((item: any) => item["Referência"]);
  const valores = dadosInternos.map((item: any) => item[chaveIndicador]);

  const dados_grafico = {
    labels: referencias,
    datasets: [
      {
        label: legenda || indicador,
        data: valores,
        fill: false,
        borderColor: "#3b82f6",
        borderWidth: 4,
        pointRadius: 3,
        tension: 0.4,
      },
    ],
  };

  const configuracao_grafico = {
    responsive: true,
    plugins: {
      legend: { position: "top" as const },
      title: { display: !!indicador, text: indicador },
    },
  };

  return <Line data={dados_grafico} options={configuracao_grafico} />;
};
  

export default function GraficoCompativoComponent({ municipio, isVisualizacaoGrafica, dadosEvolucaoIndicadorMunicipal, legendaDoGrafico, fecharGrafico }: GraficoCompativoProps){
    return (
        <div className={`col-span-1 md:col-span-3 md:row-span-1 ${isVisualizacaoGrafica && municipio != "" ? "visible" : "hidden"}`}>
            <div className="flex justify-center">
                <div className="bg-white border border-gray-300 w-full h-full p-6 rounded-xl flex items-center justify-center relative">
                    <button 
                        onClick={fecharGrafico}
                        className="absolute top-4 right-4 bg-gray-600 hover:bg-gray-700 text-white rounded-full w-8 h-8 flex items-center justify-center transition-colors duration-200 font-bold text-lg"
                        title="Fechar gráfico"
                    >
                        ✕
                    </button>
                    
                    <LineChart dadosEvolucao={dadosEvolucaoIndicadorMunicipal} legenda={legendaDoGrafico} />
                </div>
            </div>
        </div>
    )
}