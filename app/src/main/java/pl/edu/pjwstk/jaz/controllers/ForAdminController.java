package pl.edu.pjwstk.jaz.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAuthority('admin')")
@RestController
public class ForAdminController {

    @GetMapping("/forAdmin")
    public String forAdmin(){
        return "forAdmin";
    }
}
