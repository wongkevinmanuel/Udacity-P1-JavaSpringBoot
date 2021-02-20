package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.modelo.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchivoService {
    @Autowired
    FilesMapper archivoMapper;

    public ArchivoService(FilesMapper fM){
        this.archivoMapper = fM;
    }

    public Files archivo(Integer archivoId){
        return archivoMapper.archivo(archivoId);
    }

    public String buscar(Integer userId,String nombreArchivo){
        return archivoMapper.nombreArchivo(userId,nombreArchivo);
    }

    public List<Files> archivos(Integer userId){
        return archivoMapper.todosArchivos(userId);
    }

    public Integer guardar(Files archivo){
        return archivoMapper.guardarArchivo(archivo);
    }

    public Integer actualizar(Files archivo){
        return archivoMapper.actualizarArchivo(archivo);
    }

    public Integer eliminar(Integer idArchivo){
        return archivoMapper.eliminarArchivo(idArchivo);
    }

}
