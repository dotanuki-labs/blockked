package io.dotanuki.blockked.dashboards

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.dotanuki.blockked.dashboards.ChartModel.AvaliableData
import io.dotanuki.blockked.dashboards.ChartModel.Unavailable
import io.dotanuki.blockked.domain.BlockchainInfoIntegrationIssue
import io.dotanuki.blockked.domain.NetworkingIssue
import io.dotanuki.common.*
import io.dotanuki.logger.Logger
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.view_dashboard.*
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
    private val viewModel by kodein.instance<DashboardViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModel
            .retrieveDashboard()
            .subscribeBy(
                onNext = { changeState(it) },
                onError = { logger.e("Error -> $it") }
            )
    }

    private fun changeState(event: UIEvent<DashboardPresentation>) {
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
            is BlockchainInfoIntegrationIssue -> errorStateLabel.apply {
                Toast.makeText(this@DashboardActivity, "$reason", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun presentDashboard(presentation: DashboardPresentation) {
        dashboarView.visibility = View.VISIBLE
        val (display, chart) = presentation
        presentDisplay(display)
        presentChart(chart)

    }

    private fun presentChart(chart: ChartModel) {
        when (chart) {
            is Unavailable -> chartContainer.visibility = View.GONE
            is AvaliableData -> {

                val dataSets = listOf(
                    LineDataSet(chart.values.map { it as Entry }, chart.legend).apply {
                        setDrawValues(false)
                    }
                )

                bitcoinPriceChart.apply {
                    setPinchZoom(false)
                    setTouchEnabled(false)
                    description.isEnabled = false
                    axisRight.isEnabled = false
                    xAxis.isEnabled = false

                    setDrawBorders(false)
                    setDrawGridBackground(false)

                    axisLeft.apply {
                        axisMaximum = chart.maxValue
                        axisMinimum = chart.minValue
                        setDrawZeroLine(false)
                    }

                    data = LineData(dataSets)
                    animateX(1500)
                }
            }

        }
    }

    private fun presentDisplay(display: DisplayModel) {
        displayTitle.text = display.title
        displayValue.text = display.formattedValue
        displaySubtitle.text = display.subtitle
    }

    private fun startExecution() {
        dashboarView.visibility = View.INVISIBLE
        loadingIndication.visibility = View.VISIBLE
    }

    private fun finishExecution() {
        loadingIndication.visibility = View.INVISIBLE
    }

}