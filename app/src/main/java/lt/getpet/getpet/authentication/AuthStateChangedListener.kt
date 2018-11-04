package lt.getpet.getpet.authentication

import lt.getpet.getpet.data.RegularUser

interface AuthStateChangedListener {

    fun onRegularUserLoggedIn(regularUser: RegularUser)

    fun onAuthError(throwable: Throwable)
}