"use client"

import HeaderComponent from "@/components/HeaderComponent"
import FooterComponent from "@/components/FooterComponent"
import FiltroComponent from "@/components/FiltroComponent"
import MapaComponent from "./components/MapaComponent"
import GraficoLinhaComponent from "./components/GraficoLinhaComponent"


export default function AssistenciaSocial(){

    return (
        <div className="flex justify-center items-center bg-[#F0F0F0] min-h-screen">
            <main className="h-full
             w-[85%] py-4">
                <HeaderComponent />
                <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                    <FiltroComponent/>
                    <MapaComponent municipio="" isFiltrando={false} />

                    <div className="p-4 bg-gray-600 md:col-span-1 rounded-2xl border-gray-700 border">
                        
                    </div>

                

           

                    <GraficoLinhaComponent/>

                    <div className="grid grid-cols-2 p-4 bg-white md:col-span-3 rounded-2xl border-gray-700 border gap-4 h-40">
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
