import Link from "next/link"
export default function SeletorIndicador(){
    return (
        <div className="flex justify-center w-fit shadow-sm rounded-2xl bg-white">
            <ul className="">
                <li className="flex gap-3 rounded-[10px] p-2 " >
                    <Link href="/">Home</Link>
                    <a href="">Saude</a>
                    <Link href="/educacao">Educacao</Link>
                    <a href="">Assistencia Social</a>
                </li>
            </ul>
        </div>
    )
}