package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.modelo.Files;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FilesMapper {

    @Select("Select * from FILES where fileId=#{archivoId};")
    Files archivo(Integer archivoId);

    @Select("Select filename from FILES where userid=#{userId} and filename=#{nombreArchivo};")
    String nombreArchivo(Integer userId,String nombreArchivo);

    @Select("Select * from FILES where userid=#{userId};")
    List<Files> todosArchivos(Integer userId);

    @Insert("Insert into FILES (filename,contenttype,filesize,userid,filedata) values (#{fileName},#{contentType},#{fileSize},#{userId},#{fileData});")
    @Options(useGeneratedKeys = true,keyProperty = "fileId")
    int guardarArchivo(Files archivo);

    @Delete("Delete FILES where fileId=#{idArchivo}")
    int eliminarArchivo(Integer idArchivo);

    @Update("Update FILES set filename=#{fileName},contenttype=#{contentType},filesize=#{fileSize},userid=#{userId},filedata=#{fileData};")
    int actualizarArchivo(Files archivo);

}
