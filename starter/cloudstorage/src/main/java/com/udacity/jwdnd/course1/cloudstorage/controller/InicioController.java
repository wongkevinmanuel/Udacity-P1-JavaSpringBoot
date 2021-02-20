package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.modelo.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.modelo.Files;
import com.udacity.jwdnd.course1.cloudstorage.modelo.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.ArchivoService;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class InicioController {
    @Autowired
    private UsersService usuarioService;
    @Autowired
    private NoteService notaService;
    @Autowired
    private CredentialService credencialService;
    @Autowired
    private ArchivoService archivoService;

    @GetMapping()
    public String irPaginaHome(Authentication auth, Model model){
        model.addAttribute("noteForm", new Notes());
        model.addAttribute("formCredential", new Credentials());
        model.addAttribute("credentialService",credencialService);

        Integer idUsuario = usuarioService.obtenerIdusuario(auth.getName());
        List<Notes> notas = notaService.notas(idUsuario);
        model.addAttribute("notas",notas);

        List<Files> archivos = archivoService.archivos(idUsuario);
        model.addAttribute("archivos", archivos);

        List<Credentials> credenciales = credencialService.credenciales(idUsuario);
        model.addAttribute("credentials",credenciales);
        return "home";
    }
}
