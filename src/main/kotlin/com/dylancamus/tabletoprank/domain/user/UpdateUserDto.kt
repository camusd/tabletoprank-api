package com.dylancamus.tabletoprank.domain.user

import javax.validation.constraints.Email


data class UpdateUserDto(@Email val email: String?, val password: String?,
                         val firstName: String?, val lastName: String?,
                         val confirmed: Boolean?)
