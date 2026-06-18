"use client"

import { useState } from 'react'
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
} from 'chart.js'
import { Pie, Line } from 'react-chartjs-2'

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

export default function Saude(){
        const [indicadorSelecionado, setIndicadorSelecionado] = useState("Numero de hospitais gerais");

    const handleFeatureChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setIndicadorSelecionado(e.target.value);
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
                position: 'top' as const, 
            },
            title: {
                display: true,
                text: 'Chart.js Line Chart'
            }
            }
        },
    }

    return (
        <div className="flex justify-center items-center bg-[#F0F0F0]">
                <main className="h-full w-[85%] py-4">
                   <HeaderComponent />
                   <div className="grid grid-cols-1 md:grid-cols-5 gap-8 w-full mt-4">
                        <FiltroComponent/>
                        
                        <MapaComponent municipio="" isFiltrando={false} />
                        
                        <div className="p-4 bg-gray-600 md:col-span-1 rounded-2xl border-gray-700 border">
                            <Pie data={data} options={options} />
                        </div> 
                        
                        <section className="p-4 bg-white md:col-span-5 rounded-2xl border-gray-700 border">
                            <div className="mb-4">
                                <h2 className="text-lg font-bold text-[#061F56]">Indicadores de saúde</h2>
                            </div>
                            <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
                                <div className="p-4 bg-white rounded-2xl border-gray-700 border">
                                    <p className="font-semibold text-[#061F56]">Numero de hospitais gerais</p>
                                    <p className="text-sm text-gray-600">Fonte: IMESC</p>
                                    <p className="text-sm text-gray-600">Serie: 2005 a 2021</p>
                                </div>

                                <div className="p-4 bg-white rounded-2xl border-gray-700 border">
                                    <p className="font-semibold text-[#061F56]">Cobertura da estrategia de saude familiar</p>
                                    <p className="text-sm text-gray-600">Fonte: IMESC</p>
                                    <p className="text-sm text-gray-600">Serie: 2007 a 2020</p>
                                </div>

                                <div className="p-4 bg-white rounded-2xl border-gray-700 border">
                                    <p className="font-semibold text-[#061F56]">Cobertura vacinal</p>
                                    <p className="text-sm text-gray-600">Fonte: IMESC</p>
                                    <p className="text-sm text-gray-600">Serie: 1994 a 2021</p>
                                </div>

                                <div className="p-4 bg-white rounded-2xl border-gray-700 border">
                                    <p className="font-semibold text-[#061F56]">Numero de centros de saude</p>
                                    <p className="text-sm text-gray-600">Fonte: IMESC</p>
                                    <p className="text-sm text-gray-600">Serie: 2005 a 2021</p>
                                </div>

                                <div className="p-4 bg-white rounded-2xl border-gray-700 border">
                                    <p className="font-semibold text-[#061F56]">Taxa de mortalidade infantil</p>
                                    <p className="text-sm text-gray-600">Fonte: IMESC</p>
                                    <p className="text-sm text-gray-600">Serie: 1994 a 2020</p>
                                </div>
                            </div>
                        </section>

                        
                        <div className="p-4 bg-white md:col-span-2 rounded-2xl border-gray-700 border">
                            <fieldset className="flex flex-col gap-2">
                            <legend className="font-bold mb-2">Indicador em destaque</legend>
                            <div className="flex items-center gap-2">
                                <input
                                    type="radio"
                                    id="numero-de-hospitais-gerais"
                                    name="indicador-saude-imesc"
                                    value="Numero de hospitais gerais"
                                    checked={indicadorSelecionado === "Numero de hospitais gerais"}
                                    onChange={handleFeatureChange}
                                />
                                <label htmlFor="numero-de-hospitais-gerais" className="cursor-pointer">Numero de hospitais gerais</label>
                            </div>
                            <div className="flex items-center gap-2">
                                <input
                                    type="radio"
                                    id="cobertura-da-estrategia-de-saude-familiar"
                                    name="indicador-saude-imesc"
                                    value="Cobertura da estrategia de saude familiar"
                                    checked={indicadorSelecionado === "Cobertura da estrategia de saude familiar"}
                                    onChange={handleFeatureChange}
                                />
                                <label htmlFor="cobertura-da-estrategia-de-saude-familiar" className="cursor-pointer">Cobertura da estrategia de saude familiar</label>
                            </div>
                            <div className="flex items-center gap-2">
                                <input
                                    type="radio"
                                    id="cobertura-vacinal"
                                    name="indicador-saude-imesc"
                                    value="Cobertura vacinal"
                                    checked={indicadorSelecionado === "Cobertura vacinal"}
                                    onChange={handleFeatureChange}
                                />
                                <label htmlFor="cobertura-vacinal" className="cursor-pointer">Cobertura vacinal</label>
                            </div>
                            <div className="flex items-center gap-2">
                                <input
                                    type="radio"
                                    id="numero-de-centros-de-saude"
                                    name="indicador-saude-imesc"
                                    value="Numero de centros de saude"
                                    checked={indicadorSelecionado === "Numero de centros de saude"}
                                    onChange={handleFeatureChange}
                                />
                                <label htmlFor="numero-de-centros-de-saude" className="cursor-pointer">Numero de centros de saude</label>
                            </div>
                            <div className="flex items-center gap-2">
                                <input
                                    type="radio"
                                    id="taxa-de-mortalidade-infantil"
                                    name="indicador-saude-imesc"
                                    value="Taxa de mortalidade infantil"
                                    checked={indicadorSelecionado === "Taxa de mortalidade infantil"}
                                    onChange={handleFeatureChange}
                                />
                                <label htmlFor="taxa-de-mortalidade-infantil" className="cursor-pointer">Taxa de mortalidade infantil</label>
                            </div>
                            </fieldset>
                        </div>

                        <div className="p-4 bg-white md:col-span-3 rounded-2xl border-gray-700 border">
                            <Line data={dataLine} options={config.options} />
                        </div>

                        <div className="grid grid-cols-2 p-4 bg-white md:col-span-5 rounded-2xl border-gray-700 border gap-4 h-40">
                            <div className="w-full bg-yellow-500 rounded-lg p-2">
                                <p>LOCAL QUE VAI FICAR A CLUSTERIZAÇÃO</p>
                            </div>
                            <div className="w-full bg-yellow-500 rounded-lg p-2">
                                <p>LOCAL QUE VAI FICAR A PREDIÇÃO</p>
                            </div>
                        </div>
                    </div>
                    <FooterComponent />
                </main>
        </div>
    )
}
