import { useEffect, useState } from "react";

interface IndicadoresProps {
    nomeSelecionado: string;
}

export default function Indicadores({ nomeSelecionado }: IndicadoresProps) {

    const [densidadeDemografica, setDensidadeDemografica] = useState([])

    useEffect(() => {
        getDensidadeDemografica()
    }, [nomeSelecionado])

    async function getDensidadeDemografica() {
        if (!nomeSelecionado) {
            nomeSelecionado = "-"
        } else {
            const resposta = await fetch(`http://localhost:8080/dados_geograficos/densidade_demografica?nomeMunicipio=${nomeSelecionado}`);
            const dados = await resposta.json();
            setDensidadeDemografica(dados);
        }
    }

    return (
        <div className=" h-50 flex justify-center items-center">

            <div className="bg-white h-0 w-300 grid grid-cols-1 md:grid-cols-3 gap-10 p-3 shadow-2xl">
                <div className="flex flex-col p-6 gap-7 bg-white rounded-2xl shadow-sm">
                    <div className="flex justify-between items-center">
                        <div>
                            <h1 className="text-2xl">População Total</h1>
                        </div>
                        <img className="bg-white h-12 rounded-2xl p-2 shadow-sm" src="populacao.png" alt="" />
                    </div>
                    <div>
                        <h1 className="text-[18px]"><b>12.500.000</b></h1>
                    </div>
                </div>
                <div className="flex flex-col p-6 gap-7 bg-white rounded-2xl shadow-sm">
                    <div className="flex justify-between items-center">
                        <div>
                            <h1 className="text-2xl">PIB Municipal</h1>
                            <p className="text-gray-500 text-[12px]">São Luís - 2024</p>
                        </div>
                        <img className="bg-blue-400 h-12 rounded-2xl p-2 shadow-sm" src="diagrama.png" alt="" />
                    </div>
                    <div>
                        <h1 className="text-2xl text-green-600">R$ 857.9 bi</h1>
                    </div>
                </div>
                <div className="flex flex-col p-6 gap-7 bg-white rounded-2xl shadow-sm">
                    <div className="flex justify-between items-center">
                        <div>
                            <h1 className="text-2xl">Distribuição por Gênero</h1>
                        </div>
                        <img className="bg-white h-12 rounded-2xl p-2 shadow-sm" src="igualdade.png" alt="" />
                    </div>
                    <div className="flex gap-10">
                        <div>
                            <h1 className="text-2xl">Homens</h1>
                            <p>Valor</p>
                        </div>
                        <div>
                            <h1 className="text-2xl">Mulheres</h1>
                            <p>valor</p>
                        </div>
                    </div>
                </div>
                <div className="flex flex-col p-6 gap-7 bg-white rounded-2xl shadow-sm">
                    <div className="flex justify-between items-center">
                        <div>
                            <h1 className="text-2xl">Taxa de Natalidade</h1>
                            <p className="text-gray-500">por mil habitantes</p>
                        </div>
                        <img className="bg-white h-12 rounded-2xl p-2 shadow-sm" src="menino-menino.png" alt="" />
                    </div>
                    <div>
                        <h1 className="text-2xl text-green-600">13.2</h1>
                    </div>
                </div>
                <div className="flex flex-col p-6 gap-7 bg-white rounded-2xl shadow-sm">
                    <div className="flex justify-between items-center">
                        <div>
                            <h1 className="text-2xl">IDH</h1>
                            <p className="text-gray-500">Índice de Desenvolvimento Humano</p>
                        </div>
                        <img className="bg-white h-12 rounded-2xl p-2 shadow-sm" src="desenvolvimento.png" alt="" />
                    </div>
                </div>
                <div className="flex flex-col p-6 gap-7 bg-white rounded-2xl shadow-sm">
                    <div className="flex justify-between items-center">
                        <div>
                            <h1 className="text-2xl">Densidade Demográfica</h1>
                            <p className="text-gray-600 text-[12px]">habitantes por quilômetro quadrado (hab/km²)</p>
                        </div>
                        <img className="bg-white h-12 rounded-2xl p-2 shadow-sm" src="superpopulacao.png" alt="" />
                    </div>
                    <div className="text-2xl">
                        <h1>{densidadeDemografica}</h1>
                    </div>
                </div>

            </div>
        </div>
    )
}