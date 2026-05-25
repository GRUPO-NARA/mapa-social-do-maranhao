"use client"

import { Map, GeoJsonLoader } from "pigeon-maps"
import { use, useEffect, useState } from "react"
import Papa from "papaparse"
import { json } from "stream/consumers"

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
          getDadosPrincipaisSIDRA();
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


    const [populacao, setPopulacao] = useState<any>();
    async function getPopulacao(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`/api/v1/municipios/populacao?municipio=${municipio}`);
          const resposta = await requisicao.json();
          var populacaoMunicipal = resposta.resposta.dados ? resposta.resposta.dados.toLocaleString("pt-BR") + " habitantes" : "--"
          setPopulacao(populacaoMunicipal);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à população total do município!");
      }
    }

    const [quantidadeHomens, setQuantidadeHomens] = useState<any>()
    async function getQuantidadeHomens(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`/api/v1/municipios/quantidadeHomens?municipio=${municipio}`);
          const resposta = await requisicao.json();
          var quantidadeHomensMunicipal = resposta?.resposta?.dados ? resposta.resposta.dados.toLocaleString("pt-BR") + " homens" : "--"
          setQuantidadeHomens(quantidadeHomensMunicipal);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à quantidade de homens do município!")     
      }
    }

    const [quantidadeMulheres, setQuantidadeMulheres] = useState<any>()
    async function getQuantidadeMulheres(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`/api/v1/municipios/quantidadeMulheres?municipio=${municipio}`);
          const resposta = await requisicao.json();
          var quantidadeMulheresMunicipal = resposta?.resposta?.dados ? resposta.resposta.dados.toLocaleString("pt-BR") + " mulheres" : "--"
          setQuantidadeMulheres(quantidadeMulheresMunicipal);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à quantidade de mulheres do município!")
      }
    }

    const [areaTotal, setAreaTotal] = useState<any>()
    async function getAreaTotal(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`/api/v1/municipios/areaTotal?municipio=${municipio}`);
          const resposta = await requisicao.json();
          var areaTotalMunicipal = resposta?.resposta?.dados ? resposta.resposta.dados.toLocaleString("pt-BR") + " km²" : "--"
          setAreaTotal(areaTotalMunicipal);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à área total do município!")
      }
    }
    
    const[densidadeDemografica, setDensidadeDemografica] = useState<any>()
    async function getDensidadeDemografica(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`/api/v1/municipios/densidadeDemografica?municipio=${municipio}`);
          const resposta = await requisicao.json();
          var densidadeDemograficaMunicipal = resposta?.resposta?.dados ? resposta.resposta.dados.toLocaleString("pt-BR") + " hab/km²" : "--"
          setDensidadeDemografica(densidadeDemograficaMunicipal);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à densidade demográfica do município!")
      }
    }

    const [dadosPrincipaisSIDRA, setDadosPrincipaisSIDRA] = useState<any>({})
    async function getDadosPrincipaisSIDRA(){
      const requisicao = await fetch(`http://localhost:8080/informacoes/dadosPrincipaisSIDRA?municipio=${municipio}`);
      const resposta = await requisicao.json();

      const dadosFormatados = resposta.resposta.dados.map((item: any) => ({             
        "Indicador": item.indicador,           
        "Valor": item.valor,                   
        "Ano de Referência": item.data_coleta   
      }));

      const csv = Papa.unparse(dadosFormatados);

      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
      const url = URL.createObjectURL(blob);
      setDadosPrincipaisSIDRA(url);
    }

    async function getCoordenadasMunicipais(){
        try {
            const requisicao = await fetch("https://raw.githubusercontent.com/tbrugz/geodata-br/master/geojson/geojs-21-mun.json")
            const dados = await requisicao.json()
            setCoordenadasMunicipais(dados)
        } catch (error) {
            console.error("Erro ao buscar coordenadas municipais:", error)
        }
    }

    function coordenadasMunicipioSelecionado(){
    
        if (municipio == "") {
            console.warn("Nenhum município selecionado.")
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

    return (
        <div className="col-span-1 md:col-span-2">
          <div className="h-screen md:h-full rounded-2xl overflow-hidden ">
            <div className="relative h-full w-full">
              <div className="absolute m-2 z-10 md:left-4 md:top-4 md:z-10">
                <div className={`flex flex-col gap-1 w-fit-content rounded-xl border border-indigo-400 bg-white/95 p-4 shadow-lg
                  ${isFiltrando && municipio !== "" ? "visible" : "invisible"}`}>
                  <div className="flex gap-2">
                    <p className="text-sm md:text-base font-bold text-gray-700">População:</p>
                    <p className="text-sm md:text-base text-black">{populacao}</p>
                  </div>
                  <div className="flex gap-2">
                    <p className="text-sm md:text-base font-bold text-gray-700">Quantidade de Homens:</p>
                    <p className="text-sm md:text-base text-black">{quantidadeHomens}</p>
                  </div>
                  <div className="flex gap-2">
                    <p className="text-sm md:text-base font-bold text-gray-700">Quantidade de Mulheres:</p>
                    <p className="text-sm md:text-base text-black">{quantidadeMulheres}</p>
                  </div>
                  <div className="flex gap-2">
                    <p className="text-sm md:text-base font-bold text-gray-700">Densidade Demográfica:</p>
                    <p className="text-sm md:text-base text-black">{densidadeDemografica}</p>
                  </div>
                  <div className="flex gap-2">
                    <p className="text-sm md:text-base font-bold text-gray-700">Área Territorial:</p>
                    <p className="text-sm md:text-base text-black">{areaTotal}</p>
                  </div>
                  <div className="flex flex-col bg-gray-300 rounded p-2 mt-2">
                    <a href="https://sidra.ibge.gov.br/" target="_blank" rel="noopener noreferrer" className="text-sm text-gray-800 hover:text-blue-500">
                      Fonte: Sistema IBGE de Recuperação Automática (SIDRA)
                    </a>
                    <a href={dadosPrincipaisSIDRA} download="Dados_Mapa_SIDRA.csv" className="text-sm text-gray-800 hover:text-blue-500 font-semibold">
                      Baixar dados
                    </a>
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