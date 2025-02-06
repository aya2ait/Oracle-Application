package org.example.oracle.donnees;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // Renvoie la page "security.html" avec Thymeleaf
    @GetMapping("/security")
    public String showSecurityPage() {
        return "security"; // Le fichier "security.html" dans /src/main/resources/templates/
    }
}

