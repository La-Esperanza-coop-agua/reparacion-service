package cl.esperanza.reparacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient incidenciasWebClient(@Value("${incidencias.service.url:http://localhost:8080/api/v1/incidencias}") String incidenciasServiceUrl){
        return WebClient.builder().baseUrl(incidenciasServiceUrl).build();
    }

    // Este es para que OTROS servicios llamen a este servicio (opcional)
    @Bean
    public WebClient reparacionWebClient(@Value("${reparacion.service.url:http://localhost:8081/api/v1/reparacion}") String reparacionServiceUrl) {
        return WebClient.builder().baseUrl(reparacionServiceUrl).build();
    }
}
