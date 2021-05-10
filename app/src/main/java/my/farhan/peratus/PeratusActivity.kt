package my.farhan.peratus

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.farhan.peratus.databinding.ActivityPeratusBinding


class PeratusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPeratusBinding
    private val viewModel: PeratusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_peratus
        )
        binding.viewModel = viewModel
        binding.rvPrice.adapter = PeratusAdapter().apply {
            setOnClickListener { peratus, peratusType -> onItemClicked(peratus, peratusType) }
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isPasteAutomatically.get())
            getClipboardPasteData()
    }

    private fun getClipboardPasteData() {
        lifecycleScope.launch {
            viewModel.inputText.set("Initializing..")
            delay(1_000L)
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            try {
                val pasteData = clipboard.primaryClip?.getItemAt(0)?.text
                viewModel.inputText.set(pasteData.toString())
            } catch (e: Exception) {
                Log.d("xyz", "${e.message}")
            }
        }
    }

    private fun onItemClicked(peratus: Peratus, peratusType: PeratusType) {
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val result = when (peratusType) {
            PeratusType.Profit -> peratus.resultProfit
            PeratusType.Loss -> peratus.resultLoss
        }

        Snackbar.make(binding.rvPrice, "${peratusType.name}: $result", Snackbar.LENGTH_SHORT)
            .show()
        val clip = ClipData.newPlainText(null, result)
        clipboard.setPrimaryClip(clip)
    }
}