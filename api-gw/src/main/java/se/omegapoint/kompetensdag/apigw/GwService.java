package se.omegapoint.kompetensdag.apigw;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GwService {

    private final String backendUrl;

    public GwService(
            @Value("${external.backend.url}") String backendUrl
    ) {
        this.backendUrl = backendUrl;
    }

    public String callBackend() {
        return new RestTemplate().getForEntity(backendUrl, String.class).getBody();
    }
}
