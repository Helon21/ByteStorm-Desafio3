package bytestorm.msfuncionarios.commom;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.repository.projection.FuncionarioProjection;
import bytestorm.msfuncionarios.web.dto.FuncionarioCriarDto;

import java.time.LocalDate;

public class FuncionariosConstantes {

    public static final Funcionario FUNCIONARIO_PADRAO = new Funcionario(
            null, "Fulano", "78146454097", LocalDate.of(2000, 1, 1), Funcionario.Status.ATIVO, Funcionario.Sexo.MASCULINO);

    public static final FuncionarioCriarDto FUNCIONARIO_PADRAO_CRIAR_DTO = new FuncionarioCriarDto(
            "Fulano", "78146454097", LocalDate.of(2000, 1, 1), "MASCULINO");

    public static final Funcionario FUNCIONARIO_INVALIDO = new Funcionario(
            null, "", "", null, null, null);

    public static final FuncionarioCriarDto FUNCIONARIO_INVALIDO_CRIAR_DTO = new FuncionarioCriarDto(
            "", "", null, null);

    public static final Funcionario JORGE = new Funcionario(
            1L, "Jorge", "80506962008", LocalDate.of(1999, 3, 12), Funcionario.Status.ATIVO, Funcionario.Sexo.MASCULINO);

    public static final Funcionario JOAO = new Funcionario(
            2L, "João", "47812608026", LocalDate.of(1995, 5, 10), Funcionario.Status.ATIVO, Funcionario.Sexo.MASCULINO);

    public static final Funcionario MARIA = new Funcionario(
            3L, "Maria", "75444903008", LocalDate.of(1998, 8, 15), Funcionario.Status.ATIVO, Funcionario.Sexo.FEMININO);

    public static final Funcionario PEDRO = new Funcionario(
            4L, "Pedro", "58480776064", LocalDate.of(2002, 3, 20), Funcionario.Status.ATIVO, Funcionario.Sexo.MASCULINO);


    public static final FuncionarioProjection FUNCIONARIO_PROJECTION_PADRAO = new FuncionarioProjection() {
        @Override
        public Long getId() {
            return 1L;
        }

        @Override
        public String getNome() {
            return "João Silva";
        }

        @Override
        public String getCpf() {
            return "123.456.789-00";
        }
    };
}
