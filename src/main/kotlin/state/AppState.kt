package state

import androidx.compose.runtime.Stable

@Stable
class AppState {
    val setState = SetState()
    val active = ActiveState()
}