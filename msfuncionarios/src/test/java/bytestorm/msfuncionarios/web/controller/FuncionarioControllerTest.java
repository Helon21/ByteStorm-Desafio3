package bytestorm.msfuncionarios.web.controller;

import bytestorm.msfuncionarios.web.dto.FuncionarioCriarDto;
import bytestorm.msfuncionarios.web.dto.FuncionarioRespostaDto;
import bytestorm.msfuncionarios.web.dto.PageableDto;
import bytestorm.msfuncionarios.web.exception.MensagemErro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static bytestorm.msfuncionarios.commom.FuncionariosConstantes.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/insert_funcionarios.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/delete_funcionarios.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class FuncionarioControllerTest {

    @Autowired
    WebTestClient testClient;

    @Test
    public void criarFuncionario_ComDadosInvalidos_RetornarFuncionarioComStatus201() {
        FuncionarioRespostaDto responseBody = testClient
                .post()
                .uri("/api/v1/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(FUNCIONARIO_PADRAO_CRIAR_DTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(FuncionarioRespostaDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getNome()).isEqualTo(FUNCIONARIO_PADRAO_CRIAR_DTO.getNome());
        assertThat(responseBody.getCpf()).isEqualTo(FUNCIONARIO_PADRAO_CRIAR_DTO.getCpf());
    }

    @Test
    public void criarFuncionario_ComCpfJaCadastrado_RetornarMensagemErroStatus409() {
        MensagemErro responseBody = testClient
                .post()
                .uri("/api/v1/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(JORGE)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void criarFuncionario_ComDadosInvalidos_RetornarMensagemErroStatus422() {
        MensagemErro responseBody = testClient
                .post()
                .uri("/api/v1/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FuncionarioCriarDto("  ", "80506962008", LocalDate.of(1999, 3, 12), "MASCULINO"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FuncionarioCriarDto("Matheus", "12345678910", LocalDate.of(1999, 3, 12), "MASCULINO"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FuncionarioCriarDto("Matheus", "80506962008", null, "MASCULINO"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FuncionarioCriarDto("Matheus", "80506962008", LocalDate.of(1999, 3, 12), null))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void alterarFuncionario_ComDadosValidosEMesmoCpf_RetornarFuncionario200() {
        FuncionarioRespostaDto responseBody = testClient
                .put()
                .uri("/api/v1/funcionarios/{id}", PEDRO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(PEDRO_ALTERAR_DTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FuncionarioRespostaDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(PEDRO.getId());
        assertThat(responseBody.getNome()).isEqualTo(PEDRO_ALTERAR_DTO.getNome());
        assertThat(responseBody.getCpf()).isEqualTo(PEDRO_ALTERAR_DTO.getCpf());
    }

    @Test
    public void alterarFuncionario_ComDadosValidosENovoCpf_RetornarFuncionario200() {
        FuncionarioCriarDto novoDto = new FuncionarioCriarDto(
                "Pedro2", "65688960043", LocalDate.of(2002, 5, 21), "MASCULINO");

        FuncionarioRespostaDto responseBody = testClient
                .put()
                .uri("/api/v1/funcionarios/{id}", PEDRO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(novoDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FuncionarioRespostaDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(PEDRO.getId());
        assertThat(responseBody.getNome()).isEqualTo(novoDto.getNome());
        assertThat(responseBody.getCpf()).isEqualTo(novoDto.getCpf());
    }

    @Test
    public void alterarFuncionario_IdInexistente_DeveLancarFuncionarioNaoEncontradoException() {
        MensagemErro responseBody = testClient
                .put()
                .uri("/api/v1/funcionarios/{id}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(PEDRO_ALTERAR_DTO)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void alterarFuncionario_ComCpfJaCadastrado_DeveLancarCpfRepetidoException() {
        FuncionarioCriarDto dtoComCpfRepetido = new FuncionarioCriarDto(
                "Pedro2", JORGE.getCpf(), LocalDate.of(2002, 5, 21), "MASCULINO");

        MensagemErro responseBody = testClient
                .put()
                .uri("/api/v1/funcionarios/{id}", PEDRO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dtoComCpfRepetido)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void buscarFuncionario_ComIdExistente_RetornarFuncionarioComStatus200() {
        FuncionarioRespostaDto responseBody = testClient
                .get()
                .uri("/api/v1/funcionarios/id/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(FuncionarioRespostaDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(1);
    }

    @Test
    public void buscarFuncionario_ComIdInexistente_RetornarErrorMessageComStatus404() {
        MensagemErro responseBody = testClient
                .get()
                .uri("/api/v1/funcionarios/id/10000000")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void buscarFuncionario_ComCpfExistente_RetornarFuncionarioComStatus200() {
        FuncionarioRespostaDto responseBody = testClient
                .get()
                .uri("/api/v1/funcionarios/cpf/80506962008")
                .exchange()
                .expectStatus().isOk()
                .expectBody(FuncionarioRespostaDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("80506962008");
    }

    @Test
    public void buscarFuncionario_ComCpfInexistente_RetornarErrorMessageComStatus404() {
        MensagemErro responseBody = testClient
                .get()
                .uri("/api/v1/funcionarios/cpf/69102077000")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void buscarFuncionarios_ComPaginacao_RetornarClientesComStatus200() {
        PageableDto responseBody = testClient
                .get()
                .uri("/api/v1/funcionarios")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent()).hasSize(5);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);

        responseBody = testClient
                .get()
                .uri("/api/v1/funcionarios?size=1&page=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(6);
    }
}
