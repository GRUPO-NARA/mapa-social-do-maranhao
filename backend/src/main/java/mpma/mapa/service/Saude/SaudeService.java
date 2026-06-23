package mpma.mapa.service.Saude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mpma.mapa.repository.Saude.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class SaudeService {

    @Autowired
    private IdadeMedianaRepository idadeMedianaRepository;

    @Autowired
    private CasosHanseniaseRepository casosHanseniaseRepository;

    @Autowired
    private CasosTuberculoseRepository casosTuberculoseRepository;

    @Autowired
    private CoberturaDaEstrategiaDeSaudeFamiliarRepository coberturaDaEstrategiaDeSaudeFamiliarRepository;

    @Autowired
    private CoberturaDeAgentesComunitariosDeSaudeRepository coberturaDeAgentesComunitariosDeSaudeRepository;

    @Autowired
    private CoberturaVacinaRepository coberturaVacinaRepository;

    @Autowired
    private DesnutricaoInfantilUmAnoDeIdadeRepository desnutricaoInfantilUmAnoDeIdadeRepository;

    @Autowired
    private InternacoesDiabetesMellitusRepository internacoesDiabetesMellitusRepository;

    @Autowired
    private InternacoesHipertensaoPrimariaRepository internacoesHipertensaoPrimariaRepository;

    @Autowired
    private NascidosVivosMaesAdolescenteRepository nascidosVivosMaesAdolescenteRepository;

    @Autowired
    private NumeroCentroSaudeRepository numeroCentroSaudeRepository;

    @Autowired
    private NumeroDeHospitaisGeraisRepository numeroDeHospitaisGeraisRepository;

    @Autowired
    private NumeroLeitosInternacaoRepository numeroLeitosInternacaoRepository;

    @Autowired
    private NumeroMedicosTotalRepository numeroMedicosTotalRepository;

    @Autowired
    private NumerosLeitosUrgenciaRepository numerosLeitosUrgenciaRepository;

    @Autowired
    private ObitosDiabetesMellitusRepository obitosDiabetesMellitusRepository;

    @Autowired
    private ObitosDoencasCerebrovascularesRepository obitosDoencasCerebrovascularesRepository;

    @Autowired
    private TaxaMortalidadeInfantilRepository taxaMortalidadeInfantilRepository;

    public String idadeMedianaMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {
        return idadeMedianaRepository.buscarIdadeMedianaDoMunicipio(municipio);
    }

    public String casosHanseniaseMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {

        return casosHanseniaseRepository.buscarCasosHanseniaseDoMunicipio(municipio);
    }

    public String casosTuberculoseMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {

        return casosTuberculoseRepository.buscarCasosTuberculoseDoMunicipio(municipio);
    }

    public String coberturaDaEstrategiaDeSaudeFamiliarMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {

        return coberturaDaEstrategiaDeSaudeFamiliarRepository.buscarCoberturaDaEstrategiaDeSaudeFamiliarDoMunicipio(municipio);
    }
    
    public String coberturaDeAgentesComunitariosDeSaudeMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {

        return coberturaDeAgentesComunitariosDeSaudeRepository.buscarCoberturaDeAgentesComunitariosDeSaudeDoMunicipio(municipio);
    }

    public String coberturaVacinaMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return coberturaVacinaRepository.buscarCoberturaVacinalDoMunicipio(municipio);
    }

    public String desnutricaoInfantilUmAnoDeIdadeMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return desnutricaoInfantilUmAnoDeIdadeRepository.buscarDesnutricaoInfantilUmAnoDeIdadeDoMunicipio(municipio);
    }

    public String internacoesDiabetesMellitusMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return internacoesDiabetesMellitusRepository.buscarInternacoesDiabetesMellitusDoMunicipio(municipio);
    }

    public String internacoesHipertensaoPrimariaMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return internacoesHipertensaoPrimariaRepository.buscarInternacoesHipertensaoPrimariaDoMunicipio(municipio);
    }

    public String nascidosVivosMaesAdolescentesMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return nascidosVivosMaesAdolescenteRepository.buscarNascidosVivosMaesAdolescentesDoMunicipio(municipio);
    }

    public String numeroCentroSaudeMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return numeroCentroSaudeRepository.buscarNumeroCentroSaudeDoMunicipio(municipio);
    }

    public String numeroDeHospitaisGeraisMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return numeroDeHospitaisGeraisRepository.buscarNumeroDeHospitaisGeraisDoMunicipio(municipio);
    }

    public String numeroLeitosInternacaoMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return numeroLeitosInternacaoRepository.buscarNumeroLeitosInternacaoDoMunicipio(municipio);
    }

    public String numeroMedicosTotalMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return numeroMedicosTotalRepository.buscarNumeroMedicosTotalDoMunicipio(municipio);
    }

    public String numerosLeitosUrgenciaMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return numerosLeitosUrgenciaRepository.buscarNumeroLeitosUrgenciaDoMunicipio(municipio);
    }

    public String obitosDiabetesMellitusMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return obitosDiabetesMellitusRepository.buscarObitosDiabetesMellitusDoMunicipio(municipio);
    }

    public String obitosDoencasCerebrovascularesMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return obitosDoencasCerebrovascularesRepository.buscarObitosDoencasCerebrovascularesDoMunicipio(municipio);
    }

    public String taxaMortalidadeInfantilMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){

        return taxaMortalidadeInfantilRepository.buscarTaxaMortalidadeInfantilDoMunicipio(municipio);
    }
}