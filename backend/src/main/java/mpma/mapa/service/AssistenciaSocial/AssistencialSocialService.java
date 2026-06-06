package mpma.mapa.service.AssistenciaSocial;

import mpma.mapa.repository.AssistencialSocial.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AssistencialSocialService {

    @Autowired
    private FamiliasEmSituacaoDeRuaInscritasCadastroUnicoRepository familiasEmSituacaoDeRuaInscritasCadastroUnicoRepository;

    @Autowired
    private DomiciliosComEnergiaEletricaRepository domiciliosComEnergiaEletricaRepository;

    @Autowired
    private FamiliasEmSituacaoDeTrabalhoInfantilCadastroUnicoRepository familiasEmSituacaoDeTrabalhoInfantilCadastroUnicoRepository;

    @Autowired
    private ProgramaAuxilioGasParaBrasileiroRepository programaAuxilioGasParaBrasileiroRepository;

    @Autowired
    private PessoasComDeficienciaCadastradasCadastroUnicoRepository pessoasComDeficienciaCadastradasCadastroUnicoRepository;

    @Autowired
    private PessoasInscritasNoCadastroUnicoPorRacaECorRepository pessoasInscritasNoCadastroUnicoPorRacaECorRepository;

    @Autowired
    private PessoasInscritasNoCadastroUnicoPorSexoRepository pessoasInscritasNoCadastroUnicoPorSexoRepository;

    @Autowired
    private TotalDeFamiliasInscritasNoCadastroUnicoRepository totalDeFamiliasInscritasNoCadastroUnicoRepository;

    @Autowired
    private DomiciliosComAguaEncanadaRepository domiciliosComAguaEncanadaRepository;


    /**
     * Ponte de negócio entre Controller e Repository para o indicador de famílias em situação de rua inscritas no Cadastro Único.
     *
     * Recebe o nome do município, repassa para a query nativa do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto do indicador consolidado ou 0L se nenhum registro for encontrado
     */
    public Long FamiliasEmSituacaoDeRua(@Param("municipio") String municipio) {
        return familiasEmSituacaoDeRuaInscritasCadastroUnicoRepository.buscarQuantidadeFamiliasRuaPorMunicipio(municipio).orElse(0L);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de domicílios com energia elétrica.
     *
     * Recebe o nome do município, repassa para a query nativa do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto do indicador consolidado ou 0L se nenhum registro for encontrado
     */
    public Long DomiciliosComEnergiaEletrica(@Param("municipio") String municipio){
        return domiciliosComEnergiaEletricaRepository.buscarDomiciliosComEnergiaPorMunicipio(municipio).orElse(0L);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de famílias em situação de trabalho infantil inscritas no Cadastro Único.
     *
     * Recebe o nome do município, repassa para a query nativa do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto do indicador consolidado ou 0L se nenhum registro for encontrado
     */
    public Long FamiliasEmSituacaoDeTrabalhoInfantil(@Param("municipio") String municipio) {
        return familiasEmSituacaoDeTrabalhoInfantilCadastroUnicoRepository.buscarQuantidadeFamiliasTrabalhoInfantilPorMunicipio(municipio).orElse(0L);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador do Programa Auxílio Gás para Brasileiro.
     *
     * Recebe o nome do município, repassa para a query nativa do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto do indicador consolidado ou null se nenhum registro for encontrado
     */
    public Long AuxilioGasParaBrasileiroPorMunicipio(@Param("municipio") String municipio){
        return programaAuxilioGasParaBrasileiroRepository.buscarAuxilioGasParaBrasileiroPorMunicipio(municipio);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de pessoas com deficiência cadastradas no Cadastro Único.
     *
     * Recebe o nome do município, repassa para a query nativa do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto do indicador consolidado ou null se nenhum registro for encontrado
     */
    public Long PessoasComDeficienciaCadastradasCadastroUnico(@Param("municipio") String municipio){
        return pessoasComDeficienciaCadastradasCadastroUnicoRepository.buscarPessoasComDeficienciaCadastradasCadastroUnicoPorMunicipio(municipio);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de pessoas inscritas no Cadastro Único por raça e cor.
     *
     * Recebe o nome do município, repassa para a query nativa do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto do indicador consolidado ou null se nenhum registro for encontrado
     */
    public Long PessoasInscritasNoCadastroUnicoPorRacaECor(@Param("municipio") String municipio){
        return pessoasInscritasNoCadastroUnicoPorRacaECorRepository.buscarPessoasInscritasNoCadastroUnicoPorRacaECor(municipio);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de pessoas inscritas no Cadastro Único por sexo.
     *
     * Recebe o nome do município, repassa para a query nativa do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto do indicador consolidado ou 0L se nenhum registro for encontrado
     */
    public Long PessoasInscritasNoCadastroUnicoPorSexo(@Param("municipio") String municipio){
        return pessoasInscritasNoCadastroUnicoPorSexoRepository.buscarPessoasInscritasNoCadastroUnicoPorSexo(municipio).orElse(0L);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de total de famílias inscritas no Cadastro Único.
     *
     * Recebe o nome do município, repassa para a query nativa do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto do indicador consolidado ou null se nenhum registro for encontrado
     */
    public Long TotalDeFamiliasInscritasNoCadastroUnico(@Param("municipio") String municipio){
        return totalDeFamiliasInscritasNoCadastroUnicoRepository.buscarTotalDeFamiliasInscritasNoCadastroUnico(municipio);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de domicílios com água encanada.
     *
     * Recebe o nome do município, repassa para a query nativa do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto do indicador consolidado ou null se nenhum registro for encontrado
     */
    public Long DomiciliosComAguaEncanada( @Param("municipio") String municipio){
        return domiciliosComAguaEncanadaRepository.buscarDomiciliosComAguaEncanadaPorMunicipio(municipio);
    }
}