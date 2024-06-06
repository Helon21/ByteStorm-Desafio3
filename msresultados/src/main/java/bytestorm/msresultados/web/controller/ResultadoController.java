package bytestorm.msresultados.web.controller;

import bytestorm.msresultados.service.ResultadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/resultados")
public class ResultadoController {

    private final ResultadoService resultadoService;

}
