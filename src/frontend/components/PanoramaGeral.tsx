export default function PanoramaGeral() {
    return (
        <div className="flex justify-center items-center">
            <div className="group w-300 gap-5 p-6 flex flex-col rounded-2xl shadow-xl/30 shadow-sky-900 border border-gray-300 hover:border-sky-600 transition-colors duration-300">
                <div className="flex items-center gap-2">
                            <p className="w-1 h-6 rounded bg-sky-600 "></p>
                            <h1 className="text-xl font-bold group-hover:text-sky-800 transition-colors duration-300">Panorama Geral do Estado</h1>
                </div>
                <div className="grid sm:grid-cols-1 md:grid-cols-4 xl:grid-cols-4 gap-10 ">
                    <div className="group/div flex flex-col gap-2 p-7 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 ">
                        <div className="w-9 h-9 bg-black group-hover/div:bg-sky-600 transition-colors duration-300" style={{ maskImage: `url('icons_panorama_geral/icon_municipios_estado.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1>Total de Municípios</h1>
                        <p className="text-2xl font-bold">217</p>
                        <p className="text-gray-600">Municípios do estado</p>
                    </div>
                    <div className="group/div flex flex-col gap-2 p-7 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 ">
                        <div className="w-9 h-9 bg-black group-hover/div:bg-sky-600 transition-colors duration-300" style={{ maskImage: `url('icons_panorama_geral/icon_habitantes_municipios.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1>População Total</h1>
                        <p className="text-2xl font-bold">7,040,000</p>
                        <p className="text-gray-600">Habitantes nos municípios</p>
                    </div>
                    <div className="group/div flex flex-col gap-2 p-7 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 ">
                        <div className="w-9 h-9 bg-black group-hover/div:bg-sky-600 transition-colors duration-300" style={{ maskImage: `url('icons_panorama_geral/icon_pib.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1>PIB Agregado</h1>
                        <p className="text-2xl font-bold">R$ 1.7T</p>
                        <p className="text-gray-600">Produto Interno Bruto</p>
                    </div>
                    <div className="group/div flex flex-col gap-2 p-7 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 ">
                        <div className="w-9 h-9 bg-black group-hover/div:bg-sky-600 transition-colors duration-300" style={{ maskImage: `url('icons_panorama_geral/icon_idh_medio.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1>IDH Médio</h1>
                        <p className="text-2xl font-bold">0.75</p>
                        <p className="text-gray-600">Índice de Desenvolvimento Humano</p>       
                    </div>
                </div>
            </div>
        </div>
    )
}