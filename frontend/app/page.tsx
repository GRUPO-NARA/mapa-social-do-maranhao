"use client"

import AgrupamentoIndicadoresMunicipaisComponent from "@/components/componentes_indicadores/AgrupamentoIndicadoresMunicipais"
import FiltroComponent from "@/components/componentes_principais/FiltroMunicipal"
import FooterComponent from "@/components/componentes_principais/Rodape"
import HeaderComponent from "@/components/componentes_principais/Cabecalho"
import MapaComponent from "@/components/componentes_principais/Mapa"
import PanoramaGeralComponent from "@/components/componentes_indicadores/PanoramaGeral"
import CardInformativoComponent from "@/components/componentes_principais/CardInformativo"
import { useState } from "react"

export default function Home(){
  const [municipio, setMunicipio] = useState<string>("");
  const [isFiltrando, setIsFiltrando] = useState<boolean>(false);

  return (
    <div className="flex min-h-screen justify-center bg-slate-40">
        <main className="h-full w-full min-w-0 px-4 sm:px-6 lg:w-[88%] lg:max-w-360 lg:px-0">
            <HeaderComponent />
            <CardInformativoComponent titulo="Mapa Social do Maranhão" descricao="Indicadores que ajudam a enxergar o cenário social do estado"
            informacaoAdicional="Selecione um municipío, visualize seus principais indicadores sociais, de infraestrutura e assistência social, e analise as tendências de desenvolvimento do estado." />
            <div className="grid min-w-0 grid-cols-1 gap-6 md:grid-cols-3 md:gap-8">
              <div className="flex min-w-0 flex-col gap-6">
                <div className="rounded-3xl border border-slate-200 bg-white p-1 shadow-sm">
                  <FiltroComponent aoMudarMunicipio={setMunicipio} isFiltrando={setIsFiltrando} />
                </div>
                <PanoramaGeralComponent />
              </div>
              <div className="col-span-1 min-w-0 rounded-3xl border border-slate-200 bg-white p-3 shadow-sm md:col-span-2">
                <div className="flex items-center justify-between gap-3 px-2 pb-3 pt-1">
                  <div>
                    <h2 className="font-bold text-slate-800">Mapa Social do Maranhão</h2>
                    <p className="text-xs text-slate-500">{municipio || "Visão estadual"}</p>
                  </div>
                  <span className={`rounded-full px-3 py-1 text-xs font-semibold ${isFiltrando ? "bg-sky-100 text-sky-700" : "bg-sky-100 text-sky-700"}`}>
                    {isFiltrando ? "Município selecionado" : "217 municípios"}
                  </span>
                </div>
                <MapaComponent municipio={municipio} isFiltrando={isFiltrando} isMostrarApenasMapa={false}/>
              </div>
              <AgrupamentoIndicadoresMunicipaisComponent municipio={municipio} isFiltrando={isFiltrando}/>
            </div>
            <div className="mt-12 border-t border-slate-200 pt-4">
              <FooterComponent />
            </div>
        </main>
    </div>
    
  )
}
