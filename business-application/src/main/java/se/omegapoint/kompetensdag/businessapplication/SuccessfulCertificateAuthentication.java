package se.omegapoint.kompetensdag.businessapplication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class SuccessfulCertificateAuthentication implements Authentication {

    private Authentication authentication;

    public SuccessfulCertificateAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new OpUmeAuthority());
    }

    @Override
    public Object getCredentials() {
        return authentication.getCredentials();
    }

    @Override
    public Object getDetails() {
        return authentication.getDetails();
    }

    @Override
    public Object getPrincipal() {
        return authentication.getDetails();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Can not set false on a successful authentication");
    }

    @Override
    public String getName() {
        return authentication.getName();
    }

    public class OpUmeAuthority implements GrantedAuthority {
        @Override
        public String getAuthority() {
            return "OP_UMEA";
        }
    }
}
