package com.example.compose

import android.Manifest
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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import com.example.compose.networking.KtorfitInstance
import com.example.compose.networking.PoliceApiService
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject


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
            4 -> AllComponentsScreen()
            5-> WebSocketScreen()
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
        NavigationBarItem(
            selected = navController.value == 4,
            onClick = { navController.value = 4 },
            icon = { Icon(Icons.Default.Build, contentDescription = "Form") },
            label = { Text("Form") },
            modifier = Modifier.testTag("Form")
        )
        NavigationBarItem(
            selected = navController.value == 5,
            onClick = { navController.value = 5 },
            icon = { Icon(Icons.Default.Build, contentDescription = "Socket") },
            label = { Text("Socket") },
            modifier = Modifier.testTag("Socket")
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
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        Toast.makeText(
            context,
            if (granted) "Permission Granted" else "Permission Denied",
            Toast.LENGTH_LONG
        ).show()
    }
    Button(onClick = {
        launcher.launch(Manifest.permission.CAMERA)

//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val apiService = KtorfitInstance.createPoliceApiService()
//                val response = apiService.getStopAndSearchData()
//                withContext(Dispatchers.Main) {
//                    response.forEach { println(it) }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    println("Error fetching data: ${e.message}")
//                }
//            }
//        }ss

                     }, modifier = Modifier.padding(60.dp).testTag("permission_button")) {
        Text("Request Camera Permission")
    }
}

@Composable
fun AllComponentsScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 50.dp, start = 16.dp)) {
        item { SectionTitle("Text & Display") }
        item { TextDemo(16.dp,"This is a Text") }

        item { SectionTitle("Input Fields") }
        item { InputFieldsDemo() }

        item { SectionTitle("Buttons") }
        item { ButtonsDemo() }

        item { SectionTitle("Selection Controls") }
        item { SelectionControlsDemo() }

        item { SectionTitle("Scrollable Containers") }
//        item { ScrollableContainersDemo() }

        item { SectionTitle("Lists and Grids") }
        item { ListsAndGridsDemo() }

        item { SectionTitle("Images") }
        item { ImagesDemo() }

        item { SectionTitle("Modals & Popups") }
        item { ModalsAndPopupsDemo() }

        item { SectionTitle("Navigation") }
//        item { NavigationDemo() }

        item { SectionTitle("Sliders and Pickers") }
        item { SlidersAndPickersDemo() }

        item { SectionTitle("Progress Indicators") }
        item { ProgressIndicatorsDemo() }

        item { SectionTitle("Containers") }
        item { ContainersDemo() }

        item { SectionTitle("Tabs and Steppers") }
        item { TabsAndSteppersDemo() }

        item { SectionTitle("Chips") }
//        item { ChipsDemo() }

        item { SectionTitle("Gestures") }
        item { GesturesDemo() }

        item { SectionTitle("Graphics") }
        item { GraphicsDemo() }

        item { SectionTitle("Animations") }
        item { AnimationsDemo() }

        item { SectionTitle("Advanced Widgets") }
        item { AdvancedWidgetsDemo() }

//        item { SectionTitle("Platform Views") }
//        item { PlatformViewsDemo() }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

// Demo implementations for each category
@Composable
fun TextDemo(size:Dp,text1:String) {
    var text =text1
    Column {
        Text(text,maxLines = 1, modifier = Modifier.padding(size).testTag("textDemo")
            //,onValueChange = {
//            if (it.length <= 10) {
//                text = it
//            }
       // }
        )
        BasicText("This is a BasicText")
        Text("RichText Example with AnnotatedString")
    }
}

@Composable
fun InputFieldsDemo() {
    Column {
        var text by remember { mutableStateOf("") }
        OutlinedTextField(value = text,
            onValueChange = { text = it },
            label = { Text("OutlinedTextField") },
            modifier = Modifier.testTag("outline_inp"),
            maxLines = 1,
        )
        TextField(value = text,
            onValueChange = { text = it },
            label = { Text("TextField") },
            modifier = Modifier.testTag("normal_inp"),
            maxLines = 1

        )
    }
}

@Composable
fun ButtonsDemo() {
    var buttonText by remember { mutableStateOf("Click Me") }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = {buttonText = "clicked"}) { Text("Button $buttonText") }
        OutlinedButton(onClick = {
            buttonText="clicked"
        }) { Text("OutlinedButton $buttonText") }
        FloatingActionButton(onClick = {
            println("Floating Button clicked")
        }) { Text("+") }
    }
}

