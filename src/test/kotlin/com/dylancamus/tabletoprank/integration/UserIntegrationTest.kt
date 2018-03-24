package com.dylancamus.tabletoprank.integration

import com.dylancamus.tabletoprank.H2TestProfileJPAConfig
import com.dylancamus.tabletoprank.TabletopRankApplication
import com.dylancamus.tabletoprank.domain.user.*
import com.dylancamus.tabletoprank.repository.UserRepository
import com.dylancamus.tabletoprank.service.user.UserService
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.Before
import org.junit.Test

import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [
    TabletopRankApplication::class,
    H2TestProfileJPAConfig::class
])
@ActiveProfiles("test")
internal class UserIntegrationTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    private val users = mutableListOf<UserDto>()

    private var token = ""

    @Before
    fun setUp() {
        RestAssured.port = port
        users.add(userService.createUser(CreateUserDto(email = "jack.bauer@test.com",
                password = "password", firstName = "Jack", lastName = "Bauer")))
        users.add(userService.createUser(CreateUserDto(email = "chloe.obrian@test.com",
                password = "hunter2", firstName = "Chloe", lastName = "O'Brian")))
        users.add(userService.createUser(CreateUserDto(email = "kim.bauer@test.com",
                password = "password123", firstName = "Kim", lastName = "Bauer")))
        users.add(userService.createUser(CreateUserDto(email = "david.palmer@test.com",
                password = "123456", firstName = "David", lastName = "Palmer")))
        users.add(userService.createUser(CreateUserDto(email = "michelle.dessler@test.com",
                password = "password", firstName = "Michelle", lastName = "Dessler")))
        token = given().contentType(ContentType.JSON)
                .body(AuthorizationDto("jack.bauer@test.com", "password"))
                .post("/login").then().extract().header(HttpHeaders.AUTHORIZATION)
    }

    @After
    fun tearDown() {
        userRepository.deleteAll()
    }

    @Test
    fun `'getUser' should return user`() {
        val user = users[0]
        given().header(HttpHeaders.AUTHORIZATION, token)
                .get("/api/user").then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("email", `is`(user.email))
                .assertThat().body("firstName", `is`(user.firstName))
                .assertThat().body("lastName", `is`(user.lastName))
    }

    @Test
    fun `'getUser' should format error if user doesn't exist`() {
        userRepository.deleteById(users[0].id)
        given().header(HttpHeaders.AUTHORIZATION, token)
                .get("/api/user").then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .assertThat().body("status", `is`(HttpStatus.NOT_FOUND.name))
                .assertThat().body("message", `is`(notNullValue()))
                .assertThat().body("timestamp", `is`(notNullValue()))
    }

    @Test
    fun `'createUser' should return new user`() {
        val dto = CreateUserDto("pear", "mango", "apple", "banana")
        given().contentType(ContentType.JSON).body(dto)
                .post("/api/user").then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("email", `is`(dto.email))
                .assertThat().body("firstName", `is`(dto.firstName))
                .assertThat().body("lastName", `is`(dto.lastName))
        assertThat(userRepository.findAll().size, `is`(users.size + 1))
    }

    @Test
    fun `'createUser' should should format error if body is missing`() {
        given().contentType(ContentType.JSON)
                .post("/api/user").then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .assertThat().body("status", `is`(HttpStatus.BAD_REQUEST.name))
                .assertThat().body("message", `is`(notNullValue()))
                .assertThat().body("timestamp", `is`(notNullValue()))
    }

    @Test
    fun `'createUser' should should format error if body is malformed`() {
        given().contentType(ContentType.JSON).body("aaa")
                .post("/api/user").then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .assertThat().body("status", `is`(HttpStatus.BAD_REQUEST.name))
                .assertThat().body("message", `is`(notNullValue()))
                .assertThat().body("timestamp", `is`(notNullValue()))
    }

    @Test
    fun `'updateUser' should return updated user`() {
        val user = users[0]
        val dto = UpdateUserDto(null, null, null,"banana", null)
        given().header(HttpHeaders.AUTHORIZATION, token)
                .contentType(ContentType.JSON).body(dto)
                .put("/api/user").then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("firstName", `is`(user.firstName))
                .assertThat().body("lastName", `is`(dto.lastName))
        assertThat(userRepository.findAll().size, `is`(users.size))
    }

    @Test
    fun `'updateUser' should format error if user doesn't exist`() {
        userRepository.deleteById(users[0].id)
        val dto = UpdateUserDto("apple", null, null, null, null)
        given().header(HttpHeaders.AUTHORIZATION, token)
                .contentType(ContentType.JSON).body(dto)
                .put("/api/user").then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .assertThat().body("status", `is`(HttpStatus.NOT_FOUND.name))
                .assertThat().body("message", `is`(notNullValue()))
                .assertThat().body("timestamp", `is`(notNullValue()))
    }

    @Test
    fun `'updateUser' should should format error if body is missing`() {
        given().header(HttpHeaders.AUTHORIZATION, token)
                .contentType(ContentType.JSON)
                .put("/api/user").then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .assertThat().body("status", `is`(HttpStatus.BAD_REQUEST.name))
                .assertThat().body("message", `is`(notNullValue()))
                .assertThat().body("timestamp", `is`(notNullValue()))
    }

    @Test
    fun `'updateUser' should should format error if body is malformed`() {
        given().header(HttpHeaders.AUTHORIZATION, token)
                .contentType(ContentType.JSON).body("aaa")
                .put("/api/user").then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .assertThat().body("status", `is`(HttpStatus.BAD_REQUEST.name))
                .assertThat().body("message", `is`(notNullValue()))
                .assertThat().body("timestamp", `is`(notNullValue()))
    }
}
