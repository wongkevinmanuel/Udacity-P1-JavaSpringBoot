package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.modelo.Users;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthenticationService implements AuthenticationProvider {
    private UsersMapper usuarioMapper;
    private HashService hashService;

    private String usuarioNombre;
    private String usuarioPassword;

    public AuthenticationService(UsersMapper uM, HashService hS){
        this.usuarioMapper = uM;
        this.hashService = hS;
    }

    private Authentication validarCredenciales(Users usuario)
    {
        String encodeSatl = usuario.getSalt();
        String hashedPassword = hashService.getHashedValue(usuarioPassword,encodeSatl);
        if(!usuario.getPassword().equals(hashedPassword))
            return null;

        return new UsernamePasswordAuthenticationToken(usuarioNombre,usuarioPassword
                                                        , new ArrayList<>());
    }

    private boolean esVacio(String valor){
        return valor.trim().isEmpty() || valor.isBlank();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        usuarioNombre = authentication.getName();
        usuarioPassword = authentication.getCredentials().toString();

        if (esVacio(usuarioNombre) || esVacio(usuarioPassword))
            return null;

        Users usuario = usuarioMapper.obtenerUsuario(usuarioNombre);

        Optional<Users> existeUsuario = Optional.ofNullable(usuario);
        if (!existeUsuario.isPresent())
            return null;

        Authentication usuarioAutenticado = validarCredenciales(usuario);
        return usuarioAutenticado;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
