package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.modelo.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UsersMapper {
    @Select("Select * from USERS where username=#{username};")
    Users obtenerUsuario(String usuarioNombre);

    @Insert("Insert into USERS(username,salt,password,firstname,lastname) Values (#{userName},#{salt},#{password},#{firstName},#{lastName});")
    @Options(useGeneratedKeys = true,keyProperty = "userId")
    int guardarUsuario(Users usuarioNuevo);
}
