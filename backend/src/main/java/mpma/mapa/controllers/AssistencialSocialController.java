package mpma.mapa.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assistencia_social")
@RateLimiter(name = "RateLimiter")
@Tag(name = "Assistência Social", description = "Endpoints relacionados a dados de assistência social dos municípios do Maranhão.")
public class AssistencialSocialController {
}
