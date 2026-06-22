"use client"

import { Map, GeoJsonLoader } from "pigeon-maps"
import { useEffect, useState } from "react"
import IndicadoresMapaComponent from "./IndicadoresMapaComponent"

interface GeoJsonData {
    features: Array<{
        properties: { name?: string }
        geometry: { coordinates: number[][][] }
    }>
}

interface MapaProps{
    municipio: string,
    isFiltrando?: boolean,
    isMostrarApenasMapa?: boolean
}

export default function MapaComponent({municipio, isFiltrando, isMostrarApenasMapa} : MapaProps){
    const [coordenadasMunicipais, setCoordenadasMunicipais] = useState<GeoJsonData | null>(null);
    const [coordenadasMunicipio, setCoordenadasMunicipio] = useState<[number, number]>([-5.0892, -45.3806]);
    const [zoom, setZoom] = useState(7);

    useEffect(() => {
          getCoordenadasMunicipais()
      }, [])

      useEffect(() => {
          if (coordenadasMunicipais) {
              coordenadasMunicipioSelecionado()
          }
      }, [coordenadasMunicipais, municipio])

    async function getCoordenadasMunicipais(){
        try {
            const requisicao = await fetch("https://raw.githubusercontent.com/tbrugz/geodata-br/master/geojson/geojs-21-mun.json",
              {
                next: { revalidate: 86400 }
              }
            )
            const dados = await requisicao.json()
            setCoordenadasMunicipais(dados)
        } catch (error) {
            console.error("Erro ao buscar coordenadas municipais:", error)
        }
    }

    function coordenadasMunicipioSelecionado(){
    
        if (municipio == "") {
            setZoom(6)
            return
        }

        if (!coordenadasMunicipais?.features) return
        
        if(isFiltrando){
            coordenadasMunicipais.features.forEach((feature: GeoJsonData['features'][0]) => {
            if(feature.properties?.name?.trim() === municipio.trim()){
                let novasCoordenadas: [number, number]
                const [lon, lat] = feature.geometry.coordinates[0][0]
                novasCoordenadas = [lat, lon]
                setCoordenadasMunicipio(novasCoordenadas)
                setZoom(7.5) 
            }
          })
        }
    }

    const alturaResponsiva = isMostrarApenasMapa
      ? "h-[22rem] sm:h-[28rem] md:h-full md:min-h-[30rem]"
      : "h-screen md:h-full";

    return (
        <div className={`group min-w-0 overflow-hidden rounded-2xl ${alturaResponsiva}`}>
          <div className="h-full min-w-0 overflow-hidden rounded-2xl">
            <div className="relative h-full w-full">
              <IndicadoresMapaComponent municipio={municipio} isFiltrando={isFiltrando} isMostrarApenasMapa={isMostrarApenasMapa} />
              <Map center={coordenadasMunicipio} zoom={zoom}>
                <GeoJsonLoader
                  link="https://raw.githubusercontent.com/tbrugz/geodata-br/master/geojson/geojs-21-mun.json"
                  styleCallback={(feature: any, hover: boolean) => {
                    const isSelecionado = feature.properties?.name?.trim() === municipio?.trim()
                    
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
    )
}
