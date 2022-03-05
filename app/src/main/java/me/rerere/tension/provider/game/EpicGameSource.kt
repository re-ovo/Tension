package me.rerere.tension.provider.game

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.rerere.tension.provider.Category
import me.rerere.tension.provider.DataProvider
import me.rerere.tension.ui.components.LoadingAnimation
import me.rerere.tension.ui.modifier.coilShimmer
import me.rerere.tension.util.DataState
import me.rerere.tension.util.Display
import me.rerere.tension.util.parseIso8601TimeToLocalTime
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EpicGameSource : DataProvider {
    override fun getId(): String = "epic_free_games"

    override fun getDisplayName(): String = "Epic 免费游戏"

    override fun getCategory(): Category = Category.GAME

    @Composable
    override fun getPreviewUI(navigator: DestinationsNavigator): @Composable () -> Unit = {
        val viewModel = viewModel<EpicGameSourceViewModel>()
        val freeGames by viewModel.freeGames.collectAsState()
        freeGames.Display(
            onLoading = {
                LoadingAnimation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }
        ) { gameList ->
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(
                    gameList.data.catalog.searchStore.elements.filter {
                        !it.promotions.isJsonNull
                    }
                ){
                    FreeGame(it)
                }
            }
        }
    }

    @Composable
    override fun getDetailUI(navigator: DestinationsNavigator): @Composable () -> Unit {
        TODO("Not yet implemented")
    }

    @Composable
    fun FreeGame(element: GameList.Data.Catalog.SearchStore.Element) {
        val context = LocalContext.current
        Column(
            modifier = Modifier.clickable {
                 // Open Url
                 context.startActivity(
                     Intent(Intent.ACTION_VIEW, Uri.parse("https://www.epicgames.com/store/zh-CN/p/${element.urlSlug}"))
                 )
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Preview Image
            val imagePainter = rememberImagePainter(
                data = element.keyImages.firstOrNull {
                    it.type.startsWith("Code")
                }?.url ?: element.keyImages.last().url
            )
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .coilShimmer(imagePainter)
                    .heightIn(max = 120.dp)
                    .width(150.dp),
                painter = imagePainter,
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            // Title
            Text(
                text = element.title.trim(),
                style = MaterialTheme.typography.bodyLarge
            )
            println(element.promotions)
            // Date
            element.promotions.asJsonObject?.let { promotions ->
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

/// region: model
data class GameList(@SerializedName("data") val `data`: Data) {
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
/// endregion