package io.dotanuki.blockked.dashboards

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.dotanuki.blockked.domain.NetworkingIssue
import io.dotanuki.blockked.domain.RemoteIntegrationIssue
import io.dotanuki.common.*
import io.dotanuki.logger.Logger
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class DashboardActivity : AppCompatActivity(), KodeinAware {

    private val graph by closestKodein()

    override val kodein = Kodein.lazy {
        extend(graph)
    }

    private val logger by kodein.instance<Logger>()
    private val disposer by kodein.instance<Disposer>()
    private val viewModel by kodein.instance<DashboardViewModel>()

    private val dashboardsAdapter by lazy {
        GroupAdapter<ViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setupViews()
        loadDashboard()
        lifecycle.addObserver(disposer)
    }

    private fun setupViews() {
        dashboarView.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = dashboardsAdapter
        }

        swipeToRefresh.apply {
            setColorSchemeColors(ContextCompat.getColor(this@DashboardActivity, R.color.colorPrimary))
            setOnRefreshListener { loadDashboard() }
        }

        setSupportActionBar(toolbar)
    }

    private fun loadDashboard() {
        val toDispose = viewModel
            .retrieveDashboard()
            .subscribeBy(
                onNext = { changeState(it) },
                onError = { logger.e("Error -> $it") }
            )

        disposer.collect(toDispose)
    }

    private fun changeState(event: UIEvent<List<DashboardPresentation>>) {
        when (event) {
            is Launched -> startExecution()
            is Result -> presentDashboard(event.value)
            is Failed -> reportError(event.reason)
            is Done -> finishExecution()
        }
    }

    private fun reportError(reason: Throwable) {
        logger.e("Error -> $reason")

        when (reason) {
            is NetworkingIssue,
            is RemoteIntegrationIssue -> errorStateLabel.apply {
                Toast.makeText(this@DashboardActivity, "$reason", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun presentDashboard(dashboards: List<DashboardPresentation>) {
        logger.i("Loaded Dashboards")
        val entries = dashboards.map { DashboardEntry(it) }

        dashboarView.visibility = View.VISIBLE
        dashboardsAdapter.apply {
            clear()
            addAll(entries)
        }
    }

    private fun startExecution() {
        dashboarView.visibility = View.INVISIBLE
        swipeToRefresh.isRefreshing = true
    }

    private fun finishExecution() {
        swipeToRefresh.isRefreshing = false
    }

}