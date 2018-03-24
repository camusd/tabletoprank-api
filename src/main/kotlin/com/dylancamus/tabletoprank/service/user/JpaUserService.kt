package com.dylancamus.tabletoprank.service.user

import com.dylancamus.tabletoprank.domain.user.*
import com.dylancamus.tabletoprank.repository.UserRepository
import com.dylancamus.tabletoprank.util.RestException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
@Transactional
internal class JpaUserService(val userRepository: UserRepository,
                              val bCryptPasswordEncoder: BCryptPasswordEncoder) : UserService {

    override fun getUser(): UserDto {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.principal.toString()
        return userRepository.findByEmail(email)?.toDto()
                ?: throw EntityNotFoundException("User with email: $email not found")
    }

    override fun createUser(user: CreateUserDto): UserDto {
        try {
            val userEncrypted = user.copy(password = bCryptPasswordEncoder.encode(user.password))
            return userRepository.save(UserEntity.fromDto(userEncrypted)).toDto()
        } catch (ex: DataIntegrityViolationException) {
            throw RestException("Validation error", "${user.email} already exists")
        }
    }

    override fun updateUser(user: UpdateUserDto): UserDto {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.principal.toString()
        val currentUser = userRepository.findByEmail(email)
        val encryptedPassword = if (user.password != null) bCryptPasswordEncoder.encode(user.password) else null
        return if (currentUser != null) userRepository
                .save(UserEntity.fromDto(user.copy(password = encryptedPassword), currentUser)).toDto()
        else throw EntityNotFoundException("User with email: $email not found")
    }
}
