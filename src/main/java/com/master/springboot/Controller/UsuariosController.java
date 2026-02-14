package com.master.springboot.Controller;

import com.master.springboot.Models.Usuarios;
import com.master.springboot.service.ServiceUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UsuariosController {

    @Autowired
    ServiceUsuarios serviceUsuarios;

    @GetMapping("api/usuarios")
    public List<Usuarios> MostrarUsuarios(){
        return serviceUsuarios.findAll();
    }

    @GetMapping("/{id}")
    public Usuarios ObtenerPorId(@PathVariable int id){
        return serviceUsuarios.findById(id);
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarUsuarios(@RequestParam(required = false)String query, @RequestParam(required = false)Integer rolId){
      try{
       List<Usuarios> usuarios= serviceUsuarios.findAll();

       if(query != null && !query.isEmpty()){
        String queryLower = query.toLowerCase();
        usuarios = usuarios.stream()
                .filter(u ->u.getNombreusuario().toLowerCase().contains(queryLower) ||
                        (u.getEmail() != null && u.getEmail().toLowerCase().contains(queryLower)))
                .collect(Collectors.toList());
       }

       if(rolId != null){
           usuarios = usuarios.stream().
                   filter(u -> u.getRole() != null && u.getRole().getId() == rolId)
                   .collect(Collectors.toList());
           Map<String, Object> response = new HashMap<>();
           response.put("success", false);
           response.put("data", usuarios);
           response.put("Total", usuarios.size());
           return ResponseEntity.ok(response);
       }

      }catch(Exception e){
          Map<String, Object> response = new HashMap<>();
          response.put("success", false);
          response.put("message", "Error en la busqueda" + e.getMessage());
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      }
      return null;
    }

}
