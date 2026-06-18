
export default function IndicadoresSociaisComponent(){
    return (
         <div className="group">
            <div className="flex flex-col gap-6">
                <div className="flex items-center gap-2">
                            <p className="w-1 h-6 rounded bg-sky-600 "></p>
                            <h1 className="text-lg font-bold group-hover:text-sky-800 transition-colors duration-300">Indicadores Sociais</h1>
                </div>
                <div className="grid sm:grid-cols-1 md:grid-cols-3 xl:grid-cols-3 gap-4 ">
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Taxa de Analfabetismo 15+</h1>
                                    <p className="text-gray-600 text-sm">por mil habitantes</p>
                                </div>
                                <span className="rounded bg-gray-100 p-2 text-xs font-semibold w-fit h-fit text-gray-800">Educação</span>
                            </div>
                            <h1 className="font-bold text-2xl">
                                --
                            </h1>
                        </div>
                    </div>
                     <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Aprovação no Ensino Fundamental</h1>
                                    <p className="text-gray-600 text-sm">por mil habitantes nascidos vivos</p>
                                </div>
                                <span className="rounded bg-gray-100 p-2 text-xs font-semibold w-fit h-fit text-gray-800">Educação</span>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Aprovação no Ensino Médio</h1>
                                    <p className="text-gray-600 text-sm">Ensino fundamental completo</p>
                                </div>
                                <span className="rounded bg-gray-100 p-2 text-xs font-semibold w-fit h-fit text-gray-800">Educação</span>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>  
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Mortalidade Infantil</h1>
                                    <p className="text-gray-600 text-sm">Ensino fundamental completo</p>
                                </div>
                                <span className="rounded bg-red-100 p-2 text-xs font-semibold w-fit h-fit text-red-800">Saúde</span>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>  
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Nascidos Vivos de Mães Adolescentes</h1>
                                    <p className="text-gray-600 text-sm">Ensino fundamental completo</p>
                                </div>
                                <span className="rounded bg-red-100 p-2 text-xs font-semibold w-fit h-fit text-red-800">Saúde</span>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>  
                    <div className="bg-white rounded-2xl p-7 shadow-2x border border-gray-300 hover:border-sky-600 transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Razão de Mortalidade Materna</h1>
                                    <p className="text-gray-600 text-sm">Ensino fundamental completo</p>
                                </div>
                                <span className="rounded bg-red-100 p-2 text-xs font-semibold w-fit h-fit text-red-800">Saúde</span>
                            </div>
                            <h1 className="font-bold text-2xl">--</h1>
                        </div>
                    </div>  

                    
                </div>
            </div>
            
        </div>
    )
}
