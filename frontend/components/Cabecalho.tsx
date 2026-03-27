
export default function Cabecalho() {
    return (
        <header className="flex justify-between items-center p-5 bg-blue-600">
            <div className="flex items-center">
                <img className="h-20 w-30" src="logo.png" alt="" />
                <h1>Mapa Social do Maranhão</h1>
            </div>
            <nav className="flex gap-7">
                <a className=" p-3 bg-white rounded-2xl" href="">Contato</a>
                <a className=" p-3 bg-white rounded-2xl" href="">Equipe</a>
            </nav>
        </header>
    )
}