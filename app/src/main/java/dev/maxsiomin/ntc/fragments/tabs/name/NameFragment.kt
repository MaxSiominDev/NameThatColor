package dev.maxsiomin.ntc.fragments.tabs.name

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.ntc.R
import dev.maxsiomin.ntc.databinding.FragmentNameBinding
import dev.maxsiomin.ntc.extensions.addOnTouchListener
import dev.maxsiomin.ntc.extensions.dataBundle
import dev.maxsiomin.ntc.fragments.BaseFragment
import dev.maxsiomin.ntc.util.ScreenMetricsCompat.getScreenSize
import timber.log.Timber

@AndroidEntryPoint
class NameFragment : BaseFragment(R.layout.fragment_name) {

    private lateinit var binding: FragmentNameBinding

    override val mRoot get() = binding.root

    private val mViewModel by viewModels<NameViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNameBinding.bind(view)

        binding.apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner

            dataBundle.getString(IMAGE_URI)?.let {
                imageView.setImageURI(Uri.parse(it))
            }

            fab.setOnClickListener {
                val screenSize = getScreenSize()

                val width = screenSize.width
                val height = screenSize.height

                ImagePicker.with(this@NameFragment)
                    .maxResultSize(width, height)
                    .createIntent { intent ->
                        imageResultListener.launch(intent)
                    }
            }

            imageView.addOnTouchListener { v, ev ->
                // Exception when user touches at corner of image
                try {
                    val pixel = (v.drawable as BitmapDrawable).bitmap.getPixel(ev.x.toInt(), ev.y.toInt())
                    mViewModel.processCoords(pixel)
                } catch (e: IllegalArgumentException) {
                    Timber.w(e)
                }
            }
        }
    }

    private val imageResultListener =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                // Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                dataBundle.putString(IMAGE_URI, fileUri.toString())
                binding.imageView.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                mViewModel.toast(ImagePicker.getError(data), Toast.LENGTH_SHORT)
            }
        }

    companion object {
        private const val IMAGE_URI = "NameFragment/imageUri"
    }
}
