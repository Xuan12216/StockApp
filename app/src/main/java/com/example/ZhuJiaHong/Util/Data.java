package com.example.ZhuJiaHong.Util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LifecycleOwner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import okhttp3.WebSocket;

public class Data {
    private static String expire_time = "";
    private static String productId = "20220427115113869230";
    public static String deviceType = "A";
    private static String deviceToken = "";
    public static String deviceUUID = String.valueOf(UUID.randomUUID());
    private static String memberToken = "";
    private static String memberId = "";
    public static String message = "";
    public static String txid = "GLX_O_01";
    public static String txid_18 = "GLX_O_18";
    public static String txid_23 = "GLX_O_23";
    public static String txid_24 = "GLX_O_24";
    public static String loginServerUrl = "https://tomcat85-5.mdevelop.com/GalaxyApi/dataTrans.do";
    public static String webSocketServerUrl = "wss://gcp-owl-data-center-test.data-bee.com/owl2-realtime/portfolio/websocket/";
    public static String webSocketServerUrl_taifex = "wss://gcp-owl2-test-taifex-starwave-websocket.data-bee.com/owl2-taifex/portfolio/websocket/";
    public static String webSocketApi = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/auth";
    public static String DataControllerApi = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/data/BU25-15023b";
    public static String DataController_Qi_Api = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/data/E4-13028b";
    public static String industryName_Api = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/data/BU25-14693b";
    public static String sortStockNoApi = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/stock/quote";
    public static String drawPlateApi = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/stock/sellBuy";
    public static String upDownCountApi = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/realtime/upDownCount";
    public static String monetFlowApi = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/realtime/moneyFlow";
    public static String financingBalanceApi = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/data/BU1-10002a/Y9999/1";
    public static String foreignLetterSelfTotalApi = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/data/BU1-10005b";
    public static String industryApi = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/sc/industry";
    public static String groupSharesApi = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/sc/groupShares";
    public static String conceptAPi = "https://gcp-owl-data-center-test.data-bee.com/owl2-api/api/sc/concept";
    public static String appId = "20220425110826803";
    public static String appSecret = "f9c3c620c44411eca4dd00090ffe0001";
    public static String StrategyApi = "https://tomcat85-5.mdevelop.com/tradingDisciplinesApi/";
    public static String appId_strategy = "1660268702927";
    public static String appSecret_strategy = "6263cdb019e011ed81b24ae7daaa2639";
    private static String token = "";
    private static String tokenStrategy = "";
    private static WebSocket webSocket1 = null;
    private static WebSocket webSocket2 = null;
    private static WebSocket webSocket3 = null;
    private static JSONArray groupListArray = null;//Page1Fragment自選組的groupList資料
    private static JSONArray stockListName = null;//1.1.1.2自選列表，自選股股名、股票資訊
    private static JSONArray stockQi = null;//1.1.1.4取得期貨清單
    private static JSONObject sortStockNo = null;//1.1.1.5排序邏輯漲跌、量的儲存地方
    private static JSONObject allStockForDrawPlate = null; //
    private static boolean isConnectToWebsocket = false;
    private static String oriStockList = "";
    private static HashSet<String> stockCodeSet = null;
    private static HashSet<String> stockCodeSet1 = null;
    private static HashMap<String, Stock> stockMap = null;
    private static HashMap<String,String> industryName = null;
    private static String updateDate = "";
    private static List<Stock> RecyclerViewCurrentStockList = new ArrayList<>();
    private static List<Stock> RecyclerViewCurrentStockListPage1 = new ArrayList<>();
    private static List<Stock> RecyclerViewCurrentStockListPage2 = new ArrayList<>();
    private static List<Stock> RecyclerViewCurrentStockListSearchView = new ArrayList<>();
    private static HashMap<String, double[]> plateDataHashMap = new HashMap<>();
    private static String click_zhang = "zhang_no";
    private static String click_liang = "liang_no";
    private static String click_qi = "qi_no";
    private static int positionOnResume = 0;
    private static String ranking = "add";
    private static String categories = "全部";
    private static String strategy = "波段";
    private static String strategy_1 = "頭高底高";
    private static String priceFilter = "全部";
    private static LifecycleOwner lifecycleOwner;
    private static final String PREF_NAME = "FlexBoxClickStatus";
    private static final String KEY_IS_CLICKED_PREFIX = "isClicked_";


    //==============================================================================

