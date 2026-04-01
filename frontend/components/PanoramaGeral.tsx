export default function PanoramaGeral() {
    return (
        <div className="flex justify-center items-center">
            <div className="w-300 gap-5 p-6 flex flex-col rounded-2xl shadow-xl/30 shadow-sky-600 border border-sky-600">
                <div className="flex items-center gap-2">
                            <p className="w-1 h-6 rounded bg-sky-600"></p>
                            <h1 className="text-xl font-bold">Panorama Geral do Estado</h1>
                </div>
                <div className="grid sm:grid-cols-1 md:grid-cols-4 xl:grid-cols-4 gap-10 ">
                    <div className="flex flex-col shadow-2xl p-7 rounded-2xl transition-all duration-300 hover:-translate-y-1">
                        <img className="w-9 h-9" src="" alt="" />
                        <h1>Total de Municípios</h1>
                        <p className="text-2xl font-bold">217</p>
                        <p className="text-gray-600">Municípios do estado</p>
                    </div>
                    <div className="flex flex-col shadow-2xl p-7 rounded-2xl transition-all duration-300 hover:-translate-y-1">
                        <img className="w-9 h-9" src="populacao.png" alt="" />
                        <h1>População Total</h1>
                        <p className="text-2xl font-bold">7,040,000</p>
                        <p className="text-gray-600">Habitantes nos municípios</p>
                    </div>
                    <div className="flex flex-col shadow-2xl p-7 rounded-2xl transition-all duration-300 hover:-translate-y-1">
                        <img className="w-9 h-9" src="" alt="" />
                        <h1>PIB Agregado</h1>
                        <p className="text-2xl font-bold">R$ 1.7T</p>
                        <p className="text-gray-600">Produto Interno Bruto</p>
                    </div>
                    <div className="flex flex-col shadow-2xl p-7 rounded-2xl transition-all duration-300 hover:-translate-y-1">
                        <img className="w-9 h-9" src="" alt="" />
                        <h1>IDH Médio</h1>
                        <p className="text-2xl font-bold">0.75</p>
                        <p className="text-gray-600">Índice de Desenvolvimento Humano</p>
                    </div>
                </div>
            </div>
        </div>
    )
}