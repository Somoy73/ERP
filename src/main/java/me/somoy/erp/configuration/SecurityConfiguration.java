package me.somoy.erp.configuration;


import me.somoy.erp.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthUserService authUserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
            auth.userDetailsService(authUserService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http.httpBasic().authenticationEntryPoint(new BasicAuthenticationEntryPoint());
        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/users**").hasAnyAuthority("USER", "EMPLOYEE", "ADMIN")
                .antMatchers(HttpMethod.POST,"/api/register").hasAnyAuthority("ADMIN", "EMPLOYEE")
                .antMatchers(HttpMethod.PUT,"/api/edit/**").hasAnyAuthority("ADMIN", "EMPLOYEE")
                .antMatchers(HttpMethod.DELETE, "/api/delete/**").hasAuthority("ADMIN")
                .antMatchers("/**/*.js", "/**/*.css").permitAll()
                .antMatchers("/")
                .hasAnyAuthority("EMPLOYEE","ADMIN","USER")
                .antMatchers("/edit/**", "/addUser/**", "/addUser").hasAnyAuthority("EMPLOYEE", "ADMIN")
                .antMatchers("/delete/**").hasAnyAuthority("ADMIN")
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll()
                .and().logout().invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login");
//                .and()
//                .cors(cors -> cors.disable())

        ;
        http.csrf().disable();
        //http.cors().disable();

    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}


