import Link from "next/link"
export default function NavegacaoEntreIndicadores() {
    return (
        <div className="flex justify-center items-center">
            <div className="flex justify-center w-fit shadow-xl/20 rounded-2xl bg-sky-700 overflow-hidden">
                <ul>
                    <li className="flex divide-x divide-white">
                        <Link
                            className="hover:bg-red-600 text-white font-bold px-4 py-2 transition-colors duration-300 "
                            href="/"
                        >
                            Home
                        </Link>
                        <a
                            className="hover:bg-red-600 text-white font-bold px-4 py-2 transition-colors duration-300"
                            href=""
                        >
                            Saúde
                        </a>
                        <Link
                            className="hover:bg-red-600 text-white font-bold px-4 py-2 transition-colors duration-300"
                            href="/educacao"
                        >
                            Educação
                        </Link>
                        <a
                            className="hover:bg-red-600 text-white font-bold px-4 py-2 transition-colors duration-300"
                            href=""
                        >
                            Assistência Social
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    )
}