package com.daw.contafin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.daw.contafin.security.UserRepositoryAuthenticationProvider;

@Configuration
@Order(1)
@CrossOrigin(maxAge =3600)
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	public  UserRepositoryAuthenticationProvider authenticationProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.antMatcher("/api/**");
		
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/login").authenticated();
		
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/signup").permitAll();
		
		// URLs that need authentication to access to it
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/User/").hasAnyRole("ADMIN","USER");	
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/User/*").hasAnyRole("ADMIN","USER");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/User/Photo").hasAnyRole("ADMIN","USER");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/User/*/Photo").hasAnyRole("ADMIN","USER");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/User/*").hasRole("USER");

		http.authorizeRequests().antMatchers("/api/Admin/*").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/Admin/UserData").hasRole("ADMIN");	
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/Admin/UserData/*").hasRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/Admin/UserData/Excel").hasRole("ADMIN");
		
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/Unit/").hasRole("ADMIN");	
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/Unit/*/Images").hasRole("ADMIN");	
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/Unit/*").hasRole("ADMIN");	
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/Unit/*").hasRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/Unit/NumberOfUnitsCompleted").hasAnyRole("USER");
		
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/Unit/*/Lesson/*").hasRole("ADMIN");
		
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/Unit/*/Lesson/*/Exercise/*").hasRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/Unit/*/Lesson/*/Exercise/*/Answer").hasRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/Unit/*/Lesson/*/Exercise/*/Answer").hasRole("ADMIN");
		
		// Other URLs can be accessed without authentication
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/Unit/*/Lesson/*/Exercise/*/Solution").permitAll();
		http.authorizeRequests().anyRequest().permitAll();
		
		// Disable CSRF protection (it is difficult to implement with ng2)
		http.csrf().disable();

		// Use Http Basic Authentication
		http.httpBasic();

		// Do not redirect when logout
		http.logout().logoutSuccessHandler((rq, rs, a) -> {	});
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// Database authentication provider
		auth.authenticationProvider(authenticationProvider);
	}
}