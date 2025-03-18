package ru.kuz.education.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kuz.education.auth.model.MyUser;
import ru.kuz.education.auth.service.AuthService;


@RestController
@RequestMapping("")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class); // Логгер

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public ModelAndView login(
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        log.info("Отработал метод login()");
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы.");
        }
        return new ModelAndView("login");
    }

    @GetMapping("/register")
    public ModelAndView register() {
        log.info("Отработал метод register()");
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute MyUser user, RedirectAttributes redirectAttributes) {
        log.info("Register User");
        try {
            log.info(user.toString());
            authService.registerUser(user);
            redirectAttributes.addFlashAttribute("message", "Пользователь успешно зарегистрирован!");

            return new ModelAndView("redirect:/login");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return new ModelAndView("redirect:/register");
        }
    }

}
