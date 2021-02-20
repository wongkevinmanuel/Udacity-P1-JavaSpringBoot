package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class KErrorController implements ErrorController {

    @RequestMapping("/error")
    public String manejarError(HttpServletRequest request){
        Object estado = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (estado != null){
            Integer estadoCodigo = Integer.valueOf(estado.toString());

            if (estadoCodigo == HttpStatus.NOT_FOUND.value())
                return "error";
            else if(estadoCodigo == HttpStatus.INTERNAL_SERVER_ERROR.value())
                return "error";
            else if(estadoCodigo == HttpStatus.FORBIDDEN.value())
                return "error";
        }
        return "error";
    }


    @Override
    public String getErrorPath() {
        return null;
    }
}
