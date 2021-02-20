package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.modelo.Credentials;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("Select * from CREDENTIALS where userid=#{userId};")
    List<Credentials> todasCredenciales(Integer userId);

    @Insert("Insert into CREDENTIALS (url,username,key,password,userid) values (#{url},#{userName},#{key},#{password},#{userId});")
    @Options(useGeneratedKeys = true,keyProperty = "credentialId")
    int guardarCredencial(Credentials credentials);

    @Update("Update CREDENTIALS set url=#{url},username=#{userName},key=#{key},password=#{password} where credentialid=#{credentialId};")
    int actualizarCredencial(Credentials credentials);

    @Delete("Delete from CREDENTIALS where credentialid=#{credentialId};")
    int eliminarCredencial(Integer credentialId);
}
