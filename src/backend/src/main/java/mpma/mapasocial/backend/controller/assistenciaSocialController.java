package mpma.mapasocial.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assistencia_social")
@Tag(name = "Assistência Social", description = "Endpoints para dados de assistência social")
public class assistenciaSocialController {
}
