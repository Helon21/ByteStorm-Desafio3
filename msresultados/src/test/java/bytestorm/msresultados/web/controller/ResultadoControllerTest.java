package bytestorm.msresultados.web.controller;

import bytestorm.msresultados.entity.Resultado;
import bytestorm.msresultados.web.expection.MensagemErro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/insert_resultados.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/delete_resultados.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ResultadoControllerTest {

    @Autowired
    WebTestClient testClient;

    @Test
    public void buscarFuncionario_ComIdExistente_RetornarFuncionarioComStatus200() {
        Resultado responseBody = testClient
                .get()
                .uri("/api/v1/resultados/id/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Resultado.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(1);
    }

    @Test
    public void buscarFuncionario_ComIdInexistente_RetornarErrorMessageComStatus404() {
        MensagemErro responseBody = testClient
                .get()
                .uri("/api/v1/resultados/id/10000000")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(404);
    }

}
