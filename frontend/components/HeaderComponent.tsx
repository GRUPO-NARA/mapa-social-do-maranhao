import Link from 'next/link';
import MenuMobileComponent from './MenuMobileComponent';
import { useState } from 'react';

export default function HeaderComponent(){
    const [menuAberto, setMenuAberto] = useState(false);
    return (
        <header className="flex justify-between items-center p-6 text-white">     
            <div className="flex items-center">
                <img className="h-20 w-30" src="logo.png" alt="" />
                <div>
                    <h1 className="text-2xl font-bold text-[#061F56]">Mapa Social do Maranhão</h1>
                    <p className="text-sm text-[#061F56] hidden md:block">Plataforma de mapeamento social</p>
                </div>
            </div>
            <ul className="flex gap-4 items-center hidden invisible md:block md:visible">
                <Link className="text-[#061F56] text-[20px] font-bold p-2 rounded-2xl hover:text-[#790000]" href="/">Home</Link>
                <span className="text-[#061F56]">|</span>
                <Link className="text-[#061F56] text-[20px] font-bold p-2 rounded-2xl hover:text-[#790000]" href="/educacao">Educação</Link>
                <span className="text-[#061F56]">|</span>
                <Link className="text-[#061F56] text-[20px] font-bold p-2 rounded-2xl hover:text-[#790000]" href="/saude">Saúde</Link>
                <span className="text-[#061F56]">|</span>
                <Link className="text-[#061F56] text-[20px] font-bold p-2 rounded-2xl hover:text-[#790000]" href="/assistencia">Assistência Social</Link>
            </ul>
            <div className="flex items-center gap-4 md:hidden">
                <button className="text-[#061F56] text-[20px] font-bold p-2 rounded-2xl hover:text-[#790000] md:hidden" onClick={() => {
                    setMenuAberto(!menuAberto);
                }}>
                    ☰
                </button>
            </div>
            <MenuMobileComponent menuAberto={menuAberto} setMenuAberto={setMenuAberto} />
            
            

        </header>
    )
}