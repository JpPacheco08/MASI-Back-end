package br.edu.fiec.MapeamentoDeSaude.features.search.services.impl;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.EnderecoDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDistanciaDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.UbsCsvRepresentation;
import br.edu.fiec.MapeamentoDeSaude.features.search.repositories.UbsRepository;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.UbsService;
import br.edu.fiec.MapeamentoDeSaude.shared.service.GeocodingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.*;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UbsServiceImpl implements UbsService {

    private GeocodingService geocodingService;

    private final UbsRepository ubsRepository;
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

    @Override
    public Ubs updateUbs(String name, UbsDTO ubsDto) {
        return null;
    }

    @Override
    public void deleteUbs(UUID uuid) {

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
        } else if (ubsDto.getLogradouro() != null) {
            double[] coords = parseCoordinatesFromJson(geocodingService.getCoordinatesFromAddress(ubsDto.getLogradouro()));
            if (coords != null) {
                ubs.setLatitude(coords[0]);
                ubs.setLongitude(coords[1]);
            }
        }
        return ubsRepository.save(ubs);
    }

    @Override
    public Ubs getUbsByName(String name) {
        return null;
    }

    @Override
    public Optional<Ubs> getById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    @Transactional // quero tudo ou nada
    public void createAllUbs(InputStream inputStream) {


        List<UbsCsvRepresentation> allUbs = new ArrayList<>();
        try (Reader reader = new InputStreamReader(inputStream)) {

            // Create a CsvToBean object from the Reader
            CsvToBean<UbsCsvRepresentation> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(UserCsvRepresentation.class) // Specify the target bean class
                    .withIgnoreLeadingWhiteSpace(true) // Clean up any extra spaces
                    .withSkipLines(0) // Skips the header row if present
                    .build();

            // Parse the data and return a List of beans
            allUbs = csvToBean.parse();
        } catch (Exception e) {
            // Handle IO or CSV parsing exceptions
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage(), e);
        }

        try {

            for (UbsCsvRepresentation csvUbs : allUbs) {
                Ubs ubs = new Ubs();
                ubs.setNomeUbs(csvUbs.getNomeUbs());
                ubs.setCep(csvUbs.getCep());
                ubs.setLogradouro(csvUbs.getLogradouro());
                ubs.setComplemento(csvUbs.getComplemento());
                ubs.setTelefone(csvUbs.getTelefone());
                ubs.setId(csvUbs.getIdUbs());

                double[] coords = parseCoordinatesFromJson(geocodingService.getCoordinatesFromAddress(ubs.getLogradouro()));
                if (coords != null) {
                    ubs.setLatitude(coords[0]);
                    ubs.setLongitude(coords[1]);

                }
                ubsRepository.save(ubs);

            }
        }catch (Exception ex){
            throw new RuntimeException("Failed to parse CSV file: " + ex.getMessage(), ex);

        }


    }

    @Override
    public List<UbsDistanciaDTO> findUbsMaisProximas(EnderecoDTO enderecoDTO) {
        // 1. Descobrir onde o usuário está
        String jsonResponse = geocodingService.getCoordinatesFromAddress(enderecoDTO.getEndereco());
        double[] userCoords = parseCoordinatesFromJson(jsonResponse);

        if (userCoords == null) {
            throw new RuntimeException("Endereço não localizado: " + enderecoDTO.getEndereco());
        }

        double userLat = userCoords[0];
        double userLng = userCoords[1];

        // 2. Pegar todas as UBS e calcular distância
        return ubsRepository.findAll().stream()
                .filter(ubs -> ubs.getLatitude() != null && ubs.getLongitude() != null)
                .map(ubs -> new UbsDistanciaDTO(
                        ubs,
                        haversine(userLat, userLng, ubs.getLatitude(), ubs.getLongitude())
                ))
                // 3. Ordenar da MENOR distância para a MAIOR
                .sorted(Comparator.comparingDouble(UbsDistanciaDTO::getDistanciaEmKm))
                .collect(Collectors.toList());
    }

}