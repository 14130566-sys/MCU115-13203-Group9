package com.example.lab15

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 取得資料庫實體
        dbrw = MyDBHelper(this).writableDatabase
        // 宣告Adapter並連結ListView
        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, items)
        findViewById<ListView>(R.id.listView).adapter = adapter
        // 設定監聽器
        setListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbrw.close() // 關閉資料庫
    }

    // 設定監聽器
    private fun setListener() {
        val edBrand = findViewById<EditText>(R.id.edBrand)
        val edModel = findViewById<EditText>(R.id.edModel)
        val edYear = findViewById<EditText>(R.id.edYear)
        val edPrice = findViewById<EditText>(R.id.edPrice)

        findViewById<Button>(R.id.btnInsert).setOnClickListener {
            // 判斷是否有填入廠牌、車型、年份或價格
            if (edBrand.length() < 1 || edModel.length() < 1 || edYear.length() < 1 || edPrice.length() < 1)
                showToast("欄位請勿留空")
            else
                try {
                    // 新增一筆車子紀錄於carTable資料表
                    dbrw.execSQL(
                        "INSERT INTO carTable(brand, model, year, price) VALUES(?,?,?,?)",
                        arrayOf(
                            edBrand.text.toString(),
                            edModel.text.toString(),
                            edYear.text.toString(),
                            edPrice.text.toString()
                        )
                    )
                    showToast("新增:${edBrand.text},車型:${edModel.text},年份:${edYear.text},價格:${edPrice.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("新增失敗:$e")
                }
        }

        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            // 判斷是否有填入廠牌、車型、年份或價格
            if (edBrand.length() < 1 || edModel.length() < 1 || edYear.length() < 1 || edPrice.length() < 1)
                showToast("欄位請勿留空")
            else
                try {
                    // 尋找相同廠牌的紀錄並更新車型、年份與價格欄位的值
                    dbrw.execSQL(
                        "UPDATE carTable SET model=?, year=?, price=? WHERE brand LIKE ?",
                        arrayOf(
                            edModel.text.toString(),
                            edYear.text.toString(),
                            edPrice.text.toString(),
                            edBrand.text.toString()
                        )
                    )
                    showToast("更新:${edBrand.text},車型:${edModel.text},年份:${edYear.text},價格:${edPrice.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("更新失敗:$e")
                }
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            // 判斷是否有填入廠牌
            if (edBrand.length() < 1)
                showToast("廠牌請勿留空")
            else
                try {
                    // 從carTable資料表刪除相同廠牌的紀錄
                    dbrw.execSQL(
                        "DELETE FROM carTable WHERE brand LIKE ?",
                        arrayOf(edBrand.text.toString())
                    )
                    showToast("刪除:${edBrand.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("刪除失敗:$e")
                }
        }

        findViewById<Button>(R.id.btnQuery).setOnClickListener {
            // 若無輸入廠牌則SQL語法為查詢全部車子，反之查詢該廠牌資料
            val c = if (edBrand.length() < 1) {
                dbrw.rawQuery("SELECT * FROM carTable", null)
            } else {
                dbrw.rawQuery("SELECT * FROM carTable WHERE brand LIKE ?", arrayOf(edBrand.text.toString()))
            }

            c.moveToFirst() //從第一筆開始輸出
            items.clear() //清空舊資料
            showToast("共有${c.count}筆資料")
            for (i in 0 until c.count) {
                // 加入新資料
                items.add("廠牌:${c.getString(0)}\t車型:${c.getString(1)}\t年份:${c.getInt(2)}\t價格:${c.getInt(3)}")
                c.moveToNext() // 移動到下一筆
            }
            adapter.notifyDataSetChanged() // 更新列表資料
            c.close() // 關閉Cursor
        }
    }

    // 建立showToast方法顯示Toast訊息
    private fun showToast(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()

    // 清空輸入的欄位內容
    private fun cleanEditText() {
        findViewById<EditText>(R.id.edBrand).setText("")
        findViewById<EditText>(R.id.edModel).setText("")
        findViewById<EditText>(R.id.edYear).setText("")
        findViewById<EditText>(R.id.edPrice).setText("")
    }
}
