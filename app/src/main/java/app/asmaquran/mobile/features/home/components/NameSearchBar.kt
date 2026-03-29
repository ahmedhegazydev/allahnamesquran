package app.asmaquran.mobile.features.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.asmaquran.mobile.R
import app.asmaquran.mobile.core.ui.preview.PreviewSurface
import app.asmaquran.mobile.core.ui.theme.QuranFontFamily

@Composable
fun NameSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(R.string.search_name_placeholder),
                fontFamily = QuranFontFamily,
                color = Color(0xFF9BA3AF)
            )
        },
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Rounded.Search,
//                contentDescription = null,
//                tint = Color(0xFF8A928F),
//                modifier = Modifier
//                    .background(Color(0xFFF4F1EC), CircleShape)
//                    .border(1.dp, Color(0xFFE7E0D4), CircleShape)
//                    .padding(8.dp)
//            )
//        },
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = Color(0xFF2F6D58),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        textStyle = TextStyle(
            fontFamily = QuranFontFamily,
            color = Color(0xFF243042)
        ),
        singleLine = true
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F3ED)
@Composable
private fun NameSearchBarPreview() {
    PreviewSurface {
        NameSearchBar(
            query = "الرحمن",
            onQueryChange = {}
        )
    }
}