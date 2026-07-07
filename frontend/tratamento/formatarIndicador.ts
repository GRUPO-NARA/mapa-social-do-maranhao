const nomesOficiais: Record<string, string> = {
    taxa_de_aprovacao_no_ensino_medio: "Taxa de aprovação no ensino médio",
    taxa_de_analfabetismo_15_anos_ou_mais: "Taxa de analfabetismo (15 anos ou mais)",
    taxa_de_aprovacao_no_ensino_fundamental: "Taxa de aprovação no ensino fundamental",
};

const termosComAcento: Record<string, string> = {
    aprovacao: "aprovação",
    avaliacao: "avaliação",
    conclusao: "conclusão",
    creche: "creche",
    distorcao: "distorção",
    educacao: "educação",
    evasao: "evasão",
    frequencia: "frequência",
    medio: "médio",
    matricula: "matrícula",
    matriculas: "matrículas",
    pre: "pré",
    reprovação: "reprovação",
    reprovacao: "reprovação",
    serie: "série",
    series: "séries",
};

const siglas = new Set(["eja", "enem", "ideb", "idhm"]);

function formatarNomeGenerico(indicador: string) {
    const palavras = indicador
        .trim()
        .toLocaleLowerCase("pt-BR")
        .replace(/[_-]+/g, " ")
        .replace(/\s+/g, " ")
        .split(" ")
        .map((palavra) => {
            if (siglas.has(palavra)) return palavra.toUpperCase();
            return termosComAcento[palavra] || palavra;
        });

    let nome = palavras.join(" ");
    nome = nome.replace(/\banalfabetismo (15 anos ou mais)\b/, "analfabetismo ($1)");

    return nome.charAt(0).toLocaleUpperCase("pt-BR") + nome.slice(1);
}

export function formatarNomeIndicador(indicador?: string) {
    if (!indicador) {
        return "Indicador educacional";
    }

    return nomesOficiais[indicador] || formatarNomeGenerico(indicador);
}
