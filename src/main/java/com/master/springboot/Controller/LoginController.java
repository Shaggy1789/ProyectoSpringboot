package com.master.springboot.Controller;

import com.master.springboot.Models.Roles;
import com.master.springboot.Models.Usuarios;
import com.master.springboot.service.AuthCaptchaService;
import com.master.springboot.service.ServiceUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private ServiceUsuarios serviceUsuarios;

    @Autowired
    private AuthCaptchaService authCaptchaService;

    // ========== MÉTODOS GET (PARA MOSTRAR PÁGINAS) ==========

    @GetMapping("/login")
    public String mostrarLogin(Model model,
                               @RequestParam(required = false) String error,
                               @RequestParam(required = false) String registro,
                               @RequestParam(required = false) String usuario) {
        model.addAttribute("titulo", "Inicio de Sesión");

        if (error != null) {
            model.addAttribute("error", "Error en el inicio de sesión");
        }
        if (registro != null && registro.equals("exitoso")) {
            model.addAttribute("mensajeExito", "¡Registro exitoso! Ahora puedes iniciar sesión.");
        }
        if (usuario != null) {
            model.addAttribute("usuarioRegistrado", usuario);
        }

        return "login"; // Renderiza login.html
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model){
        model.addAttribute("titulo", "Registro de Usuarios");
        return "registro"; // Renderiza registro.html
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model){
        model.addAttribute("titulo", "Dashboard");
        return "dashboard"; // Renderiza dashboard.html
    }

    @GetMapping("/error")
    public String mostrarError(Model model){
        model.addAttribute("mensaje","Tenemos inconveniencias en la página, por favor espere");
        return "error"; // Renderiza error.html
    }

    @GetMapping("/holamundo")
    public String holamundo(Model model){
        model.addAttribute("mensaje", "Hola Mundo desde Springboot");
        return "hola"; // Renderiza hola.html
    }

    // ========== MÉTODOS POST (PARA PROCESAR FORMULARIOS) ==========

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String nombre,
            @RequestParam String password,
            @RequestParam("g-recaptcha-response") String recaptchaResponse,
            RedirectAttributes redirectAttributes) {

        // Validar CAPTCHA
        if(!authCaptchaService.verifyRecaptcha(recaptchaResponse)){
            redirectAttributes.addAttribute("error", "captcha");
            return "redirect:/login?error=captcha";
        }

        List<Usuarios> usuarios = serviceUsuarios.findAll();

        for (Usuarios usuario : usuarios) {
            if (usuario.getNombre().equals(nombre)) {
                String hashedPassword = md5(password);

                if (usuario.getPassword() != null &&
                        usuario.getPassword().equals(hashedPassword)) {

                    // Redirigir al dashboard si el login es exitoso
                    return "redirect:/dashboard";
                } else {
                    redirectAttributes.addAttribute("error", "password");
                    return "redirect:/login?error=password";
                }
            }
        }

        redirectAttributes.addAttribute("error", "usuario");
        return "redirect:/login?error=usuario";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombre,
            @RequestParam String apellidopaterno,
            @RequestParam String apellidomaterno,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam int telefono,
            @RequestParam("g-recaptcha-response") String recaptchaResponse,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validar CAPTCHA
        if(!authCaptchaService.verifyRecaptcha(recaptchaResponse)){
            model.addAttribute("Error","CAPTCHA inválido, por favor trata de nuevo");
            return "registro";
        }

        // Verificar si el usuario ya existe
        List<Usuarios> usuarios = serviceUsuarios.findAll();
        for (Usuarios usuario : usuarios) {
            if (usuario.getNombre().equals(nombre)) {
                model.addAttribute("Error","El usuario ya existe");
                return "registro";
            }
        }

        try {
            Usuarios nuevoUsuario = new Usuarios();

            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidopaterno(apellidopaterno);
            nuevoUsuario.setApellidomaterno(apellidomaterno);
            nuevoUsuario.setEmail(email); // Nota: Tu modelo Usuarios no tiene campo email, ¿necesitas agregarlo?
            nuevoUsuario.setPassword(md5(password));
            nuevoUsuario.setTelefono(telefono);

            Roles role = new Roles();
            role.setId(3);
            role.setNombre("Usuario");
            nuevoUsuario.setRole(role);

            // Guardar el usuario
            serviceUsuarios.save(nuevoUsuario);

            // Redirigir al login con mensaje de éxito
            redirectAttributes.addAttribute("registro", "exitoso");
            redirectAttributes.addAttribute("usuario", nombre);
            return "redirect:/login";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("Error", "Error al registrar: " + e.getMessage());
            return "registro";
        }
    }

    // ========== MÉTODO AUXILIAR ==========

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