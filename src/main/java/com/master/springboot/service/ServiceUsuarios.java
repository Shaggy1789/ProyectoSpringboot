package com.master.springboot.service;

import com.master.springboot.Models.Usuarios;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public interface ServiceUsuarios {
    public List<Usuarios> findAll();
    public Usuarios save(Usuarios nuevoUsuario);
    //public List<Usuarios> findByNombre(String nombre);
}
