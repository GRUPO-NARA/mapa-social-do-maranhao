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
    isVisualizacaoGrafica: boolean
}

const LineChart = () => {
  const data = {
    labels: [1999, 2000, 2001, 2002, 2003],
    datasets: [
      {
        label: 'IDH',
        data: [65, 59, 80, 81, 56],
        fill: false,
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1, 
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: { position: 'top' as const },
      title: { display: true, text: 'Histórico do IDH' },
    },
  };

  return <Line data={data} options={options} />;
};

export default function GraficoCompativoComponent({ isVisualizacaoGrafica }: GraficoCompativoProps){
    return (
        <div className={`grid col-span-1 md:col-span-3  ${isVisualizacaoGrafica ? "visible" : "hidden"}`}>
            <div className="flex justify-center">
                <div className="w-full h-64 bg-gray-200 rounded-lg flex items-center justify-center">
                    <LineChart />
                </div>
            </div>
        </div>
    )
}