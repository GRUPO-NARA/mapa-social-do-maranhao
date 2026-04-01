
export default function IndicadoresSociais() {
    return (
        <div className="flex justify-center items-center">
            <div className="flex flex-col gap-6 w-300 p-6 ">
                <div className="">
                    <h1 className="font-bold text-xl">Indicadores Sociais</h1>
                </div>
                <div className="grid sm:grid-cols-1 md:grid-cols-3 xl:grid-cols-3 gap-4 ">
                    <div className="bg-white rounded-2xl p-7 border border-sky-600 shadow-2xl transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Taxa de Natalidade</h1>
                                    <p className="text-gray-600 text-sm">por mil habitantes</p>
                                </div>
                                <p className="w-10 h-10 bg-sky-950 rounded-2xl"></p>
                            </div>
                            <h1 className="font-bold text-2xl text-sky-600">
                                2
                            </h1>
                            <div className="flex items-center gap-2">
                                <p className=" text-sm bg-green-500 rounded-full p-2">+4.2%</p>
                                <p className="text-gray-600">em relação ao ano anterior</p>
                            </div>
                        </div>
                    </div>
                     <div className="bg-white rounded-2xl p-7 border border-black shadow-2xl transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Mortalidade Infantil</h1>
                                    <p className="text-gray-600 text-sm">por mil habitantes nascidos vivos</p>
                                </div>
                                <p className="w-10 h-10 bg-red-600 rounded-2xl"></p>
                            </div>
                            <h1 className="font-bold text-2xl">10.2</h1>
                            <div className="flex items-center gap-2">
                                <p className=" text-sm bg-green-500 rounded-full p-2">+4.2%</p>
                                <p className="text-gray-600">em relação ao ano anterior</p>
                            </div>
                        </div>
                    </div>
                     <div className="bg-white rounded-2xl p-7 border border-red-600 shadow-2xl transition-all duration-300 hover:-translate-y-1">
                        <div className="flex flex-col gap-8">
                            <div className="flex justify-between">
                                <div>
                                    <h1 className="font-bold text-sm">Taxa de Escolaridade</h1>
                                    <p className="text-gray-600 text-sm">Ensino fundamental completo</p>
                                </div>
                                <p className="w-10 h-10 bg-sky-600 rounded-2xl"></p>
                            </div>
                            <h1 className="font-bold text-2xl">85.3%</h1>
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