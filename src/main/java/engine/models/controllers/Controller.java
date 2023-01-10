package engine.models.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;




    @org.springframework.stereotype.Controller
    public class Controller {

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "index";
    }

}
