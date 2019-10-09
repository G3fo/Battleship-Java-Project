package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
class WebSecurityAuthentication extends GlobalAuthenticationConfigurerAdapter {
  @Autowired
  PlayerRepository playerRepo;
  public void init(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(inputName-> {
      Player player = playerRepo.findByUserName(inputName);
      if (player != null) {
        return new User(player.getUserName(),player.getPassword(),
                AuthorityUtils.createAuthorityList("USER"));
      } else {
        throw new UsernameNotFoundException("Unknown user: " + inputName);
      }
    });
  }
}
