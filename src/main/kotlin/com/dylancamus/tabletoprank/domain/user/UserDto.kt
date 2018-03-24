package com.dylancamus.tabletoprank.domain.user

import javax.validation.constraints.Email


data class UserDto(val id: Long, @Email val email: String,
                   val firstName: String, val lastName: String,
                   val confirmed: Boolean)
