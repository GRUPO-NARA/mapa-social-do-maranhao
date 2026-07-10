const nomesOficiais: Record<string, string> = {
    taxa_de_aprovacao_no_ensino_medio: "Taxa de aprovação no ensino médio",
    taxa_de_aprovacao_no_ensino_fundamental: "Taxa de aprovação no ensino fundamental",
    taxa_de_analfabetismo_15_anos_ou_mais: "Taxa de analfabetismo (15 anos ou mais)",
    pib_municipal: "PIB municipal",
    pib_per_capita: "PIB per capita",
    idh_municipal: "IDH municipal",
    densidade_demografica: "Densidade demográfica",
    area_territorial: "Área territorial",
    populacao_urbana: "População urbana",
    populacao_rural: "População rural",
    populacao_em_favelas: "População em favelas",
    razao_mortalidade_materna: "Razão de mortalidade materna",
    mortalidade_infantil: "Mortalidade infantil",
    nascidos_vivos_de_maes_adolescentes: "Nascidos vivos de mães adolescentes",
    domicilios_com_agua_encanada: "Domicílios com água encanada",
    domicilios_com_energia_eletrica: "Domicílios com energia elétrica",
    total_de_familias_inscritas_no_cadastro_unico: "Total de famílias inscritas no Cadastro Único",
    familias_em_situacao_de_rua_inscritas_cadastro_unico: "Famílias em situação de rua inscritas no Cadastro Único",
    familias_em_situacao_de_trabalho_infantil_cadastro_unico: "Famílias em situação de trabalho infantil no Cadastro Único",
    programa_auxilio_gas_para_brasileiros: "Programa Auxílio Gás dos Brasileiros",
};

const termosComAcento: Record<string, string> = {
    agua: "água",
    aguas: "águas",
    alfabetizacao: "alfabetização",
    aprovacao: "aprovação",
    avaliacao: "avaliação",
    auxilio: "auxílio",
    basica: "básica",
    cidadao: "cidadão",
    cidadaos: "cidadãos",
    crianca: "criança",
    criancas: "crianças",
    conclusao: "conclusão",
    creche: "creche",
    deficiencia: "deficiência",
    demografica: "demográfica",
    distorcao: "distorção",
    domicilios: "domicílios",
    educacao: "educação",
    eletrica: "elétrica",
    evasao: "evasão",
    familia: "família",
    familias: "famílias",
    frequencia: "frequência",
    gas: "gás",
    gestacao: "gestação",
    mae: "mãe",
    maes: "mães",
    medio: "médio",
    matricula: "matrícula",
    matriculas: "matrículas",
    municipio: "município",
    municipios: "municípios",
    populacao: "população",
    pre: "pré",
    razao: "razão",
    reprovacao: "reprovação",
    saude: "saúde",
    serie: "série",
    series: "séries",
    situacao: "situação",
    unico: "único",
    numero: "número",
    raca: "raça",
};

const siglas = new Set(["eja", "enem", "ideb", "idh", "idhm", "pib", "sus"]);
const palavrasMinusculas = new Set(["a", "as", "ao", "aos", "com", "da", "das", "de", "do", "dos", "e", "em", "na", "nas", "no", "nos", "para", "por"]);

function formatarNomeGenerico(indicador: string) {
    const palavras = indicador
        .trim()
        .toLocaleLowerCase("pt-BR")
        .replace(/[_-]+/g, " ")
        .replace(/\s+/g, " ")
        .split(" ")
        .map((palavra, indice) => {
            if (/^\d+$/.test(palavra)) return palavra;
            if (siglas.has(palavra)) return palavra.toUpperCase();

            const palavraFormatada = termosComAcento[palavra] || palavra;
            if (indice > 0 && palavrasMinusculas.has(palavraFormatada)) {
                return palavraFormatada;
            }

            return palavraFormatada;
        });

    const nome = palavras.join(" ").replace(/\b15 anos ou mais\b/, "(15 anos ou mais)");

    return nome.charAt(0).toLocaleUpperCase("pt-BR") + nome.slice(1);
}

export function formatarNomeIndicador(indicador?: string) {
    if (!indicador) {
        return "Indicador";
    }

    return nomesOficiais[indicador] || formatarNomeGenerico(indicador);
}
