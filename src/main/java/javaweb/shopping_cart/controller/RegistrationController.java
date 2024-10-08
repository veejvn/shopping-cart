package javaweb.shopping_cart.controller;

import jakarta.validation.Valid;
import javaweb.shopping_cart.dto.UserRegistrationDto;
import javaweb.shopping_cart.entity.Role;
import javaweb.shopping_cart.entity.UserEntity;
import javaweb.shopping_cart.repository.RoleRepository;
import javaweb.shopping_cart.service.UserEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@Controller
public class RegistrationController {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserEntityServiceImpl userEntityServiceImpl;

    @GetMapping("/registration")
    public String registration(Model model){
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        model.addAttribute("userView", userRegistrationDto);
        return "/registration";
    }

    @PostMapping("/registration")
    public ModelAndView createNewUser(@ModelAttribute("userView") @Valid
                                          UserRegistrationDto userRegistrationDto,
                                      BindingResult bindingResult){
        if(userEntityServiceImpl.findByEmail(userRegistrationDto.getEmail()).isPresent()){
            bindingResult.rejectValue("email", "error.user",
                    "The email is already registered!, please use another one.");
        }

        if(userEntityServiceImpl.findByUsername(userRegistrationDto.getUsername()).isPresent()){
            bindingResult.rejectValue("username", "error.user",
                    "There is already a user registered with the username provided.");
        }

        ModelAndView modelAndView = new ModelAndView();

        if(bindingResult.hasErrors()) {
            modelAndView.setViewName("/registration");
            return modelAndView;
        }else {
            UserEntity userEntity = new UserEntity();
            userEntity.setFirstName(userRegistrationDto.getFirstName());
            userEntity.setLastName(userRegistrationDto.getLastName());
            userEntity.setUsername(userRegistrationDto.getUsername());
            userEntity.setEmail(userRegistrationDto.getEmail());
            userEntity.setPassword(userRegistrationDto.getPassword());

            Set<Role> userRoles = roleRepository.findRolesByNameSet(userRegistrationDto.getRoles());
            userEntity.setRoles(userRoles);

            userEntityServiceImpl.createNewUserEntity(userEntity);

            modelAndView.addObject("successMessage", "Username," +
                    userEntity.getUsername() + " has been registered successfully. Now you should login for shopping");
            modelAndView.addObject("userView", userRegistrationDto);

            modelAndView.setViewName("/registration");
            return modelAndView;
        }
    }

}

