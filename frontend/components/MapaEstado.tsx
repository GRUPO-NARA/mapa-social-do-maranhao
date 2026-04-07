"use client"

import {
  Map,
  MapTileLayer,
  MapZoomControl,
} from "@/components/ui/map"
import { Polygon } from "react-leaflet"
import "leaflet/dist/leaflet.css"
import { GeoJSONData } from "@/types/geojson"

type Props = {
  dados: GeoJSONData
  nomeMunicipio: string
  selecionadoMunicipio: (nome: string) => void
}

export default function MapaEstado({
  dados,
  nomeMunicipio,
  selecionadoMunicipio,
}: Props) {
  return (
    <div className="flex justify-center items-center">
      <div className="w-[1200px] h-[800px] rounded-2xl shadow-xl/30 shadow-sky-600 border border-sky-600">
        <div className="grid grid-cols-1 gap-6 p-6 h-full">
          <div className="flex items-center gap-2">
            <p className="w-1 h-6 rounded bg-sky-600"></p>
            <h1 className="text-xl font-bold">Mapa do Maranhão</h1>
          </div>

          <div className="h-full border border-sky-600 rounded-2xl overflow-hidden">
            <Map center={[-5.2, -45.3]} zoom={6}>
              <MapTileLayer />
              <MapZoomControl />

              {dados.features.map((feature) => {
                const selecionado =
                    feature.properties.name.toLowerCase() ===
                (nomeMunicipio ?? "").toLowerCase()

                const positions = feature.geometry.coordinates[0].map(
                  ([lng, lat]) => [lat, lng] as [number, number]
                )

                return (
                  <Polygon
                    key={feature.properties.id}
                    positions={positions}
                    pathOptions={{
                      color: selecionado ? "red" : "blue",
                      weight: selecionado ? 3 : 1,
                      fillOpacity: selecionado ? 0.5 : 0.2,
                    }}
                    eventHandlers={{
                      click: () =>
                        selecionadoMunicipio(feature.properties.name),
                    }}
                  />
                )
              })}
            </Map>
          </div>
        </div>
      </div>
    </div>
  )
}