package br.edu.fiec.MapeamentoDeSaude.shared.service;

// 1. Remova os imports do google.maps se ainda existirem
// import com.google.maps.GeoApiContext;
// import com.google.maps.model.LatLng;

// 2. Adicione os imports necessários
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value; // IMPORTAR
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; // IMPORTAR

// 3. Imports do Lombok (REMOVER @AllArgsConstructor)
import lombok.AllArgsConstructor; // <-- REMOVA ESTA LINHA

@Service
@Slf4j
// @AllArgsConstructor // 4. REMOVA ESTA ANOTAÇÃO
public class GeocodingService {

    // 5. Declare os campos
    private final RestTemplate restTemplate;
    private final String apiKey;

    // URL para geocodificação direta (forward)
    private static final String GOOGLE_API_URL =
            "https://maps.googleapis.com/maps/api/geocode/json?address={address}&key={key}";

    // 6. CRIE ESTE CONSTRUTOR MANUALMENTE
    public GeocodingService(RestTemplate restTemplate,
                            @Value("${google.maps.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    /**
     * Converte um endereço em coordenadas.
     */
    public String getCoordinatesFromAddress(String address) {

        java.util.Map<String, String> params = new java.util.HashMap<>();
        params.put("address", address);
        params.put("key", apiKey);

        try {
            // Chama a API
            String jsonResponse = restTemplate.getForObject(GOOGLE_API_URL, String.class, params);
            log.info("Geocode response: {}",jsonResponse);
            return jsonResponse;
        } catch (Exception e) {
            log.error("Erro ao geocodificar endereço: {}", address, e);
            return null; // Retorna nulo em caso de erro
        }
    }
}