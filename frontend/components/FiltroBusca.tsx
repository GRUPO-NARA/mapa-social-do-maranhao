"use client"

import { useEffect, useState } from "react"

interface FiltroBuscaProps {
  nomeSelecionado: string
  aoMudarMunicipio: (municipio: string) => void
}

type GeoJSONData = {
    features: {
    properties: {
    name: string
    }
  }[]
}

export default function FiltroBusca({ nomeSelecionado, aoMudarMunicipio,}: FiltroBuscaProps) {
  const [listaMunicipios, setListaMunicipios] = useState<string[]>([])

  useEffect(() => { getListaMunicipios() }, [])

  async function getListaMunicipios() {
    const response = await fetch(
      "https://raw.githubusercontent.com/tbrugz/geodata-br/master/geojson/geojs-21-mun.json"
    )

    const dados: GeoJSONData = await response.json()

    const nomesMunicipios = dados.features.map(
      (feature) => feature.properties.name
    ) 

    nomesMunicipios.sort((a, b) => a.localeCompare(b, "pt-BR"))

    setListaMunicipios(nomesMunicipios)
  }

  return (
    <div className="justify-center flex p-6">
      <div className="w-full max-w-[1200px] h-fit flex flex-col gap-4 rounded-2xl shadow-xl/30 shadow-sky-600 p-6 border border-sky-600">
        <div className="flex items-center gap-2">
          <p className="w-1 h-6 rounded bg-sky-600"></p>
          <h1 className="text-xl font-bold">Filtros de Busca</h1>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="flex flex-col gap-1">
            <label className="text-sm" htmlFor="seletor-municipio">
              Município
            </label>

            <select
              id="seletor-municipio"
              className="rounded-lg shadow-sm p-2 bg-sky-600 text-white"
              value={nomeSelecionado}
              onChange={(e) => aoMudarMunicipio(e.target.value)}
            >
              <option value="">Selecione um município</option>

              {listaMunicipios.map((municipio) => (
                <option key={municipio} value={municipio}>
                  {municipio}
                </option>
              ))}
            </select>
          </div>

          <div className="flex flex-col gap-1">
            <label className="text-sm" htmlFor="seletor-ano">
              Ano
            </label>
            <select
              id="seletor-ano"
              className="rounded-lg shadow-sm p-2 bg-sky-600 text-white"
            >
              <option value="">2024</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  )
}