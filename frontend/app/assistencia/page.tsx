"use client"
import { useState } from 'react'; // IMPORTADO: Necessário para controlar o estado do input
import HeaderComponent from "@/components/HeaderComponent"
import FooterComponent from "@/components/FooterComponent"
import FiltroComponent from "@/components/FiltroComponent"
import MapaComponent from "./components/MapaComponent"

import { 
  Chart as ChartJS, 
  ArcElement, 
  Tooltip, 
  Legend, 
  Title, 
  CategoryScale, // CORREÇÃO: Necessário registrar para o gráfico Line funcionar
  LinearScale,   // CORREÇÃO: Necessário registrar para o gráfico Line funcionar
  PointElement,  // CORREÇÃO: Necessário registrar para o gráfico Line funcionar
  LineElement    // CORREÇÃO: Necessário registrar para o gráfico Line funcionar
} from 'chart.js';
import { Pie, Line } from 'react-chartjs-2';

// Registrando todos os elementos necessários para ambos os gráficos
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

export default function AssistenciaSocial(){
    // ESTADO: Controla qual opção do monstro está selecionada
    const [monsterFeature, setMonsterFeature] = useState('scales');

    const handleFeatureChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setMonsterFeature(e.target.value);
    };

    const data = {
        labels: ['Red', 'Orange', 'Yellow', 'Green', 'Blue'],
        datasets: [
            {
                label: 'Dataset 1',
                data: [12, 19, 3, 5, 2],
                backgroundColor: ['#FF6384', '#FF9F40', '#FFCD56', '#4BC0C0', '#36A2EB'],
            }
        ]
    };

    const options = {
        responsive: true,
        plugins: {
            legend: { position: 'top' as const },
            title: { display: true, text: 'Chart.js Pie Chart' }
        }
    };

    const labels = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];
    const dataLine = {
    labels: labels,
    datasets: [
        {
        label: 'Dataset 1',
        data: [12, 19, 3, 5, 2],
        borderColor: '#FF6384',
        backgroundColor: '#FF6384',
        },
    ]
    };

    const config = {
        type: 'line',
        data: dataLine,
        options: {
            responsive: true,
            plugins: {
            legend: {
                position: 'top' as const, // Ajustado tipagem do const
            },
            title: {
                display: true,
                text: 'Chart.js Line Chart'
            }
            }
        },
    };

    return (
        <div className="flex justify-center items-center bg-[#F0F0F0] min-h-screen">
            <main className="h-full w-[85%] py-4">
                <HeaderComponent />
                <div className="grid grid-cols-1 md:grid-cols-5 gap-8 w-full mt-4">
                    <FiltroComponent/>
                    <MapaComponent municipio="" isFiltrando={false} />

                    <div className="p-4 bg-white md:col-span-1 rounded-2xl border-gray-700 border">
                        <Pie data={data} options={options} />
                    </div>

                    <div className="p-4 bg-white md:col-span-1 rounded-2xl border-gray-700 border">
                        <p>Domicilios com agua encanada</p>
                    </div>

                    <div className="p-4 bg-white md:col-span-1 rounded-2xl border-gray-700 border">
                        <p>Domicilios com energia eletrica</p>
                    </div>
                    
                    <div className="p-4 bg-white md:col-span-1 rounded-2xl border-gray-700 border">
                        <p>pessoas inscritas no cadastro unico por sexo</p>
                    </div>

                    <div className="p-4 bg-white md:col-span-1 rounded-2xl border-gray-700 border">
                        <p>Total de familias inscritas no cadastro unico</p>
                    </div>

                    <div className="p-4 bg-white md:col-span-1 rounded-2xl border-gray-700 border">
                        <p>Pessoas com deficienncia inscritas no cadastro unico</p>
                    </div>

                    {/* CORREÇÃO: Removido h-20 fixo para caber o conteúdo e adicionado padding */}
                    <div className="p-4 bg-white md:col-span-2 rounded-2xl border-gray-700 border">
                        <fieldset className="flex flex-col gap-2">
                        <legend className="font-bold mb-2">Choose your monster's features:</legend>
                        <div className="flex items-center gap-2">
                            <input 
                                type="radio" 
                                id="scales" 
                                name="Municipios com agua encanada" 
                                value="scales"
                                checked={monsterFeature === 'scales'} 
                                onChange={handleFeatureChange}
                            />
                            <label htmlFor="scales" className="cursor-pointer">Scales</label>
                        </div>
                        <div className="flex items-center gap-2">
                            <input 
                                type="radio" 
                                id="horns" 
                                name="monster-feature" 
                                value="horns"
                                checked={monsterFeature === 'horns'} 
                                onChange={handleFeatureChange}
                            />
                            <label htmlFor="horns" className="cursor-pointer">Horns</label>
                        </div>
                        </fieldset>
                    </div>

                    {/* CORREÇÃO: Removido h-20 fixo para o gráfico de linha não ser esmagado */}
                    <div className="p-4 bg-white md:col-span-3 rounded-2xl border-gray-700 border">
                        <Line data={dataLine} options={config.options} />
                    </div>
                </div>
                <FooterComponent />
            </main>
        </div>
    )
}
