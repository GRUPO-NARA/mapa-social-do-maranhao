
import { useState, useEffect } from "react";
import GraficoCompativoComponent from "./GraficoCompativoComponent";
import AjudaIndicadorComponent from "./AjudaIndicadorComponent";
import { formatarPibEmBilhoes } from "../utils/formatarNumeros";

interface IndicadoresPrincipaisProps {
    municipio: String
    isFiltrando?: boolean
}

export default function IndicadoresPrincipaisComponent({municipio, isFiltrando} : IndicadoresPrincipaisProps){
    const [isVisualizacaoGrafica, setIsVisualizacaoGrafica] = useState(false);
    const [legendaGrafico, setLegendaGrafico] = useState("");

    useEffect(() => {
        if (municipio != "") {
          buscarPibMunicipal()
          buscarEvolucaoIdhMunicipal()
          buscarIdhMunicipal()
          buscarPopulacaoUrbana()
          buscarPopulacaoRural()
          buscarPopulacaoEmFavelas()
          buscarPibPerCapita()
        }
      }, [municipio]);
      
    const [pibMunicipal, setPibMunicipal] = useState<any>();
    const [referenciaPibMunicipal, setReferenciaPibMunicipal] = useState("");
    const [fontePibMunicipal, setFontePibMunicipal] = useState("");
    async function buscarPibMunicipal(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/economicos/produtoInternoBruto?municipio=${municipio}`);
          if(requisicao.ok){
            const resposta = await requisicao.json();
            var dadosInternos = resposta?.["Resposta da Requisição"];
            if(dadosInternos){
              try{
                var objetoFormatado = JSON.parse(dadosInternos);
                var pibEmBilhoes = formatarPibEmBilhoes(objetoFormatado["Produto Interno Bruto"]);
                setPibMunicipal(pibEmBilhoes);
                var dataDeColeta = objetoFormatado["Referência dos Dados"];
                setReferenciaPibMunicipal(dataDeColeta);
                var fonte = objetoFormatado["Fonte dos Dados"];
                setFontePibMunicipal(fonte);
              }catch(erro){
                console.error("Erro ao converter JSON:", erro);
              }
            }
            }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes ao PIB Municipal!");
      }
    }

    const [idhMunicipal, setIdhMunicipal] = useState(0);
    const [referenciaIdhMunicipal, setReferenciaIdhMunicipal] = useState("");
    const [fonteIdhMunicipal, setFonteIdhMunicipal] = useState("");
    async function buscarIdhMunicipal(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/idh?municipio=${municipio}`);
          if(requisicao.ok){
            const resposta = await requisicao.json();
            var dadosInternos = resposta?.["Resposta da Requisição"];
            if(dadosInternos){
                try{
                    var objetoFormatado = JSON.parse(dadosInternos);
                    var idh = objetoFormatado["Índice de Desenvolvimento Humano"];
                    setIdhMunicipal(idh);
                    var dataDeColeta = objetoFormatado["Referência dos Dados"];
                    setReferenciaIdhMunicipal(dataDeColeta);
                    var fonte = objetoFormatado["Fonte dos Dados"];
                    setFonteIdhMunicipal(fonte);
                }catch(erro){
                    console.error("Erro ao converter JSON:", erro);
                }
            }
          }
        }
        }catch(error){
            console.error("Ocorreu um erro ao buscar dados referentes ao IDH Municipal!");
        }
    }

    const [dadosEvolucaoIdhMunicipal, setDadosEvolucaoIdhMunicipal] = useState<any>({});
    async function buscarEvolucaoIdhMunicipal(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/evolucaoIDH?municipio=${municipio}`);
          if(requisicao.ok){
            const resposta = await requisicao.json();
            setDadosEvolucaoIdhMunicipal(resposta);
          }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à evolução do IDH Municipal!");
      }
    }

    const [populacaoRural, setPopulacaoRural] = useState(0);
    const [referenciaPopulacaoRural, setReferenciaPopulacaoRural] = useState("");
    const [fontePopulacaoRural, setFontePopulacaoRural] = useState("");
    async function buscarPopulacaoRural(){
      try{
        if(municipio != ""){
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/quantidadeDeResidentesRurais?municipio=${municipio}`);
            if(requisicao.ok){
                const resposta = await requisicao.json();
                var dadosInternos = resposta?.["Resposta da Requisição"];
                if(dadosInternos){
                    try{
                        var objetoFormatado = JSON.parse(dadosInternos);
                        var populacaoRural = objetoFormatado["Quantidade de Residentes Rurais"].toLocaleString("pt-BR");
                        setPopulacaoRural(populacaoRural);
                        var dataDeColeta = objetoFormatado["Referência dos Dados"];
                        setReferenciaPopulacaoRural(dataDeColeta);
                        var fonte = objetoFormatado["Fonte dos Dados"];
                        setFontePopulacaoRural(fonte);
                    }catch(erro){
                        console.error("Erro ao converter JSON:", erro);
                    }
                }
            }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à população rural!");
      }
    }

    const [populacaoUrbana, setPopulacaoUrbana] = useState(0);
    const [referenciaPopulacaoUrbana, setReferenciaPopulacaoUrbana] = useState("");
    const [fontePopulacaoUrbana, setFontePopulacaoUrbana] = useState("");
    async function buscarPopulacaoUrbana(){
        try{
            if(municipio != ""){
                const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/quantidadeDeResidentesUrbano?municipio=${municipio}`);
                if(requisicao.ok){
                    const resposta = await requisicao.json();
                    var dadosInternos = resposta?.["Resposta da Requisição"];
                    if(dadosInternos){
                        try{
                            var objetoFormatado = JSON.parse(dadosInternos);
                            var populacaoUrbana = objetoFormatado["Quantidade de Residentes em Área Urbana"].toLocaleString("pt-BR");
                            setPopulacaoUrbana(populacaoUrbana);
                            var dataDeColeta = objetoFormatado["Referência dos Dados"];
                            setReferenciaPopulacaoUrbana(dataDeColeta);
                            var fonte = objetoFormatado["Fonte dos Dados"];
                            setFontePopulacaoUrbana(fonte);
                        }catch(erro){
                            console.error("Erro ao converter JSON:", erro);
                        }
                    }
                }
            }
        }catch(error){
            console.error("Ocorreu um erro ao buscar dados referentes à população urbana!", error);
        }
    }

    const [populacaoEmFavelas, setPopulacaoEmFavelas] = useState(0);
    const [referenciaPopulacaoEmFavelas, setReferenciaPopulacaoEmFavelas] = useState("");
    const [fontePopulacaoEmFavelas, setFontePopulacaoEmFavelas] = useState("");
    async function buscarPopulacaoEmFavelas(){
      try{
        if(municipio != ""){
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/populacaoEmFavela?municipio=${municipio}`);
            if(requisicao.ok){
                const resposta = await requisicao.json();
                console.log("Resposta da requisição de população em favelas:", resposta);
                var dadosInternos = resposta?.["Resposta da Requisição"];
                if(dadosInternos){
                    try{
                        var objetoFormatado = JSON.parse(dadosInternos);
                        var populacaoEmFavelas = objetoFormatado["Quantidade de Pessoas em Favelas"].toLocaleString("pt-BR");
                        setPopulacaoEmFavelas(populacaoEmFavelas);
                        var dataDeColeta = objetoFormatado["Referência dos Dados"];
                        setReferenciaPopulacaoEmFavelas(dataDeColeta);
                        var fonte = objetoFormatado["Fonte dos Dados"];
                        setFontePopulacaoEmFavelas(fonte);
                    }catch(erro){
                        console.error("Erro ao converter JSON:", erro);
                    }
                }
            }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à população em favelas!");
      }
    }

    const [pibPerCapita, setPibPerCapita] = useState(0);
    const [referenciaPibPerCapita, setReferenciaPibPerCapita] = useState("");
    const [fontePibPerCapita, setFontePibPerCapita] = useState("");
    async function buscarPibPerCapita(){
      try{
        if(municipio != ""){
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/economicos/pibPerCapita?municipio=${municipio}`);
            if(requisicao.ok){
                const resposta = await requisicao.json();
                var dadosInternos = resposta?.["Resposta da Requisição"];
                if(dadosInternos){
                    try{
                        var objetoFormatado = JSON.parse(dadosInternos);
                        var pibPerCapita = objetoFormatado["Produto Interno Bruto per Capita"].toLocaleString("pt-BR", { style: 'currency', currency: 'BRL' });
                        setPibPerCapita(pibPerCapita);
                        var dataDeColeta = objetoFormatado["Referência dos Dados"];
                        setReferenciaPibPerCapita(dataDeColeta);
                        var fonte = objetoFormatado["Fonte dos Dados"];
                        setFontePibPerCapita(fonte);
                    }
                    catch(erro){
                        console.error("Erro ao converter JSON:", erro);
                    }
                }
            }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à população em favelas!");
      }
    }

    return (
        <div className="group">
            <div className="flex flex-col gap-6">
                <div className="flex items-center gap-2">
                            <p className="w-1 h-6 rounded bg-sky-600 "></p>
                            <h1 className="text-lg font-bold group-hover:text-sky-800 transition-colors duration-300">Indicadores Principais </h1>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 ">
                    <div className="group/div bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div className="flex flex-col gap-1">
                                    <div className="flex items-center gap-2 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded p-2 w-fit h-fit">
                                        <h1 className="font-bold text-sm text-white">PIB Municipal</h1>
                                        <AjudaIndicadorComponent titulo="PIB Municipal" texto="Soma dos bens e serviços produzidos no município em um ano." variante="escura" />
                                    </div>
                                   <span className="text-gray-600 text-sm">Produto Interno Bruto do município</span>
                                    <div className="w-full h-1 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded-2xl"></div>
                                    <p className="text-gray-600 text-xs">Referência: {referenciaPibMunicipal} - Fonte: {fontePibMunicipal}</p>
                                </div>
                                <span className="rounded bg-green-100 p-2 text-xs font-semibold w-fit h-fit text-green-800">Economia</span>
                            </div>
                            <h1 className="font-bold text-2xl text-gray-800 group-hover:text-sky-600 transition-colors duration-300">
                                {pibMunicipal && isFiltrando ? pibMunicipal + " Bilhões" : "--"}
                            </h1>
                        </div>
                    </div>
                     <div className="group/div bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div className="flex flex-col gap-1">
                                    <div className="flex items-center gap-2 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded p-2 w-fit h-fit">
                                        <h1 className="font-bold text-sm text-white">PIB Per Capita</h1>
                                        <AjudaIndicadorComponent titulo="PIB Per Capita" texto="PIB do município dividido pelo número de habitantes." variante="escura" />
                                    </div>
                                    <span className="text-gray-600 text-sm">Produto Interno Bruto per capita do município</span>
                                    <div className="w-full h-1 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded-2xl"></div>
                                    <p className="text-gray-600 text-xs">Referência: {referenciaPibPerCapita} - Fonte: {fontePibPerCapita}</p>
                                </div>
                                <span className="rounded bg-green-100 p-2 text-xs font-semibold w-fit h-fit text-green-800">Economia</span>
                            </div>
                            <h1 className="font-bold text-2xl text-gray-800 group-hover:text-sky-600 transition-colors duration-300">
                                {pibPerCapita && isFiltrando ? pibPerCapita + " Reais" : "--"}
                            </h1>
                        </div>
                    </div>  
                    <div id="idhMunicipal" className="group/div bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div className="flex flex-col gap-1">
                                    <div className="flex items-center gap-2 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded p-2 w-fit h-fit">
                                        <h1 className="font-bold text-sm text-white">Índice de Desenvolvimento Humano</h1>
                                        <AjudaIndicadorComponent titulo="Índice de Desenvolvimento Humano" texto="Índice que reúne informações de renda, educação e longevidade." variante="escura" />
                                    </div>
                                    <span className="text-gray-600 text-sm">Desenvolvimento Humano</span>
                                    <div className="w-full h-1 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded-2xl"></div>
                                    <p className="text-gray-600 text-xs"> Referência: {referenciaIdhMunicipal} - Fonte: {fonteIdhMunicipal}</p>
                                </div>
                                <span className="rounded bg-indigo-100 p-2 w-fit h-fit text-xs font-semibold text-indigo-800">Desenvolvimento</span>

                            </div>
                            <div className="flex justify-between">
                                <h1 className="font-bold text-2xl text-gray-800 group-hover:text-sky-600 transition-colors duration-300">
                                    {idhMunicipal && isFiltrando ? idhMunicipal.toFixed(3) : "--"}
                                </h1>
                                <button onClick = {() => {
                                    if (isFiltrando) {
                                        setIsVisualizacaoGrafica(true);
                                        setLegendaGrafico("IDH Municipal - " + municipio);
                                    }else{
                                        setIsVisualizacaoGrafica(false);
                                    }
                                }} className="text-sm bg-gray-300 text-gray-600 p-3 rounded-lg hover:bg-gray-400 transition-colors duration-300 active:bg-gray-500 ml-2">
                                    evolução do IDH
                                </button>
                            </div>
                            
                            
                        </div>
                    </div>
                    <div className="group/div bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div className="flex flex-col gap-1">
                                    <div className="flex items-center gap-2 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded p-2 w-fit h-fit">
                                        <h1 className="font-bold text-sm text-white">População Urbana</h1>
                                        <AjudaIndicadorComponent titulo="População Urbana" texto="Número de pessoas residentes em áreas urbanas do município." variante="escura" />
                                    </div>
                                    <span className="text-gray-600 text-sm">População residente em áreas urbanas</span>
                                    <div className="w-full h-1 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded-2xl"></div>
                                    <p className="text-gray-600 text-xs">Referência: {referenciaPopulacaoUrbana} - Fonte: {fontePopulacaoUrbana}</p>
                                </div>
                                <span className="rounded bg-cyan-100 p-2 text-xs font-semibold w-fit h-fit text-cyan-800">Demografia</span>
                            </div>
                            <h1 className="font-bold text-2xl text-gray-800 group-hover:text-sky-600">{populacaoUrbana && isFiltrando ? populacaoUrbana.toLocaleString("pt-BR") : "--"}</h1>
                            
                        </div>
                    </div>  
                    <div className="group/div bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div className="flex flex-col gap-1">
                                    <div className="flex items-center gap-2 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded p-2 w-fit h-fit">
                                        <h1 className="font-bold text-sm text-white">População Rural</h1>
                                        <AjudaIndicadorComponent titulo="População Rural" texto="Número de pessoas residentes em áreas rurais do município." variante="escura" />
                                    </div>
                                    <span className="text-gray-600 text-sm">População residente em áreas rurais</span>
                                    <div className="w-full h-1 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded-2xl"></div>
                                    <p className="text-gray-600 text-xs">Referência: {referenciaPopulacaoRural} - Fonte: {fontePopulacaoRural}</p>
                                </div>
                                <span className="rounded bg-cyan-100 p-2 text-xs font-semibold w-fit h-fit text-cyan-800">Demografia</span>
                            </div>
                            <h1 className="font-bold text-2xl text-gray-800 group-hover:text-sky-600">{populacaoRural ? populacaoRural.toLocaleString("pt-BR") : "--"}</h1>
                            
                        </div>
                    </div>  
                    <div className="group/div bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div className="flex flex-col gap-1">
                                    <div className="flex items-center gap-2 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded p-2 w-fit h-fit">
                                        <h1 className="font-bold text-sm text-white">População em Favelas</h1>
                                        <AjudaIndicadorComponent titulo="População em Favelas" texto="Número de pessoas residentes em favelas e comunidades urbanas." variante="escura" />
                                    </div>
                                    <span className="text-gray-600 text-sm">População residente em favelas</span>
                                    <div className="w-full h-1 bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 rounded-2xl"></div>
                                    <p className="text-gray-600 text-xs">Referência: {referenciaPopulacaoEmFavelas} - Fonte: {fontePopulacaoEmFavelas}</p>
                                </div>
                                <span className="rounded bg-cyan-100 p-2 text-xs font-semibold w-fit h-fit text-cyan-800">Demografia</span>
                            </div>
                            <h1 className="font-bold text-2xl text-gray-800 group-hover:text-sky-600">{populacaoEmFavelas ? populacaoEmFavelas.toLocaleString("pt-BR") : "--"}</h1>
                            
                        </div>
                    </div>  
                    <GraficoCompativoComponent municipio={municipio} isVisualizacaoGrafica={isVisualizacaoGrafica} dadosEvolucaoIndicadorMunicipal={dadosEvolucaoIdhMunicipal} legendaDoGrafico={legendaGrafico} fecharGrafico={() => setIsVisualizacaoGrafica(false)} />
                </div>
            </div>
            
        </div>
    )

}
