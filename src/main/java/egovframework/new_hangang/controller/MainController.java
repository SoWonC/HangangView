package egovframework.new_hangang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class MainController {

    @RequestMapping("/main.do")
    public String mainView() {
        return "main";  // /WEB-INF/views/main.jsp
    }
}