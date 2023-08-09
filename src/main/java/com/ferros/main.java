package com.ferros;

import com.ferros.service.UserService;

public class main {

    public static void main(String[] args) {
        UserService userService = new UserService();
        System.out.println(userService.getById(1));

    }

}
