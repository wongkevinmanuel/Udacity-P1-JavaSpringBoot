package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.modelo.Users;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UsersService {
    private final UsersMapper usuarioMapper;
    private final HashService hashService;

    public UsersService(UsersMapper uM,HashService hS){
        this.usuarioMapper = uM;
        this.hashService = hS;
    }

    public boolean usuarioDisponible(String usuarioNombre){
        boolean disponible = usuarioMapper.obtenerUsuario(usuarioNombre) == null
                             ? true : false;
        return disponible;
    }

    public int crearUsuario(Users usuario){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];

        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(usuario.getPassword(),encodedSalt);
        Users usuarioNuevo = new Users(null,usuario.getUserName()
                                ,encodedSalt,hashedPassword
                                ,usuario.getFirstName(),usuario.getLastName());
        return usuarioMapper.guardarUsuario( usuarioNuevo);
    }

    public Users obtenerUsuario(String usuarioNombre){
        return usuarioMapper.obtenerUsuario(usuarioNombre);
    }

    public int obtenerIdusuario(String usuarioNombre){
        return usuarioMapper.obtenerUsuario(usuarioNombre).getUserId();
    }
}
