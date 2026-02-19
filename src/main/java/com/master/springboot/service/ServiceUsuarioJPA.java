package com.master.springboot.service;

import com.master.springboot.Models.Usuarios;
import com.master.springboot.Repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public  class ServiceUsuarioJPA implements ServiceUsuarios {

    @Autowired
    UsuariosRepository usuariosRepository;

    @Override
    public List<Usuarios> findAll() {
        return usuariosRepository.findAll();
    }

    @Override
    public Usuarios findById(int id) {
        return usuariosRepository.findById(id).get();
    }

    @Override
    public Usuarios save(Usuarios nuevoUsuario) {
        return usuariosRepository.save(nuevoUsuario);
    }

    @Override
    public void delete(int id) {
        usuariosRepository.deleteById(id);
    }
}
