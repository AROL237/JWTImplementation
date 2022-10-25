package com.Signing.JWTImplementation.Service;

import com.Signing.JWTImplementation.Model.Role;
import com.Signing.JWTImplementation.Model.User;
import com.Signing.JWTImplementation.Model.UserModel;
import com.Signing.JWTImplementation.Repository.RoleRepository;
import com.Signing.JWTImplementation.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;


@Slf4j
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user != null) {
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
        }else
            throw new UsernameNotFoundException("user not found in database");
    }
    public User add( @Valid UserModel userModel) throws Exception {
        Collection<Role> roles = new ArrayList<>();
        try{
            userModel.getRole().forEach(role -> {
                roles.add(roleRepository.findByName(role));
            });
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        User user = User.builder()
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .gender(userModel.getGender())
                .password(passwordEncoder.encode(userModel.getPassword()))
                .email(userModel.getEmail())
                .role(roles)
                .build();
        return userRepository.save(user);
    }

    public String addRole(@Valid Role role) throws Exception {
        try {
             roleRepository.save(role);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }

        return "role added";
    }

    public void update(Long id ,User user){
        if(!userRepository.findById(id).get().equals(null)){
            User update = User.builder()
                    .id(id)
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build();
            userRepository.save(update);
        }
        else
            throw new UsernameNotFoundException("username found in database");

    }
    public Page<User> getUsers(int offSet, int pageSize){
        return userRepository.findAll(PageRequest.of(offSet,pageSize));
    }


    public User getUser(String username){
        try {
           return userRepository.findByEmail(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("user not found in database");
        }
    }
}
