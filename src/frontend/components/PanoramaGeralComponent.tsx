
export default function PanoramaGeralComponent() {
    return (
        <div className="rounded-2xl p-6">
            <div className="group gap-5 flex flex-col rounded-2xl">
                <div className="flex items-center gap-2">
                            <p className="w-1 h-6 rounded bg-sky-600 "></p>
                            <h1 className="text-lg font-bold group-hover:text-sky-800 transition-colors duration-300">Panorama Geral do Estado</h1>
                </div>
                <div className="grid grid-cols-2 gap-3 ">
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        
                        <div className="w-7 h-7 bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('icons_panorama_geral/icon_municipios_estado.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">Total de Municípios</h1>
                        <p className="text-lg font-bold">217</p>
                        <p className="text-xs text-gray-600">Municípios</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-7 h-7 bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('icons_panorama_geral/icon_habitantes_municipios.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">População Total</h1>
                        <p className="text-lg font-bold">7,040,000</p>
                        <p className="text-xs text-gray-600">Habitantes</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-7 h-7 bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('icons_panorama_geral/icon_pib.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">PIB Agregado</h1>
                        <p className="text-lg font-bold">R$ 1.7T</p>
                        <p className="text-xs text-gray-600">PIB</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-7 h-7 bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('icons_panorama_geral/icon_pib.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">IDH</h1>
                        <p className="text-lg font-bold">R$ 1.7T</p>
                        <p className="text-xs text-gray-600">PIB</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-7 h-7 bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('icons_panorama_geral/icon_pib.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">IDH</h1>
                        <p className="text-lg font-bold">R$ 1.7T</p>
                        <p className="text-xs text-gray-600">Renda per Capita</p>
                    </div>
                    <div className="group/div flex flex-col gap-1 p-3 rounded-2xl border-gray-300 border hover:shadow-xl/30 hover:shadow-sky-800 hover:-translate-y-1 hover:border-sky-600 transition-all duration-300 text-center items-center">
                        <div className="w-7 h-7 bg-black group-hover/div:bg-sky-600 transition-colors duration-300 mx-auto" style={{ maskImage: `url('icons_panorama_geral/icon_pib.svg')`, maskSize: 'contain', maskRepeat: 'no-repeat' }}></div>
                        <h1 className="text-xs">Taxa de Desemprego</h1>
                        <p className="text-lg font-bold">R$ 1.7T</p>
                        <p className="text-xs text-gray-600">PIB</p>
                    </div>
                    
                </div>
            </div>
        </div>
    )
}