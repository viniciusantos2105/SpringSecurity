package com.security.security.service;

import com.security.security.model.Client;
import com.security.security.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ClientServicImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ClientRepository clientRepository;

    public boolean findByUsername(String username){
        if(clientRepository.findByUsername(username).isPresent()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean findByCpf(String cpf){
        if(clientRepository.findByCpf(cpf).isPresent()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
       Client client = clientRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Usuario inválido"));

       String[] roles = client.isAdmin() ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};

       return User.builder().username(client.getUsername()).password(client.getSenha()).roles().build();
    }

    @Transactional
    public Client salvar(Client client){
       return clientRepository.save(client);
    }
}
