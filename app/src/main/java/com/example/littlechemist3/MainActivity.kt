package com.example.littlechemist3

import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.littlechemist3.VisualElements.Companion.Add
import com.example.littlechemist3.VisualElements.Companion.AddLine
import com.example.littlechemist3.VisualElements.Companion.ResetStrokes
import com.example.littlechemist3.ui.theme.LittleChemist3Theme
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

/*interface PortsService {
    @GET("winter-navigation/ports")
    fun getCurrentPortsData( /*  @Query("lat") lat: String  */): Call<Ports>
}*/

interface GitHubService {
    @GET("moleculelist.csv")
    fun listRepos(/*@Path("user") user: String?*/): Call<String?>?  // could be "suspend"
}

class MainActivity : ComponentActivity() {
    //var retvals = ""
    //private var selected: ToolBoxItem2? = null//ToolBoxItem? = null
    //val app = mutableStateOf(ChainSystem(""))


    private val images =  // compose: painter=painterResource(R.drawable.nnn)
        arrayOf(  // was R.drawable.yellowball
            ToolBoxItem2("H", Color.YELLOW, R.drawable.yellowball),
            ToolBoxItem2("N", Color.BLUE, R.drawable.blueball),
            ToolBoxItem2("O", Color.RED, R.drawable.redball),
            ToolBoxItem2("C", Color.BLACK, R.drawable.gray),
            ToolBoxItem2("OH", Color.WHITE, R.drawable.lightgray),
            ToolBoxItem2("Ca", Color.GRAY, R.drawable.lightgray)
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getRestData("https://laitinent.github.io/" /*, response,app*/)
        setContent {
            LittleChemist3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GetDataUI(/*app.value,"https://laitinent.github.io/",*/ images)
                }
            }
        }
    }
}

/**
 * UI that calls REST reader, and also main view
 */
@OptIn(ExperimentalTextApi::class)
@Composable
fun GetDataUI(/*app: ChainSystem, url: String, */ images: Array<ToolBoxItem2>) {

    var offset = remember { mutableStateOf(Offset.Zero) } //used as parameter, must be = (not by)
    var selected by remember { mutableStateOf(images[0]) }
    var textNote = remember { mutableStateOf("--") }
    var previousPoint = remember { mutableStateOf(Offset.Zero) }
    val list2 = remember { mutableStateListOf<VisualNode>() }

    //if (selected != null) {
    //var selectedPoint = Offset.Zero//PointF()
    var image: ImageBitmap? = ImageBitmap.imageResource(selected.drawableResId)
    //}

    val imageResources = mutableListOf<ImageBitmap>()
    for (res in images) {
        imageResources.add(ImageBitmap.imageResource(res.drawableResId)) //vxcfdsew
    }

    //getRestData(url, /*response,*/ app)
    Column {
        LazyRow {
            items(images) {
                Image(
                    painter = painterResource(it.drawableResId),
                    contentDescription = it.text,
                    modifier = Modifier
                        .selectable(false) { selected = it }
                        .size(50.dp, 50.dp)
                )

            }
        }

        Row {
            Text(text = textNote.value)
            Button(onClick = {
                Clear( /*app, canvas: Canvas,*/ offset,//selectedPoint,
                    textNote
                )
                previousPoint.value = Offset.Zero
            }) {
                Text(text = "Clear")
            }
        }
        val tm = rememberTextMeasurer()
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        offset.value = it
                        Log.d("canvas", it.toString())
                        updateChain(
                            //app,
                            selected,
                            it,
                            previousPoint,
                            textNote
                        )
                        // update from ChainSystem
                        list2.clear()
                        VisualElements.lista.forEach {
                            list2.add(it)
                        }
                    }
                })
        {
            //if (image != null)
            //{
            //val io = IntOffset(offset.value.x.toLong().toInt(), offset.value.y.toLong().toInt())
            //Log.d("canvas","Int: $io.toString()")
            //see https://developer.android.com/jetpack/compose/graphics/images/custompainter
            /*drawImage(
                image = image,
                srcOffset = IntOffset.Zero,
                dstOffset = io,
                srcSize = IntSize(image.width, image.height),
                dstSize = IntSize(50, 50)
            )*/
            //}
            Log.d("COUNT", VisualElements.lista.size.toString() + " nodes")
            VisualElements.lista.forEach { vnode ->
                val d = images.toList().find { it.drawableResId == vnode.tb.drawableResId }
                val index = images.toList().indexOf(d)
                val r = imageResources[index]
                //val p = painterResource(id = d.drawableResId)
                Log.d("DRAW", "$r ($index)")
                drawGraphNode(vnode, r, tm)
            }
            VisualElements.lines.forEach { l ->
                //.dp.toPx() added for display compatibility
                drawLine(
                    androidx.compose.ui.graphics.Color.Black,
                    Offset(l.left.dp.toPx(), l.top.dp.toPx()),
                    Offset(l.right.dp.toPx(), l.bottom.dp.toPx()),
                    5f.dp.toPx()
                )
            }
        }
    }
}


