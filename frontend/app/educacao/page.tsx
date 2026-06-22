"use client"

import HeaderComponent from "@/components/HeaderComponent"
import FooterComponent from "@/components/FooterComponent"
import FiltroComponent from "@/components/FiltroComponent"
import MapaComponent from "@/components/MapaComponent"
import CardsResumoEducacaoComponent from "@/components/pages_components/CardsResumoEducacaoComponent"
import ClusterizacaoEducacaoComponent from "@/components/pages_components/ClusterizacaoEducacaoComponent"
import SeletorIndicadoresComponent from "@/components/pages_components/SeletorIndicadoresComponent"
import GraficoLinhaComponent from "@/components/pages_components/GraficoLinhaComponent"
import { useState } from "react"

type TipoGrafico = "linha" | "barra";

export default function Educacao(){
    // Futuramente colocar um endpoint que retorna os indicadores relacionados à educação para popular esse array
    const indicadores = [
        "Taxa de Aprovação no Ensino Fundamental",
        "Taxa de Aprovação no Ensino Médio",
        "Taxa de Abandono Escolar",
        "Taxa de Analfabetismo em Pessoas com 15 anos ou mais",
        "Taxa de Matrícula no Ensino Fundamental",
        "Taxa de Matrícula no Ensino Médio",
    ]

    const [tipoGraficoAtivo, setTipoGraficoAtivo] = useState<TipoGrafico | null>(null);
    const [indicadorAtivo, setIndicadorAtivo] = useState("");
    const [isFiltroEducacaoAplicado, setIsFiltroEducacaoAplicado] = useState(false);
    const [municipio, setMunicipio] = useState("");
    const [isFiltroMunicipioAplicado, setIsFiltroMunicipioAplicado] = useState(false);

    return (
        <div className="flex min-h-screen justify-center bg-[#F0F0F0]">
                <main className="h-full w-full min-w-0 px-4 sm:px-6 lg:w-[85%] lg:px-0">
                    <HeaderComponent />
                    <div className="grid min-w-0 grid-cols-1 gap-5 md:grid-cols-3 md:gap-8">
                      <div className="col-span-1 min-w-0">
                        <FiltroComponent aoMudarMunicipio={setMunicipio} isFiltrando={setIsFiltroMunicipioAplicado} />
                      </div>
                      <div className="col-span-1 min-w-0 md:col-span-2">
                        <MapaComponent municipio={municipio} isFiltrando={isFiltroMunicipioAplicado} isMostrarApenasMapa={true}/>
                      </div>
                      <SeletorIndicadoresComponent
                        indicadores={indicadores}
                        setTipoGraficoAtivo={setTipoGraficoAtivo}
                        setIndicadorAtivo={setIndicadorAtivo}
                        setIsFiltroAplicado={setIsFiltroEducacaoAplicado}
                      />
                      <CardsResumoEducacaoComponent
                        isFiltroAplicado={isFiltroEducacaoAplicado}
                        indicador={indicadorAtivo}
                      />
                      <GraficoLinhaComponent
                        tipoGrafico={tipoGraficoAtivo}
                        isFiltroAplicado={isFiltroEducacaoAplicado}
                        indicador={indicadorAtivo}
                        municipio={municipio}
                      />
                      {isFiltroEducacaoAplicado && (
                        <ClusterizacaoEducacaoComponent
                          isFiltroAplicado={isFiltroEducacaoAplicado}
                          indicador={indicadorAtivo}
                          municipio={municipio}
                        />
                      )}
                    </div>
                    <FooterComponent />
                </main>
              <link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
        </div>
    )
}
