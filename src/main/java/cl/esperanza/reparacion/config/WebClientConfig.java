package cl.esperanza.reparacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Bean(name = "incidenciasWebClient")
    public WebClient incidenciasWebClient(){
        return WebClient.builder().baseUrl("http://localhost:8080/api/v1/incidencias").build();
    }    

    // Este es para que OTROS servicios llamen a este servicio (opcional)
    @Bean(name = "reparacionWebClient")
    public WebClient reparacionWebClient() {
        return WebClient.builder().baseUrl("http://localhost:8081/api/v1/reparacion").build();
    }
}