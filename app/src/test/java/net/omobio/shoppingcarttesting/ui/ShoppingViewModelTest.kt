package net.omobio.shoppingcarttesting.ui

import com.google.common.truth.Truth.assertThat
import net.omobio.shoppingcarttesting.getOrAwaitValueTest
import net.omobio.shoppingcarttesting.other.Constants
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

    @Test
    fun `insert shopping item with too long name, returns error`() {
        val longName = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1) {
                append("1")
            }
        }
        viewModel.insertShoppingItem(longName, "5", "1.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price, returns error`() {
        val longPrice = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1) {
                append("1")
            }
        }
        viewModel.insertShoppingItem("name", "5", longPrice)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, returns error`() {
        viewModel.insertShoppingItem("name", "999999999999999999999999", "4.5")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, returns success`() {
        viewModel.insertShoppingItem("name", "10", "4.5")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

}