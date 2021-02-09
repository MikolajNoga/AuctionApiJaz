package pl.edu.pjwstk.jaz.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.UserDTO;
import pl.edu.pjwstk.jaz.services.UserService;
import pl.edu.pjwstk.jaz.exceptions.BadRequestException;
import pl.edu.pjwstk.jaz.requests.RegisterRequest;


@RestController
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest registerRequest){
        if (registerRequest.getUsername().isEmpty() || registerRequest.getPassword().isEmpty() ||
                registerRequest.getFirstname().isEmpty() || registerRequest.getLastname().isEmpty())
            throw new BadRequestException();
        UserDTO userDTO = new UserDTO(registerRequest.getUsername(),registerRequest.getPassword());
        userDTO.setFirstname(registerRequest.getFirstname());
        userDTO.setLastname(registerRequest.getLastname());
        userService.saveUser(userDTO);
    }
}
