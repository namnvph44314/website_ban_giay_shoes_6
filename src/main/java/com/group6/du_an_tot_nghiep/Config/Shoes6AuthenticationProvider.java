package com.group6.du_an_tot_nghiep.Config;

import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Shoes6AuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private TaiKhoanDao taiKhoanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    HttpSession session;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        TaiKhoan person = taiKhoanRepository.readByEmail(email);
        session.setAttribute("username", person);

        if (null != person && person.getId() > 0 && passwordEncoder.matches(pwd, person.getMatKhau())) {
            return new UsernamePasswordAuthenticationToken(email, null, getAuthorities(person.getQuyen()));
        }else{
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    private List<GrantedAuthority> getAuthorities(String role){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
