package com.security.security.controller;

import com.security.security.dto.CredentialsDTO;
import com.security.security.dto.TokenDTO;
import com.security.security.exception.PasswordInvalidException;
import com.security.security.model.Client;
import com.security.security.security.JwtService;
import com.security.security.service.ClientServicImpl;
import com.security.security.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    JwtService jwtService;

    @PostMapping("/create")
    public Client createUser(@RequestBody Client user){
        if(clientServiceImpl.findByCpf(user.getCpf()) == true || clientServiceImpl.findByUsername(user.getUsername()) == true){
            throw new UsernameNotFoundException("Nome de usuario já cadastrado ou CPF já cadastrado");
        }
        else{
            String senhaCriptografada = passwordEncoder.encode(user.getPassword());
            user.setPassword(senhaCriptografada);
            return clientServiceImpl.salvar(user);
        }
    }

    @GetMapping("/findAll")
    public List<Client> allUser(){
        return clientService.findAll();
    }

    @PostMapping("/auth")
    public TokenDTO authentic(@RequestBody CredentialsDTO credentialsDTO){
        try{
            Client client = Client.builder().username(credentialsDTO.getUsername())
                                            .password(credentialsDTO.getPassword()).build();
            UserDetails userAuthentic = clientServiceImpl.authentic(client);
            String token = jwtService.generatingToken(client);
            return new TokenDTO(client.getUsername(), token);
        }catch (UsernameNotFoundException | PasswordInvalidException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
