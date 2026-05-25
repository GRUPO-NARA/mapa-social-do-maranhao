
import { useState, useEffect } from "react";
import GraficoCompativoComponent from "./GraficoCompativoComponent";
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
          getPibMunicipal()
          getEvolucaoIdhMunicipal()
          getIdhMunicipal()
        }
      }, [municipio]);
      
    const [pibMunicipal, setPibMunicipal] = useState<any>();
    const [referenciaPibMunicipal, setReferenciaPibMunicipal] = useState("");
    async function getPibMunicipal(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`/api/v1/municipios/pib?municipio=${municipio}`);
          const resposta = await requisicao.json();
          if (resposta?.resposta){
            const pib = formatarPibEmBilhoes(resposta.resposta.dados.PIB_municipio);
            const referencia = resposta.resposta.dados.referencia;
            setPibMunicipal(pib);
            setReferenciaPibMunicipal(referencia);
        }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes ao PIB Municipal!");
      }
    }

    const [idhMunicipal, setIdhMunicipal] = useState(0);
    const [referenciaIdhMunicipal, setReferenciaIdhMunicipal] = useState("");
    async function getIdhMunicipal(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`/api/v1/municipios/idh?municipio=${municipio}`);
          const resposta = await requisicao.json();
          if(resposta?.resposta){
            const idh = resposta.resposta.dados.IDH_municipal;
            const referencia = resposta.resposta.dados.referencia;
            setIdhMunicipal(idh);
            setReferenciaIdhMunicipal(referencia);
          }
        }
        }catch(error){
            console.error("Ocorreu um erro ao buscar dados referentes ao IDH Municipal!");
        }
    }

    const [dadosEvolucaoIdhMunicipal, setDadosEvolucaoIdhMunicipal] = useState<any>({});
    async function getEvolucaoIdhMunicipal(){
      try{
        if(municipio != ""){
          const requisicao = await fetch(`/api/v1/municipios/evolucaoIdh?municipio=${municipio}`);
          const resposta = await requisicao.json();
          setDadosEvolucaoIdhMunicipal(resposta);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes à evolução do IDH Municipal!");
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
                                <div>
                                    <h1 className="font-bold text-sm">Produto Interno Bruto</h1>
                                    <p className="text-gray-600 text-sm">PIB Municipal - {referenciaPibMunicipal}</p>
                                </div>
                                <div className="w-10 h-10 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300" style={{ maskImage: `url('pib_municipal_icon.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                            </div>
                            <h1 className="font-bold text-2xl text-gray-800 group-hover:text-sky-600 transition-colors duration-300">
                                {pibMunicipal && isFiltrando ? pibMunicipal + " Bilhões" : "--"}
                            </h1>
            
                        </div>
                    </div>
                    <div id="idhMunicipal" className="group/div bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Índice de Desenvolvimento Humano</h1>
                                    <p className="text-gray-600 text-sm">IDH Municipal - {referenciaIdhMunicipal}</p>
                                </div>
                                <div className="w-10 h-10 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300" style={{ maskImage: `url('crescimento-profissional.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>

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
                                <div>
                                    <h1 className="font-bold text-sm">Taxa de Desemprego</h1>
                                    <p className="text-gray-600 text-sm">População economicamente ativa</p>
                                </div>
                                <div className="w-10 h-10 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300" style={{ maskImage: `url('homem-empregado.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                            </div>
                            <h1 className="font-bold text-2xl text-gray-800">--</h1>
                            
                        </div>
                    </div>  
                    <GraficoCompativoComponent municipio={municipio} isVisualizacaoGrafica={isVisualizacaoGrafica} dadosEvolucaoIndicadorMunicipal={dadosEvolucaoIdhMunicipal} legendaDoGrafico={legendaGrafico} fecharGrafico={() => setIsVisualizacaoGrafica(false)} />
                </div>
            </div>
            
        </div>
    )

}