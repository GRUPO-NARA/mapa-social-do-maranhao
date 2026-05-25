import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  allowedDevOrigins: ["192.168.0.50"],
  async rewrites(){
    return [
      {
        "source" : "/api/v1/municipios/lista",
        "destination" : `${process.env.URL_BASE}${process.env.URL_LISTAGEM_MUNICIPIOS}`
      },
      {
        "source" : "/api/v1/municipios/populacao",
        "destination" : `${process.env.URL_BASE}${process.env.URL_POPULACAO_TOTAL_MUNICIPIOS}`
      },
      {
        "source" : "/api/v1/estado/populacao",
        "destination" : `${process.env.URL_BASE}${process.env.URL_POPULACAO_TOTAL_ESTADO}`
      },
      {
        "source" : "/api/v1/municipios/quantidadeHomens",
        "destination" : `${process.env.URL_BASE}${process.env.URL_QUANTIDADE_HOMENS_MUNICIPIOS}`
      },
      {
        "source" : "/api/v1/municipios/quantidadeMulheres",
        "destination" : `${process.env.URL_BASE}${process.env.URL_QUANTIDADE_MULHERES_MUNICIPIOS}`
      },
      {
        "source" : "/api/v1/municipios/areaTotal",
        "destination" : `${process.env.URL_BASE}${process.env.URL_AREA_TOTAL_MUNICIPIOS}`
      },
      {
        "source" : "/api/v1/municipios/densidadeDemografica",
        "destination" : `${process.env.URL_BASE}${process.env.URL_DENSIDADE_DEMOGRAFICA_MUNICIPIOS}`
      },
      {
        "source" : "/api/v1/municipios/pib",
        "destination" : `${process.env.URL_BASE}${process.env.URL_PIB_MUNICIPIOS}`
      },
      {
        "source" : "/api/v1/municipios/idh",
        "destination" : `${process.env.URL_BASE}${process.env.URL_IDH_MUNICIPIOS}`
      },
      {
        "source" : "/api/v1/municipios/evolucaoIdh",
        "destination" : `${process.env.URL_BASE}${process.env.URL_EVOLUCAO_IDH_MUNICIPIOS}`
      },
      {
        "source" : "/api/v1/estado/pibAgregado",
        "destination" : `${process.env.URL_BASE}${process.env.URL_PIB_AGREGADO_ESTADO}`
      }
    ]
  }
};



export default nextConfig;
