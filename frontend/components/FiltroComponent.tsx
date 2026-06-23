'use client';

import { useState, useEffect } from "react";

interface FiltroProps{
    aoMudarMunicipio?: (municipio:string) => void
    isFiltrando?: (filtrando: boolean) => void
}

export default function FiltroComponent({
    aoMudarMunicipio = () => undefined,
    isFiltrando = () => undefined,
} : FiltroProps = {}){

    const[municipios, setMunicipios] = useState<any>(null);

    const[isMunicipioSelecionado, setIsMunicipioSelecionado] = useState(false);

    const[municipioSelecionado, setMunicipioSelecionado] = useState("");

    const[tentouAplicarFiltro, setTentouAplicarFiltro] = useState(false);

    function limparFiltros(){
      setIsMunicipioSelecionado(false);
      setMunicipioSelecionado("");
      aoMudarMunicipio("");
      setTentouAplicarFiltro(false);
      isFiltrando(false);
    }

    function AplicarFiltros(){
      if(isMunicipioSelecionado){
        aoMudarMunicipio(municipioSelecionado);
        isFiltrando(true);
        setTentouAplicarFiltro(false);
      }else{
        isFiltrando(false);
        setTentouAplicarFiltro(true);
      }
    }

    function ValidarMunicipioSelecionado(municipio: string){
      if(municipio != ""){
        setIsMunicipioSelecionado(true);
        setMunicipioSelecionado(municipio);
        setTentouAplicarFiltro(false);
      }else{
        setIsMunicipioSelecionado(false);
        setMunicipioSelecionado("");
      }
    }
    async function getMunicipios(){
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/estadual/municipios`, 
              {
                cache: "force-cache" 
              }
            );
            if(requisicao.ok){
              const resposta = await requisicao.json();
              setMunicipios(resposta);
            }
        }catch(error){
            console.error("Erro ao buscar a listagem dos municípios.")
        }
    }

    useEffect(() => {
        getMunicipios()
    },[])

    return (
        <div className="group flex flex-col gap-5 rounded-2xl p-5 md:p-6">
            <div className="flex items-center gap-3">
              <span className="flex h-10 w-10 items-center justify-center rounded-2xl bg-sky-100 text-sky-700">
                <svg viewBox="0 0 24 24" className="h-5 w-5" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
                  <path d="M4 5h16M7 12h10M10 19h4" strokeLinecap="round" />
                </svg>
              </span>
              <div>
                <h1 className="text-lg font-bold text-slate-900">Filtro territorial</h1>
                <p className="text-xs text-slate-500">Selecione o recorte municipal</p>
              </div>
            </div>
            <div className="flex flex-col gap-5">
              <div className="flex flex-col gap-1">
                <label className="text-sm font-semibold text-slate-700" htmlFor="seletor-municipio">
                  Município
                </label>

                <select
                  id="seletor-municipio"
                  className="w-full min-w-0 rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3.5 text-sm text-slate-800 outline-none transition focus:border-sky-500 focus:bg-white focus:ring-4 focus:ring-sky-100"
                  value={municipioSelecionado}
                  onChange={(e) => ValidarMunicipioSelecionado(e.target.value)}
                >
                <option value="">Selecione um município</option>

                {municipios?.["Resposta da Requisição"]?.map((municipio: string) => (
                <option key={municipio} value={municipio}>
                  {municipio}
                </option>
                ))}
                </select>
              </div>

              <div className={`w-full rounded-2xl border border-amber-200 bg-amber-50 p-3 ${tentouAplicarFiltro && !isMunicipioSelecionado ? "visible" : "hidden"}`}>
                <p className="text-center text-sm font-semibold text-amber-800">
                  Nenhum município selecionado
                </p>
              </div>
              
              <div className="grid grid-cols-1 gap-3 sm:grid-cols-2">
                  <button id="botao-aplicar-filtros" onClick={() =>
                {
                  AplicarFiltros();
                }} className="rounded-xl bg-[#061F56] p-3 text-sm font-semibold text-white shadow-md shadow-blue-950/10 transition hover:-translate-y-0.5 hover:bg-[#0A3477]">
                Aplicar Filtros
                </button>
                <button id="botao-limpar-filtros" onClick={() =>
                {
                  limparFiltros();
                }} className="rounded-xl bg-slate-100 p-3 text-sm font-semibold text-slate-600 transition hover:bg-slate-200">
                Limpar Filtros
                </button>
              </div>
              
            </div>
        </div>
    )
}
