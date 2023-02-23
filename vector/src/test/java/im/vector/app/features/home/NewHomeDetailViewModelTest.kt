/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.app.features.home

import com.airbnb.mvrx.test.MavericksTestRule
import im.vector.app.features.home.room.list.UnreadCounterBadgeView
import im.vector.app.features.spaces.GetSpacesUseCase
import im.vector.app.test.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Rule
import org.junit.Test

internal class NewHomeDetailViewModelTest {

    @get:Rule
    val mavericksTestRule = MavericksTestRule(testDispatcher = UnconfinedTestDispatcher())

    private val initialState = NewHomeDetailViewState()
    private val fakeGetSpacesNotificationBadgeStateUseCase = mockk<GetSpacesNotificationBadgeStateUseCase>()

    private fun createViewModel(): NewHomeDetailViewModel {
        return NewHomeDetailViewModel(
                initialState = initialState,
                getSpacesNotificationBadgeStateUseCase = fakeGetSpacesNotificationBadgeStateUseCase,
        )
    }

    @Test
    fun `given the viewModel is created then viewState is updated with space notifications badge state`() {
        // Given
        val aBadgeState = UnreadCounterBadgeView.State.Count(count = 1, highlighted = false)
        every { fakeGetSpacesNotificationBadgeStateUseCase.execute() } returns flowOf(aBadgeState)

        // When
        val viewModel = createViewModel()
        val viewModelTest = viewModel.test()

        // Then
        val expectedViewState = initialState.copy(
                spacesNotificationCounterBadgeState = aBadgeState,
        )
        viewModelTest
                .assertLatestState(expectedViewState)
                .finish()
        verify {
            fakeGetSpacesNotificationBadgeStateUseCase.execute()
        }
    }
}
