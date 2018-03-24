package com.dylancamus.tabletoprank.security

import com.dylancamus.tabletoprank.security.SecurityConstraints.Companion.HEADER_STRING
import com.dylancamus.tabletoprank.security.SecurityConstraints.Companion.TOKEN_PREFIX
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter(authenticationManager: AuthenticationManager,
                             private val secret: String?) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        val header: String? = request.getHeader(HEADER_STRING)
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response)
            return
        }
        val authentication = getAuthentication(request)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(HEADER_STRING)?.replace(TOKEN_PREFIX, "")
        if (token != null) {
            val user = Jwts.parser()
                    .setSigningKey(secret?.toByteArray())
                    .parseClaimsJws(token).body.subject
            if (user != null) {
                return UsernamePasswordAuthenticationToken(user, null, emptyList())
            }
        }
        return null
    }
}