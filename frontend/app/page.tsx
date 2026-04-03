import Cabecalho from "@/components/Cabecalho"
import Carregamento from "@/components/Carregamento"
import SeletorIndicador from "@/components/Navegacao"
import PanoramaGeral from "@/components/PanoramaGeral"
import Painel from "@/components/Painel"
import { GeoJSONData } from "@/types/geojson"
import FiltroBusca  from "@/components/FiltroBusca"

async function getDados(): Promise<GeoJSONData> {
  const res = await fetch(
    "https://raw.githubusercontent.com/tbrugz/geodata-br/master/geojson/geojs-21-mun.json"
  )

  if (!res.ok) {
    throw new Error("Dados não encontrados.")
  }

  return res.json()
}

export default async function Main() {
  const dados = await getDados()

  return (
    <main className="grid grid-cols-1 gap-10">
      <Carregamento />
      <Cabecalho />
      <SeletorIndicador />

      <Painel dados={dados} />

      <PanoramaGeral />
    </main>
  )
}