//@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawGraphNode(vnode: VisualNode, imageItem: ImageBitmap, tm: TextMeasurer) {


    //textpaint.textSize = 48f

    //paint.color= Color.RED
    //paint.strokeWidth = 5f

    //for(vnode in VisualElements.lista) {
    val xsize = 100//(vnode.s.width  ) /2
    val ysize = 100 //vnode.s.height/2
    val left = (vnode.point.x - (xsize / 2))
    val top = (vnode.point.y - (ysize / 2))
    //val right = (vnode.point.x+xsize)
    //val bottom = (vnode.point.y+ysize)

    // show active node with border(=stroke)
    //paint.color = vnode.tb.color
    //paint.style = Paint.Style.FILL
    //canvas.drawOval(vnode.x-25, vnode.y-25,vnode.x+vnode.vnode.width/2, vnode.y+vnode.vnode.height/2, paint )
    //val d = resources.getDrawable(R.drawable.redball, null)

    //painterResource(id = vnode.tb.drawableResId)
    //Log.d("MYVIEW", "${d.intrinsicHeight} x ${d.intrinsicWidth}")

    //d.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    Log.d("DRAW", "($left, $top) ")

    this.drawImage(
        image = imageItem,
        dstOffset = IntOffset(left.toInt(), top.toInt()),
        dstSize = IntSize(xsize, ysize)
    )//d.draw(canvas!!)

    // highlight current
    if (vnode.Current) {
        //paint.style = Paint.Style.STROKE
        val c = androidx.compose.ui.graphics.Color.Black
        this.drawOval(
            c,
            Offset(left.dp.toPx(), top.dp.toPx()),
            //Size(xsize.toFloat(), ysize.toFloat()),
            Size(xsize.dp.toPx(), ysize.dp.toPx()),
            1.0f.dp.toPx(),
            style = Stroke(5f.dp.toPx())
        )
    }

    this.drawText(
        tm, vnode.tb.text, Offset(vnode.point.x.dp.toPx(), (vnode.point.y + 50f).dp.toPx()), style = TextStyle(
            fontSize = 48.sp
        )
    )//, textpaint)
}


//@Composable
fun Clear(
    //app: ChainSystem,
    //canvas: Canvas,
    selectedPoint: MutableState<Offset>,
    textNote: MutableState<String>
) {
    ChainSystem.Clear()//app.Clear()
    VisualElements.Clear()
    //Canvas(modifier = Modifier.fillMaxSize()){    }//canvas.Clear()
    selectedPoint.value = Offset.Zero
    textNote.value = "--"
}

/**
 * Update application data, also in VisualElements
 * @param selected ToolBoxItem2 that user has selected to be added
 * @param hitOffset Coordinates on canvas, place where item is added
 * @param previousPoint previous point clicked . This value is returned
 */
