package br.edu.fiec.MapeamentoDeSaude.features.search.services.impl;

// 1. IMPORTS ADICIONADOS
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
// --- FIM DOS IMPORTS ADICIONADOS ---

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.search.repositories.UbsRepository;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.UbsService;
import br.edu.fiec.MapeamentoDeSaude.shared.service.GeocodingService;
// Removido: import com.google.maps.model.LatLng; (Não é mais usado)
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor // 2. O Lombok injetará os TRÊS serviços
@Slf4j // Adicionado para logs
public class UbsServiceImpl implements UbsService {

    private final UbsRepository ubsRepository;
    private final GeocodingService geocodingService;
    private final ObjectMapper objectMapper; // 3. SERVIÇO DO JACKSON INJETADO

    /**
     * 4. NOVO MÉTODO PRIVADO PARA EXTRAIR COORDENADAS DO JSON
     */
    private double[] parseCoordinatesFromJson(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return null;
        }
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode locationNode = root.path("results").path(0).path("geometry").path("location");

            if (locationNode.isMissingNode()) {
                log.warn("Não foi possível encontrar 'results[0].geometry.location' no JSON da API do Google.");
                return null;
            }

            double lat = locationNode.path("lat").asDouble();
            double lng = locationNode.path("lng").asDouble();

            if (lat != 0.0 || lng != 0.0) {
                return new double[]{lat, lng};
            } else {
                log.warn("Valores de lat/lng não encontrados ou zerados no nó 'location'.");
                return null;
            }

        } catch (Exception e) {
            log.error("Falha ao parsear JSON da geocodificação: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Ubs createUbs(UbsDTO ubsDto) {
        Ubs ubs = new Ubs();
        ubs.setNomeUbs(ubsDto.getNomeUbs());
        ubs.setTelefone(ubsDto.getTelefone());

        // --- INÍCIO DA LÓGICA DE GEOCODING (MODIFICADA) ---
        String endereco = ubsDto.getEndereco();
        ubs.setLatitude(ubsDto.getLatitude());
        ubs.setLongitude(ubsDto.getLongitude());

        if (endereco != null && !endereco.isEmpty()) {

            // 5. Recebe a String JSON
            String jsonResponse = geocodingService.getCoordinatesFromAddress(endereco);

            // 6. Faz o parse do JSON
            double[] coordinates = parseCoordinatesFromJson(jsonResponse);

            if (coordinates != null) {
                // 7. Seta a Latitude (índice 0) e Longitude (índice 1)
                ubs.setLatitude(coordinates[0]);
                ubs.setLongitude(coordinates[1]);
            }
        }
        // --- FIM DA LÓGICA DE GEOCODING ---

        return ubsRepository.save(ubs);
    }

    @Override
    public Ubs getUbsByName(String name) {
        return ubsRepository.findByNomeUbs(name)
                .orElseThrow(() -> new RuntimeException("UBS não encontrada com o nome: " + name));
    }

    @Override
    public Optional<Ubs> getById(UUID uuid) {
        return ubsRepository.findById(uuid);
    }

    @Override
    public List<Ubs> getAllUbs() {
        return ubsRepository.findAll();
    }

    @Override
    public Ubs updateUbs(String name, UbsDTO ubsDto) {
        Ubs ubs = getUbsByName(name);
        ubs.setNomeUbs(ubsDto.getNomeUbs());
        ubs.setTelefone(ubsDto.getTelefone());

        // --- INÍCIO DA LÓGICA DE GEOCODING (UPDATE - MODIFICADA) ---
        String endereco = ubsDto.getEndereco();
        ubs.setLatitude(ubsDto.getLatitude());
        ubs.setLongitude(ubsDto.getLongitude());

        if (endereco != null && !endereco.isEmpty()) {

            String jsonResponse = geocodingService.getCoordinatesFromAddress(endereco);
            double[] coordinates = parseCoordinatesFromJson(jsonResponse);

            if (coordinates != null) {
                ubs.setLatitude(coordinates[0]);
                ubs.setLongitude(coordinates[1]);
            }
        }
        // --- FIM DA LÓGICA DE GEOCODING ---

        return ubsRepository.save(ubs);
    }

    @Override
    public void deleteUbs(UUID uuid) {
        if (!ubsRepository.existsById(uuid)) {
            throw new RuntimeException("UBS não encontrada com ID: " + uuid);
        }
        ubsRepository.deleteById(uuid);
    }


    public void deleteUbs(String name) {
        Ubs ubs = getUbsByName(name);
        ubsRepository.deleteById(ubs.getId());
    }
}