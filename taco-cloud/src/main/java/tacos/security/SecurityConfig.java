package tacos.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource; 
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/design", "/orders")
				.access("hasRole('ROLE_USER')")
			.antMatchers("/","/**")
				.access("permitAll")
		.and()
			.httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication()
		.userSearchBase("ou=people")
		.userSearchFilter("(uid={0})")
		.groupSearchBase("ou=groups")
		.groupSearchFilter("member={0}")
		.contextSource()
		.root("dc=tacocloud,dc=com")
		.ldif("classpath:users.ldif")
		.and()
		.passwordCompare()
		.passwordEncoder(new BCryptPasswordEncoder())
		.passwordAttribute("userPasscode");
	}
}
