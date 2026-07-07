"use client"

import HeaderComponent from "@/components/componentes_principais/Cabecalho"
import FooterComponent from "@/components/componentes_principais/Rodape"
import FiltroComponent from "@/components/componentes_principais/FiltroMunicipal"
import MapaComponent from "@/components/componentes_principais/MapaComponent"
import ClusterizacaoComponent from "@/components/componentes_graficos/Clusterizacao"
import SeletorIndicadoresComponent from "@/components/componentes_principais/SeletorIndicadores"
import GraficoLinhaComponent from "@/components/componentes_graficos/Predicao"
import { useState } from "react"
import CardsResumoComponent from "@/components/cards/CardsResumo"
import CardInformativoComponent from "@/components/componentes_principais/CardInformativo"
type TipoGrafico = "linha" | "barra";

export default function Educacao(){

    const [tipoGraficoAtivo, setTipoGraficoAtivo] = useState<TipoGrafico | null>(null);
    const [indicadorAtivo, setIndicadorAtivo] = useState("");
    const [isFiltroEducacaoAplicado, setIsFiltroEducacaoAplicado] = useState(false);
    const [municipio, setMunicipio] = useState("");
    const [isFiltroMunicipioAplicado, setIsFiltroMunicipioAplicado] = useState(false);
    const [municipiosComparacao, setMunicipiosComparacao] = useState<string[]>([]);
    const [anoPrevisao, setAnoPrevisao] = useState<number | null>(null);

    function atualizarMunicipio(novoMunicipio: string) {
        setMunicipio(novoMunicipio);
        setIsFiltroEducacaoAplicado(false);
        setTipoGraficoAtivo(null);
        setIndicadorAtivo("");
        setMunicipiosComparacao([]);
        setAnoPrevisao(null);
    }

    return (
        <div className="flex min-h-screen justify-center bg-slate-50">
                <main className="h-full w-full min-w-0 px-4 sm:px-6 lg:w-[88%] lg:max-w-360 lg:px-0">
                    <HeaderComponent />
                    <CardInformativoComponent titulo="Educação no Maranhão" descricao="Indicadores que ajudam a enxergar o cenário educacional" />
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
                        <FiltroComponent aoMudarMunicipio={atualizarMunicipio} isFiltrando={setIsFiltroMunicipioAplicado} />
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
                        key={municipio || "sem-municipio"}
                        tipoDoIndicador="educacao"
                        municipioSelecionado={isFiltroMunicipioAplicado && municipio !== ""}
                        setTipoGraficoAtivo={setTipoGraficoAtivo}
                        setIndicadorAtivo={setIndicadorAtivo}
                        setIsFiltroAplicado={setIsFiltroEducacaoAplicado}
                        setMunicipiosComparacao={setMunicipiosComparacao}
                        setAnoPrevisao={setAnoPrevisao}
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
                          tipo_do_indicador="educacao"
                          isFiltroAplicado={isFiltroEducacaoAplicado}
                          indicador={indicadorAtivo}
                        />
                      )}
                      <GraficoLinhaComponent
                        tipoGrafico={tipoGraficoAtivo}
                        isFiltroAplicado={isFiltroEducacaoAplicado}
                        indicador={indicadorAtivo}
                        tipoDoIndicador="educacao"
                        municipio={municipio}
                        municipiosComparacao={municipiosComparacao}
                        anoPrevisao={anoPrevisao ?? undefined}
                      />
                      {isFiltroEducacaoAplicado && (
                        <ClusterizacaoComponent
                          isFiltroAplicado={isFiltroEducacaoAplicado}
                          indicador={indicadorAtivo}
                          tipoDoIndicador="educacao"
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
