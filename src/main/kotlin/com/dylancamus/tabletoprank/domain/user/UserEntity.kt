package com.dylancamus.tabletoprank.domain.user

import javax.persistence.*
import javax.validation.constraints.Email

@Entity
internal data class UserEntity(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long = 0,
        @Email @Column(unique = true) val email: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val confirmed: Boolean = false) {

    fun toDto(): UserDto = UserDto(
            id = this.id,
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName,
            confirmed = this.confirmed)

    companion object {

        fun fromDto(dto: CreateUserDto) = UserEntity(
                email = dto.email,
                password = dto.password,
                firstName = dto.firstName,
                lastName = dto.lastName)

        fun fromDto(dto: UpdateUserDto, user: UserEntity) = UserEntity(
                id = user.id,
                email = dto.email ?: user.email,
                password = dto.password ?: user.password,
                firstName = dto.firstName ?: user.firstName,
                lastName = dto.lastName ?: user.lastName,
                confirmed = dto.confirmed ?: user.confirmed)
    }
}
