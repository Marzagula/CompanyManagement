package com.gminds.auth_server.service;

import com.gminds.auth_server.model.EmployeeUser;
import com.gminds.auth_server.repository.EmployeeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class EmployeeUserDetailService implements UserDetailsService {
    private final EmployeeUserRepository employeeUserRepository;

    @Autowired
    EmployeeUserDetailService(EmployeeUserRepository employeeUserRepository){
        this.employeeUserRepository = employeeUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmployeeUser userEntity = employeeUserRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRoles()))
        );
    }
}
