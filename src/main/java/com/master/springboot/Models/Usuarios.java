package com.master.springboot.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuarios {
    @Id
    private int idusuario;
    private String nombreusuario;
    private String apellidopaterno;
    private String apellidomaterno;
    private String email;
    private String password;
    private long telefono;

    @ManyToOne
    @JoinColumn(name = "roles")
    private Roles role;

    public Roles getRole() {
        return role;
    }
}
