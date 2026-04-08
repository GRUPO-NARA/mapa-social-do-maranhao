import { useState, useEffect } from "react";

interface IndicadoresPrincipaisProps {
    municipioSelecionado: string;
}

export default function IndicadoresPrincipais({ municipioSelecionado }: IndicadoresPrincipaisProps) {
    const [pibMunicipal, setPibMunicipal] = useState<number | null>(null);

    useEffect(() => {
        getPibMunicipal();
    }, [municipioSelecionado]);

    async function getPibMunicipal() {
        if (!municipioSelecionado) {
            setPibMunicipal(null);
        } else {
            const resposta = await fetch(`http://localhost:8080/dados_economicos/produto_interno_bruto?nomeMunicipio=${municipioSelecionado}`);
            const dados = await resposta.json();
            
            const valor = Array.isArray(dados) && dados.length > 0 ? dados[0] : null;
            setPibMunicipal(valor);
        }
    }

    return (
        <div className="flex justify-center items-center">
            <div className="flex flex-col gap-6 w-full p-6 ">
                <div className="">
                    <h1 className="font-bold text-xl">Indicadores Principais</h1>
                </div>
                <div className="grid sm:grid-cols-1 md:grid-cols-3 xl:grid-cols-3 gap-4 ">
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">PIB Municipal</h1>
                                    <p className="text-gray-600 text-sm">{municipioSelecionado} - 2023</p>
                                </div>
                                <p className="w-10 h-10 bg-sky-950 rounded-2xl"></p>
                            </div>
                            <h1 className="font-bold text-2xl text-sky-600">
                                {pibMunicipal !== null
                                    ? pibMunicipal.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
                                    : '--'}
                            </h1>
                            <div className="flex items-center gap-2">
                                <p className=" text-sm bg-green-500 rounded-full p-2">+4.2%</p>
                                <p className="text-gray-600">em relação ao ano anterior</p>
                            </div>
                        </div>
                    </div>
                     <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Índice de Desenvolvimento Humano</h1>
                                    <p className="text-gray-600 text-sm">IDH Municipal</p>
                                </div>
                                <p className="w-10 h-10 bg-red-600 rounded-2xl"></p>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                            <div className="flex items-center gap-2">
                                <p className=" text-sm bg-green-500 rounded-full p-2">+4.2%</p>
                                <p className="text-gray-600">em relação ao ano anterior</p>
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
                            <div className="flex items-center gap-2">
                                <p className=" text-sm bg-red-400 rounded-full p-2">-0.7%</p>
                                <p className="text-gray-600">em relação ao ano anterior</p>
                            </div>
                        </div>
                    </div>  
                </div>
            </div>
            
        </div>
    )
}