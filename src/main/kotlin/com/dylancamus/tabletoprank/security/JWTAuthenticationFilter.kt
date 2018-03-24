package com.dylancamus.tabletoprank.security

import com.dylancamus.tabletoprank.domain.user.AuthorizationDto
import com.dylancamus.tabletoprank.security.SecurityConstraints.Companion.EXPIRATION_TIME
import com.dylancamus.tabletoprank.security.SecurityConstraints.Companion.HEADER_STRING
import com.dylancamus.tabletoprank.security.SecurityConstraints.Companion.TOKEN_PREFIX
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(private val authManager: AuthenticationManager,
                              private val secret: String?) : UsernamePasswordAuthenticationFilter() {

    private val mapper = jacksonObjectMapper()

    override fun attemptAuthentication(request: HttpServletRequest,
                                       response: HttpServletResponse): Authentication {

        val creds = mapper.readValue(request.inputStream, AuthorizationDto::class.java)
        return authManager
                .authenticate(UsernamePasswordAuthenticationToken(creds.email, creds.password, emptyList()))
    }

    override fun successfulAuthentication(request: HttpServletRequest,
                                          response: HttpServletResponse,
                                          chain: FilterChain,
                                          authResult: Authentication) {

        val token = Jwts.builder()
                .setSubject((authResult.principal as User).username)
                .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, secret?.toByteArray())
                .compact()
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token)
    }
}