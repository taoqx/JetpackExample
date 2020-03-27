package com.example.jetpack.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,   //名称
    val icon: String,   //图标
    val amount: String, //金额
    val period: String, //期数
    val interest_rate: String,  //利率
    val process_time: String,   //放款时间
    val eligibility: List<String>,    //资格
    val process: String,    //流程
    val android_jump_url: String  //跳转地址

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (name != other.name) return false
        if (icon != other.icon) return false
        if (amount != other.amount) return false
        if (period != other.period) return false
        if (interest_rate != other.interest_rate) return false
        if (process_time != other.process_time) return false
        if (eligibility != other.eligibility) return false
        if (process != other.process) return false
        if (android_jump_url != other.android_jump_url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + period.hashCode()
        result = 31 * result + interest_rate.hashCode()
        result = 31 * result + process_time.hashCode()
        result = 31 * result + eligibility.hashCode()
        result = 31 * result + process.hashCode()
        result = 31 * result + android_jump_url.hashCode()
        return result
    }
}