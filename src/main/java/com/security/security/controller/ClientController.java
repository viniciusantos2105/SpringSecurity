package com.security.security.controller;

import com.security.security.model.Client;
import com.security.security.service.ClientServicImpl;
import com.security.security.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    ClientServicImpl clientServiceImpl;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ClientService clientService;

    @PostMapping("/create")
    public Client createUser(@RequestBody Client user){
        if(clientServiceImpl.findByCpf(user.getCpf()) == true || clientServiceImpl.findByUsername(user.getUsername()) == true){
            throw new UsernameNotFoundException("Nome de usuario já cadastrado ou CPF já cadastrado");
        }
        else{
            String senhaCriptografada = passwordEncoder.encode(user.getSenha());
            user.setSenha(senhaCriptografada);
            return clientServiceImpl.salvar(user);
        }
    }

    @GetMapping("/findAll")
    public List<Client> allUser(){
        return clientService.findAll();
    }
}
