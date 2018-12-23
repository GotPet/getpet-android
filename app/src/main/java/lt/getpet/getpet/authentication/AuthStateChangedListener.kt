package lt.getpet.getpet.authentication

import lt.getpet.getpet.data.User

interface AuthStateChangedListener {

    fun onUserLoggedIn(user: User)

    fun onAuthError(throwable: Throwable)
}