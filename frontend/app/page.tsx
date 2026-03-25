import Carregamento from "@/components/Carregamento";
import FiltroBusca from "@/components/FiltroBusca";
import Indicadores from "@/components/Indicadores";
import SeletorIndicador from "@/components/SeletorIndicadores";
import Teste from "@/components/TesteAPI";

export default function Main() {
  return (

    <>
      <Carregamento />
      <SeletorIndicador />
      <FiltroBusca />
      <Indicadores />
    </>


  )
}