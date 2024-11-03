package org.openapitools.api;

import javax.servlet.http.HttpSession;

import org.openapitools.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/editProfile")
    public String showEditProfilePage(HttpSession session, Model model) {
        // Suponiendo que tienes el usuario autenticado y puedes obtener su usernam
        
    	User user = (User) session.getAttribute("user");
        
        model.addAttribute("user", user); // Pasa el objeto usuario al modelo
        return "editProfile"; // Carga el template editProfile.html
    }
}
