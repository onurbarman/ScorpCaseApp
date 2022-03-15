package com.onurbarman.scorpcaseapp.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE

abstract class RecyclerViewPaginator(recyclerView: RecyclerView) : RecyclerView.OnScrollListener() {

    /*
     * This is the Page Limit for each request
     * i.e. every request will fetch 19 transactions
     * */
    var batchSize: Long = 0

    /*
     * Variable to keep track of the current page
     * */
    private var currentPage: Long = 0L

    /*
     * This variable is used to set
     * the threshold. For instance, if I have
     * set the page limit to 20, this will notify
     * the app to fetch more transactions when the
     * user scrolls to the 18th item of the list.
     * */
    private val threshold = 1

    private var endWithAuto = false

    private val layoutManager: RecyclerView.LayoutManager?

    val startSize: Long
        get() = ++currentPage

    val maxSize: Long
        get() = currentPage + batchSize

    abstract val isLastPage: Boolean

    init {
        recyclerView.addOnScrollListener(this)
        this.layoutManager = recyclerView.layoutManager
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == SCROLL_STATE_IDLE) {
            if (layoutManager == null) return
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            var firstVisibleItemPosition = 0
            if (layoutManager is LinearLayoutManager) {
                firstVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

            } else if (layoutManager is GridLayoutManager) {
                firstVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }

            if (isLastPage) return

            if (visibleItemCount + firstVisibleItemPosition + threshold >= totalItemCount) {
                loadMore(startSize, maxSize)
            }
        }
    }

    fun reset() {
        currentPage = 0L
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
    }

    abstract fun loadMore(start: Long, count: Long)
}