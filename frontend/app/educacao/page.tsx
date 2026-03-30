import Cabecalho from "@/components/Cabecalho";
import Carregamento from "@/components/Carregamento";
import Navegacao from "@/components/Navegacao";
import SeletorIndicador from "@/components/Navegacao";

export default function Educacao(){
    return (
        <div className="flex flex-col gap-10">
            <Carregamento/>
            <Cabecalho/>
            <Navegacao />
        </div>
        
        
    )
}