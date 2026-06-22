"use client"

import { useState } from "react";

type TipoGrafico = "linha" | "barra";

interface SeletorIndicadoresProps {
    indicadores: string[];
    setTipoGraficoAtivo?: (tipo: TipoGrafico | null) => void;
    setIndicadorAtivo?: (indicador: string) => void;
    setIsFiltroAplicado?: (isAplicado: boolean) => void;
}

export default function SeletorIndicadoresComponent({
    indicadores,
    setTipoGraficoAtivo,
    setIndicadorAtivo,
    setIsFiltroAplicado,
}: SeletorIndicadoresProps) {
    const [isIndicadorSelecionado, setIsIndicadorSelecionado] = useState(false);
    const [tentouAplicarFiltro, setTentouAplicarFiltro] = useState(false);
    const [indicadorSelecionado, setIndicadorSelecionado] = useState("");
    const [tipoGraficoSelecionado, setTipoGraficoSelecionado] = useState<TipoGrafico | null>(null);
    const [anoPrevisao, setAnoPrevisao] = useState(2026);

    const referenciasDoIndicador = [
      2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009
    ];

    function selecionarTipoGrafico(tipo: TipoGrafico) {
      setTipoGraficoSelecionado(tipo);
      setIsFiltroAplicado?.(false);
      setTipoGraficoAtivo?.(null);
    }

    function selecionarIndicador(novoIndicador: string) {
      setIndicadorSelecionado(novoIndicador);
      setTentouAplicarFiltro(false);
      setIsIndicadorSelecionado(novoIndicador !== "");
      setIsFiltroAplicado?.(false);
      setTipoGraficoAtivo?.(null);
      setIndicadorAtivo?.("");

      if (novoIndicador === "") {
        setTipoGraficoSelecionado(null);
      }
    }

    function aplicarFiltros() {
      setTentouAplicarFiltro(true);
      if (isIndicadorSelecionado && tipoGraficoSelecionado) {
        setTipoGraficoAtivo?.(tipoGraficoSelecionado);
        setIndicadorAtivo?.(indicadorSelecionado);
        setIsFiltroAplicado?.(true);
      } else {
        setTipoGraficoAtivo?.(null);
        setIndicadorAtivo?.("");
        setIsFiltroAplicado?.(false);
      }
    }

    function limparFiltros() {
      setIndicadorSelecionado("");
      setIsIndicadorSelecionado(false);
      setTentouAplicarFiltro(false);
      setTipoGraficoSelecionado(null);
      setAnoPrevisao(2026);
      setTipoGraficoAtivo?.(null);
      setIndicadorAtivo?.("");
      setIsFiltroAplicado?.(false);
    }

    return (
        <section className="group col-span-1 flex min-w-0 flex-col gap-4 rounded-2xl sm:p-4 md:col-span-3 md:p-6">
            <div className="flex items-center gap-2">
              <p className="w-1 h-6 rounded bg-sky-600"></p>
              <h1 className="text-lg font-bold group-hover:text-sky-800 transition-colors duration-300">Indicadores Municipais</h1>
            </div>
            <div className="flex flex-col gap-5">
              <div className="flex flex-col gap-1">
                <label className="text-[15px]" htmlFor="seletor-indicador">
                  Indicador
                </label>

                <select
                  id="seletor-indicador"
                  className="w-full min-w-0 rounded-lg bg-sky-700 p-2 text-sm text-white shadow-sm sm:text-base"
                  value={indicadorSelecionado}
                  onChange={(e) => selecionarIndicador(e.target.value)}
                >
                    <option value="">Selecione um indicador</option>
                    {indicadores.map((indicador) => (
                        <option key={indicador} value={indicador}>
                            {indicador}
                        </option>
                    ))}
                </select>
              </div>
              

              
              {isIndicadorSelecionado && (
                <div className="flex w-full min-w-0 flex-col items-center justify-center gap-3 rounded-lg bg-gray-300 p-3">
                  <p className="text-center text-gray-700 font-semibold">
                    Selecione qual tipo de grafico deseja visualizar
                  </p>
                  <fieldset className="w-full min-w-0">
                    <legend className="sr-only">Tipo de Grafico</legend>
                    <div className="grid w-full grid-cols-1 gap-3 sm:grid-cols-2">
                      <button
                        type="button"
                        className={`w-full rounded-lg px-3 py-2 text-sm text-white cursor-pointer transition-colors duration-300 sm:text-base ${tipoGraficoSelecionado === "linha" ? "bg-sky-900" : "bg-sky-700 hover:bg-sky-800"}`}
                        onClick={() => selecionarTipoGrafico("linha")}
                      >
                        Grafico de Linha
                      </button>
                      <button
                        type="button"
                        className={`w-full rounded-lg px-3 py-2 text-sm text-white cursor-pointer transition-colors duration-300 sm:text-base ${tipoGraficoSelecionado === "barra" ? "bg-sky-900" : "bg-sky-700 hover:bg-sky-800"}`}
                        onClick={() => selecionarTipoGrafico("barra")}
                      >
                        Grafico de Barras
                      </button>
                    </div>
                  </fieldset>
                </div>
              )}

              <div className={`w-full bg-yellow-500 rounded-lg p-2 ${tentouAplicarFiltro && !isIndicadorSelecionado ? "visible" : "hidden"}`}>
                  <p className="text-center text-gray-700 font-semibold">
                    Nenhum indicador selecionado. Por favor, selecione um indicador para aplicar os filtros.
                  </p>
              </div>

              <div className={`w-full bg-yellow-500 rounded-lg p-2 ${tentouAplicarFiltro && isIndicadorSelecionado && !tipoGraficoSelecionado ? "visible" : "hidden"}`}>
                  <p className="text-center text-gray-700 font-semibold">
                    Selecione o tipo de grafico para visualizar os dados.
                  </p>
              </div>

              <div className={`w-full bg-gray-300 rounded-lg p-2 ${tentouAplicarFiltro && referenciasDoIndicador.length > 3 && isIndicadorSelecionado && tipoGraficoSelecionado === "linha" ? "visible" : "hidden"}`}>
                  <p className="text-center text-gray-700 font-semibold">
                    O sistema de previsao e habilitado para este indicador. Se quiser ver a previsao, use a visualizacao em linha.
                  </p>
              </div>
              
              <div className="grid grid-cols-1 gap-3 sm:grid-cols-2 sm:gap-4">
                <button
                  id="botao-aplicar-filtros"
                  className="w-full rounded-lg bg-sky-600 p-3 text-white transition-colors duration-300 hover:bg-sky-700 active:bg-sky-800"
                  onClick={aplicarFiltros}
                >
                    Aplicar Filtros
                </button>

                <button
                  id="botao-limpar-filtros"
                  className="w-full rounded-lg bg-gray-300 p-3 text-gray-700 transition-colors duration-300 hover:bg-gray-400 active:bg-gray-500"
                  onClick={limparFiltros}
                >
                    Limpar Filtros
                </button>
              </div>
                
              <div>
                {isIndicadorSelecionado && referenciasDoIndicador.length > 3 && tentouAplicarFiltro && tipoGraficoSelecionado === "linha" && (
                  <div className="flex flex-col gap-3 rounded-lg bg-white p-3 border border-sky-300">
                    <div className="flex items-center justify-between gap-3">
                      <label className="text-sm font-semibold text-gray-700" htmlFor="ano-previsao">
                        Ano da previsao
                      </label>
                      <span className="rounded bg-green-100 px-2 py-1 text-sm font-bold text-green-800">
                        {anoPrevisao}
                      </span>
                    </div>
                    <input
                      id="ano-previsao"
                      type="range"
                      min="2026"
                      max="2040"
                      step="1"
                      value={anoPrevisao}
                      onChange={(e) => setAnoPrevisao(Number(e.target.value))}
                      className="w-full accent-sky-600"
                    />
                    <button className="w-full bg-sky-500 text-white p-3 rounded-lg hover:bg-sky-600 transition-colors duration-300 active:bg-sky-700">
                      Ver Previsao para {anoPrevisao}
                    </button>
                  </div>
                )}
              </div>
            </div>
        </section>
    )
}
