"use client"

import { useState } from "react";

type TipoGrafico = "linha" | "barra";

interface SeletorIndicadoresProps {
    indicadores: string[];
    setTipoGraficoAtivo?: (tipo: TipoGrafico | null) => void;
}

export default function SeletorIndicadoresComponent({ indicadores, setTipoGraficoAtivo }: SeletorIndicadoresProps) {
    const [isIndicadorSelecionado, setIsIndicadorSelecionado] = useState(false);
    const [tentouAplicarFiltro, setTentouAplicarFiltro] = useState(false);
    const [indicadorSelecionado, setIndicadorSelecionado] = useState("");
    const [tipoGraficoSelecionado, setTipoGraficoSelecionado] = useState<TipoGrafico | null>(null);

    const referenciasDoIndicador = [
      2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009
    ];

    function selecionarTipoGrafico(tipo: TipoGrafico) {
      setTipoGraficoSelecionado(tipo);
      setTipoGraficoAtivo?.(tipo);
    }

    function limparFiltros() {
      setIndicadorSelecionado("");
      setIsIndicadorSelecionado(false);
      setTentouAplicarFiltro(false);
      setTipoGraficoSelecionado(null);
      setTipoGraficoAtivo?.(null);
    }

    return (
        <div className="group flex flex-col md:p-6 gap-4 rounded-2xl">
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
                  className="rounded-lg shadow-sm p-2 bg-sky-700 text-white"
                  value={indicadorSelecionado}
                  onChange={(e) => {
                    const novoIndicador = e.target.value;
                    setIndicadorSelecionado(novoIndicador);
                    setTentouAplicarFiltro(false);
                    setIsIndicadorSelecionado(novoIndicador !== "");

                    if (novoIndicador === "") {
                      setTipoGraficoSelecionado(null);
                      setTipoGraficoAtivo?.(null);
                    }
                  }}
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
                <div className="flex flex-col gap-1">
                  <label className="text-[15px]" htmlFor="seletor-referencia-indicador">
                    Referencias do Indicador
                  </label>

                  <select
                    id="seletor-referencia-indicador"
                    className="rounded-lg shadow-sm p-2 bg-sky-700 text-white"
                  >
                      <option value="">Selecione uma referencia</option>
                      {referenciasDoIndicador.map((referencia) => (
                          <option key={referencia} value={referencia}>
                              {referencia}
                          </option>
                      ))}
                  </select>
                </div>
              )}
              
              {isIndicadorSelecionado && (
                <div className="w-full bg-gray-300 rounded-lg p-3 flex flex-col items-center justify-center gap-3">
                  <p className="text-center text-gray-700 font-semibold">
                    Selecione qual tipo de grafico deseja visualizar
                  </p>
                  <fieldset className="flex gap-4">
                    <legend className="sr-only">Tipo de Grafico</legend>
                    <div className="flex items-center gap-3">
                      <button
                        type="button"
                        className={`px-3 py-2 rounded-lg text-white cursor-pointer transition-colors duration-300 ${tipoGraficoSelecionado === "linha" ? "bg-sky-900" : "bg-sky-700 hover:bg-sky-800"}`}
                        onClick={() => selecionarTipoGrafico("linha")}
                      >
                        Grafico de Linha
                      </button>
                      <button
                        type="button"
                        className={`px-3 py-2 rounded-lg text-white cursor-pointer transition-colors duration-300 ${tipoGraficoSelecionado === "barra" ? "bg-sky-900" : "bg-sky-700 hover:bg-sky-800"}`}
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
              
              <div className="grid grid-cols-2 gap-4">
                <button
                  id="botao-aplicar-filtros"
                  className="bg-sky-600 text-white p-3 rounded-lg hover:bg-sky-700 transition-colors duration-300 active:bg-sky-800"
                  onClick={() => {
                    setTentouAplicarFiltro(true);
                  }}
                >
                    Aplicar Filtros
                </button>

                <button
                  id="botao-limpar-filtros"
                  className="bg-gray-300 text-gray-700 p-3 rounded-lg hover:bg-gray-400 transition-colors duration-300 active:bg-gray-500 ml-2"
                  onClick={limparFiltros}
                >
                    Limpar Filtros
                </button>
              </div>
                
              <div>
                {isIndicadorSelecionado && referenciasDoIndicador.length > 3 && tentouAplicarFiltro && tipoGraficoSelecionado === "linha" && (
                  <button className="w-full bg-green-500 text-white p-3 rounded-lg hover:bg-green-600 transition-colors duration-300 active:bg-green-700">
                    Ver Previsao
                  </button>
                )}
              </div>
            </div>
        </div>
    )
}
