package se.omegapoint.kompetensdag.businessapplication;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class Controller {

    @PreAuthorize("hasAuthority('OP_UMEA')")
    @GetMapping("hello")
    public String sayHello() {
        return "Hello from Linus business logic application";
    }
}
