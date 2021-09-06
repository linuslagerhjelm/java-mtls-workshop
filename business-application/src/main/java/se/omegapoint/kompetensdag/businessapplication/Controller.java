package se.omegapoint.kompetensdag.businessapplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class Controller {

    @GetMapping("hello")
    public String sayHello() {
        return "Hello from business logic application";
    }
}