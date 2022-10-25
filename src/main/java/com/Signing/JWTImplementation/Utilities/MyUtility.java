package com.Signing.JWTImplementation.Utilities;

import com.Signing.JWTImplementation.Model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class MyUtility {

  public static  Algorithm algorithm = Algorithm.HMAC256("Hello".getBytes());


    public static List<String> roleList(Collection<GrantedAuthority> roles){
        List<String> list = new ArrayList<>();
       roles.forEach(role->{
           list.add(role.getAuthority());
       });
        return list;
    }


    public static Collection<SimpleGrantedAuthority> roleList(User user){
        Collection<SimpleGrantedAuthority> list = new ArrayList<>();
        user.getRole().forEach(role -> {
            list.add(new SimpleGrantedAuthority(role.getName()));
        });
        return list;
    }

    public static Collection<SimpleGrantedAuthority> roleList(String[] roles){
        Collection<SimpleGrantedAuthority> list = new ArrayList<>();
        Arrays.stream(roles).forEach(role-> list.add(new SimpleGrantedAuthority(role)));
        return list;
    }

    public static String CreateAccessToken(org.springframework.security.core.userdetails.User user, Algorithm  algorithm){
        return JWT.create()
                .withIssuer("Iues/Insam")
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000))
                .withClaim("roles",roleList(user.getAuthorities()))
                .sign(algorithm);
    }

    public static String CreateRefreshToken(org.springframework.security.core.userdetails.User user, Algorithm  algorithm){
        return JWT.create()
                .withIssuer("Iues/Insam")
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30*60*1000))
                .sign(algorithm);
    }
//
//    public static Map<?,?> ErrorMessage(String message, HttpServletResponse response){
//        response.setContentType(APPLICATION_JSON_VALUE);
//        response.setStatus(HttpStatus.FORBIDDEN.value());
//        error.put("error_message",message);
//        return error
//    }

}
