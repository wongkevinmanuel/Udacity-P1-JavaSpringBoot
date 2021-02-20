package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    private final UsersService usuarioServicio;

    public LoginController(UsersService uS){
        this.usuarioServicio = uS;
    }

    @RequestMapping("/login")
    public String irPaginaLogin(Model modelo){
        modelo.addAttribute("estadoAutenticacionError", false);
        modelo.addAttribute("estadoAutenticacionSalida", false);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return "login";

        if(authentication instanceof AnonymousAuthenticationToken)
            return "login";

        return "redirect:/home";
    }

    @RequestMapping("/login-error")
    public String loginError(Model modelo){
        modelo.addAttribute("estadoAutenticacionError", true);
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(Model model){
        model.addAttribute("estadoAutenticacionSalida", true);
        return "login";
    }
}
