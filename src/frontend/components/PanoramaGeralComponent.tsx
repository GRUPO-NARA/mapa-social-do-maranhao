import { useEffect, useState } from "react";
import { formatarPibEmBilhoes } from "../utils/formatarNumeros";

export default function PanoramaGeralComponent() {

    useEffect(() => {
        getPopulacaoTotalEstado(),
        getPibAgregadoEstado()
    }, []);

    const [referenciaPopulacaoEstado, setReferenciaPopulacaoEstado] = useState("");
    const [populacaoTotalEstado, setPopulacaoTotalEstado] = useState(0);
    async function getPopulacaoTotalEstado() {
        try{
            const requisicao = await fetch(`/api/v1/estado/populacao`);
            const resposta = await requisicao.json();
            if (resposta?.resposta?.dados) {
                var populacaoTotal = resposta.resposta.dados.populacao_estado.toLocaleString('pt-BR')
                var referenciaPopulacao = resposta.resposta.dados.referencia
                setPopulacaoTotalEstado(populacaoTotal);
                setReferenciaPopulacaoEstado(referenciaPopulacao);
            }
        } catch (error) {
            console.error("Erro ao buscar população total do estado:", error);
        }
    }

    const [referenciaPibAgregadoEstado, setReferenciaPibAgregadoEstado] = useState("");
    const [pibAgregadoEstado, setPibAgregadoEstado] = useState("");
    async function getPibAgregadoEstado() {
        try{
            const requisicao = await fetch(`/api/v1/estado/pibAgregado`);
            const resposta = await requisicao.json();
            if (resposta?.resposta?.dados) {
                setPibAgregadoEstado(formatarPibEmBilhoes(resposta.resposta.dados.pib_agregado));
                setReferenciaPibAgregadoEstado(resposta.resposta.dados.referencia);
            }
        } catch (error) {
            console.error("Erro ao buscar PIB agregado do estado:", error);
        }
    }

    return (
        <div className="rounded-2xl p-6">
            <div className="group gap-5 flex flex-col rounded-2xl">
                <div className="flex items-center gap-2">
                            <p className="w-1 h-6 rounded bg-sky-600 "></p>
                            <h1 className="text-lg font-bold group-hover:text-sky-800 transition-colors duration-300">Panorama Geral do Estado</h1>
                </div>
                <div className="grid grid-cols-2 gap-3 ">
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        
                        <div className="w-8 h-8 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('total_municipios_icon.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">Total de Municípios</h1>
                        <p className="text-lg font-bold text-gray-800">217</p>
                        <p className="text-xs text-gray-600">Municípios</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-8 h-8 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('populacao_estadual_icon.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">População - {referenciaPopulacaoEstado}</h1>
                        <p className="text-lg font-bold text-gray-800">{populacaoTotalEstado ? populacaoTotalEstado : "--"}</p>
                        <p className="text-xs text-gray-600">Habitantes</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-8 h-8 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('pib_agregado_icon.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">PIB Agregado - {referenciaPibAgregadoEstado}</h1>
                        <p className="text-lg font-bold text-gray-800">R$ {pibAgregadoEstado ? pibAgregadoEstado : "--"}</p>
                        <p className="text-xs text-gray-600">Bilhões</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-8 h-8 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('icons_panorama_geral/icon_idh.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">IDH Estadual</h1>
                        <p className="text-lg font-bold text-gray-800">--</p>
                        <p className="text-xs text-gray-600">IDH</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-8 h-8 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('icons_panorama_geral/icon_renda_per_capita.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">Renda per Capita</h1>
                        <p className="text-lg font-bold text-gray-800">R$ --</p>
                        <p className="text-xs text-gray-600">Reais</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-8 h-8 bg-sky-600 md:bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('icons_panorama_geral/icon_taxa_desemprego.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">Taxa de Desemprego</h1>
                        <p className="text-lg font-bold text-gray-800">--</p>
                        <p className="text-xs text-gray-600">Percentual</p>
                    </div>
                    
                </div>
            </div>
        </div>
    )
}