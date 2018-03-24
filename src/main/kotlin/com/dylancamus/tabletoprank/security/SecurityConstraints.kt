package com.dylancamus.tabletoprank.security

class SecurityConstraints {

    companion object {
        const val EXPIRATION_TIME = 864_000_000
        const val TOKEN_PREFIX = "Bearer "
        const val HEADER_STRING = "Authorization"
        const val SIGN_UP_URL = "/api/user"
    }
}