package se.omegapoint.kompetensdag.apigw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GwService {

    private final String backendUrl;
    private final HttpsTemplate httpsTemplate;

    @Autowired
    public GwService(
            @Value("${external.backend.url}") String backendUrl,
            HttpsTemplate httpsTemplate
    ) {
        this.backendUrl = backendUrl;
        this.httpsTemplate = httpsTemplate;
    }

    public String callBackend() {
        return httpsTemplate.createRestTemplate().getForEntity(backendUrl, String.class).getBody();
    }
}
