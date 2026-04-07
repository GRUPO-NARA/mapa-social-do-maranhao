"use client"

import {
  Map,
  MapPolygon,
  MapTileLayer,
  MapZoomControl, 
} from "@/components/ui/map"
import { Polygon } from "react-leaflet"
import "leaflet/dist/leaflet.css"
import { useEffect, useState } from "react"

interface MapaEstadoProps{
  municipioSelecionado: string;
}

export default function MapaEstado({ municipioSelecionado } : MapaEstadoProps) {
  const [coordenadasMunicipais, setCoordenadasMunicipais] = useState({})
  const [coordenadasMunicipioSelecionado, setCoordenadasMunicipioSelecionado] = useState([])

  useEffect(() => {
    getCoordenadasMunicipais()
  }, [municipioSelecionado])

  async function getCoordenadasMunicipais(){
    const resposta = await fetch ("https://raw.githubusercontent.com/tbrugz/geodata-br/master/geojson/geojs-21-mun.json")
    const dados = await resposta.json()
    setCoordenadasMunicipais(dados)
  }

  return (
    <div className="flex justify-center items-center">
      <div className="w-[1200px] h-[800px] rounded-2xl shadow-xl/30 shadow-sky-600 border border-sky-600">
        <div className="grid grid-cols-1 p-6 h-full">
          <div className="flex items-center gap-2">
            <p className="w-1 h-6 rounded bg-sky-600"></p>
            <h1 className="text-xl font-bold">Mapa do Maranhão</h1>
          </div>
          <div className="h-full border border-sky-600 rounded-2xl overflow-hidden">
            <Map center={[-5.0892, -45.3806]} zoom={6} style={{ height: "100%", width: "100%" }}>
                <MapTileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                {coordenadasMunicipais.features && coordenadasMunicipais.features.map((municipio, index) => (
                  <Polygon
                    key={index}
                    positions={municipio.geometry.coordinates[0].map(([lng, lat]) => [lat, lng])}
                    
                    color="blue"
                    fillColor="lightblue"
                    weight={1}
                  />
                ))}
            </Map>
          </div>
        </div>
      </div>
    </div>
  )
}