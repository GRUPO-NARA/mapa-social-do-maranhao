
export default function IndicadoresInfraestruturaRendaComponent() {
    return (
        <div className="group ">
            <div className="flex flex-col gap-6">
                <div className="flex items-center gap-2">
                            <p className="w-1 h-6 rounded bg-sky-600 "></p>
                            <h1 className="text-lg font-bold group-hover:text-sky-800 transition-colors duration-300">Infraestrutura e Assistência Social</h1>
                </div>
                <div className="grid sm:grid-cols-1 md:grid-cols-3 xl:grid-cols-3 gap-4 ">
                    <div className="group/div bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Domicílios com Água Encanada</h1>
                                    <p className="text-gray-600 text-sm">Água e esgoto tratados</p>
                                </div>
                                <span className="rounded bg-teal-100 p-2 w-fit h-fit text-xs font-semibold text-teal-800">Infraestrutura</span>
                            </div>
                            <h1 className="font-bold text-2xl">
                                --
                            </h1>
                        </div>
                    </div>
                     <div className="group/div bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Domicílios com Energia Elétrica</h1>
                                    <p className="text-gray-600 text-sm">Por pessoa</p>
                                </div>
                                <span className="rounded bg-teal-100 p-2 w-fit h-fit text-xs font-semibold text-teal-800">Infraestrutura</span>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Famílias no Cadastro Único</h1>
                                </div>
                                <span className="rounded bg-amber-100 p-2 w-fit h-fit text-xs font-semibold text-amber-800">Assistência</span>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>  
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Famílias em Situação de Rua</h1>
                                </div>
                                <span className="rounded bg-amber-100 p-2 w-fit h-fit text-xs font-semibold text-amber-800">Assistência</span>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>  
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Famílias com Trabalho Infantil</h1>
                                </div>
                                <span className="rounded bg-amber-100 p-2 w-fit h-fit text-xs font-semibold text-amber-800">Assistência</span>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>  
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Auxílio Gás para Brasileiros</h1>
                                </div>
                                <span className="rounded bg-amber-100 p-2 w-fit h-fit text-xs font-semibold text-amber-800">Assistência</span>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>  
                </div>
            </div>
            
        </div>
    )
}
