package rs.raf.jun.presentation.view.states

import rs.raf.jun.data.models.user.User

sealed class LocalUserState {
    data class GotAllUsers(val users: List<User>): LocalUserState()
    data class ErrorState(val message: String): LocalUserState()
    class AddedUser(val message: String) : LocalUserState()
    class UpdatedUserData(val message: String) : LocalUserState()
}