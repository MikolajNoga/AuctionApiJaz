package pl.edu.pjwstk.jaz.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.services.SectionService;

@RestController
@PreAuthorize("hasAuthority('admin')")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/addSection/{sectionName}")
    public void addSection(@PathVariable("sectionName") String sectionName){
        sectionService.addSection(sectionName);
    }

    @PutMapping("/editSection/{sectionId}/{sectionName}")
    public void editSection(@PathVariable("sectionId") int sectionId, @PathVariable("sectionName") String sectionName){
        sectionService.editSection(sectionId, sectionName);
    }
}
