package com.dylancamus.tabletoprank.web

import com.dylancamus.tabletoprank.domain.user.CreateUserDto
import com.dylancamus.tabletoprank.domain.user.UpdateUserDto
import com.dylancamus.tabletoprank.service.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(val service: UserService) {

    @GetMapping("")
    fun getUser() = service.getUser()

    @PostMapping("")
    fun createUser(@RequestBody dto: CreateUserDto) = service.createUser(dto)

    @PutMapping("")
    fun updateUser(@RequestBody dto: UpdateUserDto) = service.updateUser(dto)
}
