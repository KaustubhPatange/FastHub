package com.fastaccess.ui.modules.repos.code.graph


import com.google.gson.annotations.SerializedName

data class StatsModel(
    val items: List<Item>
) {
    data class Item(
        val author: Author,
        val total: Int,
        val weeks: List<Week>
    ) {
        data class Author(
            @SerializedName("avatar_url")
            val avatarUrl: String,
            @SerializedName("events_url")
            val eventsUrl: String,
            @SerializedName("followers_url")
            val followersUrl: String,
            @SerializedName("following_url")
            val followingUrl: String,
            @SerializedName("gists_url")
            val gistsUrl: String,
            @SerializedName("gravatar_id")
            val gravatarId: String,
            @SerializedName("html_url")
            val htmlUrl: String,
            val id: Int,
            val login: String,
            @SerializedName("node_id")
            val nodeId: String,
            @SerializedName("organizations_url")
            val organizationsUrl: String,
            @SerializedName("received_events_url")
            val receivedEventsUrl: String,
            @SerializedName("repos_url")
            val reposUrl: String,
            @SerializedName("site_admin")
            val siteAdmin: Boolean,
            @SerializedName("starred_url")
            val starredUrl: String,
            @SerializedName("subscriptions_url")
            val subscriptionsUrl: String,
            val type: String,
            val url: String
        )

        data class Week(
            val a: Int,
            val c: Int,
            val d: Int,
            val w: Long
        )
    }
}