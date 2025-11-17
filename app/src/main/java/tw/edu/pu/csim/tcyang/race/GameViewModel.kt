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
        //回到初使位置
        circleX = 100f
        circleY = screenHeightPx - 100f

        for (i in 0..2) {
            horses.clear()
            horses.add(Horse(i))
        }

        viewModelScope.launch {
            while (gameRunning) { // 每0.1秒循環
                delay(100)
                circleX += 10


                if (circleX >= screenWidthPx - 100){
                    circleX = 100f
                }

                for (i in 0..2) {
                    horses[i].HorseRun()
                    if (horses[i].horseX >= screenWidthPx - 200) {
                        horses[i].horseX = 0 }

                }
            }
        }
    }
}