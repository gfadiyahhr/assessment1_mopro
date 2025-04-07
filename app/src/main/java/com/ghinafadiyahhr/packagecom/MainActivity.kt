package com.ghinafadiyahhr.packagecom

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ghinafadiyahhr.packagecom.navigation.SetupNavGraph
import com.ghinafadiyahhr.packagecom.screen.MainScreen
import com.ghinafadiyahhr.packagecom.ui.theme.Packagecomghinafadiyahhr607062300001asessment1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Packagecomghinafadiyahhr607062300001asessment1Theme {
                SetupNavGraph()
            }
        }
    }
}


