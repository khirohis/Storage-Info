package net.hogelab.android.storageinfo

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import net.hogelab.android.storageinfo.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()

        var storageInfo = "■ Storage Info"
        storageInfo += "\nFilesDir: " + this.filesDir
        storageInfo += "\nDataDir" + this.dataDir
        storageInfo += "\nCacheDir" + this.cacheDir
        storageInfo += "\nExternalCacheDir" + this.externalCacheDir

        storageInfo += "\n\n"
        storageInfo += getRootDirectoryInfo()

        storageInfo += "\n\n"
        storageInfo += getDataDirectoryInfo()

        storageInfo += "\n\n"
        storageInfo += getDownloadCacheDirectoryInfo()

        storageInfo += "\n\n"
        storageInfo += getExternalStorageDirectoryInfo()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            storageInfo += "\n\n"
            storageInfo += getStorageDirectoryInfo()
        }

        binding.textviewInfo.text = storageInfo
    }


    private fun getRootDirectoryInfo(): String {
        var resultInfo = "・Root Directory Info"
        val file: File = Environment.getRootDirectory()
        resultInfo += "\n" + getStorageSizeInfo(file.path)
        return resultInfo
    }

    private fun getDataDirectoryInfo(): String {
        var resultInfo = "・Data Directory Info"
        val file: File = Environment.getDataDirectory()
        resultInfo += "\n" + getStorageSizeInfo(file.path)
        return resultInfo
    }

    private fun getDownloadCacheDirectoryInfo(): String {
        var resultInfo = "・Download Cache Directory Info"
        val file: File = Environment.getDownloadCacheDirectory()
        resultInfo += "\n" + getStorageSizeInfo(file.path)
        return resultInfo
    }

    private fun getExternalStorageDirectoryInfo(): String {
        var resultInfo = "・External Storage Directory Info"
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            resultInfo += ": not mounted"
            return resultInfo
        }

        resultInfo += ": " + Environment.getExternalStorageState()

        resultInfo += "\n Emulated(" + Environment.isExternalStorageEmulated().toString() + ")"
        resultInfo += ", Removable(" + Environment.isExternalStorageRemovable().toString() + ")"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            resultInfo += ", Legacy(" + Environment.isExternalStorageLegacy().toString() + ")"
        }

        val file: File = Environment.getExternalStorageDirectory()
        resultInfo += "\n" + getStorageSizeInfo(file.path)

        return resultInfo
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getStorageDirectoryInfo(): String {
        var resultInfo = "・Storage Info (API 30+)"
        val file: File = Environment.getStorageDirectory()
        resultInfo += "\n" + getStorageSizeInfo(file.path)
        return resultInfo
    }

    private fun getStorageSizeInfo(path: String): String {
        val stat = StatFs(path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availableBlocks = stat.availableBlocksLong
        val totalSize = totalBlocks * blockSize
        val freeSize = availableBlocks * blockSize

        return """
            Path : $path
            Total: ${android.text.format.Formatter.formatFileSize(this, totalSize)}
            Free : ${android.text.format.Formatter.formatFileSize(this, freeSize)}
        """.trimIndent()
    }
}
