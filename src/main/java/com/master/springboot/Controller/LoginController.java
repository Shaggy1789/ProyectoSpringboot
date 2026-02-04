package com.master.springboot.Controller;

import com.master.springboot.Models.Roles;
import com.master.springboot.Models.Usuarios;
import com.master.springboot.service.AuthCaptchaService;
import com.master.springboot.service.ServiceUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private ServiceUsuarios serviceUsuarios;

    @Autowired
    private AuthCaptchaService authCaptchaService;

    @GetMapping("/Login")
    public String Login(){
        return "Login";
    }

    @PostMapping("Login")
    public String Login(@RequestParam String nombre, @RequestParam String password, @RequestParam("g-recaptcha-response") String recaptchaResponse) {

        if(!authCaptchaService.verifyRecaptcha(recaptchaResponse)){
            return "Error: CAPTCHA inválido";
        }

        List<Usuarios> usuarios = serviceUsuarios.findAll();

        for (Usuarios usuario : usuarios) {
            if (usuario.getNombre().equals(nombre)) {
                // Encriptación básica con MD5 (para demostración)
                String hashedPassword = md5(password);

                if (usuario.getPassword() != null &&
                        usuario.getPassword().equals(hashedPassword)) {

                    return "Bienvenido " + usuario.getNombre() + " " +
                            usuario.getApellidopaterno() + " " + usuario.getApellidomaterno();
                } else {
                    return "Error: Contraseña incorrecta";
                }
            }
        }

        return "Usuario no encontrado";
    }
    private String md5(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            java.math.BigInteger no = new java.math.BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping
    public String mostrarRegistro(Model model){
     model.addAttribute("Titulo","Registro de usuarios");
     return "Registro";
    }

    @PostMapping
    public String registro(@RequestParam String nombre,
                           @RequestParam String apellidopaterno,
                           @RequestParam String apellidomaterno,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam int telefono,
                           @RequestParam("g-recaptcha-response") String recaptchaResponse,
                           Model model){
        if(!authCaptchaService.verifyRecaptcha(recaptchaResponse)){
            model.addAttribute("Error","CAPTCHA invalido, porfavor trata denuevo");
            return  "registro";
        }

        List<Usuarios> usuarios = serviceUsuarios.findAll();
        for (Usuarios usuario : usuarios) {
            if (usuario.getNombre().equals(nombre)) {
                model.addAttribute("Error","Usuario ya existe");
                return  "registro";
            }
        }
        try{
            Usuarios nuevoUsuario= new Usuarios();

            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidopaterno(apellidopaterno);
            nuevoUsuario.setApellidomaterno(apellidomaterno);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(md5(password));
            nuevoUsuario.setTelefono(telefono);

            Roles role = new Roles();
            role.setId(3);
            role.setNombre("Usuario");

            nuevoUsuario.setRole(role);

            Usuarios usuarioGuardado = serviceUsuarios.save(nuevoUsuario);
        }catch (Exception e){
            return  "error";
        }
        return "registro";
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model){
        model.addAttribute("titulo", "Dashboard");
        return "dashboard";
    }




}
