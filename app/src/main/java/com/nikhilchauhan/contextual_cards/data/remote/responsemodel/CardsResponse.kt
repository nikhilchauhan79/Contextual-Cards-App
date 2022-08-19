package com.nikhilchauhan.contextual_cards.data.remote.responsemodel


import com.google.gson.annotations.SerializedName

data class CardsResponse(
    @SerializedName("card_groups")
    val cardGroups: List<CardGroup?>?
) {
    data class CardGroup(
        @SerializedName("card_type")
        val cardType: Int?,
        @SerializedName("cards")
        val cards: List<Card?>?,
        @SerializedName("design_type")
        val designType: String?,
        @SerializedName("height")
        val height: Int?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("is_scrollable")
        val isScrollable: Boolean?,
        @SerializedName("level")
        val level: Int?,
        @SerializedName("name")
        val name: String?
    ) {
        data class Card(
            @SerializedName("bg_color")
            val bgColor: String?,
            @SerializedName("bg_image")
            val bgImage: BgImage?,
            @SerializedName("cta")
            val cta: List<Cta?>?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("formatted_description")
            val formattedDescription: FormattedDescription?,
            @SerializedName("formatted_title")
            val formattedTitle: FormattedTitle?,
            @SerializedName("icon")
            val icon: Icon?,
            @SerializedName("is_disabled")
            val isDisabled: Boolean?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("title")
            val title: String?,
            @SerializedName("url")
            val url: String?
        ) {
            data class BgImage(
                @SerializedName("aspect_ratio")
                val aspectRatio: Double?,
                @SerializedName("image_type")
                val imageType: String?,
                @SerializedName("image_url")
                val imageUrl: String?
            )

            data class Cta(
                @SerializedName("bg_color")
                val bgColor: String?,
                @SerializedName("text")
                val text: String?,
                @SerializedName("text_color")
                val textColor: String?,
                @SerializedName("url")
                val url: String?,
                @SerializedName("url_choice")
                val urlChoice: String?
            )

            data class FormattedDescription(
                @SerializedName("align")
                val align: String?,
                @SerializedName("entities")
                val entities: List<Any?>?,
                @SerializedName("text")
                val text: String?
            )

            data class FormattedTitle(
                @SerializedName("align")
                val align: String?,
                @SerializedName("entities")
                val entities: List<Any?>?,
                @SerializedName("text")
                val text: String?
            )

            data class Icon(
                @SerializedName("aspect_ratio")
                val aspectRatio: Double?,
                @SerializedName("image_type")
                val imageType: String?,
                @SerializedName("image_url")
                val imageUrl: String?
            )
        }
    }
}