
import { 
  Chart as ChartJS, 
  ArcElement, 
  Tooltip, 
  Legend, 
  Title, 
  CategoryScale, 
  LinearScale,  
  PointElement,  
  LineElement    
} from 'chart.js';
import { Pie, Line } from 'react-chartjs-2';

ChartJS.register(
  ArcElement, 
  Tooltip, 
  Legend, 
  Title, 
  CategoryScale, 
  LinearScale, 
  PointElement, 
  LineElement
);

export default function GraficoLinhaComponent(){

    const legendas = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho'];
    const dados = {
        labels: legendas,
        datasets: [
            {
                label: 'Dados Reais',
                data: [12, 19, 3, 5, 2, 3, 7],
                borderColor: '#FF6384',
                backgroundColor: '#FF6384',
            },
            {
                label: 'Previsão',
                data: [8, 11, 5, 6, 3, 4, 9],
                borderColor: '#36A2EB',
                backgroundColor: '#36A2EB',
            }
        ]
    };

    const configuracao = {
        type: 'line',
        data: dados,
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top' as const,
                },
            },
            title: {
                display: true,
                text: 'Gráfico de Linha Exemplo'
            }
        },
    };

    return (
        <div className="p-4 bg-white md:col-span-3 rounded-2xl border-gray-700 border">
            <Line data={dados} options={configuracao.options} />
        </div>
    )
}