
export default function IndicadoresInfraestruturaRendaComponent() {
    return (
        <div className="group ">
            <div className="flex flex-col gap-6">
                <div className="flex items-center gap-2">
                            <p className="w-1 h-6 rounded bg-sky-600 "></p>
                            <h1 className="text-lg font-bold group-hover:text-sky-800 transition-colors duration-300">Indicadores Infraestrutura e Renda</h1>
                </div>
                <div className="grid sm:grid-cols-1 md:grid-cols-3 xl:grid-cols-3 gap-4 ">
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Cobertura de Saneamento</h1>
                                    <p className="text-gray-600 text-sm">Água e esgoto tratados</p>
                                </div>
                                <p className="w-10 h-10 bg-sky-950 rounded-2xl"></p>
                            </div>
                            <h1 className="font-bold text-2xl text-sky-600">
                                --
                            </h1>
                        </div>
                    </div>
                     <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Renda Média</h1>
                                    <p className="text-gray-600 text-sm">Por pessoa</p>
                                </div>
                                <p className="w-10 h-10 bg-red-600 rounded-2xl"></p>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>
                     <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Distribuição por Gênero</h1>
                                </div>
                                <p className="w-10 h-10 bg-sky-600 rounded-2xl"></p>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>  
                </div>
            </div>
            
        </div>
    )
}