package br.edu.fiec.MapeamentoDeSaude.features.search.dto;

import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import lombok.Data;

@Data
public class UbsDistanciaDTO {

    private String id;
    private String nomeUbs;
    private String telefone;
    private Double latitude;
    private Double longitude;
    private String cep;
    private double distanciaEmKm; // 📍 O campo da foto!

    public UbsDistanciaDTO(Ubs ubs, double distanciaEmKm) {
        this.id = ubs.getId().toString();
        this.nomeUbs = ubs.getNomeUbs();
        this.telefone = ubs.getTelefone();
        this.latitude = ubs.getLatitude();
        this.longitude = ubs.getLongitude();
        this.cep = ubs.getCep();
        this.distanciaEmKm = distanciaEmKm;
    }
}