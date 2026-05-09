package app.iesjdlc.tipslab.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.iesjdlc.tipslab.R
import app.iesjdlc.tipslab.core.constants.FormConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifehackStepsSheet(
    steps: List<String>,
    onConfirm: (List<String>) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val initialSteps = if (steps.isEmpty()) {
        mutableListOf("", "", "")
    } else {
        steps.toMutableList().also {
            while (it.size < 3) it.add("")
        }
    }

    var currentSteps by remember { mutableStateOf(initialSteps.toList()) }
    var attemptedConfirm by remember { mutableStateOf(false) }

    fun isStepValid(step: String) = step.isBlank() || step.length >= FormConstants.MIN_STEP_LENGTH

    fun hasValidSteps(): Boolean {
        val filled = currentSteps.filter { it.isNotBlank() }
        return filled.isNotEmpty() && filled.all { isStepValid(it) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 700.dp)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.add_steps_to_lifehack),
                style = MaterialTheme.typography.titleLarge,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentSteps.forEachIndexed { index, step ->
                    val isError = attemptedConfirm && step.isNotBlank() && step.length < FormConstants.MIN_STEP_LENGTH

                    StepField(
                        index = index,
                        value = step,
                        isError = isError,
                        canMoveUp = index > 0,
                        canMoveDown = index < currentSteps.lastIndex,
                        canDelete = currentSteps.size > 3,
                        onValueChange = { newValue ->
                            if (newValue.length <= FormConstants.MAX_STEP_LENGTH) {
                                currentSteps = currentSteps.toMutableList().also {
                                    it[index] = newValue
                                }
                            }
                        },
                        onMoveUp = {
                            currentSteps = currentSteps.toMutableList().also {
                                val tmp = it[index - 1]
                                it[index - 1] = it[index]
                                it[index] = tmp
                            }
                        },
                        onMoveDown = {
                            currentSteps = currentSteps.toMutableList().also {
                                val tmp = it[index + 1]
                                it[index + 1] = it[index]
                                it[index] = tmp
                            }
                        },
                        onDelete = {
                            currentSteps = currentSteps.toMutableList().also {
                                it.removeAt(index)
                            }
                        }
                    )
                }

                TextButton(
                    onClick = {
                        currentSteps = currentSteps + ""
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.add_step),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        attemptedConfirm = true
                        if (hasValidSteps()) onConfirm(currentSteps)
                    },
                    enabled = !attemptedConfirm || hasValidSteps(),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun StepField(
    index: Int,
    value: String,
    isError: Boolean,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    canDelete: Boolean,
    onValueChange: (String) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.background,
        border = if (isError) BorderStroke(1.dp, MaterialTheme.colorScheme.error) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                isError = isError,
                placeholder = {
                    Text(
                        text = stringResource(R.string.step_placeholder),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp, max = 140.dp),
                shape = MaterialTheme.shapes.medium,
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                supportingText =
                    if (isError) {
                        {
                            Text(
                                text = stringResource(R.string.step_min_chars, FormConstants.MIN_STEP_LENGTH),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    } else if (value.isNotEmpty()) {
                        {
                            Text(
                                text = "${value.length}/${FormConstants.MAX_STEP_LENGTH}",
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    } else null
            )

            Icon(
                imageVector = Icons.Rounded.DragHandle,
                contentDescription = "Reorder",
                modifier = Modifier.padding(top = 16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}