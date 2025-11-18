package br.edu.fiec.MapeamentoDeSaude.features.user.models;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UbsCsvRepresentation {

    @CsvBindByName(column = "idUbs", required = true)
    private Long idUbs;

    @CsvBindByName(column = "nomeUbs", required = true)
    private String nomeUbs;

    @CsvBindByName(column = "endereco", required = true)
    private String endereco;
}
