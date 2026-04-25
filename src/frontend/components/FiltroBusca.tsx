"use client"

import { useEffect, useState } from "react"

interface FiltroBuscaProps {
  aoMudarMunicipio: (municipio: string) => void
}

export default function FiltroBusca({ aoMudarMunicipio,}: FiltroBuscaProps) {
  const [listaMunicipios, setListaMunicipios] = useState<string[]>([])

  useEffect(() => { getListaMunicipios() }, [])

  async function getListaMunicipios() {
    const resposta = await fetch(
      `${process.env.NEXT_PUBLIC_URL_BASE}${process.env.NEXT_PUBLIC_URL_LISTA_DE_MUNICIPIOS}`
    )
    const nomesMunicipios = await resposta.json()
    setListaMunicipios(nomesMunicipios)
  }

  return (
    <div className="justify-center flex p-6">
      <div className="group w-full max-w-[1200px] h-fit flex flex-col gap-4 rounded-2xl shadow-xl/30 p-6 border shadow-sky-900 border border-gray-300 hover:border-sky-600 transition-colors duration-300">
        <div className="flex items-center gap-2">
          <p className="w-1 h-6 rounded bg-sky-600"></p>
          <h1 className="text-xl font-bold group-hover:text-sky-800 transition-colors duration-300">Filtros de Busca</h1>
        </div>

        <div className="gap-4">
          <div className="flex flex-col gap-1">
            <label className="text-sm" htmlFor="seletor-municipio">
              Município
            </label>

            <select
              id="seletor-municipio"
              className="rounded-lg shadow-sm p-2 bg-sky-700 text-white"
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
        </div>
      </div>
    </div>
  )
}