package tw.edu.pu.csim.tcyang.canvas2026_11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import tw.edu.pu.csim.tcyang.canvas2026_11.ui.theme.Canvas2026_11Theme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Canvas2026_11Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DigitalInk(
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Canvas2026_11Theme {
        Greeting("Android")
    }
}

@Composable
fun TestCanvas(modifier: Modifier = Modifier) {
    // 載入圖片資源 (小小兵)
    val minionsImage: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.minions)

    // 建立一個新的路徑物件
    /*val path =
        Path().apply {
            moveTo(50f, 100f)  // 移動到起始點 (50f, 100f)
            lineTo(200f, 500f)  // 畫一條線到 (200f, 500f)
            lineTo(450f, 400f)  // 畫一條線到 (450f, 400f)
            close()  // 閉合路徑
        }

     */
    var path by remember {
        mutableStateOf(Path())
    }


    Canvas(
        modifier = modifier.fillMaxSize()
            .pointerInput(Unit) {
                // 監聽拖曳手勢
                detectDragGestures(
                    onDragStart = { offset -> /* 手指按下 */
                        // 按下：記錄 Path 起點
                        val x = offset.x
                        val y = offset.y

                        path.reset()  // 將 path 物件完全清空並重置
                        path.moveTo(x, y)
                    },

                    onDrag = { change, dragAmount -> /* 手指移動 */
                        // 消費拖曳事件（避免影響父層元件手勢）
                        // 這個移動手勢已經被 Canvas 處理，請不要再傳給外層的其他元件
                        change.consume()

                        // 移動：拉線並加入點座標
                        val x = change.position.x
                        val y = change.position.y
                        path.lineTo(x, y)

                        //複製出一個內容一模一樣、但記憶體位址全新的 Path 物件
                        // 並指派回 path，藉此強制 Compose 重繪 Canvas 畫面
                        val tempPath = Path()
                        tempPath.addPath(path)
                        path = tempPath
                    },

                    onDragEnd = { /* 手指離開 */ }
                )
            }

    ) {
        //繪製洋紅色的圓, 半徑 100, 圓心 (100,100)
        drawCircle(
            Color.Magenta,
            100f,
            Offset(100f, 100f)
        )

        //繪製藍色空心矩形, 左上角 (300,200), 寬度與高度 200
        drawRect(
            Color.Blue,
            Offset(300f, 200f),
            Size(200f, 200f),
            style = Stroke(width = 5f) // 使用 style 控制外框樣式
        )

        // 繪製紅色的線， 從 (200,800) 畫到右下角, 線條粗細 10
        // size.width：畫布的總寬度（Float）
        // size.height：畫布的總高度（Float)

        drawLine(
            Color.Red,
            Offset(200f, 800f),
            Offset(size.width, size.height),
            strokeWidth = 10f
        )

        //取得小小兵圖檔的寬度與高度
        val minionsWidth = minionsImage.width
        val minionsHeight = minionsImage.height

        // 置中公式：(畫布寬高 - 圖片寬高) / 2f
        val offsetX = (size.width - minionsWidth) / 2f
        val offsetY = (size.height - minionsHeight) / 2f

        // 將畫布起點移至置中位置後畫圖
        translate(offsetX, offsetY) {
            drawImage(minionsImage)
        }

        // 繪製綠色路徑，透明度1 (不透明)，線條粗細10
        drawPath(path, Color.Green, 1f, Stroke(width = 10f))



    }
}

@Composable
fun DigitalInk(modifier: Modifier = Modifier) {

    var path by remember { mutableStateOf(Path() )}

    var recognizedText by remember {
        mutableStateOf("請在畫布區手寫，再按「開始辨識」按鈕")
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier.size(300.dp)
                .border(2.dp, Color.Gray)
        ) {
        drawPath(path, Color.Black, 1f, Stroke(width = 10f))
    }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            // 開始辨識
            Button(onClick = {
            }) {
                Text("開始辨識")
            }
            Spacer(modifier = Modifier.width(16.dp))
            // 清除畫布
            Button(onClick = {
                path = Path()  // 指派新物件位址，立刻觸發 Canvas 重繪並清空畫面
            }){
                Text("清除")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(recognizedText)
    }
}
