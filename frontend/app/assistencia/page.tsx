"use client"

import Cabecalho from "@/components/componentes_principais/Cabecalho"
import Rodape from "@/components/componentes_principais/Rodape"
import FiltroMunicipal from "@/components/componentes_principais/FiltroMunicipal"
import Mapa from "@/components/componentes_principais/Mapa"
import Clusterizacao from "@/components/componentes_graficos/Clusterizacao"
import SeletorIndicadores from "@/components/componentes_principais/SeletorIndicadores"
import SeletorMunicipiosComparacao from "@/components/componentes_principais/SeletorMunicipiosComparacao"
import Graficos from "@/components/componentes_graficos/graficos"
import { useState } from "react"
import CardsResumo from "@/components/cards/CardsResumo"
import CardInformativo from "@/components/componentes_principais/CardInformativo"

type TipoGrafico = "linha" | "barra";

export default function Assistencia(){

    const [tipoGraficoAtivo, setTipoGraficoAtivo] = useState<TipoGrafico | null>(null);
    const [indicadorAtivo, setIndicadorAtivo] = useState("");
    const [isFiltroAssistenciaAplicado, setIsFiltroAssistenciaAplicado] = useState(false);
    const [municipio, setMunicipio] = useState("");
    const [isFiltroMunicipioAplicado, setIsFiltroMunicipioAplicado] = useState(false);
    const [municipiosComparacao, setMunicipiosComparacao] = useState<string[]>([]);
    const [anoPrevisao, setAnoPrevisao] = useState<number | null>(null);

    function atualizarMunicipio(novoMunicipio: string) {
        setMunicipio(novoMunicipio);
        setIsFiltroAssistenciaAplicado(false);
        setTipoGraficoAtivo(null);
        setIndicadorAtivo("");
        setMunicipiosComparacao([]);
        setAnoPrevisao(null);
    }

    return (
        <div className="flex min-h-dvh justify-center bg-slate-50">
            <div className="flex w-full min-w-0 flex-col px-4 sm:px-6 lg:w-[88%] lg:max-w-360 lg:px-0">
                <main className="min-w-0 flex-1">
                    <Cabecalho />
                    <CardInformativo titulo="Assistência Social no Maranhão" descricao="Indicadores que ajudam a enxergar o cenário da assistência social" />



                    <section className="mb-8">
                      <div className="mb-5 flex flex-col gap-2 sm:flex-row sm:items-end sm:justify-between">
                        <div>
                          <span className="text-xs font-bold uppercase tracking-[0.16em] text-sky-700">Etapa 1</span>
                          <h2 className="mt-1 text-2xl font-bold text-slate-900">Explore o território</h2>
                          <p className="mt-1 text-sm text-slate-500">Escolha um município para destacar sua localização e contextualizar os resultados.</p>
                        </div>
                      </div>

                      <div className="grid min-w-0 grid-cols-1 gap-5 lg:grid-cols-3 lg:gap-6">
                      <aside className="col-span-1 min-w-0 h-fit rounded-3xl border border-slate-200 bg-white p-1 shadow-sm">
                        <FiltroMunicipal aoMudarMunicipio={atualizarMunicipio} isFiltrando={setIsFiltroMunicipioAplicado} />
                        <div className="mx-5 mb-5 rounded-2xl bg-slate-50 p-4 text-xs leading-5 text-slate-500">
                          O município selecionado será usado como recorte nos gráficos de evolução.
                        </div>
                      </aside>
                      <article className="col-span-1 min-w-0 rounded-3xl border border-slate-200 bg-white p-3 shadow-sm lg:col-span-2">
                        <div className="flex items-center justify-between gap-3 px-2 pb-3 pt-1">
                          <div>
                            <h3 className="font-bold text-slate-800">Mapa dos municípios</h3>
                            <p className="text-xs text-slate-500">{municipio || "Maranhão"}</p>
                          </div>
                          <span className={`rounded-full px-3 py-1 text-xs font-semibold ${isFiltroMunicipioAplicado ? "bg-sky-100 text-sky-700" : "bg-slate-100 text-slate-600"}`}>
                            {isFiltroMunicipioAplicado ? "Visão Municipal" : "Visão Estadual"}
                          </span>
                        </div>
                        <Mapa municipio={municipio} isFiltrando={isFiltroMunicipioAplicado} isMostrarApenasMapa={true}/>
                      </article>
                      </div>
                    </section>

                    <section className="mb-8">
                      <div className="mb-5">
                        <span className="text-xs font-bold uppercase tracking-[0.16em] text-sky-700">Etapa 2</span>
                        <h2 className="mt-1 text-2xl font-bold text-slate-900">Configure sua análise</h2>
                        <p className="mt-1 text-sm text-slate-500">Selecione o indicador e a visualização mais adequada para a leitura dos dados.</p>
                      </div>
                      <SeletorIndicadores
                        key={municipio || "sem-municipio"}
                        tipoDoIndicador="assistencia_social"
                        municipioSelecionado={isFiltroMunicipioAplicado && municipio !== ""}
                        municipioAtual={municipio}
                        setTipoGraficoAtivo={setTipoGraficoAtivo}
                        setIndicadorAtivo={setIndicadorAtivo}
                        setIsFiltroAplicado={setIsFiltroAssistenciaAplicado}
                        setMunicipiosComparacao={setMunicipiosComparacao}
                        setAnoPrevisao={setAnoPrevisao}
                      />
                    </section>

                    {isFiltroAssistenciaAplicado && (
                    <section className="mb-8 flex flex-col gap-5">
                      <div>
                        <span className="text-xs font-bold uppercase tracking-[0.16em] text-sky-700">Etapa 3</span>
                        <h2 className="mt-1 text-2xl font-bold text-slate-900">Resultados da análise</h2>
                        <p className="mt-1 text-sm text-slate-500">Resumo, evolução histórica e agrupamentos municipais aparecem após a aplicação dos filtros.</p>
                      </div>
                      {tipoGraficoAtivo !== "barra" && (
                        <CardsResumo
                          municipio={municipio}
                          tipo_do_indicador="assistencia_social"
                          isFiltroAplicado={isFiltroAssistenciaAplicado}
                          indicador={indicadorAtivo}
                        />
                      )}
                      {tipoGraficoAtivo === "barra" && (
                        <SeletorMunicipiosComparacao
                          municipioAtual={municipio}
                          municipiosSelecionados={municipiosComparacao}
                          setMunicipiosComparacao={setMunicipiosComparacao}
                        />
                      )}
                      {(tipoGraficoAtivo !== "barra" || municipiosComparacao.length >= 2) && (
                        <Graficos
                          tipoGrafico={tipoGraficoAtivo}
                          isFiltroAplicado={isFiltroAssistenciaAplicado}
                          indicador={indicadorAtivo}
                          tipoDoIndicador="assistencia_social"
                          municipio={municipio}
                          municipiosComparacao={municipiosComparacao}
                          anoPrevisao={anoPrevisao ?? undefined}
                        />
                      )}
                      <Clusterizacao
                        isFiltroAplicado={isFiltroAssistenciaAplicado}
                        indicador={indicadorAtivo}
                        tipoDoIndicador="assistencia_social"
                        municipio={municipio}
                      />
                    </section>
                    )}

                </main>
                <div className="mt-auto border-t border-slate-200 pt-4">
                  <Rodape />
                </div>
            </div>
        </div>
    )
}
