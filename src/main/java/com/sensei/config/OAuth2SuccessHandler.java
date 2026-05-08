package com.sensei.config;

// OAuth2SuccessHandler is disabled until Google OAuth2 credentials are configured.
// To enable:
// 1. Uncomment spring-boot-starter-oauth2-client in pom.xml
// 2. Add Google client-id/secret in application.yml
// 3. Restore the full implementation of this class
// 4. Uncomment .oauth2Login() in SecurityConfig.java

import org.springframework.stereotype.Component;

@Component
public class OAuth2SuccessHandler {
    // Placeholder - OAuth2 disabled for local dev
}
