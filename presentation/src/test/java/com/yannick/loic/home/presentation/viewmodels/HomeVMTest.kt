package com.yannick.loic.home.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yannick.loic.home.domain.repositories.ComicsDataRepository
import com.yannick.loic.home.domain.usecases.GetComicsListAction
import com.yannick.loic.home.domain.usecases.GetComicsListAction.GetComicsListActionResult
import com.yannick.loic.home.presentation.utils.PagingDataSourceUtil
import com.yannick.loic.home.presentation.utils.TestDataContainer
import com.yannick.loic.home.presentation.utils.observeOnce
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeVMTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val comicsDataRepository: ComicsDataRepository = mockk()

    private lateinit var comicsListHomeVM: HomeVM

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setup() {

        val getComicsListAction = getComicsListAction()
        Dispatchers.setMain(mainThreadSurrogate)


        comicsListHomeVM = HomeVM(
            getComicsListAction
        )

        val result =
            GetComicsListActionResult(
                mockk(),
                mockk()
            )

        every {
            comicsDataRepository.getComicsList(
                any()
            )
        }.returns(result)

    }

    @Test
    fun test_comicsList() {
        val comicsList = TestDataContainer.getComicsList()
        val ds = PagingDataSourceUtil.createMockDataSourceFactory(comicsList)
        val actionResult = GetComicsListActionResult(ds, mockk())

        every { comicsDataRepository.getComicsList(any()) } returns (actionResult)



        comicsListHomeVM.search("112")

        comicsListHomeVM.comicsListSource.observeOnce {
            assertThat(it.snapshot(), equalTo(comicsList))
        }


    }

    private fun getComicsListAction(): GetComicsListAction {
        return GetComicsListAction(
            comicsDataRepository
        )
    }

}