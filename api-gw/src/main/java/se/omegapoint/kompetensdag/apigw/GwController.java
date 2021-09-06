package se.omegapoint.kompetensdag.apigw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class GwController {

    private final GwService service;

    @Autowired
    public GwController(GwService service) {
        this.service = service;
    }

    @GetMapping("hello-client")
    public String helloFromClient() {
        return "Hello from client";
    }

    @GetMapping("hello-proxy")
    public String proxyRequestToBackend() {
        return service.callBackend();
    }
}
