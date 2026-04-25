"use client"

import React from "react"
import { Map, Marker, GeoJsonLoader } from "pigeon-maps"

import { useEffect, useState } from "react"

interface MapaEstadoProps{
  municipioSelecionado: string;
}

export default function MapaEstado({ municipioSelecionado } : MapaEstadoProps) {
  const [coordenadasMunicipais, setCoordenadasMunicipais] = useState<Object>({})
  const [coordenadasMunicipio, setCoordenadasMunicipio] = useState<Array<number>>([-5.0892, -45.3806])
  const [zoom, setZoom] = useState(7)

  useEffect(() => {
    getCoordenadasMunicipais()
  }, [])

  useEffect(() => {
    if (coordenadasMunicipais?.features) {
      coordenadasMunicipioSelecionado()
    }
  }, [coordenadasMunicipais, municipioSelecionado])

  async function getCoordenadasMunicipais(){
    try {
      const resposta = await fetch("https://raw.githubusercontent.com/tbrugz/geodata-br/master/geojson/geojs-21-mun.json")
      const dados = await resposta.json()
      setCoordenadasMunicipais(dados)
    } catch (error) {
      console.error("Erro ao buscar coordenadas municipais:", error)
    }
  }

  function coordenadasMunicipioSelecionado(){
    
    if (!municipioSelecionado) {
      console.warn("Nenhum município selecionado.")
      setZoom(7)
      return
    }

    if (!coordenadasMunicipais?.features) return
    
    coordenadasMunicipais.features.forEach((feature: any, index: number) => {
      if(feature.properties?.name?.trim() === municipioSelecionado.trim()){
        let novasCoordenadas: [number, number]
        const [lon, lat] = feature.geometry.coordinates[0][0]
        novasCoordenadas = [lat, lon]
        setCoordenadasMunicipio(novasCoordenadas)
        setZoom(9) // Zoom aumentado
      }
    })
  }

  return (
    <div className="flex justify-center items-center">
      <div className="w-[1200px] h-[800px] rounded-2xl shadow-xl/30 shadow-sky-600 border border-sky-600">
        <div className="flex flex-col gap-4 p-6 h-full">
          <div className="flex items-center gap-2">
            <p className="w-1 h-6 rounded bg-sky-600"></p>
            <h1 className="text-xl font-bold">Mapa do Maranhão</h1>
          </div>
          <div className="h-full border border-sky-600 rounded-2xl overflow-hidden">
            <Map center={coordenadasMunicipio} zoom={zoom} >
              <GeoJsonLoader
                link="https://raw.githubusercontent.com/tbrugz/geodata-br/master/geojson/geojs-21-mun.json"
                styleCallback={(feature, hover) => {
                  const isSelecionado = feature.properties?.name?.trim() === municipioSelecionado?.trim()
                  
                  if (isSelecionado) {
                    return {
                      fill: "#fbbf24",
                      stroke: "#d97706",
                      fillOpacity: hover ? 0.6 : 0.5,
                      strokeWidth: hover ? 2 : 2
                    }
                  }
                  
                  return {
                    fill: hover ? "#3b82f6" : "#0ea5e9",
                    stroke: hover ? "#1d4ed8" : "#0284c7",
                    fillOpacity: hover ? 0.7 : 0.3,
                    strokeWidth: hover ? 2 : 1
                  }
                }}
              />
            </Map>
          </div>
        </div>
      </div>
    </div>
  )
}