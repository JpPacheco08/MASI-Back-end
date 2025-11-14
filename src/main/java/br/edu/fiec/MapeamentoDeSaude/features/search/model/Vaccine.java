package br.edu.fiec.MapeamentoDeSaude.features.search.model;

import com.google.firebase.database.DatabaseError;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vacinacao")
public class Vaccine {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "codigo_vacina")
    private String codigoVacina;
    @Column(name = "nome")
    private String nomeVacina;
    @Column(name = "descricao_doenca")
    private String descVacina;
    @Column(name = "tratamento")
    private String tratamento;
    @Column(name = "faixa_etaria")
    private String faixaEtaria;
    @Column(name = "intervalo_entre_doses")
    private String intervaloEntreDoses;
    @Column(name = "numero_doses")
    private Integer numeroDoses;
    @Column(name = "quantidade_em_estoque")
    private Integer quantidadeEstoque;
    @Column(name = "lote")
    private String lote;
    @Column(name = "data_fabricacao")
    private Date dataFabricacao;
    @Column(name = "validade")
    private Date validade;
}