package bytestorm.msfuncionarios.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PageableDto {
    private List content = new ArrayList<>();
    @JsonProperty("primeiro")
    private boolean first;
    @JsonProperty("ultimo")
    private boolean last;
    @JsonProperty("pagina")
    private int number;
    @JsonProperty("tamanho")
    private int size;
    @JsonProperty("numeroDeElementos")
    private int numberOfElements;
    @JsonProperty("totalDePaginas")
    private int totalPages;
    @JsonProperty("TotalDeElementos")
    private int totalElements;
}
