package pl.edu.pjwstk.jaz.services;

import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.entities.Category;
import pl.edu.pjwstk.jaz.entities.Section;
import pl.edu.pjwstk.jaz.exceptions.EntityAlreadyExistException;
import pl.edu.pjwstk.jaz.exceptions.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    private final EntityManager em;

    public CategoryService(EntityManager em) {
        this.em = em;
    }

    public void addCategory(String name, Section section){
        if (findCategoryByName(name).isPresent())
            throw new EntityAlreadyExistException();
        var category = new Category();
        category.setName(name);
        category.setSection(section);
        em.persist(category);
    }

    public void editCategory(int id, String name){
        if (findCategoryById(id).isEmpty())
            throw new EntityNotFoundException();
        if (findCategoryByName(name).isPresent())
            throw new EntityAlreadyExistException();
        var category = findCategoryById(id).get();
        category.setName(name);
        em.merge(category);
    }

    public Optional<Category> findCategoryByName(String name){
        try {
            return Optional.ofNullable(em.createQuery("SELECT c FROM Category c WHERE c.name = :name", Category.class)
                    .setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    public Optional<Category> findCategoryById(int id){
        try {
            return Optional.ofNullable(em.createQuery("SELECT c FROM Category c WHERE c.id = :id", Category.class)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }
}
