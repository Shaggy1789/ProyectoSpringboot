package com.master.springboot.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roles {
    @Id
    private int id;
    private String nombre;

    @OneToMany
    private List<Usuarios> usuarios;
}
