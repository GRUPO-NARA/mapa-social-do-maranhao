"use client"

import { useState } from "react"
import Cabecalho from "@/components/Cabecalho"
import Carregamento from "@/components/Carregamento"
import NavegacaoEntreIndicadores from "@/components/NavegacaoEntreIndicadores"
import PanoramaGeral from "@/components/PanoramaGeral"
import FiltroBusca from "@/components/FiltroBusca"
import MapaEstado from "@/components/MapaEstado"
import Indicadores from "@/components/Indicadores"
import ResumoGeral from "@/components/ResumoGeral"

export default function Main() {
  const [municipioSelecionado, setMunicipioSelecionado] = useState("")

  return (
    <main className="grid grid-cols-1 gap-10 ">
      
      <Carregamento />
      <Cabecalho />
      <NavegacaoEntreIndicadores />  
      <FiltroBusca aoMudarMunicipio={setMunicipioSelecionado}/>
      <MapaEstado municipioSelecionado={municipioSelecionado}/>
      <Indicadores municipioSelecionado={municipioSelecionado}/>
      <ResumoGeral municipioSelecionado={municipioSelecionado}/>
      <PanoramaGeral />
    </main>
  )
}