package my.farhan.peratus

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText

class PeratusViewModel : ViewModel() {
    var peratusList = ObservableArrayList<Peratus>()
    val inputText = ObservableField<String>()
    private val multiplier = mutableListOf<Double>()
    val isPasteAutomatically = ObservableBoolean(true)
    val errorText = ObservableField<String>()

    init {
        createNewMultiplier(0.5, 10)
    }

    fun onPriceChanged(view: TextInputEditText) {
        try {
            val temp = view.text!!.toString().trim().toDouble()
            updateListPrice(temp)

        } catch (e: Exception) {
            showError(e.message)
        }
    }

    private fun updateListPrice(price: Double) {
        errorText.set("")
        peratusList.clear()
        for (item in multiplier) {
            peratusList.add(
                Peratus(
                    getLabel(item),
                    item,
                    getResult(price, item, PeratusType.Profit),
                    getResult(price, item, PeratusType.Loss)
                )
            )
        }
    }

    private fun showError(error: String?) {
        errorText.set(error)
        peratusList.clear()
    }

    private fun createNewMultiplier(stepSize: Double, max: Int) {
        multiplier.clear()
        var min = 0.0
        while (min < max) {
            min += stepSize
            multiplier.add(min)
        }
    }

    private fun getLabel(multiplier: Double): String {
        return String.format("%.1f%%", multiplier)
    }

    private fun getResult(price: Double, multiplier: Double, peratusType: PeratusType): String {
        val coefficient = when (peratusType) {
            PeratusType.Loss -> -1
            PeratusType.Profit -> 1
        }

        val x = price * ((100.0 + multiplier * coefficient) / 100.0)
        return String.format("%.7f", x)
    }
}