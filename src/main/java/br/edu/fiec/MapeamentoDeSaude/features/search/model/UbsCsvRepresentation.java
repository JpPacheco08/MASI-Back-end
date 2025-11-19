package br.edu.fiec.MapeamentoDeSaude.features.search.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UbsCsvRepresentation {

    @CsvBindByName(column = "idUbs", required = true)
    private Long idUbs;

    @CsvBindByName(column = "nomeUbs", required = true)
    private String nomeUbs;

    @CsvBindByName(column = "logradouro", required = true)
    private String logradouro;

    @CsvBindByName(column = "cep", required = true)
    private String cep;

    @CsvBindByName(column = "complemento", required = true)
    private String complemento;

    @CsvBindByName(column = "telefone", required = true)
    private String telefone;
}
