package bytestorm.msresultados.web.controller;

import bytestorm.msresultados.entity.Resultado;
import bytestorm.msresultados.service.ResultadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/resultados")
public class ResultadoController {

    private final ResultadoService resultadoService;

    @GetMapping("/{id}")
    public ResponseEntity<Resultado> buscarPorId(@PathVariable Long id) {
        Resultado resultado = resultadoService.buscarResultadoPorId(id);
        return ResponseEntity.ok(resultado);
    }

}