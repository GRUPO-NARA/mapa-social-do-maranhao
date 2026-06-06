"use client"

import { Map, GeoJsonLoader } from "pigeon-maps"
import { use, useEffect, useState } from "react"
import Papa from "papaparse"

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
          getAreaTerritorial();
          getDensidadeDemografica();
          getDadosPrincipais();
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
          const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/populacao?municipio=${municipio}`);
          if (requisicao.ok) {
            const resposta = await requisicao.json();
            var dadosInternos = resposta?.["Resposta da Requisição"];
            var populacaoMunicipal = "--"
            if (dadosInternos) {
              try {
                  var objetoFormatado = JSON.parse(dadosInternos);     
                  populacaoMunicipal = objetoFormatado["Quantidade de Pessoas"].toLocaleString("pt-BR") + " habitantes";
              } catch (erro) {
                  console.error("Erro ao converter JSON:", erro);
              }
              setPopulacao(populacaoMunicipal);
            }
          }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à população total do município!");
      }
    }

    const [quantidadeHomens, setQuantidadeHomens] = useState<any>()
    async function getQuantidadeHomens(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/quantidadeDeHomens?municipio=${municipio}`);
          if (requisicao.ok) {
            const resposta = await requisicao.json();
            var dadosInternos = resposta?.["Resposta da Requisição"];
            var quantidadeHomensMunicipal = "--"
            if (dadosInternos) {
              try {
                  var objetoFormatado = JSON.parse(dadosInternos);     
                  quantidadeHomensMunicipal = objetoFormatado["Quantidade de Homens"].toLocaleString("pt-BR") + " homens";
              } catch (erro) {
                  console.error("Erro ao converter JSON:", erro);
              }
              setQuantidadeHomens(quantidadeHomensMunicipal);
            }
          }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à quantidade de homens do município!")     
      }
    }

    const [quantidadeMulheres, setQuantidadeMulheres] = useState<any>()
    async function getQuantidadeMulheres(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/quantidadeDeMulheres?municipio=${municipio}`);
          if (requisicao.ok) {
            const resposta = await requisicao.json();
            var dadosInternos = resposta?.["Resposta da Requisição"];
            var quantidadeMulheresMunicipal = "--"
            if (dadosInternos) {
              try {
                  var objetoFormatado = JSON.parse(dadosInternos);     
                  quantidadeMulheresMunicipal = objetoFormatado["Quantidade de Mulheres"].toLocaleString("pt-BR") + " mulheres";
              } catch (erro) {
                  console.error("Erro ao converter JSON:", erro);
              }
              setQuantidadeMulheres(quantidadeMulheresMunicipal);
            }
          }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à quantidade de mulheres do município!")
      }
    }

    const [areaTerritorial, setAreaTerritorial] = useState<any>()
    async function getAreaTerritorial(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/geograficos/areaTotal?municipio=${municipio}`);
          if (requisicao.ok) {
            const resposta = await requisicao.json();
            var dadosInternos = resposta?.["Resposta da Requisição"];
            var areaTerritorialMunicipal = "--"
            if (dadosInternos) {
              try {
                  var objetoFormatado = JSON.parse(dadosInternos);     
                  areaTerritorialMunicipal = objetoFormatado["Área Territorial"].toLocaleString("pt-BR") + " km²";
              } catch (erro) {
                  console.error("Erro ao converter JSON:", erro);
              }
              setAreaTerritorial(areaTerritorialMunicipal);
            }
          }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à área territorial do município!")
      }
    }
    
    const[densidadeDemografica, setDensidadeDemografica] = useState<any>()
    async function getDensidadeDemografica(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/geograficos/densidadeDemografica?municipio=${municipio}`);
          if (requisicao.ok) {
            const resposta = await requisicao.json();
            var dadosInternos = resposta?.["Resposta da Requisição"];
            var densidadeDemograficaMunicipal = "--"
            if (dadosInternos) {
              try {
                  var objetoFormatado = JSON.parse(dadosInternos);     
                  densidadeDemograficaMunicipal = objetoFormatado["Densidade Demográfica"] + " hab/km²";
              } catch (erro) {
                  console.error("Erro ao converter JSON:", erro);
              }
              setDensidadeDemografica(densidadeDemograficaMunicipal);
            }
          }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à densidade demográfica do município!")
      }
    }

    const [dadosPrincipais, setDadosPrincipais] = useState<string>("");

    async function getDadosPrincipais() {
      try {
        if (municipio !== "") {
          const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/informacoes/dadosPrincipaisMunicipal?municipio=${municipio}`);
          
          if (requisicao.ok) {
            const resposta = await requisicao.json();
            const dadosInternos = resposta?.["Resposta da Requisição"];
            
            if (dadosInternos) {
              try {
                const objetoFormatado = JSON.parse(dadosInternos); 
                
                const dadosNormalizados = objetoFormatado.map((item: any) => {
       
                  const chaveIndicador = Object.keys(item).find(
                    key => key !== "Referência dos dados" && key !== "Fonte dos Dados"
                  ) || "";

                  return {
                    "Indicador": chaveIndicador,
                    "Valor": item[chaveIndicador],
                    "Ano de Referência": item["Referência dos dados"] || item["Referência dos Dados"],
                    "Fonte": item["Fonte dos Dados"]
                  };
                });

                const csv = Papa.unparse(dadosNormalizados);
                const csvComBom = "\uFEFF" + csv; 
                
                const blob = new Blob([csvComBom], { type: 'text/csv;charset=utf-8;' });
                const url = URL.createObjectURL(blob);
                
                setDadosPrincipais(url);
              } catch (erro) {
                console.error("Erro ao converter JSON:", erro);
              }
            }
          }
        }
      } catch (error) {
        console.error("Ocorreu um erro ao buscar dados principais do município!", error);
      }
    }

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
                    <p className="text-sm md:text-base text-black">{areaTerritorial}</p>
                  </div>
                  <div className="flex flex-col bg-gray-300 rounded p-2 mt-2">
                    <a href="https://sidra.ibge.gov.br/" target="_blank" rel="noopener noreferrer" className="text-sm text-gray-800 hover:text-blue-500">
                      Fonte: Sistema IBGE de Recuperação Automática (SIDRA)
                    </a>
                    <a href={dadosPrincipais} download={`Dados Principais do Município de ${municipio}.csv`} className="text-sm text-gray-800 hover:text-blue-500 font-semibold">
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