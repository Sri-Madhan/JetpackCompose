package com.example.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.flow.MutableStateFlow


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = remember { mutableStateOf(0) }
    val title = when (navController.value) {
        0 -> "XML"
        1 -> "Swip"
        2 -> "Web"
        else -> "App"
    }

    Scaffold(
        topBar = { TopBar(title) },
        bottomBar = { BottomNavBar(navController) },
    ) { paddingValues ->
        when (navController.value) {
            0 -> XMLScreen(Modifier.padding(paddingValues))
            1 -> HomeScreen(Modifier.padding(paddingValues))
            2 -> WebScreen(Modifier.padding(paddingValues))
            3 -> PermissionsScreen()
        }
    }
}

@Composable
fun TopBar(title: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        IconButton(onClick = { /* Handle action */ }) {
            Icon(Icons.Filled.Menu, contentDescription = "Menu")
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.semantics {
                contentDescription = title+"_heading"
            }
        )
    }
}

@Composable
fun BottomNavBar(navController: MutableState<Int>) {
    NavigationBar {
        NavigationBarItem(
            selected = navController.value == 0,
            onClick = { navController.value = 0 },
            icon = { Icon(Icons.Default.Home, contentDescription = "XML") },
            label = { Text("XML") },
            modifier = Modifier.testTag("xmlNav")
        )
        NavigationBarItem(
            selected = navController.value == 1,
            onClick = { navController.value = 1 },
            icon = { Icon(Icons.Default.Edit, contentDescription = "Swip") },
            label = { Text("Swip") },
            modifier = Modifier.testTag("swipNav")
        )
        NavigationBarItem(
            selected = navController.value == 2,
            onClick = { navController.value = 2 },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Web") },
            label = { Text("Web") },
            modifier = Modifier.testTag("webNav")
        )
        NavigationBarItem(
            selected = navController.value == 3,
            onClick = { navController.value = 3 },
            icon = { Icon(Icons.Default.Place, contentDescription = "Permission") },
            label = { Text("Permission") },
            modifier = Modifier.testTag("permissionNav")
        )
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = viewModel()) {
    val items by viewModel.items.collectAsState()
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f).testTag("listView")) {
            items(items.size) { index ->
                Text(
                    text = items[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .semantics { testTag = "item_$index" }
                )
            }
        }
        Button(
            onClick = { viewModel.addItem("Item #${items.size + 1}") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Item")
        }
    }
}

open class HomeViewModel : ViewModel() {
    private val _items = MutableStateFlow(listOf("Item #1", "Item #2"))
    var items: MutableStateFlow<List<String>> = _items

    fun addItem(item: String) {
        _items.value = _items.value + item
    }
}


@Composable
fun XMLScreen(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            LayoutInflater.from(context).inflate(R.layout.samplexml,null)
        },
        update = { view ->
            val textview: TextView = view.findViewById(R.id.sample_text)
            textview.text = "Updated Text from Compose"

            val button: Button = view.findViewById(R.id.sample_button)
            button.setOnClickListener {
                textview.text = "Button Clicked!"
            }
        },
        modifier = modifier.testTag("xmlscreen")
    )
}

@Composable
fun WebScreen(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        request?.url?.let {
                            view?.loadUrl(it.toString())
                        }
                        return true
                    }
                }
                webChromeClient = WebChromeClient()
                loadUrl("https://developer.android.com/compose")
            }
        },
        modifier = Modifier.fillMaxSize().semantics { contentDescription = "web_view" }
    )
//    AndroidView(
//        factory = { context ->
//            WebView(context).apply {
//                settings.javaScriptEnabled = true
//                loadUrl("https://developer.android.com/compose")
//            }
//        },
//        modifier = modifier.fillMaxSize().semantics { testTag = "web_view" }
//    )
}

@Composable
fun PermissionsScreen() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        Toast.makeText(
            context,
            if (granted) "Permission Granted" else "Permission Denied",
            Toast.LENGTH_LONG
        ).show()
    }
    Button(onClick = { launcher.launch(android.Manifest.permission.CAMERA) }, modifier = Modifier.padding(60.dp).testTag("permission_button")) {
        Text("Request Camera Permission")
    }
}
