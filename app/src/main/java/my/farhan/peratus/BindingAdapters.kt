package my.farhan.peratus

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ListenerUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout

interface BindableAdapterList<T> {
    fun setData(data: List<T>?)
}

@BindingAdapter("dataList")
fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, data: List<T>?) {
    if (recyclerView.adapter is BindableAdapterList<*>) {
        (recyclerView.adapter as BindableAdapterList<T>).setData(data)
    }
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, value: Boolean?) {
    value?.let {
        view.visibility = if (it) View.VISIBLE else View.GONE
    }
}

@BindingAdapter("app:errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.error = errorMessage

    if (errorMessage.isNullOrEmpty())
        view.error = null
}

@BindingAdapter("android:onTextChanged")
fun setListener(view: TextView?, onTextChanged: OnTextChanged?) {
    setListener(view!!, null, onTextChanged, null)
}

@BindingAdapter(
    value = ["android:beforeTextChanged", "android:onTextChanged", "android:afterTextChanged"],
    requireAll = false
)
fun setListener(
    view: TextView,
    before: BeforeTextChanged?,
    on: OnTextChanged?,
    after: AfterTextChanged?
) {
    val newValue: TextWatcher?
    if (before == null && after == null && on == null) {
        newValue = null
    } else {
        newValue = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                before?.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                on?.onTextChanged(s, start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                after?.afterTextChanged(s)
            }

        }
    }
    val oldValue: TextWatcher? = ListenerUtil.trackListener(view, newValue, R.id.textWatcher)
    if (oldValue != null) {
        view.removeTextChangedListener(oldValue)
    }
    if (newValue != null) {
        view.addTextChangedListener(newValue)
    }

}

interface AfterTextChanged {
    fun afterTextChanged(s: Editable?)
}

interface BeforeTextChanged {
    fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
}

interface OnTextChanged {
    fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
}

fun Float.closestNumber(): Double {
    val doubleVal = this.toDouble()
    val stepSize = 0.1

    val remaining = doubleVal % stepSize
    val bias = stepSize / remaining
    return when {
        remaining == 0.0 -> doubleVal
        bias < 50 -> this - remaining + stepSize
        else -> this - remaining
    }
}