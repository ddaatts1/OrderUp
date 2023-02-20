package com.FoodOrdering.OrderUp.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {


    @GetMapping("/index")
    public String index(){
        return "Do Tien Dat";
    }

    @GetMapping("/index2")
    public String index2(){
        return "index2";
    }

}
