package se.omegapoint.kompetensdag.businessapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BusinessApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(BusinessApplication.class, args);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated()
				.and()
				.x509()
				.and()
				.authenticationProvider(authenticationProvider());
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new AuthenticationProvider() {
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				var cert = (X509Certificate)authentication.getCredentials();
				try {
					cert.checkValidity();
				} catch (CertificateExpiredException | CertificateNotYetValidException e) {
					throw new UnauthorizedException("Unauthorized:", e);
				}

				var certFields = X509Fields.fromPrincipal(cert.getSubjectX500Principal());


				if (certFields.organization().equals("Omegapoint") &&
					certFields.email().matches("[a-zA-Z\\d.-]*@omegapoint\\.se") &&
					certFields.locality().equals("Umea")
				) {
					return new SuccessfulCertificateAuthentication(authentication);
				}

				return authentication;
			}

			@Override
			public boolean supports(Class<?> authentication) {
				return true;
			}
		};
	}
}
