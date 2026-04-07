import Cabecalho from "@/components/Cabecalho";
import Carregamento from "@/components/Carregamento";
import Navegacao from "@/components/NavegacaoEntreIndicadores";
import SeletorIndicador from "@/components/NavegacaoEntreIndicadores";

export default function Educacao(){
    return (
        <div className="flex flex-col gap-10">
            <Carregamento/>
            <Cabecalho/>
            <Navegacao />
        </div>
        
        
    )
}