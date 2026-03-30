
export default function Cabecalho() {
    return (
        <header className="flex justify-between items-center p-5 bg-gradient-to-r from-sky-600 to-red-600 text-white shadow-xl/20  ">     
            <div className="flex items-center">
                <img className="h-20 w-30" src="logo.png" alt="" />
                <div>
                    <h1 className="text-2xl font-bold">Mapa Social do Maranhão</h1>
                    <p className="text-sm">Plataforma de mapeamento social</p>
                </div>
            </div>
        </header>
    )
}