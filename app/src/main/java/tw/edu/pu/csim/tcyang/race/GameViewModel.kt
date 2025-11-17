package tw.edu.pu.csim.tcyang.race

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {
    var screenWidthPx by mutableFloatStateOf(0f)
        private set

    var screenHeightPx by mutableStateOf(0f)
        private set

    var gameRunning by mutableStateOf(false)

    var circleX by mutableStateOf(0f)
    var circleY by mutableStateOf(0f)

    var winnerHorse by mutableStateOf<Int?>(null) // 儲存獲勝馬的編號


// 設定螢幕寬度與高度
    fun SetGameSize(w: Float, h: Float) {
        screenWidthPx = w
        screenHeightPx = h
    }

    //val horse = Horse()
    val horses = mutableListOf<Horse>()
    fun MoveCircle(x: Float, y: Float) {
        circleX += x
        circleY += y
    }
    fun StartGame() {
        gameRunning = true
        winnerHorse = null  // 重置獲勝者
        //回到初使位置
        circleX = 100f
        circleY = screenHeightPx - 100f

        horses.clear()
        for (i in 0..2) {
            horses.add(Horse(i))
        }
        for (horse in horses) {
            horse.horseX = 0
        }

        viewModelScope.launch {
            while (gameRunning) { // 每0.1秒循環
                delay(100)
                circleX += 10


                if (circleX >= screenWidthPx - 100){
                    circleX = 100f
                }

                for (i in horses.indices) {
                    horses[i].HorseRun()
                    if (horses[i].horseX >= screenWidthPx - 200 && winnerHorse == null) {
                        winnerHorse = i + 1  // 記錄獲勝者
                        gameRunning = false   // 停止這一輪賽跑
                        break  // 跳出循環，開始下一輪
                    }

                }
                if (winnerHorse != null) {
                    delay(1000)  // 顯示獲勝訊息
                    winnerHorse = null
                    for (horse in horses) {
                        horse.horseX = 0  // 重置每匹馬的 X 坐標
                    }
                    gameRunning = true  // 重新啟動遊戲
                }
            }
        }
    }
}