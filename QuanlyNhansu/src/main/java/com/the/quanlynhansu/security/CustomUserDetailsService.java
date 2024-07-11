package com.the.quanlynhansu.security;


import com.the.quanlynhansu.entity.RoleEntity;
import com.the.quanlynhansu.entity.UserEntity;
import com.the.quanlynhansu.repository.RoleRepository;
import com.the.quanlynhansu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    public RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Lấy ra thông tin user theo username
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity != null) {
            Collection<RoleEntity> roles = userEntity.getRoles(); // Lấy danh sách quyền trực tiếp từ UserEntity
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    convertStrToAuthor(roles)
            );
            return userDetails;
        } else {
            throw new UsernameNotFoundException("Invalid user with username!");
        }
    }


    private Collection<? extends GrantedAuthority> convertStrToAuthor(Collection<RoleEntity> roles){
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        List<RoleEntity> roleEntities = roles.stream().toList();
        List<SimpleGrantedAuthority> roleConfigSecurity = new ArrayList<>();
        for (int i = 0; i < roles.size(); i++) {
            RoleEntity roleEntity = roleEntities.get(i);
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleEntity.getName());
            roleConfigSecurity.add(simpleGrantedAuthority);
        }
        return roleConfigSecurity;
    }
}
