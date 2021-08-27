package com.example.gallerydemo

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import com.example.gallerydemo.ui.theme.GalleryDemoTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GalleryDemoTheme {
                var isHidden = remember({false})
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Button(onClick = { isHidden=true },modifier = Modifier.wrapContentSize()) {
                        Text(text = "click Me")
                    }

                    MainScreen(applicationContext)


                }
            }
        }
    }
}
val array= arrayListOf<ImageData>()
@Composable
fun MainScreen(applicationContext: Context) {
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE,
    )
    val cursor = applicationContext.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        null
    )
    cursor.use {
        val idColumn=cursor?.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val nameColumn=cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        if(cursor!=null){
            while(cursor.moveToNext()){
                val id= idColumn?.let { it1 -> cursor.getLong(it1) }
                val name= nameColumn?.let { it1 -> cursor.getString(it1) }
                val contentUri: Uri? = id?.let {
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        it
                    )
                }
                array.add(ImageData(name = name ?:"",uri = contentUri))
            }
        }
    }

    LazyColumn {
        items(count = array.size) {
            val bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), array.get(it).uri)

            Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)
        }
    }


}

data class ImageData(
    var name: String ="",
    var uri: Uri? =null
    )

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GalleryDemoTheme {

    }
}