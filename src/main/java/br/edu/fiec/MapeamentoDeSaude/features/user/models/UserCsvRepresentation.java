package br.edu.fiec.MapeamentoDeSaude.features.user.models;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

@Data
@NoArgsConstructor
public class UserCsvRepresentation {

    @CsvBindByName(column = "email", required = true)
    private String email;

    @CsvBindByName(column = "senha", required = true)
    private String password;

    @CsvBindByName(column = "nome")
    private String name;

    @CsvBindByName(column = "accesLevel", required = true)
    private String accesLevel;

    // Campos UbsAdmin

    @CsvBindByName(column = "cnpj", required = true)
    private String cnpj;

    @CsvBindByName(column = "nomeDaUbs", required = true)
    private String nomeDaUbs;

    // Campos Paciente

    @CsvBindByName(column = "cpf", required = true)
    private String cpf;

    @CsvBindByName(column = "cidade")
    private String cidade;

    @CsvBindByName(column = "cep")
    private String cep;
}
