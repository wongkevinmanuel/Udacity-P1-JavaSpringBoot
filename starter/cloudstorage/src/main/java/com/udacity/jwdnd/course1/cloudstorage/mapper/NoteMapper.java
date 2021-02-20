package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.modelo.Notes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("Select * from NOTES where noteid=#{noteId};")
    Notes obtenerNota(Integer noteId);

    @Select("Select * from NOTES where notetitle=#{title} and notedescription=#{descripcion};")
    Notes buscarNota(String title, String descripcion);

    @Select("Select * from NOTES where userid=#{userId};")
    List<Notes> todasNotas(Integer userId);

    @Update("Update NOTES set notetitle=#{noteTitle},notedescription=#{noteDescription} Where noteid=#{noteId};")
    int actualizarNota(Notes nota);

    @Delete("Delete from NOTES where noteid=#{noteId};")
    int eliminarNota(Integer noteId);

    @Insert("Insert into NOTES (notetitle,notedescription,userid) values (#{noteTitle},#{noteDescription},#{userId});")
    @Options(useGeneratedKeys = true, keyProperty = "noteId" )
    int guardarNota(Notes nota);
}
