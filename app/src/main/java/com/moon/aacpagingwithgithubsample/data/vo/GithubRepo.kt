package com.moon.aacpagingwithgithubsample.data.vo

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "github_repos")
data class GithubRepo(
    @PrimaryKey @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @ColumnInfo(name = "full_name") @SerializedName("full_name") val fullName: String?,
    @SerializedName("description") val description: String?,
    @Embedded @SerializedName("owner") val owner: User?
) {
    data class User(
        @SerializedName("login") val login: String,
        @ColumnInfo(name = "avatar_url") @SerializedName("avatar_url") val avatarUrl: String
    )
}
