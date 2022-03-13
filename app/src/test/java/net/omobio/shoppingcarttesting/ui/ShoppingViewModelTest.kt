package net.omobio.shoppingcarttesting.ui

import com.google.common.truth.Truth.assertThat
import net.omobio.shoppingcarttesting.getOrAwaitValueTest
import net.omobio.shoppingcarttesting.other.Status
import net.omobio.shoppingcarttesting.repositories.FakeShoppingRepository
import org.junit.Before
import org.junit.Test

class ShoppingViewModelTest {

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field, returns error`() {
        viewModel.insertShoppingItem("name", "", "1.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


}