package mpma.mapasocial.backend.service.saude;

import mpma.mapasocial.backend.repository.saude.IdadeMedianaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.repository.query.Param;

@Service
public class saudeService {
    @Autowired
    private IdadeMedianaRepository idadeMedianaRepository;

    public Double buscarIdadeMedianaPorMunicipio(String ano, String municipio) {
        return null;
    }
}