    public static void saveClickStatus(Context context, String flexBoxId, String textViewId, boolean isClicked) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getPrefKey(flexBoxId, textViewId), isClicked);
        editor.apply();
    }

    public static boolean loadClickStatus(Context context, String flexBoxId, String textViewId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(getPrefKey(flexBoxId, textViewId), false);
    }

    private static String getPrefKey(String flexBoxId, String textViewId) {
        return KEY_IS_CLICKED_PREFIX + flexBoxId + "_" + textViewId;
    }

    public LifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        Data.lifecycleOwner = lifecycleOwner;
    }

    public void resetData(Context context) {
        RecyclerViewCurrentStockListPage1 = null;
        RecyclerViewCurrentStockListPage2 = null;
        RecyclerViewCurrentStockList = null;
        RecyclerViewCurrentStockListSearchView = null;
        oriStockList = "";
        sortStockNo = null;
        expire_time = "";
        deviceToken = "";
        memberToken = "";
        memberId = "";
        message = "";
        token = "";
        tokenStrategy = "";
        webSocket1 = checkWebsocketAndClose(webSocket1);
        webSocket2 = checkWebsocketAndClose(webSocket2);
        webSocket3 = checkWebsocketAndClose(webSocket3);
        groupListArray = null;
        stockListName = null;
        allStockForDrawPlate = null;
        isConnectToWebsocket = false;
        stockCodeSet.clear();
        stockCodeSet1.clear();
        stockMap.clear();
        updateDate = "";
        plateDataHashMap.clear();
        resetSharedPreferences("MyPrefs",context);
        click_zhang = "zhang_no";
        click_liang = "liang_no";
        click_qi = "qi_no";
        positionOnResume = 0;
        ranking = "add";
        categories = "全部";
        strategy = "波段";
        strategy_1 = "頭高底高";
        priceFilter = "全部";
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        Data.strategy = strategy;
    }

    public String getStrategy_1() {
        return strategy_1;
    }

    public void setStrategy_1(String strategy_1) {
        Data.strategy_1 = strategy_1;
    }

    public String getPriceFilter() {
        return priceFilter;
    }

    public void setPriceFilter(String priceFilter) {
        Data.priceFilter = priceFilter;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        Data.ranking = ranking;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        Data.categories = categories;
    }

    public int getPositionOnResume() {
        return positionOnResume;
    }

    public void setPositionOnResume(int positionOnResume) {
        Data.positionOnResume = positionOnResume;
    }

    public String getClick_zhang() {
        return click_zhang;
    }

    public void setClick_zhang(String click_zhang) {
        Data.click_zhang = click_zhang;
    }

    public String getClick_liang() {
        return click_liang;
    }

    public void setClick_liang(String click_liang) {
        Data.click_liang = click_liang;
    }

    public String getClick_qi() {
        return click_qi;
    }

    public void setClick_qi(String click_qi) {
        Data.click_qi = click_qi;
    }

    private void resetSharedPreferences(String string, Context context) {
        if (context != null){
            SharedPreferences sharedPreferences = context.getSharedPreferences(string, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    private WebSocket checkWebsocketAndClose(WebSocket webSocket) {
        if (webSocket != null){
            webSocket.send("DISCONNECT\nreceipt:99\n\n\0");
        }
        return null;
    }

    public JSONObject getAllStockForDrawPlate() {
        return allStockForDrawPlate;
    }

    public void setAllStockForDrawPlate(JSONObject allStockForDrawPlate) {
        Data.allStockForDrawPlate = allStockForDrawPlate;
    }

    public List<Stock> getRecyclerViewCurrentStockListSearchView() {
        return RecyclerViewCurrentStockListSearchView;
    }

    public void setRecyclerViewCurrentStockListSearchView(List<Stock> recyclerViewCurrentStockListSearchView) {
        RecyclerViewCurrentStockListSearchView = recyclerViewCurrentStockListSearchView;
    }

    public List<Stock> getRecyclerViewCurrentStockListPage2() {
        return RecyclerViewCurrentStockListPage2;
    }

    public void setRecyclerViewCurrentStockListPage2(List<Stock> recyclerViewCurrentStockListPage2) {
        RecyclerViewCurrentStockListPage2 = recyclerViewCurrentStockListPage2;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        Data.expire_time = expire_time;
    }

    public List<Stock> getRecyclerViewCurrentStockListPage1() {
        return RecyclerViewCurrentStockListPage1;
    }

    public void setRecyclerViewCurrentStockListPage1(List<Stock> recyclerViewCurrentStockListPage1) {
        RecyclerViewCurrentStockListPage1 = recyclerViewCurrentStockListPage1;
    }
    public WebSocket getWebSocket3() {
        return webSocket3;
    }

    public void setWebSocket3(WebSocket webSocket3) {
        Data.webSocket3 = webSocket3;
    }

    public String getTokenStrategy() {
        return tokenStrategy;
    }

    public void setTokenStrategy(String tokenStrategy) {
        Data.tokenStrategy = tokenStrategy;
    }

    public String getStrategyApi() {
        return StrategyApi;
    }

    public String getAppId_strategy() {
        return appId_strategy;
    }

    public String getAppSecret_strategy() {
        return appSecret_strategy;
    }

    public HashMap<String, double[]> getPlateDataHashMap() {
        return plateDataHashMap;
    }

    public void setPlateDataHashMap(HashMap<String, double[]> plateDataHashMap) {
        Data.plateDataHashMap = plateDataHashMap;
    }

    public List<Stock> getRecyclerViewCurrentStockList() {
        return RecyclerViewCurrentStockList;
    }

    public void setRecyclerViewCurrentStockList(List<Stock> recyclerViewCurrentStockList) {
        RecyclerViewCurrentStockList = recyclerViewCurrentStockList;
    }

    public String getIndustryApi() {
        return industryApi;
    }

    public String getGroupSharesApi() {
        return groupSharesApi;
    }

    public String getConceptAPi() {
        return conceptAPi;
    }

    public String getForeignLetterSelfTotalApi() {
        return foreignLetterSelfTotalApi;
    }

    public String getFinancingBalanceApi() {
        return financingBalanceApi;
    }

    public String getMonetFlowApi() {
        return monetFlowApi;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        Data.updateDate = updateDate;
    }

    public String getUpDownCountApi() {
        return upDownCountApi;
    }

    public HashMap<String, String> getIndustryName() {
        return industryName;
    }

    public void setIndustryName(HashMap<String, String> industryName) {
        Data.industryName = industryName;
    }

    public String getIndustryName_Api() {
        return industryName_Api;
    }

    public String getDrawPlateApi() {
        return drawPlateApi;
    }

    public HashSet<String> getStockCodeSet1() {
        return stockCodeSet1;
    }

    public void setStockCodeSet1(HashSet<String> stockCodeSet1) {
        Data.stockCodeSet1 = stockCodeSet1;
    }

    public HashMap<String, Stock> getStockMap() {
        return stockMap;
    }

    public void setStockMap(HashMap<String, Stock> stockMap) {
        Data.stockMap = stockMap;
    }

    public HashSet<String> getStockCodeSet() {
        return stockCodeSet;
    }

    public void setStockCodeSet(HashSet<String> stockCodeSet) {
        Data.stockCodeSet = stockCodeSet;
    }

    public String getOriStockList() {
        return oriStockList;
    }

    public void setOriStockList(String oriStockList) {
        Data.oriStockList = oriStockList;
    }

    public boolean getIsConnectToWebsocket() {
        return isConnectToWebsocket;
    }

    public void setIsConnectToWebsocket(boolean isConnectToWebsocket) {
        Data.isConnectToWebsocket = isConnectToWebsocket;
    }

    public JSONObject getSortStockNo() {
        return sortStockNo;
    }

    public void setSortStockNo(JSONObject sortStockNo) {
        Data.sortStockNo = sortStockNo;
    }

    public String getSortStockNoApi() {
        return sortStockNoApi;
    }

    public String getDataController_Qi_Api() {
        return DataController_Qi_Api;
    }

    public JSONArray getStockQi() {return stockQi;}

    public void setStockQi(JSONArray stockQi) {Data.stockQi = stockQi;}

    public JSONArray getStockListName() {return stockListName;}

    public void setStockListName(JSONArray stockListName) {Data.stockListName = stockListName;}

    public String getDataControllerApi() {
        return DataControllerApi;
    }

    public String getTxid_24() {
        return txid_24;
    }

    public JSONArray getGroupListArray() {
        return groupListArray;
    }

    public void setGroupListArray(JSONArray groupListArray) {
        Data.groupListArray = groupListArray;
    }

    public  String getTxid_23() {return txid_23;}

    public void setProductId(String productId) {
        Data.productId = productId;
    }

    public WebSocket getWebSocket1() {
        return webSocket1;
    }

    public void setWebSocket1(WebSocket webSocket1) {
        Data.webSocket1 = webSocket1;
    }

    public WebSocket getWebSocket2() {
        return webSocket2;
    }

    public void setWebSocket2(WebSocket webSocket2) {
        Data.webSocket2 = webSocket2;
    }

    public String getToken() {
        return token;
    }

    public String getWebSocketServerUrl_taifex() {
        return webSocketServerUrl_taifex;
    }

    public void setToken(String token) {
        Data.token = token;
    }

    public String getTxid_18() {return txid_18;}
    public String getWebSocketApi() {
        return webSocketApi;
    }

    public String getLoginServerUrl() {
        return loginServerUrl;
    }

    public String getWebSocketServerUrl() {
        return webSocketServerUrl;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getMemberToken() {
        return memberToken;
    }

    public void setMemberToken(String memberToken) {
        this.memberToken = memberToken;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getProductId() {
        return productId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public String getTxid() {
        return txid;
    }
}
