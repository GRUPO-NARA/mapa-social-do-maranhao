
import { useState, useEffect } from "react";
import { formatarPibEmBilhoes } from "@/tratamento/formatarNumeros";
import CartaoIndicadorComponent  from "@/components/cards/CardIndicador";

interface IndicadoresPrincipaisProps {
    municipio: string
    isFiltrando?: boolean
}

export default function IndicadoresPrincipaisComponent({municipio, isFiltrando} : IndicadoresPrincipaisProps){

    useEffect(() => {
        if (municipio != "") {
          buscarPibMunicipal()
          buscarIdhMunicipal()
          buscarPopulacaoUrbana()
          buscarPopulacaoRural()
          buscarPopulacaoEmFavelas()
          buscarPibPerCapita()
        }
      }, [municipio]);
      
    const [pibMunicipal, setPibMunicipal] = useState<any>(0);
    const [referenciaPibMunicipal, setReferenciaPibMunicipal] = useState("");
    const [fontePibMunicipal, setFontePibMunicipal] = useState("");
    async function buscarPibMunicipal(){
      try{
        const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/economicos/produtoInternoBruto?municipio=${municipio}`);
        if(requisicao.ok){
            const resposta = await requisicao.json();
            const dadosInternos = resposta?.["Resposta da Requisição"];
            if(dadosInternos){
                const objetoFormatado = JSON.parse(dadosInternos);
                setPibMunicipal(formatarPibEmBilhoes(objetoFormatado["Produto Interno Bruto"].toLocaleString("pt-BR")));
                setReferenciaPibMunicipal(objetoFormatado["Referência dos Dados"]);
                setFontePibMunicipal(objetoFormatado["Fonte dos Dados"]);
            }
        }
      }catch(error){
        console.error("Ocorreu um erro ao buscar dados referentes ao PIB Municipal!");
      }
    }

    const [idhMunicipal, setIdhMunicipal] = useState(0);
    const [referenciaIdhMunicipal, setReferenciaIdhMunicipal] = useState("");
    const [fonteIdhMunicipal, setFonteIdhMunicipal] = useState("");
    async function buscarIdhMunicipal(){
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/idh?municipio=${municipio}`);
            if(requisicao.ok){
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if(dadosInternos){
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setIdhMunicipal(objetoFormatado["Índice de Desenvolvimento Humano"].toLocaleString("pt-BR"));
                    setReferenciaIdhMunicipal(objetoFormatado["Referência dos Dados"]);
                    setFonteIdhMunicipal(objetoFormatado["Fonte dos Dados"]);
                }
            }
        }catch(error){
            console.error("Ocorreu um erro ao buscar dados referentes ao IDH Municipal!");
        }
    }

    const [populacaoRural, setPopulacaoRural] = useState(0);
    const [referenciaPopulacaoRural, setReferenciaPopulacaoRural] = useState("");
    const [fontePopulacaoRural, setFontePopulacaoRural] = useState("");
    async function buscarPopulacaoRural(){
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/quantidadeDeResidentesRurais?municipio=${municipio}`);
            if(requisicao.ok){
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if(dadosInternos){
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setPopulacaoRural(objetoFormatado["Quantidade de Residentes Rurais"].toLocaleString("pt-BR"));
                    setReferenciaPopulacaoRural(objetoFormatado["Referência dos Dados"]);
                    setFontePopulacaoRural(objetoFormatado["Fonte dos Dados"]);
                }
            }
        }catch(error){
            console.error("Ocorreu um erro ao buscar dados referentes à população rural!");
        }  
    }

    const [populacaoUrbana, setPopulacaoUrbana] = useState(0);
    const [referenciaPopulacaoUrbana, setReferenciaPopulacaoUrbana] = useState("");
    const [fontePopulacaoUrbana, setFontePopulacaoUrbana] = useState("");
    async function buscarPopulacaoUrbana(){
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/quantidadeDeResidentesUrbanos?municipio=${municipio}`);
            if(requisicao.ok){
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if(dadosInternos){
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setPopulacaoUrbana(objetoFormatado["Quantidade de Residentes em Área Urbana"].toLocaleString("pt-BR"));
                    setReferenciaPopulacaoUrbana(objetoFormatado["Referência dos Dados"]);
                    setFontePopulacaoUrbana(objetoFormatado["Fonte dos Dados"]);
                }
            }
        }catch(error){
            console.error("Ocorreu um erro ao buscar dados referentes à população urbana!");
        }
    }

    const [populacaoEmFavelas, setPopulacaoEmFavelas] = useState("");
    const [referenciaPopulacaoEmFavelas, setReferenciaPopulacaoEmFavelas] = useState("");
    const [fontePopulacaoEmFavelas, setFontePopulacaoEmFavelas] = useState("");
    async function buscarPopulacaoEmFavelas(){
        setPopulacaoEmFavelas("Dados não disponíveis");
        setReferenciaPopulacaoEmFavelas("");
        setFontePopulacaoEmFavelas("");

        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/demograficos/populacaoEmFavela?municipio=${municipio}`);
            if(requisicao.ok){
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if(!dadosInternos){
                    return;
                }

                const objetoFormatado = JSON.parse(dadosInternos);
                const quantidadeDePessoasEmFavelas = objetoFormatado["Quantidade de Pessoas em Favelas"];
                if(quantidadeDePessoasEmFavelas === null || quantidadeDePessoasEmFavelas === undefined){
                    setReferenciaPopulacaoEmFavelas(objetoFormatado["Referência dos Dados"]);
                    setFontePopulacaoEmFavelas(objetoFormatado["Fonte dos Dados"]);
                    return;
                }

                setPopulacaoEmFavelas(quantidadeDePessoasEmFavelas.toLocaleString("pt-BR"));
                setReferenciaPopulacaoEmFavelas(objetoFormatado["Referência dos Dados"]);
                setFontePopulacaoEmFavelas(objetoFormatado["Fonte dos Dados"]);
            }
        }catch(error){
            console.error("Ocorreu um erro ao buscar dados referentes à população em favelas!");
        }
    }

    const [pibPerCapita, setPibPerCapita] = useState(0);
    const [referenciaPibPerCapita, setReferenciaPibPerCapita] = useState("");
    const [fontePibPerCapita, setFontePibPerCapita] = useState("");
    async function buscarPibPerCapita(){
        try{
            const requisicao = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/economicos/pibPerCapita?municipio=${municipio}`);
            if(requisicao.ok){
                const resposta = await requisicao.json();
                const dadosInternos = resposta?.["Resposta da Requisição"];
                if(dadosInternos){
                    const objetoFormatado = JSON.parse(dadosInternos);
                    setPibPerCapita(objetoFormatado["Produto Interno Bruto per Capita"].toLocaleString("pt-BR"));
                    setReferenciaPibPerCapita(objetoFormatado["Referência dos Dados"]);
                    setFontePibPerCapita(objetoFormatado["Fonte dos Dados"]);
                }
            }
        }catch(error){
            console.error("Ocorreu um erro ao buscar dados referentes ao PIB per Capita!");
        }
    }

    return (
        <section className="group">
            <div className="flex flex-col gap-6">
                <div className="flex items-center gap-2">
                    <span className="h-6 w-1 rounded bg-sky-600" />
                    <h2 className="text-lg font-bold transition-colors duration-300 group-hover:text-sky-800">Indicadores Principais</h2>
                </div>
        
                <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
                    <CartaoIndicadorComponent
                        titulo="PIB Municipal"
                        detalhes={isFiltrando ? `Referência: ${referenciaPibMunicipal || "--"} - Fonte: ${fontePibMunicipal || "--"}` : undefined} 
                        descricao="Produto Interno Bruto do município, em bilhões de reais."
                        valor={pibMunicipal !== undefined && isFiltrando ? `${pibMunicipal} Bilhões` : "--"}
                    />
                    <CartaoIndicadorComponent
                        titulo="IDH Municipal"
                        detalhes={isFiltrando ? `Referência: ${referenciaIdhMunicipal || "--"} - Fonte: ${fonteIdhMunicipal || "--"}` : undefined} 
                        descricao="Índice de Desenvolvimento Humano do município."
                        valor={idhMunicipal !== undefined && isFiltrando ? `${idhMunicipal}` : "--"}
                    />
                    <CartaoIndicadorComponent
                        titulo="População Urbana"
                        detalhes={isFiltrando ? `Referência: ${referenciaPopulacaoUrbana || "--"} - Fonte: ${fontePopulacaoUrbana || "--"}` : undefined} 
                        descricao="Quantidade de residentes em área urbana."
                        valor={populacaoUrbana !== undefined && isFiltrando ? `${populacaoUrbana}` : "--"}
                    />
                    <CartaoIndicadorComponent
                        titulo="População Rural"
                        detalhes={isFiltrando ? `Referência: ${referenciaPopulacaoRural || "--"} - Fonte: ${fontePopulacaoRural || "--"}` : undefined} 
                        descricao="Quantidade de residentes em área rural."
                        valor={populacaoRural !== undefined && isFiltrando ? `${populacaoRural}` : "--"}
                    />
                    <CartaoIndicadorComponent
                        titulo="População em Favelas"
                        detalhes={isFiltrando ? `Referência: ${referenciaPopulacaoEmFavelas || "--"} - Fonte: ${fontePopulacaoEmFavelas || "--"}` : undefined} 
                        descricao="Quantidade de residentes em favelas."
                        valor={populacaoEmFavelas !== undefined && isFiltrando ? `${populacaoEmFavelas}` : "--"}
                    />
                    <CartaoIndicadorComponent
                        titulo="PIB per Capita"
                        detalhes={isFiltrando ? `Referência: ${referenciaPibPerCapita || "--"} - Fonte: ${fontePibPerCapita || "--"}` : undefined} 
                        descricao="Produto Interno Bruto do município dividido pela população."
                        valor={pibPerCapita !== undefined && isFiltrando ? `${pibPerCapita}` : "--"}
                    />
                </div>
            </div>
        </section>
    )

}
