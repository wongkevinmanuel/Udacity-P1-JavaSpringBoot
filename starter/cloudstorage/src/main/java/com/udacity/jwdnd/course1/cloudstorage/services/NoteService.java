package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.modelo.Notes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper notaMapper;

    public NoteService(NoteMapper nM){
        this.notaMapper =nM;
    }
    public List<Notes> notas(Integer userId){
        return notaMapper.todasNotas(userId);
    }

    public Notes nota(Integer notaId){
        return notaMapper.obtenerNota(notaId);
    }

    public Notes nota(String title,String descripcion){
        return notaMapper.buscarNota(title,descripcion);}

    public int actualizar(Notes nota){
        return notaMapper.actualizarNota(nota);
    }

    public int guardarNota(Notes nota){
        return notaMapper.guardarNota(nota);
    }

    public int eliminarNota(Integer notaId){
        return notaMapper.eliminarNota(notaId);
    }
}
