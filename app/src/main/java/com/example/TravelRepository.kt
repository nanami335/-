package com.example

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class TravelRepository(private val travelDao: TravelDao) {

    // 取得所有的行程項目的 Flow，任何資料庫內異動都會即時被 Room 反映與推送到 UI (與 addSnapshotListener 效果相同)
    val allTrips: Flow<List<TravelItem>> = travelDao.getAllTripsFlow()

    fun getTripsByDay(day: String): Flow<List<TravelItem>> {
        return travelDao.getTripsByDayFlow(day)
    }

    suspend fun insertTrip(trip: TravelItem) {
        travelDao.insertTrip(trip)
    }

    suspend fun updateTrip(trip: TravelItem) {
        travelDao.updateTrip(trip)
    }

    suspend fun deleteAll() {
        travelDao.deleteAllTrips()
    }

    /**
     * 檢查資料庫是否為空，如果是空的話，將預先準備好的 4 天 3 夜「澎湖舒壓旅行」行程自動匯入！
     */
    suspend fun seedDatabaseIfEmpty() {
        val currentTrips = allTrips.first()
        if (currentTrips.isEmpty()) {
            val initialTrips = listOf(
                // ==================== DAY 1 ====================
                TravelItem(
                    id = "trip_101",
                    day = "Day 1",
                    date = "5/27 週三",
                    time = "10:30 - 11:30",
                    title = "搭乘飛機：松山 ✈️ 馬公",
                    desc = "抵達機場後，民宿專車會在大廳接待。請記得拿取託運行李，並隨身攜帶外套避免風大喔！",
                    location = "澎湖機場",
                    order = 101
                ),
                TravelItem(
                    id = "trip_102",
                    day = "Day 1",
                    date = "5/27 週三",
                    time = "12:00 - 13:30",
                    title = "午餐：鮮甜澎湖海鮮麵線",
                    desc = "老字號清淡海鮮麵線，湯頭鮮甜好消化，食材保證新鮮，先讓爸媽暖暖胃、補充體力。",
                    location = "澎湖馬公市海鮮麵線",
                    order = 102
                ),
                TravelItem(
                    id = "trip_103",
                    day = "Day 1",
                    date = "5/27 週三",
                    time = "14:30 - 16:30",
                    title = "慢步：篤行十村文化園區",
                    desc = "台灣最古老的眷村，平坦好走。可以參觀張雨生故事館，聽聽經典老歌回味。有很多拍照長椅。",
                    location = "篤行十村文化園區官方停車場",
                    order = 103
                ),
                TravelItem(
                    id = "trip_104",
                    day = "Day 1",
                    date = "5/27 週三",
                    time = "17:30 - 19:30",
                    title = "晚餐與休息：觀音亭欣賞夕陽",
                    desc = "慢走吹吹微風，觀看美麗無比的虹橋日落。接著前往附近享用少油、利於胃口消化的養生海鮮鍋。",
                    location = "馬公市觀音亭",
                    order = 104
                ),

                // ==================== DAY 2 ====================
                TravelItem(
                    id = "trip_201",
                    day = "Day 2",
                    date = "5/28 週四",
                    time = "09:00 - 10:30",
                    title = "地標拍照：澎湖跨海大橋",
                    desc = "澎湖代表性的大地標！我們在橋頭的大石碑拍照留念，風大請戴上帽子遮風。可以試試著名的易家仙人掌冰，酸甜冰涼消暑氣。",
                    location = "澎湖跨海大橋 (白沙端)",
                    order = 201
                ),
                TravelItem(
                    id = "trip_202",
                    day = "Day 2",
                    date = "5/28 週四",
                    time = "11:00 - 12:30",
                    title = "懷舊：二崁古厝聚落",
                    desc = "閩南古厝古色古香，道路寬敞且皆為平坦通道。來一碗純熟杏仁茶與手工豆花，體驗濃濃古早味，放鬆身心。",
                    location = "二崁聚落保存區",
                    order = 202
                ),
                TravelItem(
                    id = "trip_203",
                    day = "Day 2",
                    date = "5/28 週四",
                    time = "13:00 - 14:30",
                    title = "精緻午餐：清心飲食店",
                    desc = "蔣經國總統多次造訪的老牌鮮味店。特別推薦鮮嫩紅蟳粥、軟糯炸生蠔，口感適合長輩，好嚼健康。",
                    location = "清心飲食店",
                    order = 203
                ),
                TravelItem(
                    id = "trip_204",
                    day = "Day 2",
                    date = "5/28 週四",
                    time = "15:00 - 16:30",
                    title = "奇觀：大菓葉柱狀玄武岩",
                    desc = "就在路邊的巨石群，不用爬山，下車只要走1分鐘即可抵達。陽光灑落時與清幽水池交織，拍照最神氣、最壯觀！",
                    location = "池東大菓葉柱狀玄武岩",
                    order = 204
                ),

                // ==================== DAY 3 ====================
                TravelItem(
                    id = "trip_301",
                    day = "Day 3",
                    date = "5/29 週五",
                    time = "09:00 - 11:30",
                    title = "出海：虎井嶼平緩島嶼巡禮",
                    desc = "搭乘大艘平穩的接駁半潛艇前往遠離塵囂的虎井嶼（超有名的貓咪之島）。島上能搭乘專用導覽小巴，坐著享受海天一色。",
                    location = "馬公南海遊客中心",
                    order = 301
                ),
                TravelItem(
                    id = "trip_302",
                    day = "Day 3",
                    date = "5/29 週五",
                    time = "12:00 - 13:30",
                    title = "漁夫午餐：現燉虎井鮮魚湯",
                    desc = "品味最純淨的海口極品！漁民當天新鮮捕撈的超鮮美魚湯，湯頭不加化學佐料，高含優質與軟滑豐富營養。",
                    location = "虎井嶼來福鮮魚湯",
                    order = 302
                ),
                TravelItem(
                    id = "trip_303",
                    day = "Day 3",
                    date = "5/29 週五",
                    time = "15:30 - 17:30",
                    title = "沙灘午茶：林投公園與沙灘",
                    desc = "全澎湖遮蔭率最高的公園。成片的蒼翠木麻黃綠樹，走在平坦平緩的木棧道。可坐在林投海景咖啡廳，悠哉點杯果汁吹風發呆。",
                    location = "林投公園咖啡館",
                    order = 303
                ),

                // ==================== DAY 4 ====================
                TravelItem(
                    id = "trip_401",
                    day = "Day 4",
                    date = "5/30 週六",
                    time = "09:30 - 11:00",
                    title = "朝聖：澎湖開台天后宮",
                    desc = "全台灣歷史最悠久的媽祖總廟，莊嚴幽靜。與老爸媽一同入廟頂禮參拜，祈求全家無災、平安順心。台階平實，進門請慢行扶好。",
                    location = "澎湖天后宮",
                    order = 401
                ),
                TravelItem(
                    id = "trip_402",
                    day = "Day 4",
                    date = "5/30 週六",
                    time = "11:15 - 12:30",
                    title = "歷史古趣：中央老街與四眼井",
                    desc = "古樸街道，但完全鋪設平滑大石。瞧瞧四個相連的神秘古井、品嚐香氣十足的藥膳蛋，聽聽幾百年的老街坊古老趣味事。",
                    location = "中央老街四眼井",
                    order = 402
                ),
                TravelItem(
                    id = "trip_403",
                    day = "Day 4",
                    date = "5/30 週六",
                    time = "13:00 - 14:30",
                    title = "豪華謝幕午宴：蒸鮮私房菜",
                    desc = "挑選馬公港口老牌海鮮名店，精心烹製清蒸鮮石斑、海蝦、鮮蚵。美味清甜，軟嫩好咬，回味最美澎湖滋味！",
                    location = "澎湖馬公港口海鮮名店",
                    order = 403
                ),
                TravelItem(
                    id = "trip_404",
                    day = "Day 4",
                    date = "5/30 週六",
                    time = "15:30 - 16:30",
                    title = "特產：採購黑糖糕與熱門伴手禮",
                    desc = "帶阿爸阿媽吃一口剛出爐澎鬆軟濡的黑糖糕！可以採買海苔鹹餅、冬瓜糕。店家可直接郵寄回台北家，爸媽不費一絲力氣、不需拎重物！",
                    location = "澎湖黑糖糕專賣店",
                    order = 404
                ),
                TravelItem(
                    id = "trip_405",
                    day = "Day 4",
                    date = "5/30 週六",
                    time = "18:00 - 19:30",
                    title = "搭機：幸福返回台北家",
                    desc = "專車送機返回馬公機場，託運行李，帶著四天滿滿的澎湖清澈回憶與溫暖陽光回家！辛苦爸媽囉！",
                    location = "澎湖機場航廈",
                    order = 405
                )
            )
            travelDao.insertTrips(initialTrips)
        }
    }
}
