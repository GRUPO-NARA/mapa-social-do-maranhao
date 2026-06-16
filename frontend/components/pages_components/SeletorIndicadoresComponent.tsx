"use client"

import { useState } from 'react';

interface SeletorIndicadoresProps {
    indicadores: string[];
}

export default function SeletorIndicadoresComponent({ indicadores }: SeletorIndicadoresProps) {
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
                >
                    <option value="">Selecione um indicador</option>
                    {indicadores.map((indicador) => (
                        <option key={indicador} value={indicador}>
                            {indicador}
                        </option>
                    ))}
                </select>
              </div>

              
              
              <div className="grid grid-cols-3 gap-4">
                <button id="botao-aplicar-filtros" className="bg-sky-600 text-white p-3 rounded-lg hover:bg-sky-700 transition-colors duration-300 active:bg-sky-800">
                    Aplicar Filtros
                </button>

                <button id="botao-limpar-filtros" className="bg-gray-300 text-gray-700 p-3 rounded-lg hover:bg-gray-400 transition-colors duration-300 active:bg-gray-500 ml-2">
                    Limpar Filtros
                </button>

                <button id="botao-predicao" className="bg-green-600 text-white p-3 rounded-lg hover:bg-green-700 transition-colors duration-300 active:bg-green-800">
                    Previsão
                </button>
              </div>

              <div>
                <p className="text-sm text-gray-500">
                  Selecione um indicador para visualizar os dados correspondentes no mapa.
                </p>
              </div>
              
            </div>
        </div>
    )
}