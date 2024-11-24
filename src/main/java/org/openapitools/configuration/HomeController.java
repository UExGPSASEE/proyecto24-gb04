package org.openapitools.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public RedirectView home() {
        return new RedirectView("/login.html");
    }

}