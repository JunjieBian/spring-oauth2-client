package com.adley.oauth.client.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String root() {return "redirect:/index";}

    @RequestMapping("/index")
    public String index() {return "index";}

    @RequestMapping("/user/index")
    public String userIndex() {return "user/index";}

    @RequestMapping("/toauth/authpwd")
    public String passwordToken() {return "oauth/authpwd";}

    @RequestMapping("/toauth/codeauth")
    public String codeToken() {return "oauth/codeauth";}

    @RequestMapping("/login")
    public String login() {return "login";}

    @RequestMapping("/login-error")
    public String loginError(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            errorMessage = ((Throwable)session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION")).getMessage();
        }
        model.addAttribute("errorMessage",errorMessage);
        model.addAttribute("loginError",true);
        return "login";
    }
}
