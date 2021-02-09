package pl.edu.pjwstk.jaz.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.services.CategoryService;
import pl.edu.pjwstk.jaz.services.SectionService;

@RestController
@PreAuthorize("hasAuthority('admin')")
public class CategoryController {

    private final CategoryService categoryService;
    private final SectionService sectionService;

    public CategoryController(CategoryService categoryService, SectionService sectionService) {
        this.categoryService = categoryService;
        this.sectionService = sectionService;
    }

    @PostMapping("/addCategory/{sectionId}/{categoryName}")
    public void addSection(@PathVariable("sectionId") int sectionId, @PathVariable("categoryName") String categoryName){
        categoryService.addCategory(categoryName, sectionService.findSectionById(sectionId).get());
    }

    @PutMapping("/editCategory/{categoryId}/{categoryName}")
    public void editSection(@PathVariable("categoryId") int categoryId, @PathVariable("categoryName") String categoryName){
        categoryService.editCategory(categoryId, categoryName);
    }
}
