package com.dylancamus.tabletoprank.domain.user

import javax.validation.constraints.Email


data class AuthorizationDto(@Email val email: String, val password: String)
