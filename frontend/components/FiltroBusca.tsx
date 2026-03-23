export default function FiltroBusca(){
    return (
        <div className="justify-center flex p-6 ">
            <div className="w-300 h-fit flex flex-col rounded-2xl shadow-sm bg-white p-6">
                <h1 className="pb-4"><b>Filtros de Busca</b></h1>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 ">
                    <div className="flex flex-col gap-1">
                        <label className="text-[14px]"htmlFor="seletor-municipio">Municipio</label>
                        <select className="rounded-lg shadow-sm p-2" name="seletor-municipio" id="">
                            <option value="">Fortaleza</option>
                        </select>
                    </div>
                    <div className="flex flex-col gap-1">
                        <label className="text-[14px]" htmlFor="seletor-ano">Ano</label>
                        <select className="rounded-lg shadow-sm p-2" name="seletor-ano" id="">
                            <option value="">2024</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
    )
}