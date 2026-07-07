"use client"

import {useState, useEffect} from "react";
import Papa from "papaparse"

interface IndicadoresMapaProps{
    municipio: string,
    isFiltrando?: boolean,
    isMostrarApenasMapa?: boolean
}

export default function IndicadoresMapaComponent({municipio, isFiltrando, isMostrarApenasMapa} : IndicadoresMapaProps){

    useEffect(() => {
        if (municipio) {
            buscarPopulacao();
            buscarQuantidadeHomens();
            buscarQuantidadeMulheres();
            buscarAreaTerritorial();
            buscarDensidadeDemografica();
            buscarDadosPrincipais();
        }
    }, [municipio])

    const [populacao, setPopulacao] = useState<any>();
        async function buscarPopulacao(){
          try{
            if(municipio != ""){
              const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/populacao?municipio=${municipio}`);
              if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                let populacaoMunicipal = "--"
                if (dadosInternos) {
                  try {
                      const objetoFormatado = JSON.parse(dadosInternos);     
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
    async function buscarQuantidadeHomens(){
        try{
            if(municipio != ""){
              const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/quantidadeDeHomens?municipio=${municipio}`);
              if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                let quantidadeHomensMunicipal = "--"
                if (dadosInternos) {
                  try {
                      const objetoFormatado = JSON.parse(dadosInternos);     
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
    async function buscarQuantidadeMulheres(){
        try{
            if(municipio != ""){
              const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/quantidadeDeMulheres?municipio=${municipio}`);
              if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                let quantidadeMulheresMunicipal = "--"
                if (dadosInternos) {
                  try {
                      const objetoFormatado = JSON.parse(dadosInternos);     
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
    async function buscarAreaTerritorial(){
        try{
            if(municipio != ""){
              const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/geograficos/areaTotal?municipio=${municipio}`);
              if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                let areaTerritorialMunicipal = "--"
                if (dadosInternos) {
                  try {
                      const objetoFormatado = JSON.parse(dadosInternos);     
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
    async function buscarDensidadeDemografica(){
        try{
            if(municipio != ""){
              const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/geograficos/densidadeDemografica?municipio=${municipio}`);
              if (requisicao.ok) {
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                let densidadeDemograficaMunicipal = "--"
                if (dadosInternos) {
                  try {
                      const objetoFormatado = JSON.parse(dadosInternos);     
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
    
    async function buscarDadosPrincipais() {
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
    

    return (
        <div className={`absolute m-2 z-10 md:left-4 md:top-4 md:z-10 ${isMostrarApenasMapa ? "hidden" : ""}`}>
            <div className={`flex flex-col gap-1 rounded-xl bg-white p-4 border-slate-200 shadow-md
                ${isFiltrando && municipio !== "" ? "visible" : "invisible"}`}>
                <div className="flex gap-2">
                    <p className="text-sm md:text-base font-bold text-black">População:</p>
                    <p className="text-sm md:text-base text-black">{populacao}</p>
                </div>
                <div className="flex gap-2">
                    <p className="text-sm md:text-base font-bold text-black">Quantidade de Homens:</p>
                    <p className="text-sm md:text-base text-black">{quantidadeHomens}</p>
                </div>
                <div className="flex gap-2">
                    <p className="text-sm md:text-base font-bold text-black">Quantidade de Mulheres:</p>
                    <p className="text-sm md:text-base text-black">{quantidadeMulheres}</p>
                </div>
                <div className="flex gap-2">
                    <p className="text-sm md:text-base font-bold text-black">Densidade Demográfica:</p>
                    <p className="text-sm md:text-base text-black">{densidadeDemografica}</p>
                </div>
                <div className="flex gap-2">
                    <p className="text-sm md:text-base font-bold text-black">Área Territorial:</p>
                    <p className="text-sm md:text-base text-black">{areaTerritorial}</p>
                </div>
                <div className="flex flex-col bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded p-2 mt-2">
                    <a href="https://sidra.ibge.gov.br/" target="_blank" rel="noopener noreferrer" className="text-sm text-white hover:text-blue-700">
                      Fonte: Sistema IBGE de Recuperação Automática (SIDRA)
                    </a>
                    <a href={dadosPrincipais} download={`Dados Principais do Município de ${municipio}.csv`} className="text-sm text-white hover:text-blue-700 font-semibold">
                      Baixar dados
                    </a>
                </div>
            </div>
        </div>
    )
}