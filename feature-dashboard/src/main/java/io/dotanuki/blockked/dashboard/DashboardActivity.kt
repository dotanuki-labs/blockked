package io.dotanuki.blockked.dashboard

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.dotanuki.blockked.dashboard.ChartModel.AvaliableData
import io.dotanuki.blockked.dashboard.ChartModel.Unavailable
import io.dotanuki.common.*
import io.dotanuki.logger.Logger
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.view_dashboard.*
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance

class DashboardActivity : AppCompatActivity() {

    private val dependencies by lazy {
        (application as KodeinAware).kodein.direct
    }

    private val logger by lazy {
        dependencies.instance<Logger>()
    }

    private val viewModel by lazy {
        dependencies.instance<DashboardViewModel>()
    }

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
        when(event) {
            is InFlight -> startExecution()
            is Result -> presentDashboard(event.value)
            is Failed -> reportError(event.reason)
            is Done -> finishExecution()
        }
    }


    private fun reportError(reason: Throwable) {
        logger.e("Error -> $reason")
    }

    private fun presentDashboard(presentation: DashboardPresentation) {
        dashboarView.visibility = View.VISIBLE
        val (display, chart) = presentation
        present(display)
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

    private fun present(display: DisplayModel) {
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