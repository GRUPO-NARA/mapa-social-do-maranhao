"use client"

import HeaderComponent from "@/components/HeaderComponent"
import FooterComponent from "@/components/FooterComponent"
import FiltroComponent from "@/components/FiltroComponent"
import MapaComponent from "@/components/MapaComponent"
import SeletorIndicadoresComponent from "@/components/pages_components/SeletorIndicadoresComponent"
import GraficoLinhaComponent from "@/components/pages_components/GraficoLinhaComponent"

export default function Educacao(){
    const indicadores = [
        "Taxa de Aprovação no Ensino Fundamental",
        "Taxa de Aprovação no Ensino Médio",
        "Taxa de Abandono Escolar",
        "Taxa de Analfabetismo em Pessoas com 15 anos ou mais",
        "Taxa de Matrícula no Ensino Fundamental",
        "Taxa de Matrícula no Ensino Médio",
    ]
    return (
        <div className="flex justify-center items-center bg-[#F0F0F0]">
                <main className="h-full w-[85%]">
                    <HeaderComponent />
                    <div className="grid grid-cols-1 md:grid-cols-4 gap-8 ">
                      <FiltroComponent aoMudarMunicipio={(municipio) => console.log("Município selecionado:", municipio)} isFiltrando={(filtrando) => console.log("Está filtrando?", filtrando)} />
                      <MapaComponent municipio="" isFiltrando={false} />
                      <div className="bg-black h-64">

                      </div>
                      <SeletorIndicadoresComponent indicadores={indicadores} />
                      <GraficoLinhaComponent />
                    </div>
                    <FooterComponent />
                </main>
              <link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
        </div>
    )
}