package components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImprovedTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    defaultText: String = "",
    fontSize: TextUnit = 16.sp,
    color: Color = MaterialTheme.colors.onSecondary,
    textAlign: TextAlign = TextAlign.Start,
    leadingIcon: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(top = 1.dp, bottom = 1.dp),
    singleLine: Boolean = true,
    onSubmit: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        modifier = modifier
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    focusManager.clearFocus()
                    onSubmit()
                    true
                } else false
            },
        textStyle = TextStyle(fontSize = fontSize, color = color, textAlign = textAlign),
    ) {
        TextFieldDefaults.TextFieldDecorationBox(
            value = value.text,
            innerTextField = it,
            singleLine = singleLine,
            enabled = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() },
            contentPadding = contentPadding,
            leadingIcon = leadingIcon,
            placeholder = { Text(defaultText, Modifier.fillMaxWidth(), fontSize = fontSize, textAlign = textAlign) }
        )
    }
}