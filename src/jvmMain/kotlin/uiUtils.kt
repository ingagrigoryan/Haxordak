import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat

@Composable
internal fun getText(text: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text,
            Modifier
                .padding(start = 8.dp, top = 4.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        0.0F to Color.Blue,
                        1.0F to Color(160, 32, 240)
                    ),
                    shape = RoundedCornerShape(16))
                .padding(8.dp),
            color = Color.White
        )
        time(Modifier.align(Alignment.Bottom))
    }
}

@Composable
internal fun getTextFrom(text: String) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End) {
        time(Modifier.align(Alignment.Bottom))
        Text(
            text,
            Modifier
                .padding(end = 8.dp, top = 4.dp)
                .background(Color.Blue, shape = RoundedCornerShape(16))
                .padding(8.dp),
            color = Color.White
        )
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
internal fun time(modifier: Modifier) {
    val time = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("HH:mm")
    val formattedText = dateFormat.format(time)
    return Text(formattedText, modifier.padding(start = 4.dp, end = 4.dp), fontSize = TextUnit(12F, TextUnitType.Sp))
}