@Composable
fun SelectionControlsDemo() {
    Column {
        var checked by remember { mutableStateOf(false) }
        Checkbox(checked = checked, onCheckedChange = { checked = it })
        RadioButton(selected = checked, onClick = { checked = !checked })
        Switch(checked = checked, onCheckedChange = { checked = it })
    }
}

@Composable
fun ScrollableContainersDemo() {
    LazyColumn {
        items(20) { Text("Item #$it") }
    }
}

@Composable
fun ListsAndGridsDemo() {
    LazyRow {
        items(10) { Text("Row Item #$it", modifier = Modifier.padding(8.dp)) }
    }
}

@Composable
fun ImagesDemo() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(Icons.Default.Home, contentDescription = "Home")
        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "App Icon")
    }
}

@Composable
fun ModalsAndPopupsDemo() {
    var showDialog by remember { mutableStateOf(false) }
    Button(onClick = { showDialog = true }) { Text("Show Dialog") }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Dialog Title") },
            text = { Text("Dialog content goes here.") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("OK") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDemo() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("TopAppBar") }) },
        content = TODO(),
    )
//        bottomBar = { BottomNavigation { Text("Bottom Navigation") } }
//    ) {
//        Text("Navigation content goes here.")
//    }
}

@Composable
fun SlidersAndPickersDemo() {
    var sliderValue by remember { mutableStateOf(0f) }
    Slider(value = sliderValue, onValueChange = { sliderValue = it }, valueRange = 0f..100f)
}

@Composable
fun ProgressIndicatorsDemo() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CircularProgressIndicator()
        LinearProgressIndicator()
    }
}

@Composable
fun ContainersDemo() {
    Card(modifier = Modifier.padding(8.dp)) {
        Text("Card Content")
    }
}

@Composable
fun TabsAndSteppersDemo() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    TabRow(selectedTabIndex = selectedTabIndex) {
        Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) { Text("Tab 1") }
        Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) { Text("Tab 2") }
    }

}

@Composable
fun ChipsDemo() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = true, onClick = {},
            label = TODO(),
            modifier = TODO(),
            enabled = TODO(),
            leadingIcon = TODO(),
            trailingIcon = TODO(),
            shape = TODO(),
            colors = TODO(),
            elevation = TODO(),
            border = TODO()
        )
//        ActionChip(onClick = {}) { Text("Chip 2") }
    }
}

