package com.example.todomono.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacadeInterface {

    Authentication getAuthentication();

}
