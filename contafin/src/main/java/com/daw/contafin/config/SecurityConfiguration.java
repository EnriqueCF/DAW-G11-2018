package com.daw.contafin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.daw.contafin.security.UserRepositoryAuthenticationProvider;



@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
    public UserRepositoryAuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
   
    			//Private pages
    			http.authorizeRequests().antMatchers("/User/*").hasAnyRole("ADMIN","USER");
    			http.authorizeRequests().antMatchers("/Admin/*").hasAnyRole("ADMIN");
    			http.authorizeRequests().antMatchers("/UpdateExercise").hasAnyRole("ADMIN");
    			http.authorizeRequests().antMatchers("/UnitCreation").hasAnyRole("ADMIN");
    			
    			//Public pages
    			http.authorizeRequests().antMatchers("index").permitAll();
    			http.authorizeRequests().antMatchers("/home").permitAll();
    			http.authorizeRequests().antMatchers("/Unit/*/lessons").permitAll();
    			http.authorizeRequests().antMatchers("/Unit/*/lessons/*/Exercise/*/*").permitAll();
    			http.authorizeRequests().antMatchers("/Unit/*/lessons").permitAll();
    			http.authorizeRequests().antMatchers("/Unit/*/lessons/*/Exercise/*/*/Completed").permitAll();
    			http.authorizeRequests().antMatchers("/lesson/*/lessonCompleted/").permitAll();
    			http.authorizeRequests().antMatchers("/continueLesson").permitAll();
    			http.authorizeRequests().antMatchers("/User/DeleteAcount").permitAll();
    			
    			// Login form
    	        http.formLogin().loginPage("/");
    	        http.formLogin().usernameParameter("email");
    	        http.formLogin().passwordParameter("pass");
    	        http.formLogin().defaultSuccessUrl("/home");
    	        http.formLogin().failureUrl("/loginerror");
    	        
    	        // Logout
    	        http.logout().logoutUrl("/logout");
    			http.logout().logoutSuccessUrl("/");
    	        
    			// Disable CSRF at the moment
    			http.csrf().disable();
    	        
    	        //Use Http Basic Authentication
    	        http.httpBasic();

    			
    			
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        // Database authentication provider
        auth.authenticationProvider(authenticationProvider);
    }

}

