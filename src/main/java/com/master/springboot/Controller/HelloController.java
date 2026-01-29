package com.master.springboot.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HelloController {

    @GetMapping("/holamundo")
    public String holamundo(Model model){
        model.addAttribute("mensaje", "Hola Mundo desde Springboot");
        return "hola";
    }

    @GetMapping("/error")
    public String error(Model model){
        model.addAttribute("mensaje","Tenemos incoveniencias en la pagina, por favor espere");
        return "error";
    }

//    @GetMapping("/holamundo")
//    public Map<String,String> foo(){
//        Map<String,String> json = new HashMap<>();
//        json.put("Message","Hola mundo desde springboot");
//        return json;
//    }

}
