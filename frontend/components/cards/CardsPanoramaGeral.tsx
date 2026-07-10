import AjudaIndicadorComponent from "../componentes_principais/AjudaIndicador";

interface CartaoIndicadorPanoramaGeralProps {
    titulo: string;
    descricao: string;
    texto: string;
    valor: string;
    referencia?: string;
    fonte?: string;
    alinhamento: "inicio" | "centro" | "fim";
    categoria: string;
}

export default function CartaoIndicadorPanoramaGeral({
    titulo,
    descricao,
    texto,
    valor,
    referencia,
    fonte,
    alinhamento,
    categoria
}: CartaoIndicadorPanoramaGeralProps) {
    return (
        <div className="group/div flex min-w-0 flex-col items-center gap-1 rounded-2xl border border-slate-200 bg-slate-50 p-3 text-center transition-all duration-300 hover:-translate-y-1 hover:border-sky-300 hover:bg-white hover:shadow-md">
                <span className="rounded bg-linear-to-br from-[#061F56] via-[#0A3A7A] to-sky-600 p-1 text-white text-sm w-full">{categoria}</span>
                <div className="flex items-center justify-center gap-1.5">
                    <h1 className="min-w-0 text-xs leading-tight">{titulo}</h1>
                    <AjudaIndicadorComponent titulo={titulo} texto={texto} alinhamento={alinhamento} />
                </div>
                <p className="max-w-full break-words text-lg font-bold leading-tight text-gray-800">{valor}</p>
                <p className="text-xs text-gray-600">{descricao}</p>
                <p className="text-xs text-gray-500">Referência: {referencia}</p>
                <p className="text-xs text-gray-500">Fonte: {fonte}</p> 
        </div>
    )
} 
