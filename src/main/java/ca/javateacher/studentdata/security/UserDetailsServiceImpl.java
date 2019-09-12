package ca.javateacher.studentdata.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service("userDetailsServiceImpl")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private LoginDataService loginDataService;

    @Autowired
    public UserDetailsServiceImpl(LoginDataService loginDataService) {

        this.loginDataService = loginDataService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        logger.trace("loadUserByUsername() is called");
        if(loginDataService.userExists(login)){
            logger.trace("user " + login + " is found");
            String password = loginDataService.getPassword(login);
            return new User(login,password,getAuthorities(login));
        }else{
            logger.trace("user " + login + " is not found");
            throw new UsernameNotFoundException("Login " + login + " is not found");
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String login)
    {
        logger.trace("getAuthorities() is called");
        List<String> listOfRoles = loginDataService.getAllRoles(login);
        String[] arrayOfRoles = listOfRoles.toArray(new String[listOfRoles.size()]);
        logger.trace("roles for login=" +
                login + ":[" + String.join(",", arrayOfRoles) + "]");
        return AuthorityUtils.createAuthorityList(arrayOfRoles);
    }

}
