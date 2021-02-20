package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.modelo.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class CredentialsController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private CredentialService credencialServicio;

    @Autowired
    private UsersService usuarioServicio;

    public CredentialsController(CredentialService cS, UsersService uS, MessageSource ms){
        this.credencialServicio =cS;
        this.usuarioServicio = uS;
        this.messageSource = ms;
    }

    private int actualizarCredencial(Credentials credencial){
        return credencialServicio.actualizarCredencial(credencial);
    }

    private int guardarCredencial(Credentials credencial){
        return credencialServicio.guardarCredencial(credencial);
    }

    private boolean esVacio(String valor){
        return valor.trim().isEmpty() || valor.isBlank();
    }

    @PostMapping("/guardarCredencial")
    public String guardarCredencial(Authentication auth
                                    , @ModelAttribute("formCredential") Credentials formCredential
                                    , Model modelo){
        if(esVacio(formCredential.getUrl()  ) || esVacio(formCredential.getUserName())
                || esVacio(formCredential.getPassword())){
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje", messageSource.getMessage("datos.incorrectos",null
                                                            , Locale.getDefault()));
            return "result";
        }

        boolean guardar = formCredential.getCredentialId() == null
                            ? true: false;

        if(guardar)
        {
            formCredential.setUserId(usuarioServicio.obtenerIdusuario(auth.getName()));
            if(guardarCredencial(formCredential) != 1){
                modelo.addAttribute("error", true);
                modelo.addAttribute("mensaje", messageSource.getMessage("credencial.guardar.error",null, Locale.getDefault()));
            }else
            {
                modelo.addAttribute("success",true);
                modelo.addAttribute("mensaje", "Url: " + formCredential.getUrl() + " , save.");
            }
            return "result";
        }else{
            if(actualizarCredencial(formCredential) !=1 ){
                modelo.addAttribute("error", true);
                modelo.addAttribute("mensaje", messageSource.getMessage("credencial.guardar.error",null, Locale.getDefault()));
            }else{
                modelo.addAttribute("success",true);
                modelo.addAttribute("mensaje", "Url: " + formCredential.getUrl() + " , updated.");
            }
            return "result";
        }
    }

    @GetMapping("/eliminarCredencial")
    public String eliminarCredencial(@RequestParam(value="credentialId", required = true)
                                     Integer credentialId, Model modelo){
        boolean credencialEliminada = credencialServicio.eliminarCredencial(credentialId) != 1;
        if (credencialEliminada)
        {
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje",messageSource.getMessage("credencial.eliminar.error",null,Locale.getDefault()));
        }else
        {
            modelo.addAttribute("success",true);
            modelo.addAttribute("mensaje",messageSource.getMessage("credencial.guardar", null,Locale.getDefault()));
        }
        return "result";
    }
}
