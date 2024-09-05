package com.aaditx23.bracusocial.components

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun ImagePicker(onImagePicked: (image: File) -> Unit){
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var showGallery by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    var hasImagePermission by remember{ mutableStateOf(false) }
    hasImagePermission = permissionLauncher(
        context = context,
        permission = Manifest.permission.READ_MEDIA_IMAGES
    )

    val cropLauncher = rememberLauncherForActivityResult(contract = CropImageContract()) {result ->
        if (result.isSuccessful){
            selectedImageUri = result.uriContent
            val file = createFileFromUri(context, selectedImageUri!!)
            onImagePicked(file)

        }
        else{
            Toast.makeText(context, "An Error occured while cropping", Toast.LENGTH_SHORT).show()
            println("${result.error}")
        }

    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri: Uri? ->
        val cropOptions = CropImageContractOptions(uri, CropImageOptions().apply {
            fixAspectRatio = true
            aspectRatioX = 800
            aspectRatioY = 600
            outputRequestWidth = 800
            outputRequestHeight = 600
        })
        cropLauncher.launch(cropOptions)


    }
    if (hasImagePermission){
        if(!showGallery){
            showGallery = true
            println("LAUNCHING")
            launcher.launch(("image/*"))

        }


    }

}

private fun createFileFromUri(context: Context, uri: Uri): File{
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val tempFile = File(context.cacheDir, "selectedImage.jpg")

    FileOutputStream(tempFile).use { outputStream ->
        inputStream?.copyTo(outputStream)
    }
    return tempFile
}