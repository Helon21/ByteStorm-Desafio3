package bytestorm.msfuncionarios.web.controller;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.web.dto.FuncionarioAlterarStatusDto;
import bytestorm.msfuncionarios.web.dto.FuncionarioCriarDto;
import bytestorm.msfuncionarios.web.dto.FuncionarioRespostaDto;
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

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo(FUNCIONARIO_PADRAO_CRIAR_DTO.getNome());
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo(FUNCIONARIO_PADRAO_CRIAR_DTO.getCpf());
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

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
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

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FuncionarioCriarDto("Matheus", "12345678910", LocalDate.of(1999, 3, 12), "MASCULINO"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FuncionarioCriarDto("Matheus", "80506962008", null, "MASCULINO"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FuncionarioCriarDto("Matheus", "80506962008", LocalDate.of(1999, 3, 12), null))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void alterarStatus_ComDadosValidos_RetornarFuncionarioComStatus200() {
        FuncionarioAlterarStatusDto statusDto = new FuncionarioAlterarStatusDto();
        statusDto.setStatus("ATIVO");

        FuncionarioRespostaDto responseBody = testClient
                .patch()
                .uri("/api/v1/funcionarios/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(statusDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FuncionarioRespostaDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(Funcionario.Status.ATIVO);
    }

    @Test
    public void alterarStatus_FuncionarioNaoEncontrado_RetornarMensagemErroStatus404() {
        FuncionarioAlterarStatusDto statusDto = new FuncionarioAlterarStatusDto();
        statusDto.setStatus("INATIVO");

        MensagemErro responseBody = testClient
                .patch()
                .uri("/api/v1/funcionarios/{id}", 9999)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(statusDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void alterarStatus_ComDadosInvalidos_RetornarMensagemErroStatus422() {
        FuncionarioAlterarStatusDto statusDto = new FuncionarioAlterarStatusDto();
        statusDto.setStatus("STATUS_INVALIDO");

        MensagemErro responseBody = testClient
                .patch()
                .uri("/api/v1/funcionarios/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(statusDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MensagemErro.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }
}
