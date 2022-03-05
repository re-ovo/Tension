package me.rerere.tension.provider.game

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.rerere.tension.provider.Category
import me.rerere.tension.provider.DataProvider
import me.rerere.tension.util.DataState
import me.rerere.tension.util.parseIso8601TimeToLocalTime
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EpicGameSource : DataProvider {
    override fun getDisplayName(): String {
        return "Epic 免费游戏"
    }

    override fun getCategory(): Category = Category.GAME

    @Composable
    override fun getPreviewUI(): @Composable () -> Unit = {
        val viewModel = viewModel<EpicGameSourceViewModel>()
        val freeGames by viewModel.freeGames.collectAsState()

        when (freeGames) {
            is DataState.Success -> {
                freeGames.readSafely()!!.data.catalog.searchStore.elements
                    .filter {
                        !it.promotions.isJsonNull
                    }.forEach {
                        Column {
                            Text(
                                text = it.title.trim(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            it.promotions.asJsonObject?.let { promotions ->
                                if (promotions.getAsJsonArray("promotionalOffers").size() > 0) {
                                    // 正在促销
                                    val endDate = promotions
                                        .getAsJsonArray("promotionalOffers")
                                        .get(0).asJsonObject
                                        .getAsJsonArray("promotionalOffers")
                                        .get(0).asJsonObject
                                        .get("endDate").asString
                                    Text(
                                        text = "截止 ${parseIso8601TimeToLocalTime(endDate)}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                } else {
                                    // 即将促销
                                    val (startDate, endDate) = promotions
                                        .getAsJsonArray("upcomingPromotionalOffers")
                                        .get(0).asJsonObject
                                        .getAsJsonArray("promotionalOffers")
                                        .get(0).asJsonObject.let {
                                            Pair(
                                                it.get("startDate").asString,
                                                it.get("endDate").asString
                                            )
                                        }
                                    Text(
                                        text = "${parseIso8601TimeToLocalTime(startDate)} 到 ${
                                            parseIso8601TimeToLocalTime(
                                                endDate
                                            )
                                        }",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
            }
            is DataState.Loading -> {
                CircularProgressIndicator()
            }
            is DataState.Error -> {
                Text("Error")
            }
            else -> {}
        }
    }

    @Composable
    override fun getDetailUI(): @Composable () -> Unit {
        TODO("Not yet implemented")
    }
}

class EpicGameSourceViewModel : KoinComponent, ViewModel() {
    private val okHttpClient: OkHttpClient by inject()

    val freeGames = MutableStateFlow<DataState<GameList>>(DataState.Empty)

    private fun loadFreeGames() {
        viewModelScope.launch(Dispatchers.IO) {
            freeGames.value = DataState.Loading
            try {
                val request = Request.Builder()
                    .url("https://store-site-backend-static-ipv4.ak.epicgames.com/freeGamesPromotions?locale=zh-CN&country=CN&allowCountries=CN")
                    .get()
                    .build()
                val response = okHttpClient.newCall(request).execute()
                val body = response.body?.string()
                val gameList = Gson().fromJson(body, GameList::class.java)
                freeGames.value = DataState.Success(gameList)
            } catch (e: Exception) {
                e.printStackTrace()
                freeGames.value = DataState.Error(e.javaClass.simpleName)
            }
        }
    }

    init {
        loadFreeGames()
    }
}

data class GameList(
    @SerializedName("data")
    val `data`: Data
) {
    data class Data(
        @SerializedName("Catalog")
        val catalog: Catalog
    ) {
        data class Catalog(
            @SerializedName("searchStore")
            val searchStore: SearchStore
        ) {
            data class SearchStore(
                @SerializedName("elements")
                val elements: List<Element>,
                @SerializedName("paging")
                val paging: Paging
            ) {
                data class Element(
                    @SerializedName("catalogNs")
                    val catalogNs: CatalogNs,
                    @SerializedName("categories")
                    val categories: List<Category>,
                    @SerializedName("customAttributes")
                    val customAttributes: List<CustomAttribute>,
                    @SerializedName("description")
                    val description: String,
                    @SerializedName("effectiveDate")
                    val effectiveDate: String,
                    @SerializedName("expiryDate")
                    val expiryDate: Any,
                    @SerializedName("id")
                    val id: String,
                    @SerializedName("isCodeRedemptionOnly")
                    val isCodeRedemptionOnly: Boolean,
                    @SerializedName("items")
                    val items: List<Item>,
                    @SerializedName("keyImages")
                    val keyImages: List<KeyImage>,
                    @SerializedName("namespace")
                    val namespace: String,
                    @SerializedName("offerMappings")
                    val offerMappings: List<OfferMapping>,
                    @SerializedName("offerType")
                    val offerType: String,
                    @SerializedName("price")
                    val price: Price,
                    @SerializedName("productSlug")
                    val productSlug: String,
                    @SerializedName("promotions")
                    val promotions: JsonElement,
                    @SerializedName("seller")
                    val seller: Seller,
                    @SerializedName("status")
                    val status: String,
                    @SerializedName("tags")
                    val tags: List<Tag>,
                    @SerializedName("title")
                    val title: String,
                    @SerializedName("url")
                    val url: Any,
                    @SerializedName("urlSlug")
                    val urlSlug: String
                ) {
                    data class CatalogNs(
                        @SerializedName("mappings")
                        val mappings: List<Mapping>
                    ) {
                        data class Mapping(
                            @SerializedName("pageSlug")
                            val pageSlug: String,
                            @SerializedName("pageType")
                            val pageType: String
                        )
                    }

                    data class Category(
                        @SerializedName("path")
                        val path: String
                    )

                    data class CustomAttribute(
                        @SerializedName("key")
                        val key: String,
                        @SerializedName("value")
                        val value: String
                    )

                    data class Item(
                        @SerializedName("id")
                        val id: String,
                        @SerializedName("namespace")
                        val namespace: String
                    )

                    data class KeyImage(
                        @SerializedName("type")
                        val type: String,
                        @SerializedName("url")
                        val url: String
                    )

                    data class OfferMapping(
                        @SerializedName("pageSlug")
                        val pageSlug: String,
                        @SerializedName("pageType")
                        val pageType: String
                    )

                    data class Price(
                        @SerializedName("lineOffers")
                        val lineOffers: List<LineOffer>,
                        @SerializedName("totalPrice")
                        val totalPrice: TotalPrice
                    ) {
                        data class LineOffer(
                            @SerializedName("appliedRules")
                            val appliedRules: List<AppliedRule>
                        ) {
                            data class AppliedRule(
                                @SerializedName("discountSetting")
                                val discountSetting: DiscountSetting,
                                @SerializedName("endDate")
                                val endDate: String,
                                @SerializedName("id")
                                val id: String
                            ) {
                                data class DiscountSetting(
                                    @SerializedName("discountType")
                                    val discountType: String
                                )
                            }
                        }

                        data class TotalPrice(
                            @SerializedName("currencyCode")
                            val currencyCode: String,
                            @SerializedName("currencyInfo")
                            val currencyInfo: CurrencyInfo,
                            @SerializedName("discount")
                            val discount: Int,
                            @SerializedName("discountPrice")
                            val discountPrice: Int,
                            @SerializedName("fmtPrice")
                            val fmtPrice: FmtPrice,
                            @SerializedName("originalPrice")
                            val originalPrice: Int,
                            @SerializedName("voucherDiscount")
                            val voucherDiscount: Int
                        ) {
                            data class CurrencyInfo(
                                @SerializedName("decimals")
                                val decimals: Int
                            )

                            data class FmtPrice(
                                @SerializedName("discountPrice")
                                val discountPrice: String,
                                @SerializedName("intermediatePrice")
                                val intermediatePrice: String,
                                @SerializedName("originalPrice")
                                val originalPrice: String
                            )
                        }
                    }

                    data class Seller(
                        @SerializedName("id")
                        val id: String,
                        @SerializedName("name")
                        val name: String
                    )

                    data class Tag(
                        @SerializedName("id")
                        val id: String
                    )
                }

                data class Paging(
                    @SerializedName("count")
                    val count: Int,
                    @SerializedName("total")
                    val total: Int
                )
            }
        }
    }
}