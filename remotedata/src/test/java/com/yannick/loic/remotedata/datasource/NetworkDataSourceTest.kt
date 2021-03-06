package com.yannick.loic.remotedata.datasource

import com.yannick.loic.home.domain.entities.ComicsEntity
import com.yannick.loic.remotedata.api.ComicsService
import com.yannick.loic.remotedata.datasource.utils.TestRemoteDataContainer.Companion.getComicsList
import com.yannick.loic.remotedata.datasource.utils.TestRemoteDataContainer.Companion.getDataWrapper
import com.yannick.loic.remotedata.mapper.ComicsRemoteMapper
import com.yannick.loic.utils.HashGenerator
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class NetworkDataSourceTest {

    private var comicsService: ComicsService = mockk()
    private val comicsRemoteMapper = ComicsRemoteMapper()
    private lateinit var networkDataSource: NetworkDataSource
    private val hashGenerator = HashGenerator()
    @Before
    fun setUp() {
        networkDataSource = NetworkDataSource(
            comicsService=comicsService,
            comicsRemoteMapper = comicsRemoteMapper,
            hashGenerator = hashGenerator,
            publicKey = "",
            privateKey = "",
            backgroundThread = Schedulers.trampoline())
    }

    @Test
    fun test_getComicsList_with_query_success() {
        val dataWrapper = getDataWrapper()
        val comicsList = getComicsList()
        var result: List<ComicsEntity>? = null
        var error: Throwable? = null

        every {
            comicsService.getComicsListStartsWithTitle(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns (Single.just(dataWrapper))


        networkDataSource.getComicsList("id", 0, 20).subscribe({result=it},{error=it})

        verify { comicsService.getComicsListStartsWithTitle(any(), any(), any(), any(),any()) }
        assertThat(
            result?.zip(comicsList)?.all { it.first == comicsRemoteMapper.from(it.second) },
            equalTo(true)
        )
        assertNull(error)
        assertNotNull(result)
    }

    @Test
    fun test_getComicsList_with_query_failure() {
        val exception = Throwable("Network Error!!")
        var result: List<ComicsEntity>? = null
        var error: Throwable? = null
        every {
            comicsService.getComicsListStartsWithTitle(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns (Single.error(exception))

        networkDataSource.getComicsList("id", 0, 20).subscribe({result=it},{error =it})

        verify { comicsService.getComicsListStartsWithTitle(any(), any(), any(), any(),any()) }
        assertNotNull(error)
        assertNull(result)
    }
}