fun updateChain(
    //App: ChainSystem,
    selected: ToolBoxItem2,
    hitOffset: Offset,
    previousPoint: MutableState<Offset>,
    textNote: MutableState<String>
) {
    //TODO: when iscomplete, count and match not executed ?
    if (ChainSystem.isEmpty() || !(ChainSystem.IsComplete() || ChainSystem.CountAndMatchKnown() != "?")) {//&& selected != null) {
        // pick item from toolbar
        //if (event.y < 200f) {
        //selected = ToolHit(event.x, event.y)
        //} else {
        // Use picked tool item
        //Add(VisualNode(event.x, event.y, shape, selectedText))
        val selectedNode = ChainSystem.Link(selected.text)
        Log.d("APP", selected.text + "(of " + selectedNode.Nodes.size + ")")
        //TODO: Hydrogen autofill when new >1 link node is selected
        // latest node is shown with border
        if (!selectedNode.IsFull()) {
            ResetStrokes()
            Add(
                VisualNode(
                    hitOffset /*Offset(hitOffset.x, hitOffset.y)*/,
                    selected,
                    true
                )
            ) // null was shape
        } else {
            Add(VisualNode(hitOffset, selected))
        }

        if (selected.text == "OH") {
            //TODO: show as 2 ellipses
        }

        if (previousPoint.value != Offset.Zero) { //.value.x != 0f && previousPoint.value.y != 0f) {
            AddLine(hitOffset /*Offset(event.x, event.y)*/, previousPoint.value)
        }
        if (!selectedNode.IsFull()) {
            previousPoint.value =
                hitOffset//Offset( event.x, event.y ) // for next use, remember position for line
        }
        if (ChainSystem.IsComplete() || ChainSystem.CountAndMatchKnown() != "?") {
            textNote.value = ChainSystem.toString(true)//textView.text = App.toString(true)
            Log.d("TOOL_HIT", textNote.value)
        }
    }
}

/**
 * actual REST reader
 * @param url base address
 */
fun getRestData(
    url: String,
    //retvals: MutableState<String>,
    //App: MutableState<ChainSystem>?
): String {   // TODO: mutableState in parameter

    // add auth header to request
    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    "Basic " + Base64.encodeToString("user:password".toByteArray(), Base64.NO_WRAP)
                )
                .build()
            chain.proceed(newRequest)
        }
        .build()

    //lateinit var App: ChainSystem
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(ScalarsConverterFactory.create()) //GsonConverterFactory.create())//
        .client(client)
        .build()

    val service =
        retrofit.create(GitHubService::class.java) //PortsService::class.java)//GitHubService::class.java)
    val call = service.listRepos()//getCurrentPortsData()

    // Fetch and print a list of the contributors to the library.
    //var retvals: String? = ""
    //retvals = call.execute().body()!!
    call?.enqueue(object : Callback<String?> {
        //Ports> {
        override fun onResponse(
            call: Call<String?>,
            response: Response<String?>
        ) {//Call<Ports>, response: Response<Ports>) {//
            if (response.code() == 200) {//response.isSuccessful) {
                val responseString = response.body()!!
                //retvals.value = responseString//.features[0].properties.name // used only for testing
                /*App?.value = */ChainSystem.setKnownData(responseString)//ChainSystem(responseString)

                Log.d("REPOS", responseString)
                /*helloTextView.text = "Ready to add Atoms!"
                toolBox2.isEnabled=true
                setCustomAdapter(images, true, toolBox2) // was on line 79
                setTouchListenetToApp(shape) // was on line 95
                 */
            }
        }

        override fun onFailure(call: Call<String?>, t: Throwable) {//<Ports>, t: Throwable) {//
            Log.d("VIRHE", t.message!!)
        }
    })
    return "ready"
}

// below not used
@Composable
fun Greeting(name: String) {//}, modifier: Modifier = Modifier) {
    //var teksti = remember { mutableStateOf("") }
    //var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    //val lista = getRestData("https://meri.digitraffic.fi/api/v1/", /*teksti,*/ null)

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LittleChemist3Theme {
        Greeting("Android")
    }
}