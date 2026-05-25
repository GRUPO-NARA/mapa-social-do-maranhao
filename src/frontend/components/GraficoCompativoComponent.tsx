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
  if (dadosEvolucao?.resposta) {
    const valor = dadosEvolucao.resposta.dados;
    
    if (typeof valor === 'object' && !Array.isArray(valor)) {
      const anos = Object.keys(valor).sort();
      const valores = anos.map(ano => valor[ano]);
      const nomeGrafico = dadosEvolucao.indicador;
      const legendaGrafico = legenda || "Evolução do Indicador";

      const dados_grafico = {
        labels: anos,
        datasets: [
          {
            label: legendaGrafico,
            data: valores,
            fill: false,
            borderColor: '#3b82f6',
            borderWidth: 4,
            pointRadius: 3,
            tension: 0.6,
          }
        ]
      };

      const configuracao_grafico = {
        responsive: true,
        plugins: {
          legend: { position: 'top' as const },
          title: { display: true, text: nomeGrafico  },
        },
      }

      return <Line data={dados_grafico} options={configuracao_grafico} />;
    }
  }
  
  return null;
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