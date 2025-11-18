package br.edu.fiec.MapeamentoDeSaude.config;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleMapsConfig {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @Bean
    public GeoApiContext geoApiContext() {
        // É importante criar este bean como um Singleton (padrão do @Bean)
        // para gerenciar o pool de conexões e limites de taxa da API.
        return new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }
}