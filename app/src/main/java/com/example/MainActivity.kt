package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.ParentTravelScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. 初始化 Room 本地離線資料庫與儲存庫
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = TravelRepository(database.travelDao())
        
        // 2. 建立 ViewModel 的 Factory 工廠，用於注入儲存庫
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TravelViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return TravelViewModel(repository) as T
                }
                throw IllegalArgumentException("找不到對應的 ViewModel 類別: ${modelClass.name}")
            }
        }

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 3. 實例化並傳入 ViewModel 渲染爸媽專用主畫面
                    val travelViewModel: TravelViewModel = viewModel(factory = viewModelFactory)
                    ParentTravelScreen(
                        viewModel = travelViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

