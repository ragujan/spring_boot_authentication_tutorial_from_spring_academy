# Securing a REST API with OAuth 2.0

This course is offered by [Spring Academy](https://spring.academy/courses/spring-academy-secure-rest-api-oauth2/lessons/introduction).

## Modules Overview

### Module 1: Introduction to Spring Security
- **Lab**: Contains default Spring Security configuration using HttpBasic.

### Module 2: Advanced Security with OAuth 2.0 and JWT
- **OAuth 2.0 and JWT Configuration**
- **Accessing Authentication in Spring MVC**
  - **Method Injection Way**
  - **Principal Type Conversion**
  - **Meta Annotations** (reduces boilerplate)
- **Spring Security Deep Dive: The Big Picture**
  - **The Filter Chain**
    - Each filter focuses on its own area of security expertise.
    - No filter needs to worry about downstream filters if it rejects the request.
    - Filter chains are categorized into four groups:
      1. **Defense Filters**
         - The first filters in the chain.
         - Examples: CSRF filter, Header filter.
      2. **Authentication Filters**
         - Each handles a single authentication scheme.
         - Examples:
           - `BasicAuthenticationFilter` - Handles HTTP Basic Authentication.
           - `BearerTokenAuthenticationFilter` - Handles Bearer Token Authentication.
           - `UsernamePasswordAuthenticationFilter` - Handles Form Login Authentication.
           - `AnonymousAuthenticationFilter` - Populates the context with a Null Object authentication instance.
         - Each authentication filter uses roughly the following pseudocode
           ```java
           // Note: this is pseudocode!
           if (!requestMatcher.matches(request)) {
             // skip this filter
           } else {
             Authentication token = getAuthenticationRequest(request); // <1>
             try {
               Authentication result = authenticationManager.authenticate(token); // <2> <3>
               saveToSecurityContextHolder(result);
               fireSuccessEvent(result);
               handleSuccess(result);
             } catch (AuthenticationException ex) {
               handleFailure(token);
             }
           }
           ```
           - More about the code.
             - getAuthenticationRequest(request) passess the request material into a credential
             - authenticationManager.authenticate(token) tests that credential and returns a principal and authorities
             - Then Constructs the principal and authorities
             - **Authentication** 
               - Authentication is a spring security interface that represents both an authentication token and an authentication result.
             - **AuthenticationManager** 
               - It tests an authentication token. If the tests succeeds, then the AuthenticationManager constructs an authentication result.
               - The AuthenticationManager is composed of several AuthenticationProviders, each of which handle a single authentication scheme, like authenticating a JWT.
             - **SecurityContext**
               - The SecurityContext is an object that holds the current Authentication like so:
                 - SecurityContextHolder has SecurityContext
                   - SecurityContext has Autentication
                     - Authentication has 
                       - Principal
                       - Credentials
                       - Authorities   
               - Security context can hold additional security information other than the current user.
           - **Reviewing Bearer JWT Authentication**  
             - If you look at the Spring Security terms about Bearer JWT Authentication
                 1. First, the BearerTokenAuthenticationFilter extracts the JWT into a JwtAuthenticationToken instance.
                 2. Then, it passes that authentication token to an AuthenticationManager. This AuthenticationManager holds a JwtAuthenticationProvider instance.
                 3. Next, JwtAuthenticationProvider authenticates the JWT and returns an authenticated instance of JwtAuthenticationToken that includes the parsed JWT and granted authorities.
                 4. If the authentication fails, JwtAuthenticationProvider throws an AuthenticationException. 
                 5. Finally the filter stores the authentication result in a SecurityContext instance for later use. 
      3. **Authorization Filters**
        - Once the request is deemed both safe and authenticated. then the filter chain decides if the request is authorized. It does this in the AuthorizationFilter
        - By default. Spring Security constructs an AuthorizationFilter that requires that all requests be authenticated. If the request is not authenticated then this filter rejects the request.
          - **SecurityFilterChain** Bean
            - The filter chain is represented by a single bean named SecurityFilterChain. It can hold on arbitary number of security filters that it will execute on every requests
            - The Default Bean is large and too much. It'll look like this 
                ```java
                @Bean
                SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                    http
                        .addFilter(webAsyncManagerIntegrationFilter) // infrastructure
                        .securityContext(withDefaults()) // infrastructure
                        .servletApi(withDefaults()) // infrastructure
                        .csrf(withDefaults()) // defense
                        .headers(withDefaults()) // defense
                        .logout(withDefaults()) // authentication
                        .sessionManagement(withDefaults()) // authentication
                        .requestCache(withDefaults()) // authentication
                        .formLogin(withDefaults()) // authentication
                        .httpBasic(withDefaults()) // authentication
                        .anonymous(withDefaults()) // authentication
                        .exceptionHandling(withDefaults()) // infrastructure
                        .authorizeHttpRequests((authorize) -> authorize  // authorization
                            .anyRequest().authenticated()
                        );

                    return http.build();
                }

                ```
                - These are default set by Spring Boot. when you have to refernce to HttpSecurity. you typically only need to specify your authentication and authorization rules like this
                - ```java 
                    @Bean
                    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                        http
                            .httpBasic(withDefaults()) // authentication
                            .authorizeHttpRequests((authorize) -> authorize  // authorization
                                .anyRequest().authenticated()
                        return http.build();
                    }                
                    ```
                - **The Default OAuth 2.0 Resource Server Bean**
                  - Since the application is an oAuth 2.0 resource server, it should instead declare oauth2ResourceServer for authentication instead of httpBasic, like this:
                  - ```java 
                        @Bean
                        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                            http
                                .oauth2ResourceServer((oauth2) -> oauth2  // authentication
                                    .jwt(withDefaults())
                                )
                                .authorizeHttpRequests((authorize) -> authorize  // authorization
                                    .anyRequest().authenticated()
                                );

                            return http.build();
                        }
                    ```
            
  
     
