package com.Signing.JWTImplementation.Controller;

import com.Signing.JWTImplementation.Model.Role;
import com.Signing.JWTImplementation.Model.User;
import com.Signing.JWTImplementation.Model.UserModel;
import com.Signing.JWTImplementation.Service.UserService;
import com.Signing.JWTImplementation.Utilities.MyUtility;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AppController {
    @Autowired
    private UserService userService;

    @GetMapping("/users/{offSet}/{pageSize}")
    public ResponseEntity<Page<User>> getUsers(@PathVariable int offSet, @PathVariable int pageSize){
        return ResponseEntity.ok(userService.getUsers(offSet,pageSize));
    }

    @PostMapping("/register")
    public User register ( @RequestBody  UserModel userModel) throws Exception {
        return userService.add(userModel);
    }

    @PostMapping("/role")
    public ResponseEntity<String> saveRole(@RequestBody Role role) throws Exception {

         return  ResponseEntity.ok(userService.addRole(role));
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        log.info("hello refresh route");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = MyUtility.algorithm;
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                String accessToken = MyUtility.CreateAccessToken(
                        new org.springframework.security.core.userdetails.User(user.getEmail().toString(), user.getPassword(), user.getAuthorities()),
                        algorithm
                );
                Map<String, String> tokens = new HashMap<>();
                response.setContentType(APPLICATION_JSON_VALUE);
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception e){
                throw new RuntimeException(e);
//                Map<String,String> error =new HashMap<>();
//                error.put("error_message",e.getMessage());
//                response.setContentType(APPLICATION_JSON_VALUE);
//                response.setStatus(HttpStatus.FORBIDDEN.value());
//                new ObjectMapper().writeValue(response.getOutputStream(),error);

            }
        }
    }

}
