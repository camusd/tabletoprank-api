package com.dylancamus.tabletoprank.service.user

import com.dylancamus.tabletoprank.domain.user.CreateUserDto
import com.dylancamus.tabletoprank.domain.user.UpdateUserDto
import com.dylancamus.tabletoprank.domain.user.UserDto

interface UserService {

    fun getUser(): UserDto

    fun createUser(user: CreateUserDto): UserDto

    fun updateUser(user: UpdateUserDto): UserDto
}
