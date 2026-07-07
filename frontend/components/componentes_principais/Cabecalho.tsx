'use client';

import Link from 'next/link';
import Image from 'next/image';
import MenuMobileComponent from './MenuMobile';
import AcessibilidadeComponent from './Acessibilidade';
import { useState } from 'react';

export default function HeaderComponent(){
    const [menuAberto, setMenuAberto] = useState(false);
    return (
        <header className="flex justify-between items-center p-6 text-white">     
            <div className="flex items-center">
                <Link href="/" aria-label="Ir para a página inicial">
                    <Image className="h-20 w-30 object-contain" src="/logo.png" width={120} height={80} alt="Logotipo do Mapa Social do Maranhão" priority />
                </Link>
                <div>
                    <h1 className="font-display text-2xl font-bold text-[#061F56]">Mapa Social do Maranhão</h1>
                    <p className="font-snas text-sm text-[#061F56] hidden md:block">Plataforma de mapeamento social</p>
                </div>
            </div>
            <nav aria-label="Navegação principal" className="hidden items-center gap-4 md:flex">
                <Link className="text-[#061F56] text-xl font-display font-bold p-2 rounded-2xl hover:text-[#790000]" href="/">Home</Link>
                <span className="text-[#061F56]" aria-hidden="true">|</span>
                <Link className="text-[#061F56] text-xl font-display font-bold p-2 rounded-2xl hover:text-[#790000]" href="/educacao">Educação</Link>
                <span className="text-[#061F56]" aria-hidden="true">|</span>
                <Link className="text-[#061F56] text-xl font-display font-bold p-2 rounded-2xl hover:text-[#790000]" href="/saude">Saúde</Link>
                <span className="text-[#061F56]" aria-hidden="true">|</span>
                <Link className="text-[#061F56] text-xl font-display font-bold p-2 rounded-2xl hover:text-[#790000]" href="/assistencia">Assistência Social</Link>
            </nav>
            <div className="flex items-center gap-4 md:hidden">
                <button className="text-[#061F56] text-[20px] font-display font-bold p-2 rounded-2xl hover:text-[#790000] md:hidden" aria-label="Abrir menu de navegação" aria-expanded={menuAberto} aria-controls="menu-mobile" onClick={() => {
                    setMenuAberto(!menuAberto);
                }}>
                    ☰
                </button>
            </div>
            <MenuMobileComponent menuAberto={menuAberto} setMenuAberto={setMenuAberto} />
            <AcessibilidadeComponent />
            
            

        </header>
    )
}
