package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.modelo.Users;
import com.udacity.jwdnd.course1.cloudstorage.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller()
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private MessageSource messagesSource;

    private final UsersService usuarioServicio;

    public SignupController(UsersService uS, MessageSource ms){
        this.usuarioServicio = uS;
        this.messagesSource = ms;
    }

    @GetMapping()
    public String verPaginaSignup(){
        Authentication authentication = SecurityContextHolder.getContext()
                                                            .getAuthentication();
        if (authentication == null)
            return "signup";

        if(authentication instanceof AnonymousAuthenticationToken)
            return "signup";

        return "redirect:/home";

    }

    private boolean esVacio(String valor){
        return valor.trim().isEmpty() || valor.isBlank();
    }

    private boolean errorDatos(Users usuario){
        if (esVacio(usuario.getFirstName()) || esVacio(usuario.getLastName())
                || esVacio(usuario.getUserName()) || esVacio(usuario.getPassword()))
            return true;
        else
            return false;
    }

    @PostMapping()
    public String registrarUsuario(@ModelAttribute Users user, Model modelo){

        String errorRegistrarUsuario= null;
        if(errorDatos(user)){
            errorRegistrarUsuario = messagesSource
                                            .getMessage("datos.incorrectos"
                                                ,null
                                                , Locale.getDefault());
        }else
        {
            if(!usuarioServicio.usuarioDisponible(user.getUserName())) {
                errorRegistrarUsuario = messagesSource
                                                .getMessage("usuario.existente"
                                                        ,null
                                                        , Locale.getDefault());
            }
        }

        if(errorRegistrarUsuario != null){
                modelo.addAttribute("registroUsuarioError",true);
                modelo.addAttribute("mensajeError" , errorRegistrarUsuario);
                return "signup";
        }

        int idNuevoUsuario = -1;
        idNuevoUsuario = usuarioServicio.crearUsuario(user);
        if(idNuevoUsuario < 0)
        {
            errorRegistrarUsuario = messagesSource.getMessage(
                                         "usuario.crear.error"
                                            ,null
                                            , Locale.getDefault());
            modelo.addAttribute("registroUsuarioError",true);
            modelo.addAttribute("mensajeError" , errorRegistrarUsuario);
            return "signup";
        }else
        {
            modelo.addAttribute("registroUsuarioCorrecto",true);
            modelo.addAttribute("nombreUsuario" , user.getUserName());
            return "login";
        }
    }
}
