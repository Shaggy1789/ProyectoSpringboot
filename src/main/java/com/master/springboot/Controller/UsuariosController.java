package com.master.springboot.Controller;

import com.master.springboot.Models.Usuarios;
import com.master.springboot.service.ServiceUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UsuariosController {

    @Autowired
    ServiceUsuarios serviceUsuarios;

    @GetMapping("api/usuarios")
    public List<Usuarios> MostrarUsuarios(){
        return serviceUsuarios.findAll();
    }


}
