package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.modelo.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;

@Controller
public class NoteController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    private NoteService servicioNota;
    @Autowired
    private UsersService usuarioServicio;

    public NoteController(NoteService servicioNota, MessageSource ms) {
        this.servicioNota = servicioNota;
        this.messageSource = ms;
    }

    private int actualizarNota(Notes nota){
        return servicioNota.actualizar(nota);
    }

    private int guardarNota(Notes nota){
        return servicioNota.guardarNota(nota);
    }

    private boolean esVacio(String valor){
        return valor.trim().isEmpty() || valor.isBlank();
    }

    private boolean existeNota(Notes notaNueva){
        Notes nota = servicioNota.nota(notaNueva.getNoteTitle(),notaNueva.getNoteDescription());
        Optional<Notes> notaOptional = Optional.ofNullable(nota);
        return notaOptional.isPresent();
    }

    @PostMapping("/guardarNota")
    public String guardarNota(Authentication auth, @ModelAttribute("noteForm") Notes noteForm, Model modelo){
        if(esVacio(noteForm.getNoteTitle()) || esVacio(noteForm.getNoteDescription()) ) {
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje", messageSource.getMessage("datos.incorrectos",null, Locale.getDefault()));
            return "result";
        }

        if (noteForm.getNoteDescription().length() > 500){
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje", messageSource.getMessage("nota.descripcion.error",null, Locale.getDefault()));
            return "result";
        }

        if(existeNota(noteForm)){
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje", messageSource.getMessage("nota.existe",null, Locale.getDefault()));
            return "result";
        }

        if(noteForm.getNoteId() == null) {
            noteForm.setUserId(usuarioServicio.obtenerIdusuario(auth.getName()));
            if(guardarNota(noteForm) != 1){
                modelo.addAttribute("error", true);
                modelo.addAttribute("mensaje",messageSource.getMessage("nota.guardar.error",null,Locale.getDefault()));
            }else{
                modelo.addAttribute("success",true);
                modelo.addAttribute("mensaje", "Note Title: " + noteForm.getNoteTitle() + " , save.");
            }
            return "result";
        }

        if (actualizarNota(noteForm) != 1) {
             modelo.addAttribute("error", true);
             modelo.addAttribute("mensaje", messageSource.getMessage("nota.editar.error",null,Locale.getDefault()));
        } else {
             modelo.addAttribute("success", true);
             modelo.addAttribute("mensaje", "Note Title: " + noteForm.getNoteTitle() + " , updated.");
        }
        return "result";
    }

    private Integer convertirAInt(String numeroNString){
        Integer numero =0;

        try {
             numero = Integer.parseInt(numeroNString);
        }catch (Exception error){
            numero = -1;
        }

        return numero;
    }

    @GetMapping("/eliminarNota")
    public String eliminarNota(@RequestParam(value="notaId", required = true) Integer notaId, Model modelo){
        boolean notaEliminada = servicioNota.eliminarNota(notaId) != 1;

        if (notaEliminada){
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje", messageSource.getMessage("nota.eliminar.error",null,Locale.getDefault()));
        }else {
            modelo.addAttribute("success", true);
            modelo.addAttribute("mensaje", messageSource.getMessage("nota.eliminar",null,Locale.getDefault()));
        }

        return "result";
    }
}
