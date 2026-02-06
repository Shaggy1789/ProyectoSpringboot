package com.master.springboot.service;

import com.master.springboot.Models.Roles;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServiceRoles {
    public List<Roles> findAll();
    public Roles save(Roles nuevoUsuario);
}
