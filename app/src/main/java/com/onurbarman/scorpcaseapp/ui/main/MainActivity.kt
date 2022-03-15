package com.onurbarman.scorpcaseapp.ui.main

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onurbarman.scorpcaseapp.R
import com.onurbarman.scorpcaseapp.databinding.ActivityMainBinding
import com.onurbarman.scorpcaseapp.model.person.FetchError
import com.onurbarman.scorpcaseapp.model.person.FetchResponse
import com.onurbarman.scorpcaseapp.model.person.Person
import com.onurbarman.scorpcaseapp.network.DataSource
import com.onurbarman.scorpcaseapp.util.RecyclerViewPaginator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var dataSource: DataSource? = null
    private var pagingNext: String? = null
    private var peopleAdapter = PeoplesAdapter()
    private var paginator: RecyclerViewPaginator? = null

    private var isScrollToBottom = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize() {
        dataSource = DataSource()
        initSwipeRefreshLayout()
        initClick()
        initializeRecyclerView()
        initData()
    }

    private fun initClick() {
        binding.run {
            btnRefresh.setOnClickListener {
                refreshList()
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.run {
            swipeRefreshLayout.setOnRefreshListener {
                refreshList()
            }
        }
    }

    private fun refreshList() {
        setLoadingState()
        paginator?.reset()
        peopleAdapter.submitList(null)
        dataSource?.fetch(null, ::onFetchResponse)
    }

    private fun setLoadingState() {
        binding.run {
            swipeRefreshLayout.isVisible = false
            noDataView.isVisible = false
            progressDialog.isVisible = true
        }

    }

    private fun initializeRecyclerView() {
        binding.run {
            recyclerViewPeople.run {
                layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
                adapter = peopleAdapter
                paginator = object : RecyclerViewPaginator(this) {
                    override val isLastPage: Boolean
                        get() = peopleAdapter.currentList.size >= dataSource?.getPeopleCount() ?: 100

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        isScrollToBottom = dy > 0 || !canScrollHorizontally(1)
                    }
                    override fun loadMore(start: Long, count: Long) {
                        Log.d("scrolltetik","tetiklendi")
                        if (isScrollToBottom || !canScrollVertically(1))
                            dataSource?.fetch(pagingNext, ::onFetchResponse)
                    }
                }
            }
        }
    }

    private fun initData() {
        dataSource?.fetch(null, ::onFetchResponse)
    }

    private fun onFetchResponse(response: FetchResponse?, error: FetchError?) {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressDialog.isVisible = false
        response?.let { users ->
            pagingNext = users.next
            paginator?.batchSize = users.people.size.toLong()
            setUserList(users.people)
        } ?: kotlin.run {
            error?.let {
                setErrorView(it.errorDescription)
            }
        }
    }

    private fun setUserList(userList: List<Person>){
        setNoDataView(userList.isEmpty())
        if (userList.isNotEmpty()){
            val mutableList = userList.toMutableList()
            peopleAdapter.submitData(mutableList)
        }
    }

    private fun setNoDataView(listIsEmpty: Boolean) {
        binding.run {
            labelNoData.text = getString(R.string.no_data_description)
            swipeRefreshLayout.isVisible = !listIsEmpty
            noDataView.isVisible = listIsEmpty
        }
    }

    private fun setErrorView(errorDescription: String) {
        binding.run {
            labelNoData.text = getString(R.string.error_description, errorDescription)
            swipeRefreshLayout.isVisible = false
            noDataView.isVisible = true
            }
    }

}