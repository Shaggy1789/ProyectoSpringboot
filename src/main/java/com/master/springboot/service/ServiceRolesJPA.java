package com.master.springboot.service;

import com.master.springboot.Models.Roles;
import com.master.springboot.Repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRolesJPA implements  ServiceRoles {

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public List<Roles> findAll() {
        return List.of();
    }

    @Override
    public Roles save(Roles nuevoRole) {
        return rolesRepository.save(nuevoRole);
    }


}
