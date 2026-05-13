
import { useState, useEffect } from "react";
import GraficoCompativoComponent from "./GraficoCompativoComponent";

interface IndicadoresPrincipaisProps {
    municipio: String
}

export default function IndicadoresPrincipaisComponent({municipio} : IndicadoresPrincipaisProps){
    const [isVisualizacaoGrafica, setIsVisualizacaoGrafica] = useState(false);

    useEffect(() => {
        if (municipio != "") {
          getPibMunicipal();
          getIdhMunicipal();
        }
      }, [municipio]);
      
    const [pibMunicipal, setPibMunicipal] = useState<any>({});
    async function getPibMunicipal(){
      try{
        if(municipio != ""){
          const resposta = await fetch(`/api/v1/municipios/pib?municipio=${municipio}`);
          const dados = await resposta.json();
          setPibMunicipal(dados);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes ao PIB Municipal!");
      }
    }
    
    const [idhMunicipal, setIdhMunicipal] = useState<any>({});
    async function getIdhMunicipal(){
      try{
        if(municipio != ""){
          const resposta = await fetch(`/api/v1/municipios/idh?municipio=${municipio}`);
          const dados = await resposta.json();
          setIdhMunicipal(dados);
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes ao IDH Municipal!");
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
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">PIB Municipal</h1>
                                    <p className="text-gray-600 text-sm">- 2023</p>
                                </div>
                                <div className="w-10 h-10 bg-black" style={{ maskImage: `url('money-bag-svgrepo-com.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                            </div>
                            <h1 className="font-bold text-2xl text-sky-600">
                                {pibMunicipal?.resposta?.valor ? "R$ " + pibMunicipal.resposta.valor.toLocaleString("pt-BR") : "--"}
                            </h1>
            
                        </div>
                    </div>
                     <div id="idhMunicipal" className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Índice de Desenvolvimento Humano</h1>
                                    <p className="text-gray-600 text-sm">IDH Municipal - </p>
                                </div>
                                <div className="w-10 h-10 bg-black" style={{ maskImage: `url('graph-up-svgrepo-com.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>

                            </div>
                            <div className="flex justify-between">
                                <h1 className="font-bold text-2xl">
                                    {idhMunicipal?.resposta?.valor ? idhMunicipal.resposta.valor.toFixed(3) : "--"}
                                </h1>
                                <button onClick = {() => setIsVisualizacaoGrafica(true)} className="text-sm bg-gray-300 text-gray-700 p-3 rounded-lg hover:bg-gray-400 transition-colors duration-300 active:bg-gray-500 ml-2">
                                    evolução do IDH
                                </button>
                            </div>
                            
                            
                        </div>
                    </div>
                     <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Taxa de Desemprego</h1>
                                    <p className="text-gray-600 text-sm">População economicamente ativa</p>
                                </div>
                                <p className="w-10 h-10 bg-sky-600 rounded-2xl"></p>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                            
                        </div>
                    </div>  
                    <GraficoCompativoComponent isVisualizacaoGrafica={isVisualizacaoGrafica} />
                </div>
            </div>
            
        </div>
    )

}