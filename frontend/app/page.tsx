import Carregamento from "@/components/Carregamento";
import FiltroBusca from "@/components/FiltroBusca";
import SeletorIndicador from "@/components/SeletorIndicadores";

export default function Main() {
  return (
    <>
      <Carregamento />
      <SeletorIndicador />
      <FiltroBusca/>
    </>

  )
}