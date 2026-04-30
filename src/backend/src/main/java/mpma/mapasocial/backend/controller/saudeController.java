package mpma.mapasocial.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saude")
@Tag(name = "Saúde", description = "Endpoints para dados de saúde")
public class saudeController {
}
