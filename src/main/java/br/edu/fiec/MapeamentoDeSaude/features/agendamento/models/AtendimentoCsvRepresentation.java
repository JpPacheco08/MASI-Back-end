package br.edu.fiec.MapeamentoDeSaude.features.agendamento.models;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AtendimentoCsvRepresentation {

    @CsvBindByName(column = "id", required = true)
    private Long id;

    @CsvBindByName(column = "procedimento", required = true)
    private String procedimento;

    @CsvBindByName(column = "descricao", required = true)
    private String descricao;

    @CsvBindByName(column = "duracao", required = true)
    private Long duracao;

    @CsvBindByName(column = "observacoes", required = true)
    private String observacoes;

}
