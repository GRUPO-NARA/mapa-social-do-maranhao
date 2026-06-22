package mpma.mapa.service.Economicos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mpma.mapa.repository.Economicos.ProdutoInternoBrutoMunicipalRepository;
import mpma.mapa.repository.Economicos.ProdutoInternoBrutoPerCapitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class EconomicosService {

    @Autowired
    private ProdutoInternoBrutoMunicipalRepository produtoInternoBrutoMunicipalRepository;

    @Autowired
    private ProdutoInternoBrutoPerCapitaRepository produtoInternoBrutoPerCapitaRepository;

    public String produtoInternoBrutoMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){
        return produtoInternoBrutoMunicipalRepository.buscarProdutoInternoBrutoMunicipal(municipio);
    }

    public String produtoInternoBrutoAgregadoEstadualRecente() {
        return produtoInternoBrutoMunicipalRepository.buscarProdutoInternoBrutoAgregadoEstadualRecente();
    }

    public String produtoInternoBrutoPerCapitaMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio){
        // CORRIGIDO: Nome do método alterado para corresponder ao Repository criado
        return produtoInternoBrutoPerCapitaRepository.buscarPibPerCapitaDoMunicipio(municipio);
    }

}