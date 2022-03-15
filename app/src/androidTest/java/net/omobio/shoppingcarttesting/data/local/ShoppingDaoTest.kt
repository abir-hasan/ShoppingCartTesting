package net.omobio.shoppingcarttesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import net.omobio.shoppingcarttesting.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi // For run runBlockingTest
//@RunWith(AndroidJUnit4::class) // Specify that these tests are instrumented and will run in emulator
@SmallTest // Specify these class will contain unit tests
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // Rule to execute functions sequentially
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase

    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        /*database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()*/

        hiltRule.inject()

        dao = database.shoppingDao()
    }

    @After
    fun teardown() {

        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem(
            name = "name",
            amount = 1,
            price = 1f,
            imageUrl = "url",
            id = 1
        )
        dao.insertShoppingItem(shoppingItem)
        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem =
            ShoppingItem(name = "test", amount = 1, price = 1f, imageUrl = "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)
        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItem).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val item1Amount = 10
        val item1Price = 1f
        val item2Amount = 20
        val item2Price = 2f
        val item3Amount = 30
        val item3Price = 3f
        val totalPrice =
            (item1Amount * item1Price) + (item2Amount * item2Price) + (item3Amount * item3Price)

        val shoppingItem1 = ShoppingItem(
            name = "test",
            amount = item1Amount,
            price = item1Price,
            imageUrl = "url",
            id = 1
        )
        val shoppingItem2 = ShoppingItem(
            name = "test",
            amount = item2Amount,
            price = item2Price,
            imageUrl = "url",
            id = 2
        )
        val shoppingItem3 = ShoppingItem(
            name = "test",
            amount = item3Amount,
            price = item3Price,
            imageUrl = "url",
            id = 3
        )
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val observedPrice = dao.observeTotalPrice().getOrAwaitValue()
        assertThat(observedPrice).isEqualTo(totalPrice)
    }

}