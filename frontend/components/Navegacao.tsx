import Link from "next/link"
export default function Navegacao() {
    return (
        <div className="flex justify-center items-center">
            <div className="flex justify-center w-fit shadow-xl/20 rounded-2xl bg-sky-600 overflow-hidden">
                <ul>
                    <li className="flex divide-x divide-white text-[20px]">
                        <Link
                            className="hover:bg-red-600 text-white font-bold px-4 py-2"
                            href="/"
                        >
                            Home
                        </Link>
                        <a
                            className="hover:bg-red-600 text-white font-bold px-4 py-2 "
                            href=""
                        >
                            Saúde
                        </a>
                        <Link
                            className="hover:bg-red-600 text-white font-bold px-4 py-2 "
                            href="/educacao"
                        >
                            Educação
                        </Link>
                        <a
                            className="hover:bg-red-600 text-white font-bold px-4 py-2 "
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