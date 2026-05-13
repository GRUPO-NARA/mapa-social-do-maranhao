
import { Map, GeoJsonLoader } from "pigeon-maps"
import { use, useEffect, useState } from "react"

interface GeoJsonData {
    features: Array<{
        properties: { name?: string }
        geometry: { coordinates: number[][][] }
    }>
}

interface MapaProps{
    municipio: String,
    isFiltrando?: boolean
}

export default function MapaComponent({municipio, isFiltrando} : MapaProps){
    const [coordenadasMunicipais, setCoordenadasMunicipais] = useState<GeoJsonData | null>(null);
    const [coordenadasMunicipio, setCoordenadasMunicipio] = useState<[number, number]>([-5.0892, -45.3806]);
    const [zoom, setZoom] = useState(7);

    useEffect(() => {
        if (municipio === "") {
          setZoom(7);
        } else {
          getPopulacao();
          getQuantidadeHomens();
          getQuantidadeMulheres();
          getAreaTotal();
          getDensidadeDemografica();
        }
      }, [municipio]);


    useEffect(() => {
          getCoordenadasMunicipais()
      }, [])

      useEffect(() => {
          if (coordenadasMunicipais) {
              coordenadasMunicipioSelecionado()
          }
      }, [coordenadasMunicipais, municipio])


    const [populacao, setPopulacao] = useState<any>({});
    async function getPopulacao(){
      try{
        if(municipio != ""){
          const resposta = await fetch(`/api/v1/municipios/populacao?municipio=${municipio}`);
          const dados = await resposta.json();
          setPopulacao(dados);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à população total do município!");
      }
    }

    const [quantidadeHomens, setQuantidadeHomens] = useState<any>({})
    async function getQuantidadeHomens(){
      try{
        if(municipio != ""){
          const resposta = await fetch(`/api/v1/municipios/quantidadeHomens?municipio=${municipio}`);
          const dados = await resposta.json();
          setQuantidadeHomens(dados);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à quantidade de homens do município!")     
      }
    }

    const [quantidadeMulheres, setQuantidadeMulheres] = useState<any>({})
    async function getQuantidadeMulheres(){
      try{
        if(municipio != ""){
          const resposta = await fetch(`/api/v1/municipios/quantidadeMulheres?municipio=${municipio}`);
          const dados = await resposta.json();
          setQuantidadeMulheres(dados);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à quantidade de mulheres do município!")
      }
    }

    const [areaTotal, setAreaTotal] = useState<any>({})
    async function getAreaTotal(){
      try{
        if(municipio != ""){
          const resposta = await fetch(`/api/v1/municipios/areaTotal?municipio=${municipio}`);
          const dados = await resposta.json();
          setAreaTotal(dados);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à área total do município!")
      }
    }
    
    const[densidadeDemografica, setDensidadeDemografica] = useState<any>({})
    async function getDensidadeDemografica(){
      try{
        if(municipio != ""){
          const resposta = await fetch(`/api/v1/municipios/densidadeDemografica?municipio=${municipio}`);
          const dados = await resposta.json();
          setDensidadeDemografica(dados);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à densidade demográfica do município!")
      }
    }

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
    
        if (municipio == "") {
            console.warn("Nenhum município selecionado.")
            setZoom(7)
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
                setZoom(9) 
            }
          })
        }
    }

    return (
        <div className="col-span-1 md:col-span-2">
          <div className="h-screen md:h-full rounded-2xl overflow-hidden ">
            <div className="relative h-full w-full">
              <div className="absolute m-2 z-10 md:left-4 md:top-4 md:z-10">
                <div className={`w-fit-content rounded-xl border border-indigo-600 bg-white/95 p-4 
                  ${isFiltrando && municipio !== "" ? "visible" : "invisible"}`}>
                  <div className="flex gap-2">
                    <p className="font-bold text-gray-700">População:</p>
                    <p className="text-black">{populacao?.resposta?.valor ? populacao.resposta.valor.toLocaleString("pt-BR") + " habitantes" : "--" }</p>
                  </div>
                  <div className="flex gap-2">
                    <p className="font-bold text-gray-700">Quantidade de Homens:</p>
                    <p className="text-black">{quantidadeHomens?.resposta?.valor ? quantidadeHomens.resposta.valor.toLocaleString("pt-BR") + " homens" : "--" }</p>
                  </div>
                  <div className="flex gap-2">
                    <p className="font-bold text-gray-700">Quantidade de Mulheres:</p>
                    <p className="text-black">{quantidadeMulheres?.resposta?.valor ? quantidadeMulheres.resposta.valor.toLocaleString("pt-BR") + " mulheres" : "--" }</p>
                  </div>
                  <div className="flex gap-2">
                    <p className="font-bold text-gray-700">Densidade Demográfica:</p>
                    <p className="text-black">{densidadeDemografica?.resposta?.valor ? densidadeDemografica.resposta.valor.toLocaleString("pt-BR") + " hab/km²": "--" }</p>
                  </div>
                  <div className="flex gap-2">
                    <p className="font-bold text-gray-700">Área Territorial:</p>
                    <p className="text-black">{areaTotal?.resposta?.valor ? areaTotal.resposta.valor.toLocaleString("pt-BR") + " km²": "--" }</p>
                  </div>
                </div>
              </div>
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