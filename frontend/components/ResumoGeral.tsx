import { useEffect, useState } from "react";

interface ResumoGeralProps {
    nomeSelecionado: string;
}

export default function ResumoGeral({ nomeSelecionado }: ResumoGeralProps) {
    const [populacaoTotal, setPopulacaoTotal] = useState([]);

    useEffect(() => {
        getPopulacaoTotal()
    }, [nomeSelecionado])

    async function getPopulacaoTotal(){
        if(!nomeSelecionado){
            setPopulacaoTotal([])
        }else{
            const resposta = await fetch(`http://localhost:8080/dados_demograficos/populacao_residente?nomeMunicipio=${nomeSelecionado}`);
            const dados = await resposta.json();
            setPopulacaoTotal(dados);
        }
        
    }

    return (
        <div className="flex justify-center items-center">
            <div className="flex flex-col gap-5 w-300 p-6 rounded-2xl shadow-xl/30 shadow-sky-600 border border-sky-600">
                <div className="flex items-center gap-2">
                    <p className="w-1 h-6 rounded bg-sky-600"></p>
                    <h1 className="text-xl font-bold">Resumo Geral</h1>
                </div>
                <div className="grid grid-cols-3 gap-4 ">
                    <div className="bg-white rounded-2xl border-sky-600 border transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-2 p-6">
                            <h1 className="text-gray-600">População Total</h1>
                            <h1 className="text-sky-600 font-bold text-2xl">{populacaoTotal}</h1>
                        </div>
                    </div>
                    <div className="bg-white rounded-2xl border-sky-600 border transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-2 p-6">
                            <h1 className="text-gray-600">PIB per capita</h1>
                            <h1 className="text-sky-600 font-bold text-2xl">R$ 80.000</h1>
                        </div>
                    </div>
                    <div className="bg-white rounded-2xl border-sky-600 border transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-2 p-6">
                            <h1 className="text-gray-600">Razão de Sexo</h1>
                            <h1 className="text-sky-600 font-bold text-2xl">98.3</h1>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}