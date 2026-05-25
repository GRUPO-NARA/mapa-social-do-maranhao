import { useState, useEffect } from "react";

interface FiltroProps{
    aoMudarMunicipio: (municipio:string) => void
    isFiltrando: (filtrando: boolean) => void
}

export default function FiltroComponent({aoMudarMunicipio, isFiltrando} : FiltroProps){

    const[municipios, setMunicipios] = useState([]);

    const[isMunicipioSelecionado, setIsMunicipioSelecionado] = useState(false);

    const[municipioSelecionado, setMunicipioSelecionado] = useState("");

    const[tentouAplicarFiltro, setTentouAplicarFiltro] = useState(false);


    async function getMunicipios(){
        try{
            const resposta = await fetch("/api/v1/municipios/lista");
            const dados = await resposta.json();
            setMunicipios(dados);
        }catch(error){
            console.error("Erro ao buscar a listagem dos municípios.")
        }
    }

    useEffect(() => {
        getMunicipios()
    },[])

    return (
        <div className="group flex flex-col gap-4 p-6 h-fit rounded-2xl">
            <div className="flex items-center gap-2">
              <p className="w-1 h-6 rounded bg-sky-600"></p>
              <h1 className="text-lg font-bold group-hover:text-sky-800 transition-colors duration-300">Filtros</h1>
            </div>
            <div className="flex flex-col gap-5">
              <div className="flex flex-col gap-1">
                <label className="text-[15px]" htmlFor="seletor-municipio">
                  Município
                </label>

                <select
                  id="seletor-municipio"
                  className="rounded-lg shadow-sm p-2 bg-sky-700 text-white"
                  value={municipioSelecionado}
                  onChange={(e) => {
                    if(e.target.value != ""){
                      setIsMunicipioSelecionado(true);
                      setMunicipioSelecionado(e.target.value);
                      setTentouAplicarFiltro(false);
                    }else{
                      setIsMunicipioSelecionado(false);
                      setMunicipioSelecionado("");
                    }
                  }}
                >
                <option value="">Selecione um município</option>

                {municipios.map((municipio: string) => (
                <option key={municipio} value={municipio}>
                  {municipio}
                </option>
                ))}
                </select>
              </div>

              <div className={`w-full bg-yellow-500 rounded-lg p-2 ${tentouAplicarFiltro && !isMunicipioSelecionado ? "visible" : "hidden"}`}>
                <p className="text-center text-gray-700 font-semibold">
                  Nenhum município selecionado
                </p>
              </div>
              
              <div className="grid grid-cols-2 gap-4">
                  <button id="botao-aplicar-filtros" onClick={() =>
                {
                  if(isMunicipioSelecionado){
                    aoMudarMunicipio(municipioSelecionado);
                    isFiltrando(true);
                    setTentouAplicarFiltro(false);
                  }else{
                    isFiltrando(false);
                    setTentouAplicarFiltro(true);
                  }
                }} className="bg-sky-600 text-white p-3 rounded-lg hover:bg-sky-700 transition-colors duration-300 active:bg-sky-800">
                Aplicar Filtros
                </button>
                <button id="botao-limpar-filtros" onClick={() =>
                {
                  setIsMunicipioSelecionado(false);
                  setMunicipioSelecionado("");
                  aoMudarMunicipio("");
                  setTentouAplicarFiltro(false);
                  isFiltrando(false);
                }} className="bg-gray-300 text-gray-700 p-3 rounded-lg hover:bg-gray-400 transition-colors duration-300 active:bg-gray-500 ml-2">
                Limpar Filtros
                </button>
              </div>
              
            </div>
        </div>
    )
}
