package pl.edu.pjwstk.jaz.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.UserDTO;
import pl.edu.pjwstk.jaz.entities.UserEntity;
import pl.edu.pjwstk.jaz.exceptions.EntityAlreadyExistException;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class UserService {
    private final EntityManager em;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(EntityManager em) {
        this.em = em;
    }

    public void saveUser(UserDTO userDTO){
        if (findUserByUsername(userDTO.getUsername()).isPresent())
            throw new EntityAlreadyExistException();

        var userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userEntity.setFirstname(userDTO.getFirstname());
        userEntity.setLastname(userDTO.getLastname());

        if (isAnyUser().isPresent())
            userEntity.setRole("user");
        else
            userEntity.setRole("admin");

        em.persist(userEntity);
    }

    public Optional<UserEntity> findUserByUsername(String username){
        try {
            return Optional.ofNullable(em.createQuery("SELECT ue FROM UserEntity ue WHERE ue.username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    public Optional<UserEntity> isAnyUser(){
        try {
            return Optional.ofNullable(em.createQuery("SELECT ue FROM UserEntity ue WHERE ue.id>0", UserEntity.class)
                    .setMaxResults(1)
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    public boolean login(String username, String password){
        if (findUserByUsername(username).isEmpty())
            return false;
        return passwordEncoder.matches(password, findUserByUsername(username).get().getPassword());
    }
}
