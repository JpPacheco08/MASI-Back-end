package br.edu.fiec.MapeamentoDeSaude.features.search.dto;

import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import lombok.Data;

@Data
public class UbsDTO {

    private Long idUbs;
    private String nomeUbs;
    private String logradouro;
    private String complemento;
    private String telefone;
    private Double latitude;
    private Double longitude;

    public static UbsDTO convertFromUbs(Ubs ubs) {
        UbsDTO ubsDTO = new UbsDTO();
        ubsDTO.setIdUbs(ubs.getId());
        ubsDTO.setNomeUbs(ubs.getNomeUbs());
        ubsDTO.setLogradouro(ubs.getLogradouro());
        ubsDTO.setComplemento(ubs.getComplemento());
        ubsDTO.setTelefone(ubs.getTelefone());
        ubsDTO.setLatitude(ubs.getLatitude());
        ubsDTO.setLongitude(ubs.getLongitude());

        return ubsDTO;
    }
}