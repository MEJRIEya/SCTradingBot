package com.solidcode.SCTradingBot.security.user;

import com.solidcode.SCTradingBot.security.role.Role;
import com.solidcode.SCTradingBot.security.role.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.solidcode.SCTradingBot.security.user.User user = getUser(username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ALL"));
        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    public com.solidcode.SCTradingBot.security.user.User getUser(String userName) {
        com.solidcode.SCTradingBot.security.user.User user = userRepo.findUserByUsername(userName);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User not Found.");
        }
        return user;
    }

    public ApiUserPayload add(ApiUserPayload apiUserPayload) throws DataAccessException {
        String REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if(apiUserPayload.getUsername() == null || apiUserPayload.getUsername().isEmpty() || !apiUserPayload.getUsername().matches(REGEX)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Email format invalid.");
        }

        if(apiUserPayload.getPassword() == null || apiUserPayload.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password should not be empty.");
        }

        if(apiUserPayload.getCompleteName() == null || apiUserPayload.getCompleteName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "CompleteName should not be empty.");
        }

        if(apiUserPayload.getStatus() == null || apiUserPayload.getStatus().isEmpty() || !(apiUserPayload.getStatus().equals("Active") || apiUserPayload.getStatus().equals("Suspended") || apiUserPayload.getStatus().equals("Verification"))){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Status should be valid.");
        }

        if(apiUserPayload.getRolePayloads() == null || apiUserPayload.getRolePayloads().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User should have at least one role assigned.");
        }

        if (userRepo.findUserByUsername(apiUserPayload.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Email already used.");
        }

        List<Role> roles = new ArrayList<>();

        apiUserPayload.getRolePayloads().forEach(rolePayload -> {
            Role role = roleRepo.findByRoleId(rolePayload.getRoleId());
            if(role == null) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Role does not exist.");
            }
            roles.add(role);
        });

        com.solidcode.SCTradingBot.security.user.User apiUser = new com.solidcode.SCTradingBot.security.user.User(apiUserPayload.getUsername(),
                passwordEncoder().encode(apiUserPayload.getPassword()),
                apiUserPayload.getCompleteName(),
                apiUserPayload.getStatus(),
                roles);

        userRepo.save(apiUser);
        apiUserPayload.setUserId(apiUser.getUserId());
        return apiUserPayload;
    }

    public ApiUserPayload update(ApiUserPayload apiUserPayload) throws DataAccessException {

        String REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        com.solidcode.SCTradingBot.security.user.User apiUser = userRepo.findUserByUserId(apiUserPayload.getUserId());

        if(apiUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User does not exist.");
        }

        if(apiUserPayload.getPassword() != null && apiUserPayload.getConfirmPassword() != null && !apiUserPayload.getPassword().equals(apiUserPayload.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password  and Password Confirmation not matching.");
        }

        if(apiUserPayload.getPassword() != null) {
            apiUser.setPassword(passwordEncoder().encode(apiUserPayload.getPassword()));
        }

        if(apiUserPayload.getCompleteName() != null && !apiUserPayload.getCompleteName().isEmpty()) {
            apiUser.setCompleteName(apiUserPayload.getCompleteName());
        }

        if(apiUserPayload.getStatus() != null && (apiUserPayload.getStatus().equals("Active") || apiUserPayload.getStatus().equals("Suspended") || apiUserPayload.getStatus().equals("Verification"))) {
            apiUser.setStatus(apiUserPayload.getStatus());
        }

        if(apiUserPayload.getRolePayloads() != null && apiUserPayload.getRolePayloads().isEmpty()) {
            apiUser.getRoles().clear();
            apiUserPayload.getRolePayloads().forEach(rolePayload -> {
                Role role = roleRepo.findByRoleId(rolePayload.getRoleId());
                if(role == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Role does not exist.");
                }
                apiUser.getRoles().add(role);
            });
        }

        userRepo.save(apiUser);
        return apiUserPayload;
    }

    public void delete(Integer userId) throws DataAccessException {
        com.solidcode.SCTradingBot.security.user.User apiUser = userRepo.findUserByUserId(userId);
        if(apiUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User does not exist.");
        }
        userRepo.delete(apiUser);
    }

    public com.solidcode.SCTradingBot.security.user.User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authenticated.");
        }
        return getUser(authentication.getName());
    }
}
