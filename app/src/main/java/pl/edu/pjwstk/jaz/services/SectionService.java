package pl.edu.pjwstk.jaz.services;

import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.entities.Section;
import pl.edu.pjwstk.jaz.exceptions.EntityAlreadyExistException;
import pl.edu.pjwstk.jaz.exceptions.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class SectionService {
    private final EntityManager em;

    public SectionService(EntityManager em) {
        this.em = em;
    }

    public void addSection(String name){
        if (findSectionByName(name).isPresent())
            throw new EntityAlreadyExistException();
        var section = new Section();
        section.setName(name);
        em.persist(section);
    }

    public void editSection(int id, String name){
        if (findSectionById(id).isEmpty())
            throw new EntityNotFoundException();
        if (findSectionByName(name).isPresent())
            throw new EntityAlreadyExistException();
        var section = findSectionById(id).get();
        section.setName(name);
        em.merge(section);
    }

    public Optional<Section> findSectionByName(String name){
        try {
            return Optional.ofNullable(em.createQuery("SELECT s FROM Section s WHERE s.name = :name", Section.class)
                    .setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    public Optional<Section> findSectionById(int id){
        try {
            return Optional.ofNullable(em.createQuery("SELECT s FROM Section s WHERE s.id = :id", Section.class)
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }
}
