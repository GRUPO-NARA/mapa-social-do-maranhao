"use client";

import Link from "next/link";
import Image from "next/image";
import { usePathname } from "next/navigation";

interface MenuMobileProps {
    menuAberto: boolean;
    setMenuAberto: (aberto: boolean) => void;
}

const links = [
    { href: "/", label: "Home" },
    { href: "/educacao", label: "Educação" },
    { href: "/saude", label: "Saúde" },
    { href: "/assistencia", label: "Assistência Social" },
];

export default function MenuMobileComponent({ menuAberto, setMenuAberto }: MenuMobileProps) {
    const pathname = usePathname();

    return (
        <nav id="menu-mobile" aria-label="Navegação para dispositivos móveis" className={`flex-col gap-4 items-center md:hidden top-0 left-0 w-full h-fit bg-white z-50 p-6 absolute shadow-lg ${menuAberto ? 'flex' : 'hidden'}`}>
            <button className="absolute top-4 right-4 text-gray-500 hover:text-gray-700 focus:outline-none" aria-label="Fechar menu de navegação" onClick={() => setMenuAberto(!menuAberto)}>
                &times;
            </button>
            <Image className="h-20 w-30 object-contain" src="/logo.png" width={120} height={80} alt="Logotipo do Mapa Social do Maranhão" />
            <div className="flex flex-col items-center">
                <h1 className="text-2xl font-bold text-[#061F56]">Mapa Social do Maranhão</h1>
                <p className="text-sm text-[#061F56]">Plataforma de mapeamento social</p>
            </div>
            <ul className="flex flex-col gap-4 w-full">
                {links.map((link) => {
                    const estaAtivo = pathname === link.href;

                    return (
                        <Link
                            key={link.href}
                            className={`flex justify-center text-[#061F56] text-[20px] font-bold p-2 rounded-2xl border border-[#061F56] hover:text-[#790000] ${estaAtivo ? "bg-[#061F56] text-white hover:text-white" : ""}`}
                            href={link.href}
                            onClick={() => setMenuAberto(false)}
                        >
                            {link.label}
                        </Link>
                    );
                })}
            </ul>
        </nav>
    );
}