@Composable
fun GesturesDemo() {
    Box(modifier = Modifier.size(100.dp).background(MaterialTheme.colorScheme.primary).clickable { }) {
        Text("Tap Me", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun GraphicsDemo() {
    Canvas(modifier = Modifier.size(100.dp)) {
        drawCircle(color = Color.Red)
    }
}

@Composable
fun AnimationsDemo() {
    var visible by remember { mutableStateOf(true) }
    Button(onClick = { visible = !visible }) { Text("Toggle Visibility") }
    AnimatedVisibility(visible = visible) {
        Text("Animated Content")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerDemo() {
    val pagerState = rememberPagerState(pageCount = { 4 })

    HorizontalPager( state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
        Text("Page $page", modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun CarouselDemo() {
    val items = List(10) { "Item #$it" }

    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(item, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingToolbarDemo() {
    val scrollState = rememberScrollState()

    Column(
//        modifier = Modifier.horizontalScroll(scrollState)
    ) {
        TopAppBar(
            title = { Text("Collapsing Toolbar") },
            modifier = Modifier.offset(y = (-scrollState.value).dp)
        )

        Spacer(modifier = Modifier.height(200.dp)) // To simulate the collapsing effect
        Text("Content Below Toolbar")
    }
}


@Composable
fun AdvancedWidgetsDemo() {
    // Placeholder for advanced widgets like Pager, Carousel, etc.
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        SectionTitle("Pager")
        PagerDemo()

        SectionTitle("Carousel")
        CarouselDemo()

        SectionTitle("Collapsing Toolbar")
        CollapsingToolbarDemo()
    }
}

@Composable
fun PlatformViewsDemo() {
    AndroidView(
        factory = { context -> WebView(context).apply { loadUrl("https://www.example.com") } },
        modifier = Modifier.height(200.dp).fillMaxWidth()
    )
}


@Composable
fun FormScreen() {
    val context = LocalContext.current

    // State variables for form fields
    var text = remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("") }

    // Options for gender and country
    val genderOptions = listOf("Male", "Female", "Other")
    val countryOptions = listOf("USA", "India", "Canada", "UK", "Australia")

    // Main Form Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Input Fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth().testTag("name"),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        // Gender Radio Buttons
        Text("Gender", style = MaterialTheme.typography.bodyMedium)
        genderOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (gender == option),
                        onClick = { gender = option }
                    )
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = (gender == option),
                    onClick = { gender = option }
                )
                Text(text = option, modifier = Modifier.padding(start = 8.dp))
            }
        }

        // Country Drop-down Menu
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCountry,
                onValueChange = {},
                label = { Text("Country") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Drop-down Icon"
                    )
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                countryOptions.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(country) },
                        onClick = {
                            selectedCountry = country
                            expanded = false
                        }
                    )
                }
            }
        }

        // Checkbox for Accepting Terms
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = { acceptTerms = it }
            )
            Text(
                text = "I accept the terms and conditions",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Submit Button
        Button(
            onClick = {
                // Show a toast or handle form submission
                Toast.makeText(
                    context,
                    "Form Submitted\nName: $name\nEmail: $email\nPhone: $phoneNumber\nGender: $gender\nCountry: $selectedCountry",
                    Toast.LENGTH_LONG
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = name.isNotEmpty() && email.isNotEmpty() && acceptTerms
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun PlayWriteSocket(){
    var text by remember { mutableStateOf("") }
    var storedText by remember { mutableStateOf("") }

    //val client = remember { OkHttpClient() }
    //var webSocket by remember { mutableStateOf<WebSocket?>(null) }

    //val wsUrl = "ws://10.0.2.2:9222/devtools/browser/6f0756f5-c61c-4d96-8543-d8299ba02983"

    //52983/3359021b0909de19f15acbb37a07a234
    //val wsUrl = "ws://10.0.2.2:4000"

    //var sessionId by remember { mutableStateOf<String?>(null) }
//    LaunchedEffect(Unit) {
//        val request = Request.Builder().url(wsUrl).build()
//
//        webSocket = client.newWebSocket(request, object : WebSocketListener() {
////            override fun onOpen(webSocket: WebSocket, response: Response) {
////                println("✅ Connected to Playwright!")
////
////                // Open the Playwright website
//////                val command = """
//////                {
//////                    "id": 1,
//////                    "method": "Target.createTarget",
//////                    "params": {
//////                        "url": "https://playwright.dev"
//////                    }
//////                }
//////            """.trimIndent()
//////
////            val command = """
////                {
////                    "id": 1,
////                   "method": "Runtime.evaluate",
////                    "params": {
////                    "expression": "
////                    window.runPlaywright = async () => {
////                    const { chromium } = require('playwright');
////                    const browser = await chromium.launch();
////                    const page = await browser.newPage();
////                    await page.goto('https://playwright.dev');
////                    await page.click('a[href="/docs/intro"]');
////                    return 'Click Successful';
////                };"
////                }
////                }
////            """.trimIndent()
////
////                webSocket.send(command);
////
////                val command1 = """
////               {
////    "id": 2,
////    "method": "Runtime.callFunctionOn",
////    "params": {
////        "functionDeclaration": "window.runPlaywright()",
////        "executionContextId": 1
////    }
////}
////            """.trimIndent()
////
////                webSocket.send(command1);
////
////
////            }
//
//            override fun onOpen(webSocket: WebSocket, response: Response) {
//                println("✅ Connected to Playwright!")
//
//                val jsonMessage = JSONObject().apply {
//                    put("event", "performAction")  // Custom event name
//                    put("data", JSONObject().apply {
//                        put("action", "open_url")
//                        put("selector", "https://playwright.dev")
//                    })
//                }.toString()
//
//                // Send the message
//                webSocket.send(jsonMessage)
////
////                val command = """{
////                        "action": "open_url",
////                        "selector": "https://playwright.dev"
////                    }""".trimMargin()
////                webSocket.send(command)
////
////                val command1= """{
////                    "action": "click",
////                    "selector": "#submit-button"
////                }"""
////                webSocket.send(command1)
//            }
//
//            override fun onMessage(webSocket: WebSocket, text: String) {
//                val json = JSONObject(text)
//                println("🔹 Message from WebSocket: $json")
//
//                if (json.has("id") && json.getInt("id") == 1) {
//                    val targetId = json.getJSONObject("result").getString("targetId")
//                    println("📌 Target ID: $targetId")
//
//                    // Attach to the target page
//                    val attachCommand = """
//                        {
//                            "id": 3,
//                            "method": "Target.attachToTarget",
//                            "params": {
//                                "targetId": "$targetId",
//                                "flatten": true
//                            }
//                        }
//                    """.trimIndent()
//                    webSocket.send(attachCommand)
//
//
//                }
//
//                if (json.has("id") && json.getInt("id") == 2) {
//                    sessionId = json.getJSONObject("result").getString("sessionId")
//                    println("🔗 Session ID: $sessionId")
//                }
//            }
//            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//                println( "❌ Connection error: ${t.message}")
//            }
//        })
//    }
    val serverUrl = "http://13.233.104.203:4000" // Emulator → Use local IP for real devices
    val options = IO.Options().apply {
        transports = arrayOf("websocket") // Force WebSocket transport
        reconnection = true // Auto-reconnect on failure
    }

    val socket = IO.socket(serverUrl, options)
    LaunchedEffect(Unit) {


        socket.on(Socket.EVENT_CONNECT) {
            println("✅ Connected to Socket.IO Server!")

            val attachCommand = JSONObject().apply {
                put("action", "open_url")
                put("selector", "https://playwright.dev")
            }
            socket.emit("performAction", attachCommand)
            Thread.sleep(3000)
            val attachCommand1 = JSONObject().apply {
                put("action", "click")
                put("selector", "//a[contains(@href, '/docs/intro')]")
            }
            socket.emit("performAction", attachCommand1)
        }

        socket.on("actionResponse") { args ->
            val response = args[0] as JSONObject
            println("📩 Server Response: $response")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            println("⚠️ Disconnected from server")
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
            println("❌ Connection Error: ${args[0]}")
        }

        socket.connect()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter text") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            // https://playwright.dev
            // click (//a[contains(@href,"/docs/intro")])[1]
            //  (//a[contains(@href,"/docs/api")])[1]
            Button(
                onClick = {
                    val parts = text.split(" ", limit = 2)
                    print("testststst:------"+parts)
                    storedText = text
                    val attachCommand = JSONObject().apply {
                        put("action", parts[0])
                        put("selector", parts[1])
                    }
                    socket.emit("performAction", attachCommand)

                },
//                onClick = {
//                    if (webSocket != null && sessionId != null) {
//                        val parts = text.split(" ", limit = 2)
//                        if (parts.size == 2) {
////                            val command1 = """
////                                {
////                                    "id": 2,
////                                    "sessionId": "$sessionId",
////                                    "method": "Runtime.evaluate",
////                                    "params": {
////                                        "expression": "window.playwright = require('playwright');"
////                                    }
////                                }
////                            """.trimIndent()
////                            webSocket?.send(command1)
//
//                            val selector = parts[0]
//                            val action = parts[1]
//
//                            val actionCommand = when (action) {
//                                "click" -> """
//                                    {
//                                        "id": 4,
//                                        "sessionId": "$sessionId",
//                                        "method": "Runtime.evaluate",
//                                        "params": {
//                                            "expression": "document.querySelector('$selector').click()",
//
//                                         }
//                                    }
//                                """.trimIndent()
//
//                                "input" -> """
//                                    {
//                                        "id": 5,
//                                        "sessionId": "$sessionId",
//                                        "method": "Runtime.evaluate",
//                                        "params": {
//                                            "expression": "document.querySelector('$selector').value = 'Hello from Kotlin'"
//                                        }
//                                    }
//                                """.trimIndent()
//
//                                else -> null
//                            }
//
//                            if (actionCommand != null) {
//                                println("📤 Sending Command: $actionCommand")
//                                webSocket?.send(actionCommand)
//                                storedText = text
//                            } else {
//                                println("❌ Unsupported action: $action")
//                            }
//                        } else {
//                            println("❌ Invalid input format! Use: `selector action` (e.g., `a[href='/docs/intro'] click`)")
//                        }
//                    } else {
//                        println("❌ WebSocket not connected or session not available!")
//                    }
//                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Stored Value: $storedText")
        }
    }
}

@Composable
fun SocketScreen() {
    var text by remember { mutableStateOf("") }
    var storedText by remember { mutableStateOf("") }

    val options = IO.Options().apply {
        reconnection = true
        transports = arrayOf("websocket", "polling") // Enable polling fallback
        secure = false
        forceNew = true // Ensures a fresh connection
    }

    val socket: Socket = IO.socket("http://192.168.1.12:3000", options)

    LaunchedEffect(Unit) {
        socket.on(Socket.EVENT_CONNECT) {
            println("✅ Socket connected!")
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
            println("❌ Socket connection error: ${args[0]}")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            println("⚠️ Socket disconnected!")
            socket.connect()
        }

        socket.on("message", Emitter.Listener { args ->
            if (args.isNotEmpty()) {
                storedText = args[0].toString()
                println("📩 Received from server: $storedText")
            }
        })

        socket.connect()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter text") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (!socket.connected()) {
                        println("SOCKET ❌ Socket not connected. Reconnecting...")
                        socket.connect()
                    } else {
                        socket.emit("message", text)
                        storedText = text
                        println("SOCKET 📤 Sent message: $text")
                    }
                    storedText = text },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Stored Value: $storedText")
        }
    }
}

@Composable
fun WebSocketScreen1() {
    lateinit var socket: Socket
    val socketManager = remember { WebSocketManager() }
    var selectedAction by remember { mutableStateOf("") }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var selector by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val actions = listOf("open_url", "click", "type", "scroll", "screenshot")

    LaunchedEffect(Unit) {
        try {
            socket = IO.socket("http://10.0.2.2:4000")
            socket.connect()

            socket.on("actionResponse") { args ->
                if (args.isNotEmpty()) {
//                    val response = args[0] as JSONObject
//                    println("WebSocket Response: ${response.toString(2)}")
                    if (args.isNotEmpty()) {
                        val response = args[0] as JSONObject
                        val status = response.optString("status", "failed")
                        val message = response.optString("message", "Unknown error")

                        // Set toast message based on response status
                        toastMessage = if (status == "success") {
                            "Action successful!"
                        } else {
                            "Action failed: $message"
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("WebSocket Error connecting: ${e.message}")
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("WebSocket Playwright Client", style = MaterialTheme.typography.titleLarge)

        // Dropdown for actions
        Box {
            Button(onClick = { expanded = true }) {
                Text(if (selectedAction.isEmpty()) "Select Action" else selectedAction)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                actions.forEach { option ->
                    DropdownMenuItem(
                        { Text(option) },
                        onClick = {
                        selectedAction = option
                        expanded = false
                    })
                }
            }
        }

//        // Input field for selector or URL
//        OutlinedTextField(
//            value = ,
//            onValueChange = { selector = it },
//            label = { Text("Selector / URL") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions.Default
//        )

        TextField(
            value = selector,
            onValueChange = { selector = it },
            label = { Text("Enter text") },
            modifier = Modifier.fillMaxWidth()
        )

        // Send button
        Button(
            onClick = {
                if (selectedAction.isNotEmpty() && selector.isNotEmpty()) {
                    //socketManager.sendAction(selectedAction, selector)
                    val jsonObject = JSONObject()
                    jsonObject.put("action", selectedAction)
                    jsonObject.put("selector", selector)
                    socket.emit("performAction", jsonObject)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedAction.isNotEmpty() && selector.isNotEmpty()
        ) {
            Text("Send Command")
        }
    }
}

@Composable
fun WebSocketScreen() {
    val context = LocalContext.current
    val socket = remember { IO.socket("http://10.0.2.2:4000").apply { connect() } }
    var selectedAction by remember { mutableStateOf("") }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var selector by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val actions = listOf("open_url", "click", "type", "scroll", "screenshot")

    // WebSocket event listener
    LaunchedEffect(socket) {
        socket.on("actionResponse") { args ->
            if (args.isNotEmpty()) {

                val response = args[0] as JSONObject
                println(response)
                val status = response.optString("status", "failed").trim().lowercase()
                val message = response.optString("message", "Unknown error")

                // Set toast message based on response status
                toastMessage = if (status == "success") {
                    "Action successful!"
                } else {
                    "Action failed: $message"
                }
            }
        }
    }

    // Show Toast when message changes
    toastMessage?.let { message ->
        LaunchedEffect(message) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            toastMessage = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("WebSocket Playwright Client", style = MaterialTheme.typography.titleLarge)

        // Dropdown for actions
        Box {
            Button(onClick = { expanded = true }) {
                Text(if (selectedAction.isEmpty()) "Select Action" else selectedAction)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                actions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedAction = option
                            expanded = false
                        }
                    )
                }
            }
        }

        // Input field for selector or URL
        TextField(
            value = selector,
            onValueChange = { selector = it },
            label = { Text("Enter Selector / URL") },
            modifier = Modifier.fillMaxWidth()
        )

        // Send button
        Button(
            onClick = {
                if (selectedAction.isNotEmpty() && selector.isNotEmpty()) {
                    val jsonObject = JSONObject().apply {
                        put("action", selectedAction)
                        put("selector", selector)
                    }
                    socket.emit("performAction", jsonObject)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedAction.isNotEmpty() && selector.isNotEmpty()
        ) {
            Text("Send Command")
        }
    }
}

class WebSocketManager {
    private lateinit var socket: Socket

    fun connect() {
        try {
            socket = IO.socket("http://10.0.2.2:4000")
            socket.connect()

            socket.on("actionResponse") { args ->
                if (args.isNotEmpty()) {
                    val response = args[0] as JSONObject
                    println("WebSocket Response: ${response.toString(2)}")
                }
            }
        } catch (e: Exception) {
            println("WebSocket Error connecting: ${e.message}")
        }
    }

    fun sendAction(action: String, selector: String) {
        val jsonObject = JSONObject()
        jsonObject.put("action", action)
        jsonObject.put("selector", selector)
        socket.emit("performAction", jsonObject)
    }

    fun disconnect() {
        socket.disconnect()
    }
}


