"use client"

import AgrupamentoIndicadoresMunicipaisComponent from "@/components/AgrupamentoIndicadoresMunicipaisComponent"
import FiltroComponent from "@/components/FiltroComponent"
import FooterComponent from "@/components/FooterComponent"
import HeaderComponent from "@/components/HeaderComponent"
import MapaComponent from "@/components/MapaComponent"
import PanoramaGeralComponent from "@/components/PanoramaGeralComponent"
import { useState } from "react"

export default function Home(){
  const [municipio, setMunicipio] = useState<String>("");
  const [isFiltrando, setIsFiltrando] = useState<boolean>(false);

  return (
    <div className="flex justify-center items-center bg-[#F0F0F0]">
        <main className="h-full w-[85%]">
            <HeaderComponent />
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8 ">
              <div className="flex flex-col gap-8">
                <FiltroComponent aoMudarMunicipio={setMunicipio} isFiltrando={setIsFiltrando}/>
                <PanoramaGeralComponent />
              </div>
              <MapaComponent municipio={municipio} isFiltrando={isFiltrando}/>
              <AgrupamentoIndicadoresMunicipaisComponent municipio={municipio} isFiltrando={isFiltrando} />
            </div>
            <FooterComponent />
        </main>
      <link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    </div>
    
  )
}