package com.philimonnag.smarttaskmanager.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.philimonnag.smarttaskmanager.ui.viewmodels.ThemeViewModel

@Composable
fun SettingScreen(
    themeViewModel: ThemeViewModel,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            themeViewModel.isDarkTheme.value?.let { Switch(checked = it, onCheckedChange ={themeViewModel.toggleTheme()} ) }
            Spacer(modifier = Modifier.padding(horizontal = 12.dp))
            Text(text = "Theme")
        }

    }
}