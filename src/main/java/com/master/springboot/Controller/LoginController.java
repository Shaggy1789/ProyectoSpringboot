package com.master.springboot.Controller;

import com.master.springboot.Models.Usuarios;
import com.master.springboot.service.AuthCaptchaService;
import com.master.springboot.service.ServiceUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class LoginController {

    @Autowired
    private ServiceUsuarios serviceUsuarios;

    @Autowired
    private AuthCaptchaService authCaptchaService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("login")
    public String login(@RequestParam String nombre,@RequestParam String password,@RequestParam("g-recaptcha-response") String recaptchaResponse) {

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

}
