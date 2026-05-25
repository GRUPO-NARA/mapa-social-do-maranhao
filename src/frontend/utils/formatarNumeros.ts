/**
 * Formata um número para o padrão de bilhões com separadores de milhar
 * Ex: 130745967 -> "130.745,96 bilhões"
 * @param numero - O número a ser formatado
 * @param casasDecimais - Quantidade de casas decimais (padrão: 2)
 * @returns String formatada
 */
export function formatarPibEmBilhoes(numero: number | string, casasDecimais: number = 2): string {
  if (!numero) return "--";
  
  const numericValue = typeof numero === "string" ? parseFloat(numero.replace(/\./g, "").replace(",", ".")) : numero;
  
  if (isNaN(numericValue)) return "--";
  
  // Se o número for muito grande, dividir por 1 bilhão
  const ehBilhoes = numericValue > 1000;
  const valor = ehBilhoes ? numericValue / 1000 : numericValue;
  
  // Formatar com separador de milhar e decimais
  return valor.toLocaleString("pt-BR", {
    minimumFractionDigits: casasDecimais,
    maximumFractionDigits: casasDecimais,
  });
}
