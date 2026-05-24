package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TravelItem
import com.example.TravelViewModel

// High Density Theme Color Palette
private val HighDensityBg = Color(0xFFF7F2FA)        // Soft lavender off-white background
private val TextDarkColor = Color(0xFF1C1B1F)        // Rich slate black for maximum contrast
private val Blue600 = Color(0xFF2563EB)              // Header Accent Blue
private val Blue700 = Color(0xFF1D4ED8)              // Core Brand Blue
private val Blue100 = Color(0xFFDBEAFE)              // Light blue highlight backplate
private val Green800 = Color(0xFF2E7D32)             // Custom High Density navigation green
private val Green900 = Color(0xFF1B5E20)             // Active state solid green
private val CoralAccent = Color(0xFFE0533C)          // Warning/Emphasis shade
private val Amber100 = Color(0xFFFEF3C7)             // Background for time tags
private val Amber900 = Color(0xFF78350F)             // Text for time tags
private val BorderSlate100 = Color(0xFFF1F5F9)       // Soft card borders
private val Slate300 = Color(0xFFCBD5E1)             // Unselected tab border
private val Slate500 = Color(0xFF64748B)             // Unselected tab text label
private val VisitedBg = Color(0xFFE8F5E9)            // Joyous light green for visited spots

// Navigation tabs for the bottom navigation bar
enum class ParentScreenTab {
    HOME,
    ITINERARY,
    SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentTravelScreen(
    viewModel: TravelViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedDay by viewModel.selectedDay.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()
    val trips by viewModel.currentDayTrips.collectAsState()
    
    // Bottom Tab state tracking
    var currentTab by remember { mutableStateOf(ParentScreenTab.ITINERARY) }

    // Helper functions for dynamic UI pairing
    val currentDate = when (selectedDay) {
        "Day 1" -> "5/27 週三"
        "Day 2" -> "5/28 週四"
        "Day 3" -> "5/29 週五"
        "Day 4" -> "5/30 週六"
        else -> "5/27 週三"
    }

    val currentEmoji = when (selectedDay) {
        "Day 1" -> "☀️"
        "Day 2" -> "🌤️"
        "Day 3" -> "🏖️"
        "Day 4" -> "🏝️"
        else -> "☀️"
    }

    Scaffold(
        topBar = {
            // Material 3 Custom High Density Header Box
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .testTag("app_header")
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 14.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "澎湖孝親旅行 🐢",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Blue600,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            Text(
                                text = currentDate,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Black,
                                color = TextDarkColor,
                                lineHeight = 44.sp
                            )
                        }
                        
                        // Weather capsule badge with dynamic emoji matching High Density Spec
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Blue100)
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentEmoji,
                                fontSize = 30.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    
                    // Connected sync flag showing Real-time Firestore monitoring
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(if (syncStatus.contains("🟢")) Color(0xFF2E7D32) else Color(0xFFED6C02))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = syncStatus,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate500,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Elegant accessibility bottom bar supporting standard safe margin drawing
            Surface(
                color = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // TAB 1: HOME
                    BottomNavItem(
                        title = "首頁",
                        icon = Icons.Default.Home,
                        isSelected = currentTab == ParentScreenTab.HOME,
                        onClick = { currentTab = ParentScreenTab.HOME }
                    )

                    // TAB 2: ITINERARY
                    BottomNavItem(
                        title = "行程",
                        icon = Icons.Default.PlayArrow,
                        isSelected = currentTab == ParentScreenTab.ITINERARY,
                        onClick = { currentTab = ParentScreenTab.ITINERARY }
                    )

                    // TAB 3: SETTINGS
                    BottomNavItem(
                        title = "設定",
                        icon = Icons.Default.Settings,
                        isSelected = currentTab == ParentScreenTab.SETTINGS,
                        onClick = { currentTab = ParentScreenTab.SETTINGS }
                    )
                }
            }
        },
        containerColor = HighDensityBg,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Main content body switching on state
            when (currentTab) {
                ParentScreenTab.HOME -> {
                    ParentHomeScreen(
                        trips = trips,
                        syncStatus = syncStatus,
                        currentDate = currentDate,
                        currentEmoji = currentEmoji,
                        onStartItinerary = { currentTab = ParentScreenTab.ITINERARY },
                        onToggleVisited = { viewModel.toggleVisited(it) },
                        onNavigateClick = { openGoogleMaps(context, it) }
                    )
                }
                ParentScreenTab.ITINERARY -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // ==================== Day Tabs (Giant Touch Targets) ====================
                        val daysList = listOf("Day 1", "Day 2", "Day 3", "Day 4")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            daysList.forEach { dayStr ->
                                val isSelected = selectedDay == dayStr
                                val dayChinese = when (dayStr) {
                                    "Day 1" -> "第一天"
                                    "Day 2" -> "第二天"
                                    "Day 3" -> "第三天"
                                    "Day 4" -> "第四天"
                                    else -> dayStr
                                }
                                
                                Surface(
                                    onClick = { viewModel.selectDay(dayStr) },
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) Blue700 else Color.White,
                                    border = if (isSelected) null else BorderStroke(2.dp, Slate300),
                                    shadowElevation = if (isSelected) 4.dp else 0.dp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(68.dp)
                                        .testTag("tab_$dayStr")
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = dayChinese,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White.copy(alpha = 0.8f) else Slate500,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = dayStr,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isSelected) Color.White else TextDarkColor,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        // ==================== Itinerary List ====================
                        if (trips.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "無行程資料\n請到設定頁重置載入！",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp),
                                contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
                            ) {
                                items(
                                    items = trips,
                                    key = { it.id }
                                ) { tripItem ->
                                    TravelCard(
                                        trip = tripItem,
                                        onToggleVisited = { viewModel.toggleVisited(tripItem) },
                                        onSaveNote = { noteText -> viewModel.saveNote(tripItem, noteText) },
                                        onQuickFeedback = { feedback -> viewModel.saveQuickFeedback(tripItem, feedback) },
                                        onNavigateClick = { location -> openGoogleMaps(context, location) }
                                    )
                                }
                            }
                        }
                    }
                }
                ParentScreenTab.SETTINGS -> {
                    ParentSettingsScreen(
                        viewModel = viewModel,
                        syncStatus = syncStatus
                    )
                }
            }
        }
    }
}

