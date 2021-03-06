package com.service;

import com.model.Role;
import com.model.User;
import com.repository.UserRepository;
import com.сonfiguration.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    PasswordEncoder passwordEncoder = WebSecurityConfig.getPasswordEncoder();
    public Optional<User> FindById(Long id) {
        return userRepository.findById(id);
    }
    public boolean FindUser(User user)
    {
        User user1 = userRepository.findByUsername(user.getUsername());
        if(user1!=null)
        {
            return true;
        }
        return false;
    }
    public boolean redactUser(String username, long id)
    {
        User user = userRepository.findById(id).orElseThrow();
        if (user!=null)
        {
            System.out.println(username);
            user.setUsername(username);
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public List<User> allUsers()
    {
        return userRepository.findAllByRoles(Role.USER);
        //return userRepository.findAll();
    }

    public boolean ExistById(long id)
    {
        return userRepository.existsById(id);
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    public void DeleteUser(long id) {
        userRepository.delete(userRepository.findById(id).orElseThrow());
    }

    public boolean redactPatient(String name, String password, long id) {
        User user = userRepository.findById(id).orElseThrow();
        if (user!=null)
        {
            user.setUsername(name);
            user.setPassword(password);
            return true;
        }
        return false;
    }

    public boolean NewUser(User userForm, boolean checkbox) {
        userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
      //  userForm.setPassword(getPassword.encode(userForm.getPassword()));
        if(checkbox)
        {
            userForm.setRoles(Collections.singleton(Role.ADMIN));
        }
        else
        {
            userForm.setRoles(Collections.singleton(Role.USER));
        }
        userForm.setActive(true);
        userRepository.save(userForm);
        return true;
    }
    public User findUser(String username)
    {
        User user = userRepository.findByUsername(username);
        return user;
    }
}
