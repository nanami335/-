package com.example

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 澎湖舒壓旅行行程項目 (TravelItem)
 * 完美支援 Room 離線儲存與 Firestore 規格項目。
 */
@Entity(tableName = "trips")
data class TravelItem(
    @PrimaryKey
    val id: String,                  // 行程唯一的 ID (例如: "trip_101")
    val day: String,                 // 第幾天，例如: "Day 1", "Day 2"
    val date: String,                // 日期，例如: "5/27 週三"
    val time: String,                // 時間區間，例如: "10:40 - 11:40"
    val title: String,               // 大字體標題，例如: "搭乘飛機前往澎湖"
    val desc: String,                // 詳細貼心提示，例如: "民宿人員接機，請記得拿行李喔！"
    val location: String,            // 一鍵喚起 Google 地圖導航的地址或座標
    val order: Int,                  // 全域排序權重，例如: 101, 102
    
    // 以下為互動擴充功能，對應 feedback 集合，存放爸媽即時筆記與打卡狀態
    val isVisited: Boolean = false,  // 爸媽是否已踩點
    val note: String? = null,        // 爸媽即時筆記或隨筆感想
    val quickFeedback: String? = null // 爸媽超大按鈕一鍵打卡狀態 (如: "太漂亮了! 🎉", "風超大! 💨", "好好吃! 😋")
)

/**
 * 數據訪問接口 (DAO)
 */
@Dao
interface TravelDao {
    @Query("SELECT * FROM trips ORDER BY [order] ASC")
    fun getAllTripsFlow(): Flow<List<TravelItem>>

    @Query("SELECT * FROM trips WHERE day = :dayStr ORDER BY [order] ASC")
    fun getTripsByDayFlow(dayStr: String): Flow<List<TravelItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(trips: List<TravelItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TravelItem)

    @Update
    suspend fun updateTrip(trip: TravelItem)

    @Query("DELETE FROM trips")
    suspend fun deleteAllTrips()
}
