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
            // Verifica se o status é OK
            if (!"OK".equals(root.path("status").asText())) {
                log.warn("Google API retornou status não-OK: {}", root.path("status").asText());
                return null;
            }

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

    @Override
    public Ubs createUbs(UbsDTO ubsDto) {
        Ubs ubs = new Ubs();
        ubs.setNomeUbs(ubsDto.getNomeUbs());
        ubs.setTelefone(ubsDto.getTelefone());
        ubs.setLogradouro(ubsDto.getLogradouro());
        ubs.setComplemento(ubsDto.getComplemento());
        ubs.setEndereco(ubsDto.getEndereco());

        // Geocoding automático na criação
        if (ubsDto.getLatitude() != null && ubsDto.getLongitude() != null) {
            ubs.setLatitude(ubsDto.getLatitude());
            ubs.setLongitude(ubsDto.getLongitude());
        } else if (ubsDto.getEndereco() != null && !ubsDto.getEndereco().isEmpty()) {
            String jsonResponse = geocodingService.getCoordinatesFromAddress(ubsDto.getEndereco());
            double[] coords = parseCoordinatesFromJson(jsonResponse);
            if (coords != null) {
                ubs.setLatitude(coords[0]);
                ubs.setLongitude(coords[1]);
            }
        }
        return ubsRepository.save(ubs);
    }

    @Override
    public Ubs updateUbs(String name, UbsDTO ubsDto) {
        // Busca a UBS pelo nome antigo (ou ID, dependendo da sua lógica de negócio, mas mantive nome conforme interface)
        Ubs ubs = ubsRepository.findByNomeUbs(name)
                .orElseThrow(() -> new RuntimeException("UBS não encontrada com o nome: " + name));

        ubs.setNomeUbs(ubsDto.getNomeUbs());
        ubs.setTelefone(ubsDto.getTelefone());
        ubs.setLogradouro(ubsDto.getLogradouro());
        ubs.setComplemento(ubsDto.getComplemento());
        ubs.setEndereco(ubsDto.getEndereco());

        // Se a latitude/longitude vierem preenchidas, usa elas
        if (ubsDto.getLatitude() != null && ubsDto.getLongitude() != null) {
            ubs.setLatitude(ubsDto.getLatitude());
            ubs.setLongitude(ubsDto.getLongitude());
        }
        // Se mudou o endereço e não mandou lat/long, recalcula
        else if (ubsDto.getEndereco() != null && !ubsDto.getEndereco().isEmpty()) {
            String jsonResponse = geocodingService.getCoordinatesFromAddress(ubsDto.getEndereco());
            double[] coords = parseCoordinatesFromJson(jsonResponse);
            if (coords != null) {
                ubs.setLatitude(coords[0]);
                ubs.setLongitude(coords[1]);
            }
        }

        return ubsRepository.save(ubs);
    }

    @Override
    public Ubs getUbsByName(String name) {
        return ubsRepository.findByNomeUbs(name)
                .orElseThrow(() -> new RuntimeException("UBS não encontrada: " + name));
    }

    @Override
    public Optional<Ubs> getById(UUID uuid) {
        return ubsRepository.findById(uuid);
    }

    @Override
    public void deleteUbs(UUID uuid) {
        if (!ubsRepository.existsById(uuid)) {
            throw new RuntimeException("UBS não encontrada para exclusão.");
        }
        ubsRepository.deleteById(uuid);
    }

    // 👇 AQUI ESTÁ A LÓGICA DAS UBSs PRÓXIMAS
    @Override
    public List<UbsDistanciaDTO> findUbsMaisProximas(EnderecoDTO enderecoDTO) {

        // 1. Descobrir onde o usuário está (Lat/Long do endereço que ele digitou)
        String jsonResponse = geocodingService.getCoordinatesFromAddress(enderecoDTO.getEndereco());
        double[] userCoords = parseCoordinatesFromJson(jsonResponse);

        if (userCoords == null) {
            throw new RuntimeException("Não foi possível localizar as coordenadas do endereço informado.");
        }

        double userLat = userCoords[0];
        double userLon = userCoords[1];

        // 2. Pegar todas as UBSs do banco
        List<Ubs> todasUbs = ubsRepository.findAll();

        // 3. Calcular distância, converter para DTO e ordenar
        return todasUbs.stream()
                // Filtra apenas UBS que tenham coordenadas cadastradas
                .filter(ubs -> ubs.getLatitude() != null && ubs.getLongitude() != null)
                .map(ubs -> {
                    // Calcula a distância
                    double distancia = haversine(userLat, userLon, ubs.getLatitude(), ubs.getLongitude());
                    // Cria o DTO com a distância
                    return new UbsDistanciaDTO(ubs, distancia);
                })
                // Ordena da menor distância para a maior
                .sorted(Comparator.comparingDouble(UbsDistanciaDTO::getDistanciaEmKm))
                .collect(Collectors.toList());
    }
}