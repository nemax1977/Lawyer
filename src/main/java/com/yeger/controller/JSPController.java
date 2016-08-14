package com.yeger.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("session")
public class JSPController {

    @RequestMapping("/")
    public String main(Model model) {
        model.addAttribute("russian", "Добрый день");
        return "index";
    }

}