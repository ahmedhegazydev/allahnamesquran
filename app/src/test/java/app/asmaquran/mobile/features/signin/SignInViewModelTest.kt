package app.asmaquran.mobile.features.signin

import android.app.Activity
import app.asmaquran.mobile.R
import app.asmaquran.mobile.data.auth.AuthSignInResult
import app.asmaquran.mobile.testutil.FakeAuthRepository
import app.asmaquran.mobile.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `signInWithGoogle emits home on success and clears loading state`() = runTest {
        val authRepository = FakeAuthRepository().apply {
            signInResult = AuthSignInResult.Success
        }
        val viewModel = SignInViewModel(authRepository)
        val activity = mock(Activity::class.java)
        val navigation = async { viewModel.navigationEvents.first() }

        viewModel.signInWithGoogle(activity)
        advanceUntilIdle()

        assertEquals(SignInUiState(), viewModel.state.value)
        assertEquals(SignInNavigationEvent.Home, navigation.await())
        assertEquals(1, authRepository.signInCalls)
        assertSame(activity, authRepository.lastActivity)
    }

    @Test
    fun `signInWithGoogle maps cancelled result to cancelled error`() = runTest {
        assertFailureState(
            result = AuthSignInResult.Cancelled,
            expectedErrorRes = R.string.sign_in_error_cancelled
        )
    }

    @Test
    fun `signInWithGoogle maps not configured result to config error`() = runTest {
        assertFailureState(
            result = AuthSignInResult.NotConfigured,
            expectedErrorRes = R.string.sign_in_error_not_configured
        )
    }

    @Test
    fun `signInWithGoogle maps no token result to token error`() = runTest {
        assertFailureState(
            result = AuthSignInResult.NoToken,
            expectedErrorRes = R.string.sign_in_error_google_token
        )
    }

    @Test
    fun `signInWithGoogle maps generic failure to generic error`() = runTest {
        assertFailureState(
            result = AuthSignInResult.Failure(IllegalStateException("boom")),
            expectedErrorRes = R.string.sign_in_error_generic
        )
    }

    @Test
    fun `skipForNow marks prompt completed and emits home`() = runTest {
        val authRepository = FakeAuthRepository()
        val viewModel = SignInViewModel(authRepository)
        val navigation = async { viewModel.navigationEvents.first() }

        viewModel.skipForNow()
        advanceUntilIdle()

        assertEquals(1, authRepository.markAuthPromptCompletedCalls)
        assertEquals(SignInNavigationEvent.Home, navigation.await())
    }

    private fun assertFailureState(
        result: AuthSignInResult,
        expectedErrorRes: Int
    ) = runTest {
        val authRepository = FakeAuthRepository().apply {
            signInResult = result
        }
        val viewModel = SignInViewModel(authRepository)
        val activity = mock(Activity::class.java)

        viewModel.signInWithGoogle(activity)
        advanceUntilIdle()

        assertEquals(SignInUiState(errorMessageRes = expectedErrorRes), viewModel.state.value)
        assertEquals(1, authRepository.signInCalls)
    }
}
