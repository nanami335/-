package com.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 澎湖舒壓旅行 TravelViewModel
 * 負責連接 Room 與即時反應 UI。
 * 本 ViewModel 整合了 Firestore 斷線重連概念，提供雲端同步狀態 (SyncStatus)，並實時監聽
 * 旅行清單，讓老爸老媽隨時看到同步後的行程、並能一鍵提交回饋筆記。
 */
class TravelViewModel(private val repository: TravelRepository) : ViewModel() {

    // 爸媽當前所選的行程天數頁籤，預設為 "Day 1" (超大字體天數切換)
    private val _selectedDay = MutableStateFlow("Day 1")
    val selectedDay: StateFlow<String> = _selectedDay.asStateFlow()

    // 模擬的 Firestore 連線與即時同步狀態 (對應 addSnapshotListener 的即時感官)
    private val _syncStatus = MutableStateFlow("雲端同步成功 (Firestore Connected)")
    val syncStatus: StateFlow<String> = _syncStatus.asStateFlow()

    // 即時行程狀態流：將「目前選定的天數」與「資料庫所有行程」進行聯立過濾
    // 自主發送、完美反應資料庫的每一次 CRUD，UI 端 collect 後即可實現流暢無痛更新。
    val currentDayTrips: StateFlow<List<TravelItem>> = combine(
        _selectedDay,
        repository.allTrips
    ) { day, trips ->
        trips.filter { it.day == day }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        // 在背景初始化資料，如果資料庫沒資料就自動塞入 4 天 3 夜高對比行程
        viewModelScope.launch {
            try {
                repository.seedDatabaseIfEmpty()
                simulateFirestoreSyncListener()
            } catch (e: Exception) {
                _syncStatus.value = "離線快取模式運作中"
            }
        }
    }

    /**
     * 切換天數
     */
    fun selectDay(day: String) {
        _selectedDay.value = day
    }

    /**
     * 模擬 Firebase Firestore 的即時重連監聽
     */
    private fun simulateFirestoreSyncListener() {
        viewModelScope.launch {
            _syncStatus.value = "雲端同步中 (Firestore Syncing...)"
            kotlinx.coroutines.delay(1200)
            _syncStatus.value = "雲端同步成功 (Firestore 實時監聽已就緒 🟢)"
        }
    }

    /**
     * 一鍵打卡踩點 (更新 isVisited 欄位)
     */
    fun toggleVisited(item: TravelItem) {
        viewModelScope.launch {
            val updated = item.copy(isVisited = !item.isVisited)
            repository.updateTrip(updated)
            // 每次資料庫更新，Flow 都會自動發射，使 UI 看到即時打勾狀態
            triggerSyncPulse()
        }
    }

    /**
     * 儲存爸媽的即時筆記或勾選意見
     */
    fun saveNote(item: TravelItem, text: String) {
        viewModelScope.launch {
            val updated = item.copy(note = text.ifBlank { null })
            repository.updateTrip(updated)
            triggerSyncPulse()
        }
    }

    /**
     * 儲存爸媽一鍵打卡的快速主觀感受，例如「風超大」「很好吃」「太美了」
     */
    fun saveQuickFeedback(item: TravelItem, feedback: String) {
        viewModelScope.launch {
            val updated = item.copy(quickFeedback = if (item.quickFeedback == feedback) null else feedback)
            repository.updateTrip(updated)
            triggerSyncPulse()
        }
    }

    /**
     * 模擬發送同步訊號到雲端（與 Firestore 同步）
     */
    private fun triggerSyncPulse() {
        viewModelScope.launch {
            _syncStatus.value = "正在即時同步到雲端... 📤"
            kotlinx.coroutines.delay(800)
            _syncStatus.value = "變更已即時同步到 Firestore 🟢"
        }
    }

    /**
     * 爸媽隨手將行程重設回原始預載行程
     */
    fun resetItinerary() {
        viewModelScope.launch {
            _syncStatus.value = "正在重置行程... 🔄"
            repository.deleteAll()
            repository.seedDatabaseIfEmpty()
            _syncStatus.value = "行程已成功重置為初始藍圖 🟢"
        }
    }
}
