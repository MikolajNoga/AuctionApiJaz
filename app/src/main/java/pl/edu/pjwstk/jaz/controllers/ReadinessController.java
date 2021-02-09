package pl.edu.pjwstk.jaz.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RestController
public class ReadinessController {

    private final EntityManager em;

    public ReadinessController(EntityManager em) {
        this.em = em;
    }

    @Transactional
    @GetMapping("/auth0/is-ready")
    public String isReady() {
        return "ready";
    }

    @GetMapping("/auth0/example")
    public void example(){
    }
}
