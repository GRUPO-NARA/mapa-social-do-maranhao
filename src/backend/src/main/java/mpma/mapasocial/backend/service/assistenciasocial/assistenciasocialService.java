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


    public Long FamiliasEmSituacaoDeRua(@Param("municipio") String municipio) {
        return familiasEmSituacaoDeRuaInscritasCadastroUnicoRepository.buscarQuantidadeFamiliasRuaPorMunicipio(municipio).orElse(0L);
    }

    public Long DomiciliosComEnergiaEletrica(@Param("municipio") String municipio){
        return domiciliosComEnergiaEletricaRepository.buscarDomiciliosComEnergiaPorMunicipio(municipio).orElse(0L);
    }

    public Long FamiliasEmSituacaoDeTrabalhoInfantil(@Param("municipio") String municipio) {
        return familiasEmSituacaoDeTrabalhoInfantilCadastroUnicoRepository.buscarQuantidadeFamiliasTrabalhoInfantilPorMunicipio(municipio).orElse(0L);
    }

    public Long AuxilioGasParaBrasileiroPorMunicipio(@Param("municipio") String municipio){
        return programaAuxilioGasParaBrasileiroRepository.buscarAuxilioGasParaBrasileiroPorMunicipio(municipio);
    }

    public Long PessoasComDeficienciaCadastradasCadastroUnico(@Param("municipio") String municipio){
        return pessoasComDeficienciaCadastradasCadastroUnicoRepository.buscarPessoasComDeficienciaCadastradasCadastroUnicoPorMunicipio(municipio);
    }

    public Long PessoasInscritasNoCadastroUnicoPorRacaECor(@Param("municipio") String municipio){
        return pessoasInscritasNoCadastroUnicoPorRacaECorRepository.buscarPessoasInscritasNoCadastroUnicoPorRacaECor(municipio);
    }

    public Long PessoasInscritasNoCadastroUnicoPorSexo(@Param("municipio") String municipio){
        return pessoasInscritasNoCadastroUnicoPorSexoRepository.buscarPessoasInscritasNoCadastroUnicoPorSexo(municipio).orElse(0L);
    }

    public Long TotalDeFamiliasInscritasNoCadastroUnico(@Param("municipio") String municipio){
        return totalDeFamiliasInscritasNoCadastroUnicoRepository.buscarTotalDeFamiliasInscritasNoCadastroUnico(municipio);
    }

    public Long DomiciliosComAguaEncanada( @Param("municipio") String municipio){
        return domiciliosComAguaEncanadaRepository.buscarDomiciliosComAguaEncanadaPorMunicipio(municipio);
    }
}