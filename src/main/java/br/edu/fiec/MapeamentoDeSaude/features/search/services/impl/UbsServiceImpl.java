package br.edu.fiec.MapeamentoDeSaude.features.search.services.impl;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.EnderecoDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDistanciaDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.search.repositories.UbsRepository;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.UbsService;
import br.edu.fiec.MapeamentoDeSaude.shared.service.GeocodingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UbsServiceImpl implements UbsService {

    private final UbsRepository ubsRepository;
    private final GeocodingService geocodingService;
    private final ObjectMapper objectMapper;

    // Raio da Terra em KM (Fórmula de Haversine)
    private static final double RAIO_TERRA_KM = 6371.0;

    /**
     * Calcula a distância entre dois pontos geográficos.
     */
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return RAIO_TERRA_KM * c;
    }

    /**
     * Lê o JSON do Google Maps e extrai Lat/Long.
     */
    private double[] parseCoordinatesFromJson(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.isEmpty()) return null;
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode locationNode = root.path("results").path(0).path("geometry").path("location");

            if (locationNode.isMissingNode()) {
                log.warn("Google API: Coordenadas não encontradas no JSON.");
                return null;
            }
            return new double[]{
                    locationNode.path("lat").asDouble(),
                    locationNode.path("lng").asDouble()
            };
        } catch (Exception e) {
            log.error("Erro ao ler JSON de geocodificação", e);
            return null;
        }
    }

    @Override
    public List<UbsDTO> getAllUbs() {
        List<Ubs> ubs = ubsRepository.findAll();
        return ubs.stream().map(UbsDTO::convertFromUbs).toList();
    }

    // --- Métodos CRUD Padrão (Mantidos) ---
    @Override
    public Ubs createUbs(UbsDTO ubsDto) {
        Ubs ubs = new Ubs();
        ubs.setNomeUbs(ubsDto.getNomeUbs());
        ubs.setTelefone(ubsDto.getTelefone());
        // Geocoding automático na criação
        if (ubsDto.getLatitude() != null) {
            ubs.setLatitude(ubsDto.getLatitude());
            ubs.setLongitude(ubsDto.getLongitude());
        } else if (ubsDto.getEndereco() != null) {
            double[] coords = parseCoordinatesFromJson(geocodingService.getCoordinatesFromAddress(ubsDto.getEndereco()));
            if (coords != null) {
                ubs.setLatitude(coords[0]);
                ubs.setLongitude(coords[1]);
            }
        }
        return ubsRepository.save(ubs);
    }

    @Override
    public void deleteUbs(UUID uuid) {

    }

}