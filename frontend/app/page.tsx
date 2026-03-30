"use client"
import Cabecalho from "@/components/Cabecalho";
import Carregamento from "@/components/Carregamento";
import FiltroBusca from "@/components/FiltroBusca";
import Indicadores from "@/components/Indicadores";
import MapaEstado from "@/components/MapaEstado";
import SeletorIndicador from "@/components/Navegacao";
import ResumoGeral from "@/components/ResumoGeral";
import { useState } from "react";


export default function Main() {

  const [nomeMunicipio, setNomeMunicipio] = useState("");

  return (
    
    /*
    <div className="flex flex-col gap-10">
      <Carregamento />
      <Cabecalho />
      <SeletorIndicador />
      <FiltroBusca
        nomeSelecionado={nomeMunicipio}
        aoMudarMunicipio={setNomeMunicipio}
      />
      <MapaEstado />
      
        <Indicadores nomeSelecionado={nomeMunicipio} />
            
    </div>
    */

    <div className="flex flex-col gap-10">
      <ResumoGeral/>
    </div>

  )
}