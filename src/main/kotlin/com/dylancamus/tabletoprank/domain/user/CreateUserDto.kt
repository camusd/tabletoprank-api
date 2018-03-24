package com.dylancamus.tabletoprank.domain.user

import javax.validation.constraints.Email


data class CreateUserDto(@Email val email: String, val password: String,
                         val firstName: String, val lastName: String)