/**
 * Bottom Navigation Item custom view to follow High Density template precision
 */
@Composable
fun BottomNavItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // High density active capsule back box
        Box(
            modifier = Modifier
                .width(64.dp)
                .height(34.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSelected) Blue100 else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) Blue700 else TextDarkColor.copy(alpha = 0.6f),
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
            color = if (isSelected) Blue700 else TextDarkColor.copy(alpha = 0.6f)
        )
    }
}

/**
 * Super Premium Dashboard view for "首頁" (Home) Tab
 */
@Composable
fun ParentHomeScreen(
    trips: List<TravelItem>,
    syncStatus: String,
    currentDate: String,
    currentEmoji: String,
    onStartItinerary: () -> Unit,
    onToggleVisited: (TravelItem) -> Unit,
    onNavigateClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val visitedCount = trips.count { it.isVisited }
    val totalCount = trips.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            // Big Welcome Jumbotron Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(2.dp, BorderSlate100)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "親愛的爸爸媽媽 💖",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Blue700,
                        lineHeight = 38.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "歡迎來到澎湖！天藍海清，今天最適合慢走散步、放鬆心情。我們為您量身打造了防抖、不累人的舒壓行程，只要點擊綠色大按鈕就能一鍵導航喔！",
                        fontSize = 21.sp,
                        color = TextDarkColor.copy(alpha = 0.8f),
                        lineHeight = 30.sp
                    )
                }
            }
        }

        item {
            // Visual Progress Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Blue100.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(2.dp, Blue100)
            ) {
                Row(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "今日踩點進度",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Blue700
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (totalCount == 0) "行程加載中" else "今日已走完 $visitedCount / $totalCount 站",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = TextDarkColor
                        )
                    }
                    Text(
                        text = if (totalCount > 0 && visitedCount == totalCount) "🎉 圓滿完成" else "🏖️",
                        fontSize = 38.sp
                    )
                }
            }
        }

        // Fast Entry Action Button
        item {
            Button(
                onClick = onStartItinerary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue700),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "查看今日詳細行程 路線 🗺️",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Quick Preview of Next Unvisited Stop (Dynamic cue)
        val firstUnvisitedItem = trips.firstOrNull { !it.isVisited }
        if (firstUnvisitedItem != null) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "🏃 貼心提醒：下一部前進目標",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate500,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(32.dp),
                        border = BorderStroke(2.dp, BorderSlate100)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            // Time Pill
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Amber100,
                                modifier = Modifier.padding(bottom = 12.dp)
                            ) {
                                Text(
                                    text = firstUnvisitedItem.time,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Amber900,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }

                            Text(
                                text = firstUnvisitedItem.title,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDarkColor,
                                lineHeight = 34.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = firstUnvisitedItem.desc,
                                fontSize = 20.sp,
                                color = TextDarkColor.copy(alpha = 0.8f),
                                lineHeight = 26.sp
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            // Big Navigation Shortcut
                            Button(
                                onClick = { onNavigateClick(firstUnvisitedItem.location) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Green800),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp),
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "一鍵導航去下一站",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Super Premium settings screen for Settings (設定) Tab
 */
@Composable
fun ParentSettingsScreen(
    viewModel: TravelViewModel,
    syncStatus: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            // Call Family Assistance Card (Giant red touch target)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(2.dp, BorderSlate100)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "家人緊急協助 ☎️",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = CoralAccent
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "迷路、找不到方向或需要詢問事情時，請直接點選下方紅色大按鈕，直接撥通電話給家人，免去在通訊錄尋找孩子的麻煩！",
                        fontSize = 20.sp,
                        color = TextDarkColor.copy(alpha = 0.8f),
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0912345678"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(76.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CoralAccent),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "打電話給兒子/女兒",
                                modifier = Modifier.size(32.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "點我 📞 聯絡兒子/女兒",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        item {
            // Reset Database Itinerary block
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(2.dp, BorderSlate100)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "行程功能設定 ⚙️",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Blue700
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "如果不小心按錯了踩點進度，或想把行程恢復成最早最完美的安排：",
                        fontSize = 20.sp,
                        color = TextDarkColor.copy(alpha = 0.8f),
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.resetItinerary()
                            Toast.makeText(context, "行程已成功重設 🔄", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .testTag("reset_itinerary_settings_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Blue700),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "恢復預設行程",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        item {
            // Live Status details
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Blue100.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "🛡️ 雲端服務狀態：",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue700
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Firestore 即時同步已啟動，您的打卡、心情勾選以及語音筆記皆會即時推送到孩子的系統，全家即時掌握好放心！",
                        fontSize = 18.sp,
                        color = TextDarkColor.copy(alpha = 0.7f),
                        lineHeight = 26.sp
                    )
                }
            }
        }
    }
}

/**
 * 每一站行程卡片 - High Density Theme Refactoring
 * Perfect Material 3 high density style with rounded-[40px] (32.dp boundary) shapes.
 */
@Composable
fun TravelCard(
    trip: TravelItem,
    onToggleVisited: () -> Unit,
    onSaveNote: (String) -> Unit,
    onQuickFeedback: (String) -> Unit,
    onNavigateClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var noteInput by remember(trip.id) { mutableStateOf(trip.note ?: "") }
    var isNoteExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("travel_card_${trip.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (trip.isVisited) VisitedBg else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(32.dp), // High Density Rounded Shape Spec
        border = BorderStroke(
            width = if (trip.isVisited) 3.dp else 2.dp,
            color = if (trip.isVisited) Green800 else BorderSlate100
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            // === Top header: Time indicator & quick tick ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Time indication tag block in Amber Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Amber100,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = trip.time,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Amber900
                        )
                    }
                }

                // Checked status selector (visited checklist button)
                Surface(
                    onClick = onToggleVisited,
                    shape = RoundedCornerShape(14.dp),
                    color = if (trip.isVisited) Green800 else Color.White,
                    border = BorderStroke(2.dp, if (trip.isVisited) Green800 else Blue700),
                    modifier = Modifier
                        .height(52.dp)
                        .clickable { onToggleVisited() }
                        .testTag("visited_checkbox_${trip.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (trip.isVisited) "已走完 ✓" else "走完打勾",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (trip.isVisited) Color.White else Blue700
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // === Trip Title ===
            Text(
                text = trip.title,
                fontSize = 32.sp, // Big text
                fontWeight = FontWeight.Black,
                color = TextDarkColor,
                lineHeight = 38.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // === Description text ===
            Text(
                text = trip.desc,
                fontSize = 21.sp, // Accessibility-First
                fontWeight = FontWeight.Medium,
                color = TextDarkColor.copy(alpha = 0.8f),
                lineHeight = 30.sp,
                modifier = Modifier.fillMaxWidth()
            )

            // === Custom User state note/feedback section ===
            if (trip.quickFeedback != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = CoralAccent,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = "目前感想：${trip.quickFeedback}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            if (!trip.note.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(14.dp)
                ) {
                    Text(
                        text = "✍️ 爸媽隨手筆記：",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Amber900
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = trip.note,
                        fontSize = 21.sp,
                        color = TextDarkColor,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 2.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // === Dynamic interactive response options ===
            Text(
                text = "告訴孩子目前感覺：",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Slate500
            )
            Spacer(modifier = Modifier.height(8.dp))

            val feedbackOptions = listOf("👍 讚啦！", "💨 風好大", "😋 很好吃", "📸 景超美")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                feedbackOptions.forEach { option ->
                    val isActive = trip.quickFeedback == option
                    Button(
                        onClick = { onQuickFeedback(option) },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp) // Touch standard 48dp+
                            .testTag("feedback_${trip.id}_${option}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isActive) CoralAccent else Color(0xFFF1F5F9),
                            contentColor = if (isActive) Color.White else Blue700
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        Text(
                            text = option,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // === Expandable text memo ===
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isNoteExpanded = !isNoteExpanded }
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isNoteExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.Edit,
                    contentDescription = null,
                    tint = Blue700,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isNoteExpanded) "隱藏筆記輸入盒" else "按我新增/修改備忘筆記 ✍️",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue700
                )
            }

            if (isNoteExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = noteInput,
                        onValueChange = { noteInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_input_${trip.id}"),
                        textStyle = LocalTextStyle.current.copy(fontSize = 20.sp, color = TextDarkColor),
                        placeholder = { Text("在此寫上提醒或是對這站的想法...", fontSize = 18.sp) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            onSaveNote(noteInput)
                            focusManager.clearFocus()
                            isNoteExpanded = false
                        }),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            onSaveNote(noteInput)
                            focusManager.clearFocus()
                            isNoteExpanded = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .testTag("save_note_button_${trip.id}"),
                        colors = ButtonDefaults.buttonColors(containerColor = Blue700),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("儲存筆記", fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // === Location Marker Segment ===
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(BorderStroke(1.dp, BorderSlate100), RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFE2E8F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📍", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = trip.location,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkColor.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // === 3. Huge Navigation Button (Spec: Height 84.dp, rounded-[28px]) ===
            Button(
                onClick = { onNavigateClick(trip.location) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
                    .testTag("navigate_button_${trip.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green800,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🧭", fontSize = 26.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "帶我導航去這裡",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

/**
 * Android Intent 一鍵開啟 Google Map 導航 (Fallback with browser search)
 */
fun openGoogleMaps(context: Context, location: String) {
    try {
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location))
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            val webIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(location))
            val webIntent = Intent(Intent.ACTION_VIEW, webIntentUri)
            context.startActivity(webIntent)
        }
    } catch (e: Exception) {
        Toast.makeText(context, "無法打開地圖，請確認是否有安裝瀏覽器或地圖", Toast.LENGTH_LONG).show()
    }
}
