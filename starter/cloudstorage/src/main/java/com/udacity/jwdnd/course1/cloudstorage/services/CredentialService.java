package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.modelo.Credentials;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credencialMapper;
    private EncryptionService encriptadorService;

    public CredentialService(CredentialMapper cM,EncryptionService eS)
    {
        this.credencialMapper = cM;
        this.encriptadorService = eS;
    }

    public List<Credentials> credenciales(Integer userId){
        return credencialMapper.todasCredenciales(userId);
    }

    public Integer eliminarCredencial(Integer credencialId){
        return credencialMapper.eliminarCredencial(credencialId);
    }

    private Credentials credencial;
    private void cifrar(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encriptadorService.encryptValue(credencial.getPassword(),encodedKey);
        credencial.setKey(encodedKey);
        credencial.setPassword(encryptedPassword);
    }

    public Integer guardarCredencial(Credentials cred){
        credencial = null;
        credencial = cred;
        cifrar();
        return credencialMapper.guardarCredencial(credencial);
    }

    public Integer actualizarCredencial(Credentials cred){
        credencial = null;
        credencial = cred;
        cifrar();
        return credencialMapper.actualizarCredencial(credencial);
    }

    public String decifrarPassword(Credentials credential){
        return encriptadorService.decryptValue(credential.getPassword(),credential.getKey());
    }
}
