"use client"

import { useState } from "react"
import FiltroBusca from "./FiltroBusca"
import MapaEstado from "./MapaEstado"
import Indicadores from "./Indicadores"
import ResumoGeral from "./ResumoGeral"
import { GeoJSONData } from "@/types/geojson"

type Props = {
  dados: GeoJSONData
}

export default function Painel({ dados }: Props) {
  const [nomeMunicipio, setNomeMunicipio] = useState("")

  return (
    <>
      <FiltroBusca
        nomeSelecionado={nomeMunicipio}
        aoMudarMunicipio={setNomeMunicipio}
      />

      <MapaEstado dados={dados} filtro={nomeMunicipio} />

      <Indicadores nomeSelecionado={nomeMunicipio} />

      <ResumoGeral nomeSelecionado={nomeMunicipio} />
    </>
  )
}