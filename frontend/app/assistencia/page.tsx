"use client"

import HeaderComponent from "@/components/HeaderComponent"
import FooterComponent from "@/components/FooterComponent"
import FiltroComponent from "@/components/FiltroComponent"
import MapaComponent from "@/components/MapaComponent"
import ClusterizacaoComponent from "@/components/pages_components/ClusterizacaoComponent"
import SeletorIndicadoresComponent from "@/components/pages_components/SeletorIndicadoresComponent"
import GraficoLinhaComponent from "@/components/pages_components/GraficosComponent"
import { useState } from "react"
import CardsResumoComponent from "@/components/pages_components/CardsResumoComponent"

type TipoGrafico = "linha" | "barra";

export default function Assistencia(){

    const [tipoGraficoAtivo, setTipoGraficoAtivo] = useState<TipoGrafico | null>(null);
    const [indicadorAtivo, setIndicadorAtivo] = useState("");
    const [isFiltroAssistenciaAplicado, setIsFiltroAssistenciaAplicado] = useState(false);
    const [municipio, setMunicipio] = useState("");
    const [isFiltroMunicipioAplicado, setIsFiltroMunicipioAplicado] = useState(false);
    const [municipiosComparacao, setMunicipiosComparacao] = useState<string[]>([]);

    return (
        <div className="flex min-h-screen justify-center bg-slate-50">
                <main className="h-full w-full min-w-0 px-4 sm:px-6 lg:w-[88%] lg:max-w-[1440px] lg:px-0">
                    <HeaderComponent />

                    <section className="relative mb-8 overflow-hidden rounded-[2rem] bg-gradient-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 px-6 py-8 text-white shadow-xl shadow-sky-950/10 sm:px-10 sm:py-10">
                      <div className="absolute -right-16 -top-20 h-64 w-64 rounded-full bg-white/10 blur-2xl" aria-hidden="true" />
                      <div className="absolute -bottom-24 right-1/3 h-56 w-56 rounded-full bg-cyan-300/10 blur-3xl" aria-hidden="true" />
                      <div className="relative grid items-end gap-8 lg:grid-cols-[1.5fr_1fr]">
                        <div className="max-w-3xl">
                          <span className="mb-5 inline-flex items-center gap-2 rounded-full border border-white/20 bg-white/10 px-3 py-1.5 text-xs font-semibold uppercase tracking-[0.16em] text-sky-100 backdrop-blur-sm">
                            <span className="h-2 w-2 rounded-full bg-cyan-300" />
                            Assistência Social no Maranhão
                          </span>
                          <h1 className="text-3xl font-bold leading-tight sm:text-4xl lg:text-5xl">
                            Indicadores que ajudam a enxergar o cenário da assistência social no estado e nos municípios
                          </h1>
                          <p className="mt-4 max-w-2xl text-sm leading-6 text-sky-100 sm:text-base">
                            Selecione um município, acompanhe a evolução dos indicadores e compare perfis de desempenho para apoiar análises e decisões públicas.
                          </p>
                        </div>
                        <div className="grid grid-cols-2 gap-3 sm:max-w-lg lg:justify-self-end">
                          <div className="rounded-2xl border border-white/15 bg-white/10 p-4 backdrop-blur-sm">
                            <p className="text-2xl font-bold">217</p>
                            <p className="mt-1 text-xs text-sky-100">municípios analisáveis</p>
                          </div>
                          <div className="rounded-2xl border border-white/15 bg-white/10 p-4 backdrop-blur-sm">
                            <p className="text-2xl font-bold">Séries</p>
                            <p className="mt-1 text-xs text-sky-100">históricas e comparativas</p>
                          </div>
                        </div>
                      </div>
                    </section>

                    <section className="mb-8">
                      <div className="mb-5 flex flex-col gap-2 sm:flex-row sm:items-end sm:justify-between">
                        <div>
                          <span className="text-xs font-bold uppercase tracking-[0.16em] text-sky-700">Etapa 1</span>
                          <h2 className="mt-1 text-2xl font-bold text-slate-900">Explore o território</h2>
                          <p className="mt-1 text-sm text-slate-500">Escolha um município para destacar sua localização e contextualizar os resultados.</p>
                        </div>
                        <span className="w-fit rounded-full bg-sky-100 px-3 py-1.5 text-xs font-semibold text-sky-800">Visão municipal</span>
                      </div>

                      <div className="grid min-w-0 grid-cols-1 gap-5 lg:grid-cols-3 lg:gap-6">
                      <aside className="col-span-1 min-w-0 rounded-3xl border border-slate-200 bg-white p-1 shadow-sm">
                        <FiltroComponent aoMudarMunicipio={setMunicipio} isFiltrando={setIsFiltroMunicipioAplicado} />
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
                          <span className={`rounded-full px-3 py-1 text-xs font-semibold ${isFiltroMunicipioAplicado ? "bg-emerald-100 text-emerald-700" : "bg-slate-100 text-slate-600"}`}>
                            {isFiltroMunicipioAplicado ? "Filtro aplicado" : "Visão estadual"}
                          </span>
                        </div>
                        <MapaComponent municipio={municipio} isFiltrando={isFiltroMunicipioAplicado} isMostrarApenasMapa={true}/>
                      </article>
                      </div>
                    </section>

                    <section className="mb-8">
                      <div className="mb-5">
                        <span className="text-xs font-bold uppercase tracking-[0.16em] text-sky-700">Etapa 2</span>
                        <h2 className="mt-1 text-2xl font-bold text-slate-900">Configure sua análise</h2>
                        <p className="mt-1 text-sm text-slate-500">Selecione o indicador e a visualização mais adequada para a leitura dos dados.</p>
                      </div>
                      <SeletorIndicadoresComponent
                        tipoDoIndicador="assistencia_social"
                        setTipoGraficoAtivo={setTipoGraficoAtivo}
                        setIndicadorAtivo={setIndicadorAtivo}
                        setIsFiltroAplicado={setIsFiltroAssistenciaAplicado}
                        setMunicipiosComparacao={setMunicipiosComparacao}
                      />
                    </section>

                    <section className="mb-8 flex flex-col gap-5">
                      <div>
                        <span className="text-xs font-bold uppercase tracking-[0.16em] text-sky-700">Etapa 3</span>
                        <h2 className="mt-1 text-2xl font-bold text-slate-900">Resultados da análise</h2>
                        <p className="mt-1 text-sm text-slate-500">Resumo, evolução histórica e agrupamentos municipais aparecem após a aplicação dos filtros.</p>
                      </div>
                      {tipoGraficoAtivo !== "barra" && (
                        <CardsResumoComponent
                          municipio={municipio}
                          tipo_do_indicador="assistencia_social"
                          isFiltroAplicado={isFiltroAssistenciaAplicado}
                          indicador={indicadorAtivo}
                        />
                      )}
                      <GraficoLinhaComponent
                        tipoGrafico={tipoGraficoAtivo}
                        isFiltroAplicado={isFiltroAssistenciaAplicado}
                        indicador={indicadorAtivo}
                        tipoDoIndicador="assistencia_social"
                        municipio={municipio}
                        municipiosComparacao={municipiosComparacao}
                      />
                      {isFiltroAssistenciaAplicado && (
                        <ClusterizacaoComponent
                          isFiltroAplicado={isFiltroAssistenciaAplicado}
                          indicador={indicadorAtivo}
                          municipio={municipio}
                        />
                      )}
                    </section>

                    <div className="mt-12 border-t border-slate-200 pt-4">
                      <FooterComponent />
                    </div>
                </main>
        </div>
    )
}
