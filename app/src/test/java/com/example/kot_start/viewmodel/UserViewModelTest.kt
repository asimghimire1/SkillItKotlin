package com.example.kot_start.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.kot_start.model.UserModel
import com.example.kot_start.repository.UserRepo
import com.google.firebase.auth.FirebaseUser
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

/**
 * Unit tests for UserViewModel.
 * Uses Mockito to mock UserRepo interface,
 * and InstantTaskExecutorRule for LiveData testing.
 */
class UserViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockRepo: UserRepo
    private lateinit var viewModel: UserViewModel

    @Before
    fun setUp() {
        mockRepo = mock()
        viewModel = UserViewModel(mockRepo)
    }

    // ======================== Login ========================

    @Test
    fun `login delegates to repo with correct params`() {
        val callback: (Boolean, String) -> Unit = { _, _ -> }
        viewModel.login("test@email.com", "pass123", callback)

        verify(mockRepo).login(eq("test@email.com"), eq("pass123"), any())
    }

    @Test
    fun `login calls callback on success`() {
        // Arrange: when repo.login is called, invoke the callback with success
        whenever(mockRepo.login(any(), any(), any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login Successful")
        }

        var success = false
        var message = ""
        viewModel.login("test@email.com", "pass123") { s, m ->
            success = s
            message = m
        }

        assertTrue(success)
        assertEquals("Login Successful", message)
    }

    @Test
    fun `login calls callback on failure`() {
        whenever(mockRepo.login(any(), any(), any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(false, "Invalid credentials")
        }

        var success = true
        var message = ""
        viewModel.login("bad@email.com", "wrong") { s, m ->
            success = s
            message = m
        }

        assertFalse(success)
        assertEquals("Invalid credentials", message)
    }

    // ======================== Register ========================

    @Test
    fun `register delegates to repo`() {
        val callback: (Boolean, String, String) -> Unit = { _, _, _ -> }
        viewModel.register("new@email.com", "pass123", callback)

        verify(mockRepo).register(eq("new@email.com"), eq("pass123"), any())
    }

    @Test
    fun `register callback returns userId on success`() {
        whenever(mockRepo.register(any(), any(), any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String, String) -> Unit>(2)
            callback(true, "Register Successful", "uid_abc123")
        }

        var resultUid = ""
        viewModel.register("new@email.com", "pass123") { success, _, uid ->
            if (success) resultUid = uid
        }

        assertEquals("uid_abc123", resultUid)
    }

    // ======================== Add User To Database ========================

    @Test
    fun `addUserToDatabase delegates to repo`() {
        val user = UserModel(userId = "uid1", email = "a@b.com", role = "Student")
        val callback: (Boolean, String) -> Unit = { _, _ -> }
        viewModel.addUserToDatabase("uid1", user, callback)

        verify(mockRepo).addUserToDatabase(eq("uid1"), eq(user), any())
    }

    @Test
    fun `addUserToDatabase success callback`() {
        whenever(mockRepo.addUserToDatabase(any(), any(), any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "User Added")
        }

        var result = false
        viewModel.addUserToDatabase("uid1", UserModel()) { success, _ ->
            result = success
        }
        assertTrue(result)
    }

    // ======================== Get User By Id ========================

    @Test
    fun `getUserById updates LiveData on success`() {
        val expectedUser = UserModel(userId = "uid1", firstName = "John", role = "Student")
        whenever(mockRepo.getUserById(any(), any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String, UserModel?) -> Unit>(1)
            callback(true, "User Found", expectedUser)
        }

        viewModel.getUserById("uid1")

        assertEquals(expectedUser, viewModel.users.value)
        assertEquals(false, viewModel.loading.value)
    }

    @Test
    fun `getUserById updates LiveData on failure`() {
        whenever(mockRepo.getUserById(any(), any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String, UserModel?) -> Unit>(1)
            callback(false, "Not found", null)
        }

        viewModel.getUserById("bad_id")

        assertNull(viewModel.users.value)
        assertEquals(false, viewModel.loading.value)
    }

    // ======================== Get All Users ========================

    @Test
    fun `getAllUser delegates to repo`() {
        viewModel.getAllUser()
        verify(mockRepo).getAllUser(any())
    }

    // ======================== Get Current User ========================

    @Test
    fun `getCurrentUser returns null when no user logged in`() {
        whenever(mockRepo.getCurrentUser()).thenReturn(null)
        assertNull(viewModel.getCurrentUser())
    }

    @Test
    fun `getCurrentUser returns FirebaseUser when logged in`() {
        val mockUser: FirebaseUser = mock()
        whenever(mockRepo.getCurrentUser()).thenReturn(mockUser)
        assertNotNull(viewModel.getCurrentUser())
    }

    // ======================== Update Profile ========================

    @Test
    fun `updateProfile delegates to repo`() {
        val user = UserModel(userId = "uid1", firstName = "Updated")
        val callback: (Boolean, String) -> Unit = { _, _ -> }
        viewModel.updateProfile("uid1", user, callback)

        verify(mockRepo).updateProfile(eq("uid1"), eq(user), any())
    }

    // ======================== Delete Profile ========================

    @Test
    fun `deleteProfile delegates to repo`() {
        val user = UserModel(userId = "uid1")
        val callback: (Boolean, String) -> Unit = { _, _ -> }
        viewModel.deleteProfile("uid1", user, callback)

        verify(mockRepo).deleteProfile(eq("uid1"), eq(user), any())
    }

    // ======================== Forget Password ========================

    @Test
    fun `forgetPassword delegates to repo`() {
        val callback: (Boolean, String) -> Unit = { _, _ -> }
        viewModel.forgetPassword("test@email.com", callback)

        verify(mockRepo).forgetPassword(eq("test@email.com"), any())
    }

    @Test
    fun `forgetPassword success callback`() {
        whenever(mockRepo.forgetPassword(any(), any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Email send")
        }

        var result = false
        viewModel.forgetPassword("test@email.com") { success, _ ->
            result = success
        }
        assertTrue(result)
    }

    // ======================== Logout ========================

    @Test
    fun `logout delegates to repo`() {
        val callback: (Boolean, String) -> Unit = { _, _ -> }
        viewModel.logout(callback)

        verify(mockRepo).logout(any())
    }

    @Test
    fun `logout success callback`() {
        whenever(mockRepo.logout(any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(0)
            callback(true, "logout successful")
        }

        var result = false
        viewModel.logout { success, _ ->
            result = success
        }
        assertTrue(result)
    }

    // ======================== Get User Role ========================

    @Test
    fun `getUserRole delegates to repo`() {
        val callback: (Boolean, String, String?) -> Unit = { _, _, _ -> }
        viewModel.getUserRole("uid1", callback)

        verify(mockRepo).getUserRole(eq("uid1"), any())
    }

    @Test
    fun `getUserRole returns Student role`() {
        whenever(mockRepo.getUserRole(any(), any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String, String?) -> Unit>(1)
            callback(true, "Role found", "Student")
        }

        var role: String? = null
        viewModel.getUserRole("uid1") { _, _, r ->
            role = r
        }
        assertEquals("Student", role)
    }

    @Test
    fun `getUserRole returns Teacher role`() {
        whenever(mockRepo.getUserRole(any(), any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String, String?) -> Unit>(1)
            callback(true, "Role found", "Teacher")
        }

        var role: String? = null
        viewModel.getUserRole("uid1") { _, _, r ->
            role = r
        }
        assertEquals("Teacher", role)
    }

    @Test
    fun `getUserRole returns null for unknown user`() {
        whenever(mockRepo.getUserRole(any(), any())).thenAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String, String?) -> Unit>(1)
            callback(false, "User not found", null)
        }

        var role: String? = "initial"
        viewModel.getUserRole("unknown") { _, _, r ->
            role = r
        }
        assertNull(role)
    }
}
