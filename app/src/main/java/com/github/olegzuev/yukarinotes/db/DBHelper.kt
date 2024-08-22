package com.github.olegzuev.yukarinotes.db

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import com.github.olegzuev.yukarinotes.utils.FileUtils
import com.github.olegzuev.yukarinotes.common.Statics
import com.github.olegzuev.yukarinotes.user.UserSettings
import com.github.olegzuev.yukarinotes.utils.LogUtils
import com.github.olegzuev.yukarinotes.utils.Utils
import java.util.*
import kotlin.collections.ArrayList

class DBHelper private constructor(
    val application: Application
) : SQLiteOpenHelper(application, Statics.DB_FILE_NAME, null, DB_VERSION) {

    companion object {
        const val DB_VERSION = 1

        @Volatile
        private lateinit var instance: DBHelper

        fun with(application: Application): DBHelper {
            synchronized (DBHelper::class.java) {
                instance = DBHelper(application)
            }
            return instance
        }

        @JvmStatic
        fun get(): DBHelper = instance
    }

    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        dropAll(db)
        onCreate(db)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> cursor2List(
        cursor: Cursor,
        theClass: Class<*>
    ): List<T>? {
        val result: MutableList<T> = mutableListOf()
        val arrField = theClass.declaredFields
        val localIndexCache = mutableMapOf<String, Int>()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = theClass.getDeclaredConstructor().newInstance()
                for (f in arrField) {
                    val columnName = f.name
                    val columnIdx = localIndexCache.getOrPut(columnName) { cursor.getColumnIndex(columnName) }
                    if (columnIdx != -1) {
                        if (!f.isAccessible) {
                            f.isAccessible = true
                        }
                        val type = f.type
                        when (type) {
                            Byte::class.javaPrimitiveType -> {
                                f[bean] = cursor.getShort(columnIdx).toByte()
                            }
                            Short::class.javaPrimitiveType -> {
                                f[bean] = cursor.getShort(columnIdx)
                            }
                            Int::class.javaPrimitiveType -> {
                                f[bean] = cursor.getInt(columnIdx)
                            }
                            Long::class.javaPrimitiveType -> {
                                f[bean] = cursor.getLong(columnIdx)
                            }
                            String::class.java -> {
                                f[bean] = cursor.getString(columnIdx)
                            }
                            ByteArray::class.java -> {
                                f[bean] = cursor.getBlob(columnIdx)
                            }
                            Boolean::class.javaPrimitiveType -> {
                                f[bean] = cursor.getInt(columnIdx) == 1
                            }
                            Float::class.javaPrimitiveType -> {
                                f[bean] = cursor.getFloat(columnIdx)
                            }
                            Double::class.javaPrimitiveType -> {
                                f[bean] = cursor.getDouble(columnIdx)
                            }
                        }
                    }
                }
                result.add(bean as T)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    /***
     * 准备游标
     * @param tableName 表名
     * @param key WHERE [key] IN ([keyValue])
     * @param keyValue WHERE [key] IN ([keyValue])
     * @return 存有数据的游标
     */
    @SuppressLint("Recycle")
    private fun prepareCursor(
        tableName: String,
        key: String?,
        keyValue: List<String>?
    ): Cursor? {
        if (!FileUtils.checkFile(FileUtils.getDbFilePath())) return null
        val db = readableDatabase ?: return null
        return if (key == null || keyValue == null || keyValue.isEmpty()) {
            db.rawQuery("SELECT * FROM $tableName ", null)
        } else {
            val paraBuilder = StringBuilder()
            paraBuilder.append("(")
            for (s in keyValue) {
                if (!TextUtils.isEmpty(s)) {
                    paraBuilder.append("?")
                    paraBuilder.append(", ")
                }
            }
            paraBuilder.delete(paraBuilder.length - 2, paraBuilder.length)
            paraBuilder.append(")")
            db.rawQuery(
                "SELECT * FROM $tableName WHERE $key IN $paraBuilder ",
                keyValue.toTypedArray()
            )
        }
    }
    /******************* Method For Use  */
    /***
     * 由表名和类名无条件从数据库获取实体列表
     * @param tableName 表名
     * @param theClass 类名
     * @param <T> theClass的类
     * @return 生成的实体列表
    </T> */
    private fun <T> getBeanList(
        tableName: String,
        theClass: Class<*>
    ): List<T>? {
        val cursor = prepareCursor(tableName, null, null) ?: return null
        return cursor2List(cursor, theClass)
    }

    /***
     * 由表名、类名、条件键值从数据库获取实体列表
     * @param tableName 表名
     * @param theClass 类名
     * @param key WHERE [key] IN ([keyValue])
     * @param keyValues WHERE [key] IN ([keyValue])
     * @param <T> theClass的类
     * @return 生成的实体列表
    </T> */
    private fun <T> getBeanList(
        tableName: String,
        theClass: Class<*>,
        key: String?,
        keyValues: List<String>?
    ): List<T>? {
        val cursor = prepareCursor(tableName, key, keyValues) ?: return null
        return cursor2List(cursor, theClass)
    }

    /***
     * 由表名、类名、条件键值从数据库获取单个实体
     * @param tableName 表名
     * @param theClass 类名
     * @param key WHERE [key] IN ([keyValue])
     * @param keyValue WHERE [key] IN ([keyValue])
     * @param <T> theClass的类
     * @return 生成的实体
    </T> */
    private fun <T> getBean(
        tableName: String,
        theClass: Class<*>,
        key: String?,
        keyValue: String
    ): T? {
        val cursor =
            prepareCursor(tableName, key, listOf(keyValue)) ?: return null
        val data: List<T>? = cursor2List(cursor, theClass)
        return if (data?.isNotEmpty() == true) data[0] else null
    }

//    /***
//     * 由SQL语句、SQL中的键值从数据库获取单个实体
//     * @param sql SQL语句
//     * @param keyValue IN (?) => ?=keyValue
//     * @param theClass 类名
//     * @param <T> theClass的类
//     * @return 生成的实体
//    </T> */
//    @SuppressLint("Recycle")
//    private fun <T> getBeanByRaw(
//        sql: String?,
//        keyValue: String,
//        theClass: Class<*>
//    ): T? {
//        if (!Utils.checkFile(Statics.DB_PATH + Statics.DB_FILE)) return null
//        val cursor =
//            readableDatabase.rawQuery(sql, arrayOf(keyValue)) ?: return null
//        val data: List<T>? = cursor2List(cursor, theClass)
//        return if (data?.isNotEmpty() == true) data[0] else null
//    }

    /***
     * 由SQL语句、SQL中的键值从数据库获取单个实体
     * @param sql SQL语句
     * @param keyValue IN (?) => ?=keyValue
     * @param theClass 类名
     * @param <T> theClass的类
     * @return 生成的实体
    </T> */
    @SuppressLint("Recycle")
    private fun <T> getBeanByRaw(
        sql: String?,
        theClass: Class<*>
    ): T? {
        if (!FileUtils.checkFile(FileUtils.getDbFilePath())) return null
        try {
            val cursor =
                readableDatabase.rawQuery(sql, null) ?: return null
            val data: List<T>? = cursor2List(cursor, theClass)
            return if (data?.isNotEmpty() == true) data[0] else null
        } catch (e: Exception) {
            LogUtils.file(LogUtils.E, "getBeanByRaw", e.message, e.stackTrace)
            return null
        }
    }

//    /***
//     * 由SQL语句无条件从数据库获取实体列表
//     * @param sql SQL语句
//     * @param theClass 类名
//     * @param <T> theClass的类
//     * @return 生成的实体列表
//    </T> */
//    @SuppressLint("Recycle")
//    private fun <T> getBeanListByRaw(
//        sql: String?,
//        theClass: Class<*>
//    ): List<T>? {
//        if (!Utils.checkFile(Statics.DB_PATH + Statics.DB_FILE)) return null
//        val cursor = readableDatabase.rawQuery(sql, null) ?: return null
//        return cursor2List(cursor, theClass)
//    }

    /***
     * 由SQL语句无条件从数据库获取实体列表
     * @param sql SQL语句
     * @param keyValue 替换？的值
     * @param theClass 类名
     * @param <T> theClass的类
     * @return 生成的实体列表
    </T> */
    @SuppressLint("Recycle")
    private fun <T> getBeanListByRaw(
        sql: String?,
        theClass: Class<*>
    ): List<T>? {
        if (!FileUtils.checkFile(FileUtils.getDbFilePath())) return null
        try {
            val cursor =
                readableDatabase.rawQuery(sql, null) ?: return null
            return cursor2List(cursor, theClass)
        } catch (e: Exception) {
            LogUtils.file(LogUtils.E, "getBeanListByRaw", e.message, e.stackTrace)
            return null
        }
    }

    /***
     * 删除所有表
     * @param db
     */
    private fun dropAll(db: SQLiteDatabase) {
        val sqls: MutableList<String> =
            ArrayList()
        val op = "DROP TABLE IF EXISTS "
        for (field in this.javaClass.declaredFields) {
            if (field.name.startsWith("TABLE_NAME")) {
                try {
                    sqls.add(op + field[this])
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
        for (sql in sqls) {
            db.execSQL(sql)
        }
    }

    /***
     * 删除表
     * @param tableName
     * @return
     */
    fun dropTable(tableName: String): Boolean {
        return try {
            writableDatabase.execSQL("DROP TABLE IF EXISTS $tableName")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /***
     * 获取查询语句的第一行第一列值
     * @param sql
     * @return
     */
    private fun getOne(sql: String?): String? {
        if (!FileUtils.checkFile(FileUtils.getDbFilePath())) return null
        val cursor = readableDatabase.rawQuery(sql, null)
        cursor.moveToNext()
        val result = cursor.getString(0)
        cursor.close()
        return result
    }

    /***
     * 获取 int-string map
     * @param sql
     * @return
     */
    @SuppressLint("Range")
    private fun getIntStringMap(
        sql: String,
        key: String,
        value: String
    ): Map<Int, String>? {
        if (!FileUtils.checkFile(FileUtils.getDbFilePath())) return null
        val cursor = readableDatabase.rawQuery(sql, null)
        val result: MutableMap<Int, String> = HashMap()
        val localIndexCache = mutableMapOf<String, Int>()
        while (cursor.moveToNext()) {
            val keyColumnIndex = localIndexCache.getOrPut(key) { cursor.getColumnIndex(key) }
            val valueColumnIndex = localIndexCache.getOrPut(value) { cursor.getColumnIndex(value) }
            result[cursor.getInt(keyColumnIndex)] = cursor.getString(valueColumnIndex)
        }
        cursor.close()
        return result
    }


    /************************* public field **************************/

    /***
     * 获取角色基础数据
     */
    fun getCharaBase(): List<RawUnitBasic>? {
        return if (getConversionCount == "0") {
//            getBeanListByRaw(
            DBOptimizer.getBeanListByRaw(
                """
                SELECT ud.unit_id
                ,ud.unit_name
                ,ud.kana
                ,ud.prefab_id
                ,ud.move_speed
                ,ud.search_area_width
                ,ud.atk_type
                ,ud.normal_atk_cast_time
                ,ud.guild_id
                ,ud.comment
                ,ud.start_time
                ,up.age
                ,up.guild
                ,up.race
                ,up.height
                ,up.weight
                ,up.birth_month
                ,up.birth_day
                ,up.blood_type
                ,up.favorite
                ,up.voice
                ,up.catch_copy
                ,up.self_text
                ,IFNULL(au.unit_name, ud.unit_name) 'actual_name' 
                FROM unit_data AS ud 
                JOIN unit_profile AS up ON ud.unit_id = up.unit_id 
                LEFT JOIN actual_unit_background AS au ON substr(ud.unit_id,1,4) = substr(au.unit_id,1,4) 
                WHERE ud.comment <> '' 
                AND ud.unit_id < 400000 
                """,
                RawUnitBasic::class.simpleName
            )
        } else {
            DBOptimizer.getBeanListByRaw(
                """
                SELECT ud.unit_id
                ,ud.unit_name
                ,ud.kana
                ,ud.prefab_id
                ,ud.move_speed
                ,ud.search_area_width
                ,ud.atk_type
                ,ud.normal_atk_cast_time
                ,ud.guild_id
                ,ud.comment
                ,ud.start_time
                ,up.age
                ,up.guild
                ,up.race
                ,up.height
                ,up.weight
                ,up.birth_month
                ,up.birth_day
                ,up.blood_type
                ,up.favorite
                ,up.voice
                ,up.catch_copy
                ,up.self_text
                ,IFNULL(au.unit_name, ud.unit_name) 'actual_name' 
                ,uc.unit_id 'unit_conversion_id' 
                FROM unit_data AS ud 
                JOIN unit_profile AS up ON ud.unit_id = up.unit_id 
                LEFT JOIN actual_unit_background AS au ON substr(ud.unit_id,1,4) = substr(au.unit_id,1,4) 
                LEFT JOIN unit_conversion AS uc ON ud.unit_id = uc.original_unit_id 
                WHERE ud.comment <> '' 
                AND ud.unit_id < 400000 
                """,
                RawUnitBasic::class.simpleName
            )
        }
    }

    private val getConversionCount: String by lazy {
        getOne("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='unit_conversion'")!!
    }

    /***
     * 获取角色星级数据
     */
    fun getUnitRarity(unitId: Int): RawUnitRarity? {
        return getBeanByRaw<RawUnitRarity>(
            """
                SELECT * 
                FROM unit_rarity 
                WHERE unit_id=$unitId 
                ORDER BY rarity DESC 
                """,
            RawUnitRarity::class.java
        )
    }

    /***
     * 获取角色星级数据
     */
    fun getUnitRarityList(unitId: Int): List<RawUnitRarity>? {
        return getBeanListByRaw(
            """
                SELECT * 
                FROM unit_rarity 
                WHERE unit_id=$unitId 
                ORDER BY rarity DESC 
                """,
            RawUnitRarity::class.java
        )
    }

    /***
     * Get Map<UnitId, List<RawUnitRarity>> of all units
     */
    fun getUnitRarityMap(): Map<Int, List<RawUnitRarity>> {
        val list = DBOptimizer.getBeanListByRaw<RawUnitRarity>(
            """
                SELECT * 
                FROM unit_rarity 
                ORDER BY unit_id ASC, rarity DESC 
                """,
            RawUnitRarity::class.simpleName
        )
        val map = mutableMapOf<Int, MutableList<RawUnitRarity>>()
        list?.distinctBy { it.unit_id }?.forEach {
            map[it.unit_id] = mutableListOf()
        }
        list?.forEach {
            map[it.unit_id]!!.add(it)
        }
        return map
    }

    /***
     * 获取角色剧情数据
     */
    fun getCharaStoryStatus(charaId: Int): List<RawCharaStoryStatus>? {
        // 国服-> 排除还没有实装的角色剧情
        if (UserSettings.get().getUserServer() == UserSettings.SERVER_CN) {
            return getBeanListByRaw(
                """
                SELECT a.* 
                FROM chara_story_status AS a
                INNER JOIN unit_data AS b ON substr(a.story_id,1,4) = substr(b.unit_id,1,4)
                WHERE a.chara_id_1 = $charaId 
                OR a.chara_id_2 = $charaId 
                OR a.chara_id_3 = $charaId 
                OR a.chara_id_4 = $charaId 
                OR a.chara_id_5 = $charaId 
                OR a.chara_id_6 = $charaId 
                OR a.chara_id_7 = $charaId 
                OR a.chara_id_8 = $charaId 
                OR a.chara_id_9 = $charaId 
                OR a.chara_id_10 = $charaId 
                """,
                RawCharaStoryStatus::class.java
            )
        }
        return getBeanListByRaw(
            """
                SELECT * 
                FROM chara_story_status 
                WHERE chara_id_1 = $charaId 
                OR chara_id_2 = $charaId 
                OR chara_id_3 = $charaId 
                OR chara_id_4 = $charaId 
                OR chara_id_5 = $charaId 
                OR chara_id_6 = $charaId 
                OR chara_id_7 = $charaId 
                OR chara_id_8 = $charaId 
                OR chara_id_9 = $charaId 
                OR chara_id_10 = $charaId 
                """,
            RawCharaStoryStatus::class.java
        )
    }

    /***
     * Get Map<chara_id_1, List<RawCharaStoryStatus>> of all units
     */
    fun getCharaStoryStatusMap(): Map<Int, List<RawCharaStoryStatus>> {
        var sqlQuery = """
                SELECT * 
                FROM chara_story_status 
                """
        if (UserSettings.get().getUserServer() == UserSettings.SERVER_CN) {
            sqlQuery = """
                SELECT a.* 
                FROM chara_story_status AS a
                INNER JOIN unit_data AS b ON substr(a.story_id,1,4) = substr(b.unit_id,1,4)
                """
        }
        val list = DBOptimizer.getBeanListByRaw<RawCharaStoryStatus>(
            sqlQuery,
            RawCharaStoryStatus::class.simpleName
        )
        val map = mutableMapOf<Int, MutableList<RawCharaStoryStatus>>()
        list?.distinctBy { it.chara_id_1 }?.forEach {
            map[it.chara_id_1] = mutableListOf()
        }
        list?.forEach {
            map[it.chara_id_1]!!.add(it)
            if (it.chara_id_2 != 0) { map[it.chara_id_2]!!.add(it)  }
            if (it.chara_id_3 != 0) { map[it.chara_id_3]!!.add(it)  }
            if (it.chara_id_4 != 0) { map[it.chara_id_4]!!.add(it)  }
            if (it.chara_id_5 != 0) { map[it.chara_id_5]!!.add(it)  }
            if (it.chara_id_6 != 0) { map[it.chara_id_6]!!.add(it)  }
            if (it.chara_id_7 != 0) { map[it.chara_id_7]!!.add(it)  }
            if (it.chara_id_8 != 0) { map[it.chara_id_8]!!.add(it)  }
            if (it.chara_id_9 != 0) { map[it.chara_id_9]!!.add(it)  }
            if (it.chara_id_10 != 0) { map[it.chara_id_10]!!.add(it)  }
        }
        return map
    }

    /***
     * 获取角色Rank汇总数据
     * @param unitId 角色id
     * @return
     */
    fun getCharaPromotionStatus(unitId: Int): List<RawPromotionStatus>? {
        return  getBeanListByRaw(
            """
                SELECT * 
                FROM unit_promotion_status 
                WHERE unit_id=$unitId 
                ORDER BY promotion_level DESC 
                """,
            RawPromotionStatus::class.java
        )
    }

    /***
     * Get Map<UnitId, List<RawPromotionStatus>> of all units
     */
    fun getCharaPromotionStatusMap(): Map<Int, List<RawPromotionStatus>> {
//        val list = getBeanListByRaw<RawPromotionStatus>(
        val list = DBOptimizer.getBeanListByRaw<RawPromotionStatus>(
            """
                SELECT * 
                FROM unit_promotion_status 
                ORDER BY promotion_level DESC 
                """,
            RawPromotionStatus::class.simpleName
        )
        val map = mutableMapOf<Int, MutableList<RawPromotionStatus>>()
        list?.distinctBy { it.unit_id }?.forEach {
            map[it.unit_id] = mutableListOf()
        }
        list?.forEach {
            map[it.unit_id]!!.add(it)
        }
        return map
    }

    /***
     * 获取角色Rank Bonus
     * @param unitId 角色id
     * @return
     */
    fun getCharaPromotionBonus(unitId: Int): List<RawPromotionBonus>? {
        // 考虑国服未实装的情况
        val count = getOne("""SELECT COUNT(*) 
                                FROM sqlite_master 
                                WHERE type='table' 
                                AND name='promotion_bonus'""")

        return if (count.equals("1")) {
            getBeanListByRaw(
                """
                    SELECT * 
                    FROM promotion_bonus
                    WHERE unit_id=$unitId 
                    ORDER BY promotion_level DESC 
                    """,
                RawPromotionBonus::class.java
            )
        } else null
    }

    /***
     * Get Map<UnitId, List<RawPromotionBonus>> of all units
     */
    fun getCharaPromotionBonusMap(): Map<Int, List<RawPromotionBonus>> {
        // 考虑国服未实装的情况
        val count = getOne("""SELECT COUNT(*) 
                                FROM sqlite_master 
                                WHERE type='table' 
                                AND name='promotion_bonus'""")
        if (!count.equals("1")) return mutableMapOf()

        val list = DBOptimizer.getBeanListByRaw<RawPromotionBonus>(
            """
                SELECT * 
                FROM promotion_bonus
                ORDER BY promotion_level DESC 
                """,
            RawPromotionBonus::class.simpleName
        )
        val map = mutableMapOf<Int, MutableList<RawPromotionBonus>>()
        list?.distinctBy { it.unit_id }?.forEach {
            map[it.unit_id] = mutableListOf()
        }
        list?.forEach {
            map[it.unit_id]!!.add(it)
        }
        return map
    }

    /***
     * 获取角色装备数据
     * @param unitId 角色id
     * @return
     */
    fun getCharaPromotion(unitId: Int): List<RawUnitPromotion>? {
        return getBeanListByRaw(
            """
                SELECT * 
                FROM unit_promotion 
                WHERE unit_id=$unitId
                ORDER BY promotion_level DESC 
                """,
            RawUnitPromotion::class.java
        )
    }

    /***
     * Get Map<UnitId, List<RawUnitPromotion>> of all units
     */
    fun getCharaPromotionMap(): Map<Int, List<RawUnitPromotion>> {
        val list = DBOptimizer.getBeanListByRaw<RawUnitPromotion>(
            """
                SELECT * 
                FROM unit_promotion 
                ORDER BY promotion_level DESC 
                """,
            RawUnitPromotion::class.simpleName
        )
        val map = mutableMapOf<Int, MutableList<RawUnitPromotion>>()
        list?.distinctBy { it.unit_id }?.forEach {
            map[it.unit_id] = mutableListOf()
        }
        list?.forEach {
            map[it.unit_id]!!.add(it)
        }
        return map
    }

    /***
     * 获取装备数据
     * @param slots 装备ids
     * @return
     */
    fun getEquipments(slots: ArrayList<Int>?): List<RawEquipmentData>? {
        return getBeanListByRaw(
            """
                SELECT 
                a.*
                ,b.max_equipment_enhance_level 
                FROM equipment_data a, 
                ( SELECT promotion_level, max( equipment_enhance_level ) max_equipment_enhance_level FROM equipment_enhance_data GROUP BY promotion_level ) b 
                WHERE a.promotion_level = b.promotion_level 
                AND a.equipment_id IN ( ${Utils.splitIntegerWithComma(slots)} ) 
                """,
            RawEquipmentData::class.java
        )
    }

    /***
     * 获取所有装备数据
     */
    fun getEquipmentAll(): List<RawEquipmentData>? {
        return DBOptimizer.getBeanListByRaw(
            """
                SELECT 
                a.* 
                ,ifnull(b.max_equipment_enhance_level, 0) 'max_equipment_enhance_level'
                ,e.description 'catalog' 
                ,CASE WHEN a.equipment_id > 10000000
                 THEN 63 + substr(a.equipment_id,7,2)
                 ELSE substr(a.equipment_id,3,1) * 10 + substr(a.equipment_id,6,1) END AS rarity 
                ,f.condition_equipment_id_1
                ,f.consume_num_1
                ,f.condition_equipment_id_2
                ,f.consume_num_2
                ,f.condition_equipment_id_3
                ,f.consume_num_3
                ,f.condition_equipment_id_4
                ,f.consume_num_4
                ,f.condition_equipment_id_5
                ,f.consume_num_5
                ,f.condition_equipment_id_6
                ,f.consume_num_6
                ,f.condition_equipment_id_7
                ,f.consume_num_7
                ,f.condition_equipment_id_8
                ,f.consume_num_8
                ,f.condition_equipment_id_9
                ,f.consume_num_9
                ,f.condition_equipment_id_10
                ,f.consume_num_10
                FROM equipment_data a  
                LEFT JOIN ( SELECT promotion_level, max( equipment_enhance_level ) max_equipment_enhance_level FROM equipment_enhance_data GROUP BY promotion_level ) b ON a.promotion_level = b.promotion_level 
                LEFT JOIN equipment_enhance_rate AS e ON a.equipment_id=e.equipment_id
                LEFT JOIN equipment_craft AS f ON a.equipment_id = f.equipment_id
                WHERE a.equipment_id < 113000 OR (a.equipment_id BETWEEN 10000000 AND 11000000)
                ORDER BY rarity DESC, a.require_level DESC, a.equipment_id ASC 
                """,
            RawEquipmentData::class.simpleName
        )
    }

    /***
     * 获取装备强化数据
     * @param slots 装备ids
     * @return
     */
    fun getEquipmentEnhance(slots: ArrayList<Int>?): List<RawEquipmentEnhanceData>? {
        return getBeanListByRaw(
            """
                SELECT * 
                FROM equipment_enhance_rate 
                WHERE equipment_id IN ( ${Utils.splitIntegerWithComma(slots)} ) 
                """,
            RawEquipmentEnhanceData::class.java
        )
    }

    /***
     * 获取装备强化数据
     * @param id 装备ids
     * @return
     */
    fun getEquipmentEnhance(equipmentId: Int): RawEquipmentEnhanceData? {
        return getBeanByRaw(
            """
                SELECT * 
                FROM equipment_enhance_rate 
                WHERE equipment_id = $equipmentId 
                """,
            RawEquipmentEnhanceData::class.java
        )
    }

    /***
     * 获取所有装备强化数据
     */
    fun getEquipmentEnhance(): List<RawEquipmentEnhanceData>? {
        return DBOptimizer.getBeanListByRaw(
            """
                SELECT * 
                FROM equipment_enhance_rate 
                """,
            RawEquipmentEnhanceData::class.simpleName
        )
    }

    /***
     * 获取专属装备数据
     * @param unitId 角色id
     * @return
     */
    fun getUniqueEquipment(unitId: Int): RawUniqueEquipmentData? {
        if (UserSettings.get().getUserServer() == UserSettings.SERVER_CN) {
            return getBeanByRaw<RawUniqueEquipmentData>(
                """
                    SELECT u.unit_id, e.*
                    ,c.item_id_1
                    ,c.consume_num_1
                    ,c.item_id_2
                    ,c.consume_num_2
                    ,c.item_id_3
                    ,c.consume_num_3
                    ,c.item_id_4
                    ,c.consume_num_4
                    ,c.item_id_5
                    ,c.consume_num_5
                    ,c.item_id_6
                    ,c.consume_num_6
                    ,c.item_id_7
                    ,c.consume_num_7
                    ,c.item_id_8
                    ,c.consume_num_8
                    ,c.item_id_9
                    ,c.consume_num_9
                    ,c.item_id_10
                    ,c.consume_num_10
                    FROM unique_equipment_data AS e 
                    JOIN unit_unique_equip AS u ON e.equipment_id=u.equip_id 
                    LEFT JOIN unique_equipment_craft AS c ON e.equipment_id=c.equip_id
                    WHERE u.unit_id=$unitId 
                    """,
                RawUniqueEquipmentData::class.java
            )
        }
        return getBeanByRaw<RawUniqueEquipmentData>(
            """
                SELECT u.unit_id, e.*
                ,c.item_id_1
                ,c.consume_num_1
                ,c.item_id_2
                ,c.consume_num_2
                ,c.item_id_3
                ,c.consume_num_3
                ,c.item_id_4
                ,c.consume_num_4
                ,c.item_id_5
                ,c.consume_num_5
                ,c.item_id_6
                ,c.consume_num_6
                ,c.item_id_7
                ,c.consume_num_7
                ,c.item_id_8
                ,c.consume_num_8
                ,c.item_id_9
                ,c.consume_num_9
                ,c.item_id_10
                ,c.consume_num_10
                FROM unique_equipment_data AS e 
                JOIN unit_unique_equipment AS u ON e.equipment_id=u.equip_id 
                LEFT JOIN unique_equipment_craft AS c ON e.equipment_id=c.equip_id
                WHERE u.unit_id=$unitId 
                """,
            RawUniqueEquipmentData::class.java
        )

    }

    /***
     * Get Map<UnitId, RawUniqueEquipmentData> of all units
     */
    fun getUniqueEquipmentMap(): Map<Int, RawUniqueEquipmentData> {
        var sqlQuery = """
                SELECT u.unit_id, e.*
                ,c.item_id_1
                ,c.consume_num_1
                ,c.item_id_2
                ,c.consume_num_2
                ,c.item_id_3
                ,c.consume_num_3
                ,c.item_id_4
                ,c.consume_num_4
                ,c.item_id_5
                ,c.consume_num_5
                ,c.item_id_6
                ,c.consume_num_6
                ,c.item_id_7
                ,c.consume_num_7
                ,c.item_id_8
                ,c.consume_num_8
                ,c.item_id_9
                ,c.consume_num_9
                ,c.item_id_10
                ,c.consume_num_10
                FROM unique_equipment_data AS e 
                JOIN unit_unique_equipment AS u ON e.equipment_id=u.equip_id 
                LEFT JOIN unique_equipment_craft AS c ON e.equipment_id=c.equip_id
                """
        if (UserSettings.get().getUserServer() == UserSettings.SERVER_CN) {
            sqlQuery = """
                    SELECT u.unit_id, e.*
                    ,c.item_id_1
                    ,c.consume_num_1
                    ,c.item_id_2
                    ,c.consume_num_2
                    ,c.item_id_3
                    ,c.consume_num_3
                    ,c.item_id_4
                    ,c.consume_num_4
                    ,c.item_id_5
                    ,c.consume_num_5
                    ,c.item_id_6
                    ,c.consume_num_6
                    ,c.item_id_7
                    ,c.consume_num_7
                    ,c.item_id_8
                    ,c.consume_num_8
                    ,c.item_id_9
                    ,c.consume_num_9
                    ,c.item_id_10
                    ,c.consume_num_10
                    FROM unique_equipment_data AS e 
                    JOIN unit_unique_equip AS u ON e.equipment_id=u.equip_id 
                    LEFT JOIN unique_equipment_craft AS c ON e.equipment_id=c.equip_id
                    """
        }
//        val list = getBeanListByRaw<RawUniqueEquipmentData>(
        val list = DBOptimizer.getBeanListByRaw<RawUniqueEquipmentData>(
            sqlQuery,
            RawUniqueEquipmentData::class.simpleName
        )
        val map = mutableMapOf<Int, RawUniqueEquipmentData>()
        list?.forEach {
            map[it.unit_id] = it
        }
        return map
    }

    /***
     * 获取专属装备强化数据
     * @param unitId 角色id
     * @return
     */
    fun getUniqueEquipmentEnhance(unitId: Int): List<RawUniqueEquipmentEnhanceData>? {
        var tableName = "unique_equip_enhance_rate"
        // 考虑国服未实装的情况
        val count = getOne("""
            SELECT COUNT(*) 
            FROM sqlite_master 
            WHERE type='table' 
            AND name='$tableName'"""
        )
        if (!count.equals("1")) {
            tableName = "unique_equipment_enhance_rate"
        }
        if (UserSettings.get().getUserServer() == UserSettings.SERVER_CN) {
            return getBeanListByRaw<RawUniqueEquipmentEnhanceData>(
            """
                SELECT e.* 
                FROM $tableName AS e 
                JOIN unit_unique_equip AS u ON e.equipment_id=u.equip_id 
                WHERE u.unit_id=$unitId 
                """,
            RawUniqueEquipmentEnhanceData::class.java
            )
        }
        return getBeanListByRaw<RawUniqueEquipmentEnhanceData>(
        """
            SELECT e.* 
            FROM $tableName AS e 
            JOIN unit_unique_equipment AS u ON e.equipment_id=u.equip_id 
            WHERE u.unit_id=$unitId 
            """,
        RawUniqueEquipmentEnhanceData::class.java
        )
    }

    /***
     * Get Map<EquipmentId, List<RawUniqueEquipmentEnhanceData>> of all units
     */
    fun getUniqueEquipmentEnhanceMap(): Map<Int, List<RawUniqueEquipmentEnhanceData>> {
        var tableName = "unique_equip_enhance_rate"
        // 考虑国服未实装的情况
        val count = getOne("""
            SELECT COUNT(*) 
            FROM sqlite_master 
            WHERE type='table' 
            AND name='$tableName'"""
        )
        if (!count.equals("1")) {
            tableName = "unique_equipment_enhance_rate"
        }

        var sqlQuery = """
            SELECT e.* 
            FROM $tableName AS e 
            JOIN unit_unique_equipment AS u ON e.equipment_id=u.equip_id 
            """
        if (UserSettings.get().getUserServer() == UserSettings.SERVER_CN) {
            sqlQuery = """
                SELECT e.* 
                FROM $tableName AS e 
                JOIN unit_unique_equip AS u ON e.equipment_id=u.equip_id 
                """
        }

        val list = DBOptimizer.getBeanListByRaw<RawUniqueEquipmentEnhanceData>(
            sqlQuery,
            RawUniqueEquipmentEnhanceData::class.simpleName
        )
        val map = mutableMapOf<Int, MutableList<RawUniqueEquipmentEnhanceData>>()
        list?.distinctBy { it.equipment_id }?.forEach {
            map[it.equipment_id] = mutableListOf()
        }
        list?.forEach {
            map[it.equipment_id]!!.add(it)
        }
        return map
    }

    /***
     * 获取角色技能数据
     * @param unitId
     * @return
     */
    fun getUnitSkillData(unitId: Int): RawUnitSkillData? {
        return getBeanByRaw<RawUnitSkillData>(
            """
                SELECT * 
                FROM unit_skill_data 
                WHERE unit_id=$unitId 
                """,
            RawUnitSkillData::class.java
        )
    }

    /***
     * Get Map<UnitId, RawUnitSkillData> of all units
     */
    fun getUnitSkillDataMap(): Map<Int, RawUnitSkillData> {
//        val list = getBeanListByRaw<RawUnitSkillData>(
        val list = DBOptimizer.getBeanListByRaw<RawUnitSkillData>(
            """
                SELECT * 
                FROM unit_skill_data 
                WHERE unit_id < 200000
                """,
            RawUnitSkillData::class.simpleName
        )
        val map = mutableMapOf<Int, RawUnitSkillData>()
        list?.forEach {
            map[it.unit_id] = it
        }
        return map
    }

    /***
     * 获取技能数据
     * @param skillId
     * @return
     */
    fun getSkillData(skillId: Int): RawSkillData? {
        return getBeanByRaw<RawSkillData>(
            """
                SELECT * 
                FROM skill_data 
                WHERE skill_id=$skillId 
                """,
            RawSkillData::class.java
        )
    }

    /***
     * Get Map<SkillId, RawSkillData> of all units
     */
    fun getSkillDataCharactersMap(): Map<Int, RawSkillData> {
        val list = DBOptimizer.getBeanListByRaw<RawSkillData>(
            """
                SELECT * 
                FROM skill_data 
                WHERE skill_id < 2000000 OR (skill_id BETWEEN 4000000 AND 5000000)
                """,
            RawSkillData::class.simpleName
        )
        val map = mutableMapOf<Int, RawSkillData>()
        list?.forEach {
            map[it.skill_id] = it
        }
        return map
    }

    /***
     * Get Map<SkillId, RawSkillData> of all CB enemies
     */
    fun getSkillDataClanBattleMap(): Map<Int, RawSkillData> {
        val list = DBOptimizer.getBeanListByRaw<RawSkillData>(
            """
                SELECT * 
                FROM skill_data 
                WHERE skill_id BETWEEN 3000000 AND 4000000
                """,
            RawSkillData::class.simpleName
        )
        val map = mutableMapOf<Int, RawSkillData>()
        list?.forEach {
            map[it.skill_id] = it
        }
        return map
    }

    /***
     * 获取技能动作数据
     * @param actionId
     * @return
     */
    fun getSkillAction(actionId: Int): RawSkillAction? {
        return getBeanByRaw<RawSkillAction>(
            """
                SELECT * 
                FROM skill_action 
                WHERE action_id=$actionId 
                """,
            RawSkillAction::class.java
        )
    }

    /***
     * Get Map<ActionId, RawSkillAction> of all units
     */
    fun getSkillActionCharactersMap(): Map<Int, RawSkillAction> {
        val list = DBOptimizer.getBeanListByRaw<RawSkillAction>(
            """
                SELECT * 
                FROM skill_action 
                WHERE action_id < 200000000 OR (action_id BETWEEN 400000000 AND 500000000)
                """,
            RawSkillAction::class.simpleName
        )
        val map = mutableMapOf<Int, RawSkillAction>()
        list?.forEach {
            map[it.action_id] = it
        }
        return map
    }

    /***
     * Get Map<ActionId, RawSkillAction> of all CB enemies
     */
    fun getSkillActionClanBattleMap(): Map<Int, RawSkillAction> {
        val list = DBOptimizer.getBeanListByRaw<RawSkillAction>(
            """
                SELECT * 
                FROM skill_action 
                WHERE action_id BETWEEN 300000000 AND 400000000
                """,
            RawSkillAction::class.simpleName
        )
        val map = mutableMapOf<Int, RawSkillAction>()
        list?.forEach {
            map[it.action_id] = it
        }
        return map
    }

    /***
     * 获取行动顺序
     * @param unitId
     * @return
     */
    fun getUnitAttackPattern(unitId: Int): List<RawUnitAttackPattern>? {
        return getBeanListByRaw(
        """
            SELECT * 
            FROM unit_attack_pattern 
            WHERE unit_id=$unitId 
            ORDER BY pattern_id 
            """,
            RawUnitAttackPattern::class.java
        )
    }

    /***
     * Get Map<UnitId, ListMRawUnitAttackPattern>> of all units
     */
    fun getUnitAttackPatternCharactersMap(): Map<Int, List<RawUnitAttackPattern>> {
        val list = DBOptimizer.getBeanListByRaw<RawUnitAttackPattern>(
            """
            SELECT * 
            FROM unit_attack_pattern 
            WHERE unit_id < 200000 OR (unit_id BETWEEN 400000 AND 500000)
            ORDER BY pattern_id 
            """,
            RawUnitAttackPattern::class.simpleName
        )
        val map = mutableMapOf<Int, MutableList<RawUnitAttackPattern>>()
        list?.distinctBy { it.unit_id }?.forEach {
            map[it.unit_id] = mutableListOf()
        }
        list?.forEach {
            map[it.unit_id]!!.add(it)
        }
        return map
    }

    /***
     * Get Map<EnemyId, ListMRawUnitAttackPattern>> of all CB enemies
     */
    fun getUnitAttackPatternClanBattleMap(): Map<Int, List<RawUnitAttackPattern>> {
        val list = DBOptimizer.getBeanListByRaw<RawUnitAttackPattern>(
            """
            SELECT * 
            FROM unit_attack_pattern 
            WHERE unit_id BETWEEN 300000 AND 400000
            ORDER BY pattern_id 
            """,
            RawUnitAttackPattern::class.simpleName
        )
        val map = mutableMapOf<Int, MutableList<RawUnitAttackPattern>>()
        list?.distinctBy { it.unit_id }?.forEach {
            map[it.unit_id] = mutableListOf()
        }
        list?.forEach {
            map[it.unit_id]!!.add(it)
        }
        return map
    }

    /***
     * 获取会战期次
     * @param
     * @return
     */
    fun getClanBattlePeriod(): List<RawClanBattlePeriod>? {
        // 国服-> 读取1014前记录
//        if (UserSettings.get().getUserServer() == UserSettings.SERVER_CN) {
//            return getBeanListByRaw(
//                """
//                SELECT *
//                FROM clan_battle_period
//                WHERE clan_battle_id <= 1014
//                ORDER BY clan_battle_id DESC
//                """,
//                RawClanBattlePeriod::class.java
//            )
//        }
        return DBOptimizer.getBeanListByRaw(
            """
                SELECT * 
                FROM clan_battle_period 
                WHERE clan_battle_id IN (SELECT DISTINCT clan_battle_id FROM clan_battle_2_map_data)
                ORDER BY clan_battle_id DESC 
                ${if (UserSettings.get().getClanBattleLimit()) "LIMIT 13 " else ""}
                """,
                RawClanBattlePeriod::class.simpleName
        )
    }

    /***
     * 获取会战phase
     * @param
     * @return
     */
    fun getClanBattlePhase(clanBattleId: Int): List<RawClanBattlePhase>? {
        // 国服-> 迎合日服结构
//        if (UserSettings.get().getUserServer() == UserSettings.SERVER_CN) {
//            return getBeanListByRaw(
//                """
//                SELECT a.clan_battle_id,
//                CASE
//                WHEN a.lap_num_from = 1 THEN 1
//                WHEN a.lap_num_from = 2 AND a.clan_battle_id <= 1009 THEN 2
//                WHEN a.lap_num_from = 2 THEN 1
//                WHEN a.lap_num_from = 4 THEN 2
//                WHEN a.lap_num_from = 6 THEN 3
//                WHEN a.lap_num_from = 11 THEN 3
//                WHEN a.lap_num_from = 35 THEN 4
//                ELSE 1 END 'phase'
//                ,b1.wave_group_id 'wave_group_id_1'
//                ,b2.wave_group_id 'wave_group_id_2'
//                ,b3.wave_group_id 'wave_group_id_3'
//                ,b4.wave_group_id 'wave_group_id_4'
//                ,b5.wave_group_id 'wave_group_id_5'
//                FROM clan_battle_map_data AS a
//                JOIN clan_battle_boss_group AS b1 ON a.clan_battle_boss_group_id = b1.clan_battle_boss_group_id AND b1.order_num = 1
//                JOIN clan_battle_boss_group AS b2 ON a.clan_battle_boss_group_id = b2.clan_battle_boss_group_id AND b2.order_num = 2
//                JOIN clan_battle_boss_group AS b3 ON a.clan_battle_boss_group_id = b3.clan_battle_boss_group_id AND b3.order_num = 3
//                JOIN clan_battle_boss_group AS b4 ON a.clan_battle_boss_group_id = b4.clan_battle_boss_group_id AND b4.order_num = 4
//                JOIN clan_battle_boss_group AS b5 ON a.clan_battle_boss_group_id = b5.clan_battle_boss_group_id AND b5.order_num = 5
//                WHERE 1=1
//                AND a.clan_battle_id = $clanBattleId
//                AND (a.lap_num_from <> a.lap_num_to OR a.rsl_unlock_lap = 1)
//                ORDER BY a.clan_battle_id,a.lap_num_from DESC
//                """,
//                RawClanBattlePhase::class.java
//            )
//        }
        return getBeanListByRaw(
            """
                SELECT DISTINCT 
                clan_battle_id
                ,phase 
                ,wave_group_id_1 
                ,wave_group_id_2 
                ,wave_group_id_3 
                ,wave_group_id_4 
                ,wave_group_id_5 
                FROM clan_battle_2_map_data WHERE clan_battle_id=$clanBattleId 
                ORDER BY phase DESC 
                """,
            RawClanBattlePhase::class.java
        )
    }

    /***
     * Get Map<ClanBattleId, List<RawClanBattlePhase>> of all CBs
     */
    fun getClanBattlePhaseMap(): Map<Int, List<RawClanBattlePhase>> {
        val list = DBOptimizer.getBeanListByRaw<RawClanBattlePhase>(
            """
                SELECT DISTINCT
                clan_battle_id
                ,phase 
                ,wave_group_id_1 
                ,wave_group_id_2 
                ,wave_group_id_3 
                ,wave_group_id_4 
                ,wave_group_id_5 
                FROM clan_battle_2_map_data
                ORDER BY phase DESC 
                """,
            RawClanBattlePhase::class.simpleName
        )
        val map = mutableMapOf<Int, MutableList<RawClanBattlePhase>>()
        list?.distinctBy { it.clan_battle_id }?.forEach {
            map[it.clan_battle_id] = mutableListOf()
        }
        list?.forEach {
            map[it.clan_battle_id]!!.add(it)
        }
        return map
    }

    /***
     * 获取wave
     * @param
     * @return
     */
    fun getWaveGroupData(waveGroupList: List<Int>): List<RawWaveGroup>? {
        return getBeanListByRaw(
                """
                    SELECT * 
                    FROM wave_group_data 
                    WHERE wave_group_id IN ( %s ) 
                    """.format(waveGroupList.toString()
                        .replace("[", "")
                        .replace("]", "")),
            RawWaveGroup::class.java
        )
    }

    /***
     * Get Map<WaveGroupId, RawWaveGroup> of all CBs
     */
    fun getWaveGroupDataMap(): Map<Int, RawWaveGroup> {
        val list = getBeanListByRaw<RawWaveGroup>(
            """
                SELECT * 
                FROM wave_group_data
                WHERE wave_group_id BETWEEN 401000000 AND 402000000
                """,
            RawWaveGroup::class.java
        )
        val map = mutableMapOf<Int, RawWaveGroup>()
        list?.forEach {
            map[it.wave_group_id] = it
        }
        return map
    }

    /***
     * 获取wave
     * @param
     * @return
     */
    fun getWaveGroupData(waveGroupId: Int): RawWaveGroup? {
        return getBeanByRaw(
            """
            SELECT * 
            FROM wave_group_data 
            WHERE wave_group_id = $waveGroupId 
            """,
            RawWaveGroup::class.java
        )
    }

    /***
     * 获取enemyList
     * @param
     * @return
     */
    fun getEnemy(enemyIdList: List<Int>): List<RawEnemy>? {
        // 国服->去掉 [sre_enemy_parameter] 表
        if (UserSettings.get().getUserServer() == UserSettings.SERVER_CN) {
            return getBeanListByRaw(
                """
                    a.* 
                    ,u.prefab_id 
                    ,u.atk_type 
					,u.search_area_width 
                    ,u.normal_atk_cast_time 
                    ,u.comment
                    ,c.child_enemy_parameter_1 
                    ,c.child_enemy_parameter_2 
                    ,c.child_enemy_parameter_3 
                    ,c.child_enemy_parameter_4 
                    ,c.child_enemy_parameter_5 
                    ,b.union_burst 
                    ,b.main_skill_1 
                    ,b.main_skill_2 
                    ,b.main_skill_3 
                    ,b.main_skill_4 
                    ,b.main_skill_5 
                    ,b.main_skill_6 
                    ,b.main_skill_7 
                    ,b.main_skill_8 
                    ,b.main_skill_9 
                    ,b.main_skill_10  
                    ,b.ex_skill_1 
                    ,b.ex_skill_evolution_1 
                    ,b.ex_skill_2 
                    ,b.ex_skill_evolution_2 
                    ,b.ex_skill_3 
                    ,b.ex_skill_evolution_3 
                    ,b.ex_skill_4 
                    ,b.ex_skill_evolution_4 
                    ,b.ex_skill_5 
                    ,b.ex_skill_evolution_5 
                    ,b.sp_skill_1 
                    ,b.sp_skill_2 
                    ,b.sp_skill_3 
                    ,b.sp_skill_4 
                    ,b.sp_skill_5 
                    ,b.union_burst_evolution 
                    ,b.main_skill_evolution_1 
                    ,b.main_skill_evolution_2
                    FROM 
                    unit_skill_data b 
                    ,enemy_parameter a 
                    LEFT JOIN enemy_m_parts c ON a.enemy_id = c.enemy_id 
                    LEFT JOIN unit_enemy_data u ON a.unit_id = u.unit_id 
                    WHERE 
                    a.unit_id = b.unit_id 
                    AND a.enemy_id in ( %s )  
                    """.format(enemyIdList.toString()
                        .replace("[", "")
                        .replace("]", "")),
                RawEnemy::class.java
            )
        }
        return getBeanListByRaw(
                """
                    SELECT 
                    a.* 
                    ,u.prefab_id 
                    ,u.atk_type 
					,u.search_area_width 
                    ,u.normal_atk_cast_time 
                    ,u.comment
                    ,c.child_enemy_parameter_1 
                    ,c.child_enemy_parameter_2 
                    ,c.child_enemy_parameter_3 
                    ,c.child_enemy_parameter_4 
                    ,c.child_enemy_parameter_5 
                    ,b.union_burst 
                    ,b.main_skill_1 
                    ,b.main_skill_2 
                    ,b.main_skill_3 
                    ,b.main_skill_4 
                    ,b.main_skill_5 
                    ,b.main_skill_6 
                    ,b.main_skill_7 
                    ,b.main_skill_8 
                    ,b.main_skill_9 
                    ,b.main_skill_10  
                    ,b.ex_skill_1 
                    ,b.ex_skill_evolution_1 
                    ,b.ex_skill_2 
                    ,b.ex_skill_evolution_2 
                    ,b.ex_skill_3 
                    ,b.ex_skill_evolution_3 
                    ,b.ex_skill_4 
                    ,b.ex_skill_evolution_4 
                    ,b.ex_skill_5 
                    ,b.ex_skill_evolution_5 
                    ,b.sp_skill_1 
                    ,b.sp_skill_2 
                    ,b.sp_skill_3 
                    ,b.sp_skill_4 
                    ,b.sp_skill_5 
                    ,b.union_burst_evolution 
                    ,b.main_skill_evolution_1 
                    ,b.main_skill_evolution_2
                    FROM 
                    unit_skill_data b 
                    ,(SELECT enemy_id,
                             unit_id,
                             name,
                             level,
                             resist_status_id,
                             hp,
                             atk,
                             magic_str,
                             def,
                             magic_def,
                             physical_critical,
                             magic_critical,
                             wave_hp_recovery,
                             wave_energy_Recovery,
                             dodge,
                             physical_penetrate,
                             magic_penetrate,
                             life_steal,
                             hp_recovery_rate,
                             energy_recovery_rate,
                             energy_reduce_rate,
                             accuracy,
                             union_burst_level,
                             main_skill_lv_1,
                             main_skill_lv_2,
                             main_skill_lv_3,
                             main_skill_lv_4,
                             main_skill_lv_5,
                             main_skill_lv_6,
                             main_skill_lv_7,
                             main_skill_lv_8,
                             main_skill_lv_9,
                             main_skill_lv_10,
                             ex_skill_lv_1,
                             ex_skill_lv_2,
                             ex_skill_lv_3,
                             ex_skill_lv_4,
                             ex_skill_lv_5 
                      FROM enemy_parameter UNION ALL 
                      SELECT enemy_id,
                             unit_id,
                             name,
                             level,
                             resist_status_id,
                             hp,
                             atk,
                             magic_str,
                             def,
                             magic_def,
                             physical_critical,
                             magic_critical,
                             wave_hp_recovery,
                             wave_energy_Recovery,
                             dodge,
                             physical_penetrate,
                             magic_penetrate,
                             life_steal,
                             hp_recovery_rate,
                             energy_recovery_rate,
                             energy_reduce_rate,
                             accuracy,
                             union_burst_level,
                             main_skill_lv_1,
                             main_skill_lv_2,
                             main_skill_lv_3,
                             main_skill_lv_4,
                             main_skill_lv_5,
                             main_skill_lv_6,
                             main_skill_lv_7,
                             main_skill_lv_8,
                             main_skill_lv_9,
                             main_skill_lv_10,
                             ex_skill_lv_1,
                             ex_skill_lv_2,
                             ex_skill_lv_3,
                             ex_skill_lv_4,
                             ex_skill_lv_5 
                      FROM sre_enemy_parameter) a 
                    LEFT JOIN enemy_m_parts c ON a.enemy_id = c.enemy_id 
                    LEFT JOIN unit_enemy_data u ON a.unit_id = u.unit_id 
                    WHERE 
                    a.unit_id = b.unit_id 
                    AND a.enemy_id in ( %s )  
                    """.format(enemyIdList.toString()
                        .replace("[", "")
                        .replace("]", "")),
                RawEnemy::class.java
        )
    }

    /***
     * Get Map<EnemyId, RawEnemy> of all CBs
     */
    fun getEnemyClanBattleMap(): Map<Int, RawEnemy> {
        var query = """
                    SELECT 
                    a.* 
                    ,u.prefab_id 
                    ,u.atk_type 
					,u.search_area_width 
                    ,u.normal_atk_cast_time 
                    ,u.comment
                    ,c.child_enemy_parameter_1 
                    ,c.child_enemy_parameter_2 
                    ,c.child_enemy_parameter_3 
                    ,c.child_enemy_parameter_4 
                    ,c.child_enemy_parameter_5 
                    ,b.union_burst 
                    ,b.main_skill_1 
                    ,b.main_skill_2 
                    ,b.main_skill_3 
                    ,b.main_skill_4 
                    ,b.main_skill_5 
                    ,b.main_skill_6 
                    ,b.main_skill_7 
                    ,b.main_skill_8 
                    ,b.main_skill_9 
                    ,b.main_skill_10  
                    ,b.ex_skill_1 
                    ,b.ex_skill_evolution_1 
                    ,b.ex_skill_2 
                    ,b.ex_skill_evolution_2 
                    ,b.ex_skill_3 
                    ,b.ex_skill_evolution_3 
                    ,b.ex_skill_4 
                    ,b.ex_skill_evolution_4 
                    ,b.ex_skill_5 
                    ,b.ex_skill_evolution_5 
                    ,b.sp_skill_1 
                    ,b.sp_skill_2 
                    ,b.sp_skill_3 
                    ,b.sp_skill_4 
                    ,b.sp_skill_5 
                    ,b.union_burst_evolution 
                    ,b.main_skill_evolution_1 
                    ,b.main_skill_evolution_2
                    FROM 
                    unit_skill_data b 
                    ,(SELECT enemy_id,
                             unit_id,
                             name,
                             level,
                             resist_status_id,
                             hp,
                             atk,
                             magic_str,
                             def,
                             magic_def,
                             physical_critical,
                             magic_critical,
                             wave_hp_recovery,
                             wave_energy_Recovery,
                             dodge,
                             physical_penetrate,
                             magic_penetrate,
                             life_steal,
                             hp_recovery_rate,
                             energy_recovery_rate,
                             energy_reduce_rate,
                             accuracy,
                             union_burst_level,
                             main_skill_lv_1,
                             main_skill_lv_2,
                             main_skill_lv_3,
                             main_skill_lv_4,
                             main_skill_lv_5,
                             main_skill_lv_6,
                             main_skill_lv_7,
                             main_skill_lv_8,
                             main_skill_lv_9,
                             main_skill_lv_10,
                             ex_skill_lv_1,
                             ex_skill_lv_2,
                             ex_skill_lv_3,
                             ex_skill_lv_4,
                             ex_skill_lv_5 
                      FROM enemy_parameter UNION ALL 
                      SELECT enemy_id,
                             unit_id,
                             name,
                             level,
                             resist_status_id,
                             hp,
                             atk,
                             magic_str,
                             def,
                             magic_def,
                             physical_critical,
                             magic_critical,
                             wave_hp_recovery,
                             wave_energy_Recovery,
                             dodge,
                             physical_penetrate,
                             magic_penetrate,
                             life_steal,
                             hp_recovery_rate,
                             energy_recovery_rate,
                             energy_reduce_rate,
                             accuracy,
                             union_burst_level,
                             main_skill_lv_1,
                             main_skill_lv_2,
                             main_skill_lv_3,
                             main_skill_lv_4,
                             main_skill_lv_5,
                             main_skill_lv_6,
                             main_skill_lv_7,
                             main_skill_lv_8,
                             main_skill_lv_9,
                             main_skill_lv_10,
                             ex_skill_lv_1,
                             ex_skill_lv_2,
                             ex_skill_lv_3,
                             ex_skill_lv_4,
                             ex_skill_lv_5 
                      FROM sre_enemy_parameter) a 
                    LEFT JOIN enemy_m_parts c ON a.enemy_id = c.enemy_id 
                    LEFT JOIN unit_enemy_data u ON a.unit_id = u.unit_id 
                    WHERE 
                    a.unit_id = b.unit_id 
                    AND (a.enemy_id BETWEEN 401000000 AND 402000000)
                    """
        if (UserSettings.get().getUserServer() == UserSettings.SERVER_CN) {
            query = """
                    SELECT 
                    a.* 
                    ,u.prefab_id 
                    ,u.atk_type 
					,u.search_area_width 
                    ,u.normal_atk_cast_time 
                    ,u.comment
                    ,c.child_enemy_parameter_1 
                    ,c.child_enemy_parameter_2 
                    ,c.child_enemy_parameter_3 
                    ,c.child_enemy_parameter_4 
                    ,c.child_enemy_parameter_5 
                    ,b.union_burst 
                    ,b.main_skill_1 
                    ,b.main_skill_2 
                    ,b.main_skill_3 
                    ,b.main_skill_4 
                    ,b.main_skill_5 
                    ,b.main_skill_6 
                    ,b.main_skill_7 
                    ,b.main_skill_8 
                    ,b.main_skill_9 
                    ,b.main_skill_10  
                    ,b.ex_skill_1 
                    ,b.ex_skill_evolution_1 
                    ,b.ex_skill_2 
                    ,b.ex_skill_evolution_2 
                    ,b.ex_skill_3 
                    ,b.ex_skill_evolution_3 
                    ,b.ex_skill_4 
                    ,b.ex_skill_evolution_4 
                    ,b.ex_skill_5 
                    ,b.ex_skill_evolution_5 
                    ,b.sp_skill_1 
                    ,b.sp_skill_2 
                    ,b.sp_skill_3 
                    ,b.sp_skill_4 
                    ,b.sp_skill_5 
                    ,b.union_burst_evolution 
                    ,b.main_skill_evolution_1 
                    ,b.main_skill_evolution_2
                    FROM 
                    unit_skill_data b 
                    ,enemy_parameter a 
                    LEFT JOIN enemy_m_parts c ON a.enemy_id = c.enemy_id 
                    LEFT JOIN unit_enemy_data u ON a.unit_id = u.unit_id 
                    WHERE 
                    a.unit_id = b.unit_id 
                    AND (a.enemy_id BETWEEN 401000000 AND 402000000)
                    """
        }
        val list = getBeanListByRaw<RawEnemy>(
            query,
            RawEnemy::class.java
        )
        val map = mutableMapOf<Int, RawEnemy>()
        list?.forEach {
            map[it.enemy_id] = it
        }
        return map
    }

    /***
     * 获取第一个enemy
     * @param
     * @return
     */
    fun getEnemy(enemyId: Int): RawEnemy? {
        return getEnemy(listOf(enemyId))?.get(0)
    }

    /***
     * 获取敌人抗性值
     * @param
     * @return
     */
    fun getResistData(resistStatusId: Int): RawResistData? {
        return getBeanByRaw<RawResistData>(
            """
                SELECT * 
                FROM resist_data 
                WHERE resist_status_id=$resistStatusId  
                """,
            RawResistData::class.java
        )
    }

    /***
     * Get Map<WaveGroupId, RawWaveGroup> of all CBs
     */
    fun getResistDataMap(): Map<Int, RawResistData> {
        val list = getBeanListByRaw<RawResistData>(
            """
                SELECT * 
                FROM resist_data 
                """,
            RawResistData::class.java
        )
        val map = mutableMapOf<Int, RawResistData>()
        list?.forEach {
            map[it.resist_status_id] = it
        }
        return map
    }

    /***
     * 获取友方召唤物
     */
    fun getUnitMinion(minionId: Int): RawUnitMinion? {
        return getBeanByRaw<RawUnitMinion>(
            """
                SELECT
                a.*,
                b.union_burst,
                b.union_burst_evolution,
                b.main_skill_1,
                b.main_skill_evolution_1,
                b.main_skill_2,
                b.main_skill_evolution_2,
                b.ex_skill_1,
                b.ex_skill_evolution_1,
                b.main_skill_3,
                b.main_skill_4,
                b.main_skill_5,
                b.main_skill_6,
                b.main_skill_7,
                b.main_skill_8,
                b.main_skill_9,
                b.main_skill_10,
                b.ex_skill_2,
                b.ex_skill_evolution_2,
                b.ex_skill_3,
                b.ex_skill_evolution_3,
                b.ex_skill_4,
                b.ex_skill_evolution_4,
                b.ex_skill_5,
                b.sp_skill_1,
                b.ex_skill_evolution_5,
                b.sp_skill_2,
                b.sp_skill_3,
                b.sp_skill_4,
                b.sp_skill_5
            FROM
                unit_skill_data b,
                unit_data a
            WHERE
                a.unit_id = b.unit_id
                AND a.unit_id = $minionId
                """,
            RawUnitMinion::class.java
        )
    }

    /***
     * Get Map<UnitId, RawUnitMinion> of all units
     */
    fun getUnitMinionMap(): Map<Int, RawUnitMinion> {
        val list = DBOptimizer.getBeanListByRaw<RawUnitMinion>(
            """
                SELECT
                a.*,
                b.union_burst,
                b.union_burst_evolution,
                b.main_skill_1,
                b.main_skill_evolution_1,
                b.main_skill_2,
                b.main_skill_evolution_2,
                b.ex_skill_1,
                b.ex_skill_evolution_1,
                b.main_skill_3,
                b.main_skill_4,
                b.main_skill_5,
                b.main_skill_6,
                b.main_skill_7,
                b.main_skill_8,
                b.main_skill_9,
                b.main_skill_10,
                b.ex_skill_2,
                b.ex_skill_evolution_2,
                b.ex_skill_3,
                b.ex_skill_evolution_3,
                b.ex_skill_4,
                b.ex_skill_evolution_4,
                b.ex_skill_5,
                b.sp_skill_1,
                b.ex_skill_evolution_5,
                b.sp_skill_2,
                b.sp_skill_3,
                b.sp_skill_4,
                b.sp_skill_5
            FROM
                unit_skill_data b,
                unit_data a
            WHERE
                (a.unit_id BETWEEN 400000 AND 500000) AND
                (b.unit_id BETWEEN 400000 AND 500000) AND
                a.unit_id = b.unit_id
                """,
            RawUnitMinion::class.simpleName
        )
        val map = mutableMapOf<Int, RawUnitMinion>()
        list?.forEach {
            map[it.unit_id] = it
        }
        return map
    }

    /***
     * 获取敌方召唤物
     */
    fun getEnemyMinion(enemyId: Int): RawEnemy? {
        return getBeanByRaw<RawEnemy>(
            """
                SELECT
                d.unit_name,
                d.prefab_id,
                d.search_area_width,
                d.atk_type,
                d.move_speed,
                a.*,
                b.*,
                d.normal_atk_cast_time,
                c.child_enemy_parameter_1,
                c.child_enemy_parameter_2,
                c.child_enemy_parameter_3,
                c.child_enemy_parameter_4,
                c.child_enemy_parameter_5
                FROM
                enemy_parameter a
                JOIN unit_skill_data AS b ON a.unit_id = b.unit_id
                JOIN unit_enemy_data AS d ON a.unit_id = d.unit_id
                LEFT JOIN enemy_m_parts c ON a.enemy_id = c.enemy_id
                WHERE a.enemy_id = $enemyId
                """,
            RawEnemy::class.java
        )
    }

    /***
     * 获取迷宫bossList
     * @param
     * @return
     */
    fun getDungeons(): List<RawDungeon>? {
        // 考虑国服未实装的情况
        val count = getOne("""SELECT COUNT(*) 
                                FROM sqlite_master 
                                WHERE type='table' 
                                AND name='dungeon_area'""")
        if (!count.equals("1")) {
            return getBeanListByRaw(
                """
                SELECT
                a.dungeon_area_id,
                a.dungeon_name,
                a.description,
                b.*
                FROM
                dungeon_area_data AS a 
                JOIN wave_group_data AS b ON a.wave_group_id=b.wave_group_id 
                ORDER BY a.dungeon_area_id DESC 
                """,
                RawDungeon::class.java
            )
        }
        return getBeanListByRaw(
            """
                SELECT
                    a.dungeon_area_id 'dungeon_area_id',
                    a.dungeon_name 'dungeon_name',
                    a.description 'description',
                    sp.mode 'mode',
                    w.*
                FROM
                    dungeon_area AS a
                JOIN dungeon_quest_data AS b ON a.dungeon_area_id = b.dungeon_area_id
                AND b.quest_type = 4
                JOIN dungeon_special_battle AS sp ON b.quest_id = sp.quest_id
                JOIN wave_group_data AS w ON sp.wave_group_id = w.wave_group_id
                UNION ALL
                SELECT
                    a.dungeon_area_id,
                    a.dungeon_name,
                    a.description,
                    0 AS 'mode',
                    w.*
                FROM
                    dungeon_area AS a
                JOIN dungeon_quest_data AS b ON a.dungeon_area_id = b.dungeon_area_id
                AND b.quest_type = 3
                JOIN wave_group_data AS w ON b.wave_group_id = w.wave_group_id
                ORDER BY
                    a.dungeon_area_id DESC,
                    sp.mode DESC
                """,
            RawDungeon::class.java
        )
    }

    /***
     * get secret dungeon schedule list
     */
    fun getSecretDungeonSchedules(): List<RawSecretDungeonSchedule>? {
        // 考虑国服未实装的情况
        val count = getOne("""SELECT COUNT(*) 
                                FROM sqlite_master 
                                WHERE type='table' 
                                AND name='secret_dungeon_schedule'""")
        if (!count.equals("1")) {
            return null;
        }
        return getBeanListByRaw(
            """
                SELECT
                    *
                FROM
                    secret_dungeon_schedule
                ORDER BY
                    start_time DESC;
                """,
            RawSecretDungeonSchedule::class.java
        )
    }

    /***
     * get specified secret dungeon quest list
     */
    fun getSecretDungeonQuests(dungeon_area_id: Int): List<RawSecretDungeonQuestData>? {
        return getBeanListByRaw(
            """
                SELECT
                    a.*, b.*
                FROM
                    secret_dungeon_quest_data AS a
                JOIN wave_group_data AS b ON a.wave_group_id = b.wave_group_id
                WHERE
                    a.dungeon_area_id = $dungeon_area_id
                AND a.wave_group_id <> 0
                ORDER BY
                    a.difficulty DESC,
                    a.floor_num DESC
                """,
            RawSecretDungeonQuestData::class.java
        )
    }

    /***
     * 获取特殊活动
     */
    fun getSpEvents(): List<RawSpEvent>? {
        return when(getSpEventCount()) {
            1 -> getBeanListByRaw(
                """
                SELECT
                a.kaiser_boss_id 'boss_id'
                ,a.name
                ,b.*
                FROM kaiser_quest_data AS a 
                JOIN wave_group_data AS b ON a.wave_group_id=b.wave_group_id 
                WHERE NOT EXISTS (SELECT 1 FROM kaiser_special_battle WHERE wave_group_id=a.wave_group_id)
                UNION ALL
                SELECT 
                0 'boss_id'
                ,'メインボスMODE'||a.mode 'name'
                ,b.*
                FROM kaiser_special_battle AS a
                JOIN wave_group_data AS b ON a.wave_group_id=b.wave_group_id 
                """,
                RawSpEvent::class.java)
            2 -> getBeanListByRaw(
                """
                SELECT
                a.legion_boss_id 'boss_id'
                ,a.name
                ,b.*
                FROM legion_quest_data AS a 
                JOIN wave_group_data AS b ON a.wave_group_id=b.wave_group_id 
                WHERE NOT EXISTS (SELECT 1 FROM legion_special_battle WHERE wave_group_id=a.wave_group_id)
                UNION ALL
                SELECT
                0 'boss_id'
                ,'メインボスMODE'||a.mode 'name'
                ,b.*
                FROM legion_special_battle AS a 
                JOIN wave_group_data AS b ON a.wave_group_id=b.wave_group_id 
                UNION ALL
                SELECT
                a.kaiser_boss_id 'boss_id'
                ,a.name
                ,b.*
                FROM kaiser_quest_data AS a 
                JOIN wave_group_data AS b ON a.wave_group_id=b.wave_group_id 
                WHERE NOT EXISTS (SELECT 1 FROM kaiser_special_battle WHERE wave_group_id=a.wave_group_id)
                UNION ALL
                SELECT 
                0 'boss_id'
                ,'メインボスMODE'||a.mode 'name'
                ,b.*
                FROM kaiser_special_battle AS a
                JOIN wave_group_data AS b ON a.wave_group_id=b.wave_group_id 
                """,
                RawSpEvent::class.java)
            3 -> getBeanListByRaw(
                """
                SELECT
                    a.sre_boss_id 'boss_id',
                    a.difficulty || '-' || b.phase || ' ' || b.name 'name',
                    c.wave_group_id,
                    c.enemy_id_1,
                    c.enemy_id_2,
                    c.enemy_id_3,
                    c.enemy_id_4,
                    c.enemy_id_5
                FROM
                    sre_quest_difficulty_data AS a
                JOIN sre_boss_data AS b ON a.sre_boss_id = b.sre_boss_id
                JOIN sre_wave_group_data AS c ON a.wave_group_id = c.wave_group_id
                UNION ALL
                SELECT
                    a.legion_boss_id 'boss_id',
                    a.name,
                    b.wave_group_id,
                    b.enemy_id_1,
                    b.enemy_id_2,
                    b.enemy_id_3,
                    b.enemy_id_4,
                    b.enemy_id_5
                FROM
                    legion_quest_data AS a
                JOIN wave_group_data AS b ON a.wave_group_id = b.wave_group_id
                WHERE
                    NOT EXISTS (
                        SELECT
                            1
                        FROM
                            legion_special_battle
                        WHERE
                            wave_group_id = a.wave_group_id
                    )
                UNION ALL
                SELECT
                    0 'boss_id',
                    'メインボスMODE' || a.mode 'name',
                    b.wave_group_id,
                    b.enemy_id_1,
                    b.enemy_id_2,
                    b.enemy_id_3,
                    b.enemy_id_4,
                    b.enemy_id_5
                FROM
                    legion_special_battle AS a
                JOIN wave_group_data AS b ON a.wave_group_id = b.wave_group_id
                UNION ALL
                SELECT
                    a.kaiser_boss_id 'boss_id',
                    a.name,
                    b.wave_group_id,
                    b.enemy_id_1,
                    b.enemy_id_2,
                    b.enemy_id_3,
                    b.enemy_id_4,
                    b.enemy_id_5
                FROM
                    kaiser_quest_data AS a
                JOIN wave_group_data AS b ON a.wave_group_id = b.wave_group_id
                WHERE
                    NOT EXISTS (
                        SELECT
                            1
                        FROM
                            kaiser_special_battle
                        WHERE
                            wave_group_id = a.wave_group_id
                    )
                UNION ALL
                SELECT
                    0 'boss_id',
                    'メインボスMODE' || a.mode 'name',
                    b.wave_group_id,
                    b.enemy_id_1,
                    b.enemy_id_2,
                    b.enemy_id_3,
                    b.enemy_id_4,
                    b.enemy_id_5
                FROM
                    kaiser_special_battle AS a
                JOIN wave_group_data AS b ON a.wave_group_id = b.wave_group_id
                """,
                RawSpEvent::class.java)
            else -> null
        }
    }

    private fun getSpEventCount(): Int {
        return getOne(
            """
                SELECT COUNT(*) 'num'
                FROM sqlite_master 
                WHERE type = 'table' 
                AND name in ('legion_quest_data', 'kaiser_quest_data', 'sre_quest_difficulty_data')
            """
        ).let {
            it!!.toInt()
        }
    }

    /***
     * 获取所有Quest
     */
    fun getQuests(): List<RawQuest>? {
        return getBeanListByRaw(
            """
                SELECT * FROM quest_data WHERE quest_id < 13000000 ORDER BY daily_limit ASC, quest_id DESC 
                """,
            RawQuest::class.java
        )
    }

    /***
     * 获取掉落奖励
     */
    fun getEnemyRewardData(dropRewardIdList: List<Int>): List<RawEnemyRewardData>? {
        return getBeanListByRaw(
            """
                SELECT * 
                FROM enemy_reward_data 
                WHERE drop_reward_id IN ( %s ) 
                """.format(dropRewardIdList.toString()
                .replace("[", "")
                .replace("]", "")),
            RawEnemyRewardData::class.java
        )
    }

    /***
    * 获取掉落奖励
    */
    fun getEnemyRewardData(dropRewardId: Int): RawEnemyRewardData? {
        return getEnemyRewardData(listOf(dropRewardId))?.get(0)
    }

    /***
     * 获取campaign日程
     */
    fun getCampaignSchedule(nowTimeString: String?): List<RawScheduleCampaign>? {
        var sqlString = " SELECT * FROM campaign_schedule WHERE id < 5000 "
        nowTimeString?.let {
            sqlString += " AND end_time > '$it' "
        }
        return getBeanListByRaw(sqlString, RawScheduleCampaign::class.java)
    }

    /***
     * 获取free gacha日程
     */
    fun getFreeGachaSchedule(nowTimeString: String?): List<RawScheduleFreeGacha>? {
        var sqlString = " SELECT * FROM campaign_freegacha "
        nowTimeString?.let {
            sqlString += " WHERE end_time > '$it' "
        }
        return getBeanListByRaw(sqlString, RawScheduleFreeGacha::class.java)
    }

    /***
     * 获取hatsune日程
     */
    fun getHatsuneSchedule(nowTimeString: String?): List<RawScheduleHatsune>? {
        var sqlString = """
            SELECT a.event_id, a.start_time, a.end_time, b.title 
            FROM hatsune_schedule AS a JOIN event_story_data AS b ON a.event_id = b.value
            UNION ALL
            SELECT a.event_id, a.start_time, a.end_time, b.title 
            FROM hatsune_schedule AS a JOIN event_story_data AS b ON a.event_id = b.value-10000
            """
        nowTimeString?.let {
            sqlString += " WHERE a.end_time > '$it' "
        }
        sqlString += " ORDER BY a.event_id DESC "
        return getBeanListByRaw(sqlString, RawScheduleHatsune::class.java)
    }

    /***
     * 获取hatsune一般boss数据
     */
    fun getHatsuneBattle(eventId: Int): List<RawHatsuneBoss>? {
        return getBeanListByRaw(
            """
            SELECT
            a.*
            FROM
            hatsune_boss a
            WHERE
            event_id = $eventId 
            AND area_id <> 0 
            """,
            RawHatsuneBoss::class.java
        )
    }

    /***
     * 获取hatsune SP boss数据
     */
    fun getHatsuneSP(eventId: Int): List<RawHatsuneSpecialBattle>? {
        return getBeanListByRaw(
            """
            SELECT
            a.*
            FROM hatsune_special_battle a
            WHERE event_id = $eventId
            """,
            RawHatsuneSpecialBattle::class.java
        )
    }

    /***
     * 获取露娜塔日程
     */
    fun getTowerSchedule(nowTimeString: String?): List<RawTowerSchedule>? {
        var sqlString = " SELECT * FROM tower_schedule "
        nowTimeString?.let {
            sqlString += " WHERE end_time > '$it' "
        }
        return getBeanListByRaw(sqlString, RawTowerSchedule::class.java)
    }

    /***
     * 获取装备碎片
     */
    fun getEquipmentPiece(): List<RawEquipmentPiece>? {
        return DBOptimizer.getBeanListByRaw(
            """
            SELECT equipment_id, equipment_name 
            FROM equipment_data 
            WHERE (equipment_id BETWEEN 113000 AND 139999) OR equipment_id > 11000000
            """,
            RawEquipmentPiece::class.simpleName
        )
    }

    /***
     * 获取异常状态map
     * @param
     * @return
     */
    val ailmentMap: Map<Int, String>?
        get() = getIntStringMap(
            "SELECT * FROM ailment_data ",
            "ailment_id",
            "ailment_name"
        )

    /***
     * Get Map<AilmentId, AilmentName> of all ailments
     */
    fun getAilmentClanBattleMap(): Map<Int, String> {
        return getIntStringMap(
            "SELECT * FROM ailment_data ",
            "ailment_id",
            "ailment_name"
        )!!
    }

    val maxCharaLevel: Int
        get() {
            val result = getOne("SELECT max(team_level) FROM experience_team ")
            return result?.toInt() ?: 0
        }

    val maxCharaRank: Int
        get() {
            val result = getOne("SELECT max(promotion_level) FROM unit_promotion ")
            return result?.toInt() ?: 0
        }

    val maxUniqueEquipmentLevel: Int
        get() {
            val result =
                getOne("SELECT max(enhance_level) FROM unique_equipment_enhance_data ")
            return result?.toInt() ?: 0
        }

    val maxEnemyLevel: Int
        get() {
            val result = getOne("SELECT MAX(level) FROM enemy_parameter ")
            return result?.toInt() ?: 0
        }

    val hasAction10: Boolean
        get() {
            val result = getOne("""
                SELECT COUNT(*)
                FROM sqlite_master 
                WHERE type='table'
                AND name='skill_data'
                AND sql like '%action_10%';"""
            )
            return result == "1"
        }

    /***
     * 随机生成16位随机英数字符串
     * @return
     */
    val randomId: String
        get() {
            val str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            val random = Random()
            val sb = StringBuffer()
            for (i in 0..15) {
                val number = random.nextInt(36)
                sb.append(str[number])
            }
            return sb.toString()
        }
}
