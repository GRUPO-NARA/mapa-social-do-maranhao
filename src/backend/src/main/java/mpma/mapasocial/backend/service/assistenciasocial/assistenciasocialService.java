package mpma.mapasocial.backend.service.assistenciasocial;

import mpma.mapasocial.backend.repository.assistenciaSocial.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class assistenciasocialService {

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


    public Long FamiliasEmSituacaoDeRua(@Param("ano") Integer ano, @Param("municipio") String municipio) {
        return familiasEmSituacaoDeRuaInscritasCadastroUnicoRepository.buscarQuantidadeFamiliasRuaPorMunicipio(ano, municipio);
    }

    public Long DomiciliosComEnergiaEletrica(@Param("ano") Integer ano, @Param("municipio") String municipio){
        return domiciliosComEnergiaEletricaRepository.buscarDomiciliosComEnergiaPorMunicipio(ano, municipio);
    }

    public Long FamiliasEmSituacaoDeTrabalhoInfantil(@Param("ano") Integer ano, @Param("municipio") String municipio) {
        return familiasEmSituacaoDeTrabalhoInfantilCadastroUnicoRepository.buscarQuantidadeFamiliasTrabalhoInfantilPorMunicipio(ano, municipio);
    }

    public Long AuxilioGasParaBrasileiroPorMunicipio(@Param("ano") Integer ano, @Param("municipio") String municipio){
        return programaAuxilioGasParaBrasileiroRepository.buscarAuxilioGasParaBrasileiroPorMunicipio(ano, municipio);
    }

    public Long PessoasComDeficienciaCadastradasCadastroUnico(@Param("ano") Integer ano, @Param("municipio") String municipio){
        return pessoasComDeficienciaCadastradasCadastroUnicoRepository.buscarPessoasComDeficienciaCadastradasCadastroUnicoPorMunicipio(ano, municipio);
    }

    public Long PessoasInscritasNoCadastroUnicoPorRacaECor(@Param("ano") Integer ano, @Param("municipio") String municipio){
        return pessoasInscritasNoCadastroUnicoPorRacaECorRepository.buscarPessoasInscritasNoCadastroUnicoPorRacaECor(ano, municipio);
    }

    public Long PessoasInscritasNoCadastroUnicoPorSexo(@Param("ano") Integer ano, @Param("municipio") String municipio){
        return pessoasInscritasNoCadastroUnicoPorSexoRepository.buscarPessoasInscritasNoCadastroUnicoPorSexo(ano, municipio);
    }

    public Long TotalDeFamiliasInscritasNoCadastroUnico(@Param("ano") Integer ano, @Param("municipio") String municipio){
        return totalDeFamiliasInscritasNoCadastroUnicoRepository.buscarTotalDeFamiliasInscritasNoCadastroUnico(ano, municipio);
    }

    public Long DomiciliosComAguaEncanada(@Param("ano") Integer ano, @Param("municipio") String municipio){
        return domiciliosComAguaEncanadaRepository.buscarDomiciliosComAguaEncanadaPorMunicipio(ano, municipio);
    }
}