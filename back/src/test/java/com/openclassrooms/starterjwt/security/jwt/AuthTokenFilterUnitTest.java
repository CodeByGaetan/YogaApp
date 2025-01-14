package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterUnitTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthTokenFilter authTokenFilterUnderTest;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    String username = "test@studio.com";
    String jwt = "JsonWebToken";

    @BeforeEach
    public void init() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();

        SecurityContextHolder.clearContext();
    }

    @Test
    public void filter_request_setValidContext() throws Exception {
        request.addHeader("Authorization", "Bearer " + jwt);

        UserDetails userDetails = new UserDetailsImpl(null, username, null, null, null, null);

        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        authTokenFilterUnderTest.doFilterInternal(request, response, filterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        verify(jwtUtils).validateJwtToken(jwt);
        verify(jwtUtils).getUserNameFromJwtToken(jwt);
        verify(userDetailsService).loadUserByUsername(username);

        assertEquals(userDetails, authentication.getPrincipal());

        WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);
        assertEquals(details, authentication.getDetails());
    }

    @Test
    public void filter_unAuthRequest_notSetContext() throws Exception {

        authTokenFilterUnderTest.doFilterInternal(request, response, filterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        verify(jwtUtils, never()).validateJwtToken(jwt);
        verify(jwtUtils, never()).getUserNameFromJwtToken(jwt);
        verify(userDetailsService, never()).loadUserByUsername(username);

        assertNull(authentication);
    }

    @Test
    public void filter_invalidJwtRequest_notSetContext() throws Exception {
        request.addHeader("Authorization", "Bearer " + jwt);

        when(jwtUtils.validateJwtToken(jwt)).thenReturn(false);

        authTokenFilterUnderTest.doFilterInternal(request, response, filterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        verify(jwtUtils).validateJwtToken(jwt);
        verify(jwtUtils, never()).getUserNameFromJwtToken(jwt);
        verify(userDetailsService, never()).loadUserByUsername(username);
        
        assertNull(authentication);
    }

    @Test
    public void filter_invalidUserRequest_notSetContext() throws Exception {
        request.addHeader("Authorization", "Bearer " + jwt);

        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new UsernameNotFoundException("User not found"));

        authTokenFilterUnderTest.doFilterInternal(request, response, filterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        verify(jwtUtils).validateJwtToken(jwt);
        verify(jwtUtils).getUserNameFromJwtToken(jwt);
        verify(userDetailsService).loadUserByUsername(username);
        
        assertNull(authentication);
    }



    
}
