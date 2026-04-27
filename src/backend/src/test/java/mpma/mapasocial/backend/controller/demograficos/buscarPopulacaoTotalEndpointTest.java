package mpma.mapasocial.backend.controller.demograficos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import mpma.mapasocial.backend.controller.demograficosController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import mpma.mapasocial.backend.service.demograficos.demograficosService;
import mpma.mapasocial.backend.service.gerarRespostaRequisicaoService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(demograficosController.class)
@Import(gerarRespostaRequisicaoService.class)
public class buscarPopulacaoTotalEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private demograficosService demograficosService;

    @Test
    @DisplayName("GET /demograficos/buscarPopulacaoTotal - Deve retornar a população total do município")
    @WithMockUser
    void deveRetornarPopulacaoTotalDoMunicipioInformado() throws Exception{
        // Exemplo de município
        String municipioInformado = "São Luís";
        Long populacaoTotalDoMunicipioInformado = 1037775L;
        when(demograficosService.populacaoTotalDoMunicipio(municipioInformado)).thenReturn(populacaoTotalDoMunicipioInformado);

        mockMvc.perform(get("/demograficos/buscarPopulacaoTotal")
                        .param("municipio", municipioInformado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.indicador").value("População Total do Município de " + municipioInformado))
                .andExpect(jsonPath("$.resposta.valor").value(populacaoTotalDoMunicipioInformado))
                .andExpect(jsonPath("$.status").value("200"));
    }

    @Test
    @DisplayName("GET /demograficos/buscarPopulacaoTotal - Deve retornar erro 204 quando a população total do município não for encontrada")
    @WithMockUser
    void deveRetornarErro204QuandoPopulacaoTotalDoMunicipioNaoForEncontrada() throws Exception{
        String municipioInformadoFicticio = "São Paulo";
        when(demograficosService.populacaoTotalDoMunicipio(municipioInformadoFicticio)).thenReturn(null);

        mockMvc.perform(get("/demograficos/buscarPopulacaoTotal")
                .param("municipio", municipioInformadoFicticio))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /demograficos/buscarPopulacaoTotal - Deve retornar erro 400 quando o parâmetro município não for informado")
    @WithMockUser
    void deveRetornarErro400QuandoParametroMunicipioNaoForInformado() throws Exception{
        mockMvc.perform(get("/demograficos/buscarPopulacaoTotal"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /demograficos/buscarPopulacaoTotal - Deve retornar erro 404 quando o endpoint for acessado com método HTTP diferente de GET")
    @WithMockUser
    void deveRetornarErro404QuandoEndpointForAcessadoComMetodoHTTPDiferenteDeGET() throws Exception {
        mockMvc.perform(get("/demograficos/buscarPopulacaoTo")
                        .param("São Luís"))
                .andExpect(status().isNotFound());
    }
}
