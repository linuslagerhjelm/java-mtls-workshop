package se.omegapoint.kompetensdag.apigw;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class GwController {

    @GetMapping("hello-client")
    public String helloFromClient() {
        return "Hello from client";
    }

}
