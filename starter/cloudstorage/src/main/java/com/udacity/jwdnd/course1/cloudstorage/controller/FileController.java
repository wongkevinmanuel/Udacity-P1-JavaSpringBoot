package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.modelo.Files;
import com.udacity.jwdnd.course1.cloudstorage.services.ArchivoService;
import com.udacity.jwdnd.course1.cloudstorage.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

@Controller
public class FileController {
    @Autowired
    MessageSource messageSource;

    @Autowired
    private ArchivoService archivoServicio;

    @Autowired
    private UsersService usuarioService;

    public FileController(ArchivoService aS, UsersService uS, MessageSource ms){
        this.usuarioService = uS;
        this.archivoServicio = aS;
        this.messageSource =ms;
    }

    @PostMapping("/subirArchivo")
    public String guardarArchivo(@RequestParam("fileUpload")MultipartFile archivo
                                , Model modelo
                                , Authentication auth) {
        if(archivo == null){
            modelo.addAttribute("error",true);
            modelo.addAttribute("mensaje",messageSource.getMessage("archivo.seleccion.error",null, Locale.getDefault()));
            return "result";
        }

        try {
            if (archivo.getSize() > 128000){
                modelo.addAttribute("error", true);
                modelo.addAttribute("mensaje",messageSource.getMessage("archivo.tamanomaximo",null,Locale.getDefault()));
                return "result";
            }
        }catch (Exception errorTamano){
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje",messageSource.getMessage("archivo.tamanomaximo",null,Locale.getDefault()));
            return "result";
        }

        if(archivo.getOriginalFilename().isEmpty()) {
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje",messageSource.getMessage("archivo.seleccion.error",null, Locale.getDefault()));
            return "result";
        }

        Integer idUser = usuarioService.obtenerIdusuario(auth.getName());
        String archivoExiste = archivoServicio.buscar(idUser,archivo.getOriginalFilename()) == null ? "no":"si";
        if("si".equals(archivoExiste)) {
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje",messageSource.getMessage("archivo.existe",null,Locale.getDefault()));
            return "result";
        }

        Files upFile = null;
        String tamano ;
        FastByteArrayOutputStream outputStream = null;


        try (InputStream fis = archivo.getInputStream()){
            outputStream = new FastByteArrayOutputStream();
            fis.transferTo(outputStream);
            tamano = String.valueOf(outputStream.size());
        }catch (IOException  e){
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje",messageSource.getMessage("archivo.error",null,Locale.getDefault()));
            return "result";
        }

        upFile = new Files(null,archivo.getOriginalFilename()
                , archivo.getContentType(), tamano,idUser,outputStream.toByteArray());

        int archivoGuardado = archivoServicio.guardar(upFile);
        if(archivoGuardado != 1){
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje",messageSource.getMessage("archivo.guardar.error", null,Locale.getDefault()));
        }
        else{
            modelo.addAttribute("success", true);
            modelo.addAttribute("mensaje", messageSource.getMessage("archivo.guardar",null,Locale.getDefault()));
        }

        return "result";
    }

    @GetMapping("/descargarArchivo")
    public ResponseEntity<Resource> descargarArchivo(@RequestParam("idArchivo") Integer idArchivo
                                                    ,Model modelo){
        Files archivo = archivoServicio.archivo(idArchivo);
        ResponseEntity respuesta = null;
        try {
            respuesta = ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(archivo.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION ,"attachment; filename=\""+ archivo.getFileName()+"\"")
                    .body(new ByteArrayResource(archivo.getFileData()));
        }catch (Exception exception){   }
        return respuesta;
    }
    @GetMapping("eliminarArchivo")
    public String eliminarArchivo(@RequestParam("idArchivo") Integer idArchivo, Model modelo){
        Integer archivoEliminado = archivoServicio.eliminar(idArchivo);

        if (archivoEliminado != 1)
        {
            modelo.addAttribute("error", true);
            modelo.addAttribute("mensaje", messageSource.getMessage("archivo.eliminar.error",null,Locale.getDefault()));
        }
        else{
            modelo.addAttribute("success", true);
            modelo.addAttribute("mensaje",messageSource.getMessage("archivo.eliminar",null,Locale.getDefault()));
        }
        return "result";
    }
}

