package com.security.security.service;

import com.security.security.SecurityApplication;
import com.security.security.model.Client;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${securirty.jwt.expiracao}")
    private String expiracao;

    @Value("${security.jwt.chave-assinatura}")
    private String chaveAssinatura;

    public String gerarToken(Client client){
        long expString = Long.valueOf(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);
        return Jwts.builder()
                .setSubject(client.getUsername())//Colocar o login do usuario para saber quem ta fazendo a requisição
                .setExpiration(data).signWith(SignatureAlgorithm.HS512, chaveAssinatura).compact();
    }

    public static void main(String[]args){
        ConfigurableApplicationContext context = SpringApplication.run(SecurityApplication.class);
        JwtService service = context.getBean(JwtService.class);
        Client client = Client.builder().username("test").build();
        String token = service.gerarToken(client);
        System.out.println(token);
    }
}
