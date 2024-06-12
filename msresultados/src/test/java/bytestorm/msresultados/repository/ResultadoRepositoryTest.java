package bytestorm.msresultados.repository;

import bytestorm.msresultados.entity.Resultado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ResultadoRepositoryTest {

    @Autowired
    private ResultadoRepository resultadoRepository;

    @BeforeEach
    public void setUp() {
        Resultado resultado = new Resultado(
                1L, 1L, 2L, "Test Title", "Test Description",
                LocalDateTime.of(2023, 6, 10, 10, 0),
                100, 20, Resultado.Status.APROVADO
        );
        resultadoRepository.save(resultado);
    }

    @Test
    public void testFindByPropostaId() {
        Optional<Resultado> optionalResultado = resultadoRepository.findByPropostaId(1L);
        assertTrue(optionalResultado.isPresent());
        Resultado resultado = optionalResultado.get();
        assertEquals(1L, resultado.getPropostaId());
        assertEquals("Test Title", resultado.getTitulo());
    }

    @Test
    public void testFindByPropostaIdNotFound() {
        Optional<Resultado> optionalResultado = resultadoRepository.findByPropostaId(2L);
        assertFalse(optionalResultado.isPresent());
    }
}
