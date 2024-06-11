package bytestorm.msresultados.web.expection;

import bytestorm.msresultados.exception.ResultadoNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResultadoNaoEncontradoException.class)
    public ResponseEntity<MensagemErro> entityNotFoundException(RuntimeException ex,
                                                                HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MensagemErro> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String error = String.format("O parâmetro '%s' do valor '%s' não pôde ser convertido para o tipo '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.BAD_REQUEST, error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensagemErro> methodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                        HttpServletRequest request,
                                                                        BindingResult result) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) invalido(s)", result));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MensagemErro> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler({NoResourceFoundException.class, ChangeSetPersister.NotFoundException.class})
    public ResponseEntity<MensagemErro> handleHttpMessageNotReadableException(Exception ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<MensagemErro> handleDataIntegrityViolationException(Exception ex, HttpServletRequest request) {
        String errorMessage = "Erro de integridade de dados. Por favor, verifique os dados fornecidos.";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.BAD_REQUEST, errorMessage));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MensagemErro> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MensagemErro(request, HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não suportado"));
    }
}