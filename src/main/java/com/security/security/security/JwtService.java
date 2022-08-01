package com.security.security.security;

import com.security.security.SecurityApplication;
import com.security.security.model.Client;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
    private String expiration;

    @Value("${security.jwt.chave-assinatura}")
    private String signatureKey;

    public String generatingToken(Client client){
        long expString = Long.valueOf(expiration);
        LocalDateTime dateHourExpiration = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dateHourExpiration.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);
        return Jwts.builder()
                .setSubject(client.getUsername())//Colocar o login do usuario para saber quem ta fazendo a requisição
                .setExpiration(data).signWith(SignatureAlgorithm.HS512, signatureKey).compact();
    }

    private Claims gettingClaims(String token) throws ExpiredJwtException {
        return Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token).getBody();
    }

    public boolean tokenValid(String token){
        try{
            Claims claims = gettingClaims(token);
            Date dateExpiration = claims.getExpiration();
            LocalDateTime localDateTime = dateExpiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(localDateTime);
        }catch (Exception e){
            return false;
        }
    }

    public String getClientUsername(String token) throws ExpiredJwtException{
        return (String) gettingClaims(token).getSubject();
    }

    public static void main(String[]args){
        ConfigurableApplicationContext context = SpringApplication.run(SecurityApplication.class);
        JwtService service = context.getBean(JwtService.class);
        Client client = Client.builder().username("test").build();
        String token = service.generatingToken(client);
        System.out.println(token);
        boolean tokenValid = service.tokenValid(token);
        System.out.println(tokenValid);
        System.out.println(service.getClientUsername(token));
    }
}
