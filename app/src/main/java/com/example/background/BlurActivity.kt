package com.example.background

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.example.background.databinding.ActivityBlurBinding

class BlurActivity : AppCompatActivity() {

    private val binding:ActivityBlurBinding by lazy{
        ActivityBlurBinding.inflate(layoutInflater)
    }

    private val viewModel: BlurViewModel by viewModels {
        BlurViewModel.BlurViewModelFactory(
            application
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.goButton.setOnClickListener { viewModel.applyBlur(blurLevel) }

        viewModel.outputWorkInfos.observe(this){
            if (it.isNullOrEmpty()){
                return@observe
            }
            val workInfo=it[0]
            if (workInfo.state.isFinished){
                showWorkFinished()

                val outputImageUri=workInfo.outputData.getString(KEY_IMAGE_URI)
                viewModel.setOutputUri(outputImageUri)
                binding.seeFileButton.visibility=View.VISIBLE

            }else{
                showWorkInProgress()
            }
        }
        binding.seeFileButton.setOnClickListener {
            viewModel.outputUri?.let {
                val intentForActionView=Intent(Intent.ACTION_VIEW,it)
                intentForActionView.resolveActivity(packageManager)?.run {
                    startActivity(intentForActionView)
                }
            }
        }
        binding.cancelButton.setOnClickListener {
            viewModel.cancelWork()
        }
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private fun showWorkInProgress() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            goButton.visibility = View.GONE
            seeFileButton.visibility = View.GONE
        }
    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private fun showWorkFinished() {
        with(binding) {
            progressBar.visibility = View.GONE
            cancelButton.visibility = View.GONE
            goButton.visibility = View.VISIBLE
        }
    }

    private val blurLevel: Int
        get() =
            when (binding.radioBlurGroup.checkedRadioButtonId) {
                R.id.radio_blur_lv_1 -> 1
                R.id.radio_blur_lv_2 -> 2
                R.id.radio_blur_lv_3 -> 3
                else -> 1
            }
}
