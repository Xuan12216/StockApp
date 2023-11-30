package com.mdbs.marbo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.content.res.AssetManager;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

@SuppressWarnings("JniMissingFunction")
public class MarboView extends GLTextureView
{
	GestureDetector mGestureDetector;
	ShowInfoListener mShowInfoListener = null;
	SlotInfoButtonClickListener mSlotInfoButtonClickListener = null;
	ScrollStateListener mScrollStateListener = null;
    int [] mTouches = new int[10];
	long mContextId = 0;
	int mScrollState = 0;

    // enum of chart types
    public static final int CT_CURVE_MA_SHORT = 0x01;
	public static final int CT_CURVE_MA_LONG = 0x02;
	public static final int CT_CURVE_BOLL = 0x04;
	public static final int CT_CURVE_MA_WEEK = 0x08;
	public static final int CT_BAR_VOLUME = 0x10;
	public static final int CT_CURVE_MACD = 0x20;
	public static final int CT_CURVE_KD = 0x40;
	public static final int CT_CURVE_RSI = 0x80;
	public static final int CT_BAR_NETBUYSELL_FOREIGN = 0x100;
	public static final int CT_BAR_NETBUYSELL_DOMESTIC = 0x200;
	public static final int CT_BAR_NETBUYSELL_MAJOR = 0x300;
	public static final int CT_BAR_NETBUYSELL_DEALER = 0x400;
    public static final int CT_BAR_NETBUYSELL_CUSTOM1 = 0x500;
    public static final int CT_BAR_NETBUYSELL_CUSTOM2 = 0x600;
    public static final int CT_BAR_NETBUYSELL_CUSTOM3 = 0x700;
	public static final int CT_BAR_NETBUYSELL_CUSTOM_MAX = 0xF00;
	public static final int CT_SHOW_VOLUME_MA = 0x1000;
	public static final int CT_SHOW_NETBUYSELL_HEDGE = 0x2000;
    public static final int CT_SHOW_BUBBLE = 0x4000;
	public static final int CT_CURVE_DMI = 0x8000;
	public static final int CT_SHOW_ICON = 0x10000;
	public static final int CT_BAR_MARGIN_BALANCE = 0x20000;
	public static final int	CT_CURVE_KDJ = 0x40000;
	public static final int CT_BAR_TOWER = 0x80000;
	public static final int CT_BAR_SHORT = 0x100000;
	public static final int CT_BAR_LENDING = 0x200000;
	public static final int CT_SHOW_NETBUYSELL_HOLDING = 0x400000;
	public static final int CT_BAR_BROKER_BUYSELL = 0x1000000;
	public static final int CT_CURVE_BIAS = 0x2000000;
	public static final int CT_CURVE_RAINBOW = 0x4000000;
	public static final int CT_BAR_STRENGTH_K = 0x8000000;
	public static final int CT_BAR_DAY_TRADING = 0x10000000;
	
	// enum of graph color types
	public static final int GC_UP = 0;
	public static final int GC_DOWN = 1;
	public static final int GC_FLAT = 2;
	public static final int GC_MAJOR_GRID = 3;
	public static final int GC_MINOR_GRID = 4;
	public static final int GC_BOLL = 5;
	public static final int GC_DIF = 6;
	public static final int GC_MACD = 7;
	public static final int GC_DIF_MACD = 8;
	public static final int GC_K9 = 9;
	public static final int GC_D9 = 10;
	public static final int GC_RSI5 = 11;
	public static final int GC_RSI10 = 12;
	public static final int GC_HEDGE = 13;
	public static final int GC_DI_PLUS = 14;
	public static final int GC_DI_MINUS = 15;
	public static final int GC_ADX = 16;
	public static final int GC_BOLL_FILL = 17;
	public static final int GC_CROSS_LINE = 18;
	public static final int GC_MARGIN_BALANCE = 19;
	public static final int GC_J9 = 20;
	public static final int GC_HOLDING = 21;
	public static final int GC_PRICE = 22;
	public static final int GC_BAND = 23;
	public static final int GC_BIAS = 24;
	public static final int GC_DAY_TRADING = 25;
	public static final int GC_DAY_TRADING_RATIO = 26;
	
	// enum of MA types
	public static final int MA_5 = 0;
	public static final int MA_10 = 1;
	public static final int MA_20 = 2;
	public static final int MA_60 = 3;
	public static final int MA_120 = 4;
	public static final int MA_240 = 5;
	public static final int MA_8W = 6;
	public static final int MA_21W = 7;
	public static final int MA_55W = 8;

	// enum of MA groups
	public static final int MA_GROUP_SHORT = 0;
	public static final int MA_GROUP_LONG = 1;
	public static final int MA_GROUP_WEEK = 2;

	// enum of DisplayDateFormat
	public static final int DD_FORMAT_yyyyMMddhhmm = 0;
	public static final int DD_FORMAT_MMddhhmm = 1;
	public static final int DD_FORMAT_MMdd = 2;
	public static final int DD_FORMAT_hhmm = 3;

	// enum of AutoCrossLineType
	public static final int	ACT_FREE = 0;
	public static final int ACT_FIT_CLOSE = 1;
	public static final int ACT_FIT_OPEN = 2;
	public static final int ACT_FIT_HIGH = 3;
	public static final int ACT_FIT_LOW = 4;

	// enum of DisplayOption
	public static final int DO_COLOR_PRICE = 0x01;
	public static final int DO_AUTOSCALE_CHART_PANEL = 0x02;
	public static final int DO_MA_WITH_SIGN = 0x04;
	public static final int DO_PRICE_WITH_SIGN = 0x08;
	public static final int DO_SCROLLABLE = 0x10;
	public static final int DO_FULL_SCREEN_SCROLL = 0x20;
	public static final int DO_HIDE_MAIN_CHART = 0x40;
	public static final int DO_PRETTY_FLOAT_VALUE = 0x80;
	
	// enum IconPosition
	public static final int IP_CENTER = 0;
	public static final int IP_ABOVE = 1;
	public static final int IP_BELOW = 2;
	
	public static final int MAX_NETBUYSELL_TYPES = 15;
	public static final int MAX_COST_TYPES = 15;
	public static final int MAX_BUBBLE_TYPES = 6;
	public static final int MAX_COST_FILL_TYPES = 15;
	public static final int MAX_ICON_TYPES = 15;
	
	// enum of ColumnIndex
	public static final int CI_TIME = 0;
	public static final int CI_OPEN = 1;
	public static final int CI_HIGH = 2;
	public static final int CI_LOW = 3;
	public static final int CI_CLOSE = 4;
	public static final int CI_CHANGE_P = 5;
	public static final int CI_VOLUME = 6;
	public static final int CI_K9 = 7;
	public static final int CI_D9 = 8;
	public static final int CI_MACD = 9;
	public static final int CI_DIF_MACD = 10;
	public static final int CI_DIF = 11;
	public static final int CI_BOLL_TOP = 12;
	public static final int CI_BOLL_BOTTOM = 13;
	public static final int CI_RSI5 = 14;
	public static final int CI_RSI10 = 15;
	public static final int CI_NETBUYSELL = 16;
	public static final int CT_HEDGE = (CI_NETBUYSELL + MAX_NETBUYSELL_TYPES);
	public static final int CI_COST = 46;
	public static final int CI_BUBBLE = 61;
	public static final int CI_DI_PLUS = 62;
	public static final int CI_DI_MINUS = 63;
	public static final int CI_ADX = 64;
	public static final int CI_ICON = 65;
	public static final int CI_MARGIN_BALANCE = 66;
	public static final int CI_MARGIN_CHANGE = 67;
	public static final int CI_SHORT_CHANGE = 68;
	public static final int CI_DAY_TRADING = 69;
	public static final int CI_SECURITY_LENDING = 70;
	public static final int CI_LENDING_BALANCE = 71;
	public static final int CI_SHORT_BALANCE = 72;
	public static final int CI_TOTAL_SHARES = 73;
	public static final int CI_SHARE_HOLDING = 74;
		
	// enum of DetailListType
	public static final int DLT_NONE = 0;
	public static final int DLT_NETBUYSELL = 1;
	public static final int DLT_MARGIN = 2;
	public static final int DLT_BROKER_BUYSELL = 3;
	
	// enum of AuroraType
	public static final int AURORA_TYPE_DEFAULT = 0;
	public static final int AURORA_TYPE_SHORT = 1;
	public static final int AURORA_TYPE_LONG = 2;

	// enum of VolumeColorRule
	public static final int VOLUME_COLOR_BY_CANDLE = 0;
	public static final int VOLUME_COLOR_BY_PRICE = 1;
	public static final int VOLUME_COLOR_BY_VOLUME = 2;
	
	// enum of CostInfoStyle
	public static final int CIS_NORMAL = 0x00;
	public static final int CIS_NO_VALUE = 0x01;
	public static final int CIS_COMPARE_CANDLE_SIGN = 0x02;
	public static final int CIS_COMPARE_PREVIOUS_SIGN = 0x04;
	public static final int CIS_COMPARE_SIGN_INVERSE = 0x08;

	// enum of ScrollState
	public static final int SS_NONE = 0;
	public static final int SS_MOVING_UP = 1;
	public static final int SS_MOVING_DOWN = 2;
	public static final int SS_OVER_UP = 3;
	public static final int SS_OVER_DOWN = 4;
	
	class MyGestureListener extends GestureDetector.SimpleOnGestureListener
	{
		@Override
		public void onLongPress(MotionEvent e)
		{
			super.onLongPress(e);
			MarboApp_OnLongPress(mContextId);
			requestRender();
		}
	}

	public class MarboViewRenderer implements GLSurfaceView.Renderer 
	{
		boolean mInited = false;

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config)
		{
		}
		
		@Override
		public void onDrawFrame(GL10 gl) 
		{
			MarboApp_draw(mContextId);
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) 
		{
			if (!mInited)
            {
				MarboApp_init(mContextId, width, height);
                mInited = true;
            }
            else
            {
                MarboApp_DeviceReset(mContextId);
                MarboApp_DeviceRestore(mContextId);
                MarboApp_ChangeClientSize(mContextId, width, height);
            }

			MarboApp_BackgroundColor(mContextId, ArgbToAbgr(getBackgroundColor()));
		}
	};
	
	public interface ShowInfoListener {
        void onReceive(String text);
    }
	public interface SlotInfoButtonClickListener {
		void onClick(int slot_index);
	}
	public interface ScrollStateListener {
		void onChange(int state);
	}

	public MarboView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(!isInEditMode()) initView(context);
	}

	public MarboView(Context context) {
		super(context);
		if(!isInEditMode()) initView(context);
	}
	
	public void initView(Context context)
	{
		mContextId = JNIInit(context.getResources().getAssets(), context.getFilesDir().getAbsolutePath());

		//setEGLConfigChooser(false);
		//setEGLContextClientVersion(2);
		setRenderer(new MarboViewRenderer());
		setRenderMode(RENDERMODE_WHEN_DIRTY);

		setLongClickable(true);
		mGestureDetector = new GestureDetector(context, new MyGestureListener());

		for (int i = 0; i < 10; i++)
		{
			mTouches[i] = 0;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		MarboApp_deinit(mContextId);
		super.finalize();
	}

	@Override
	public boolean onTouchEvent(MotionEvent evt)
	{
		int count = evt.getPointerCount();
		int index = evt.getActionIndex();
		int pointerId = evt.getPointerId(index) + 1;
		int touch_id;
	
		switch(evt.getActionMasked())
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				touch_id = findTouchIndex(0);
				if (touch_id != -1)
				{
					mTouches[touch_id] = pointerId;
					MarboApp_OnTouchBegin(mContextId, touch_id, (int)evt.getX(index), (int)evt.getY(index));
					mScrollState = SS_NONE;
				}

				//System.out.println("OnTouchBegin: touch_id=" + touch_id + " count=" + count);
				requestRender();

				break;

			case MotionEvent.ACTION_MOVE:
				for (int i = 0; i < count; i++)
				{
					pointerId = evt.getPointerId(i) + 1;
					touch_id = findTouchIndex(pointerId);
					if (touch_id != -1)
					{
						MarboApp_OnTouchMove(mContextId, touch_id, (int)evt.getX(i), (int)evt.getY(i));

						int state = MarboApp_GetClientScrollState(mContextId);
						if(mScrollState != state)
						{
							mScrollState = state;
							if(mScrollStateListener != null) mScrollStateListener.onChange(state);
						}
					}

					//System.out.println("OnTouchMove: touch_id=" + touch_id + " count=" + count);
				}
				requestRender();

				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:
				touch_id = findTouchIndex(pointerId);
				if (touch_id != -1)
				{
					mTouches[touch_id] = 0;
					MarboApp_OnTouchEnd(mContextId, touch_id, (int)evt.getX(index), (int)evt.getY(index));
					mScrollState = SS_NONE;
				}
				//System.out.println("OnTouchEnd: touch_id=" + touch_id + " count=" + count);
				requestRender();
				break;
		}

		mGestureDetector.onTouchEvent(evt);

		return true;
	}

	private int findTouchIndex(int pointerId)
	{
		for (int i = 0; i < 10; i++)
		{
			if (pointerId == mTouches[i])
			{
				return i;
			}
		}

		return -1;
	}
	
	public void LoadChartData(String json, boolean reset)
	{
		if(reset) MarboApp_ChartClear(mContextId, json == null || json.isEmpty());
		MarboApp_ChartLoadAll(mContextId, json);
		requestRender();
	}
	
	public void LoadBrokerData(String json)
	{
		MarboApp_ChartLoadBroker(mContextId, json);
		requestRender();
	}

	public void LoadRainbowData(String json)
	{
		MarboApp_ChartLoadRainbow(mContextId, json);
		requestRender();
	}
	
	public void LoadTrendData(String json, boolean reset)
	{
		if(reset) MarboApp_ChartClear(mContextId, json == null || json.isEmpty());
		MarboApp_ChartLoadTrend(mContextId, json);
		requestRender();
	}
	
	public void SetChartTypes(int types)
	{
		MarboApp_ChartTypes(mContextId, types);
	}
	
	public void SetChartSlotTypes(int [] types)
	{
		MarboApp_ChartSlotTypes(mContextId, types);
	}
	
	public void SetChartSlotStyle(int slot_index, int height, boolean show_info)
	{
		MarboApp_ChartSlotStyle(mContextId, slot_index, height, show_info);
	}

	public void SetChartColor(int type, int color)
	{
		MarboApp_ChartColor(mContextId, type, ArgbToAbgr(color));
	}
	
	public void SetMAColor(int type, int color)
	{
		MarboApp_MAColor(mContextId, type, ArgbToAbgr(color));
	}

	public void SetMAGroupTypes(int group, int type1, int type2, int type3)
	{
		MarboApp_MAGroupTypes(mContextId, group, type1, type2, type3);
	}
	
	public void SetMACandles(int type, int count)
	{
		MarboApp_MACandles(mContextId, type, count);
	}

	public void SetDisplayDateFormat(int major_format, int minor_format)
	{
		MarboApp_DisplayDateFormat(mContextId, major_format, minor_format);
	}

	public void SetAutoCrossLineType(int type)
	{
		MarboApp_AutoCrossLineType(mContextId, type);
	}
	
	public void SetDisplayOptions(int options)
	{
		MarboApp_DisplayOptions(mContextId, options);
	}
	
	public void RemapChartColumnIndex(int column_index, int new_index)
	{
		MarboApp_RemapChartColumnIndex(mContextId, column_index, new_index);
	}
	
	public void SetTrendYScale(float scale)
	{
		MarboApp_TrendYScale(mContextId, scale);
	}
	
	public void SetTrendAutoYScale(boolean enabled, float limit_percentage)
	{
		MarboApp_TrendAutoYScale(mContextId, enabled, limit_percentage);
	}
	
	public void SetTrendTimeSpan(int start_hour, int start_min, int total_minutes)
	{
		MarboApp_TrendTimeSpan(mContextId, start_hour, start_min, total_minutes);
	}

	public void SetBackgroundColor(int color)
	{
		MarboApp_BackgroundColor(mContextId, ArgbToAbgr(color));
		setBackgroundColor(color);
	}
	
	public void SetCostTypes(int types)
	{
		MarboApp_CostTypes(mContextId, types);
	}
	
	public void SetSlotCostTypes(int [] types)
	{
		MarboApp_SlotCostTypes(mContextId, types);
	}	

	public void SetCostColor(int cost_index, int color)
	{
		MarboApp_CostColor(mContextId, cost_index, ArgbToAbgr(color));
	}
	
	public void SetCostFillColor(int fill_id, int cost_index1, int cost_index2, int color)
	{
		MarboApp_CostFillColor(mContextId, fill_id, cost_index1, cost_index2, ArgbToAbgr(color));
	}
	
	public void SetCostDashLength(int cost_index, float length)
	{
		MarboApp_CostDashLength(mContextId, cost_index, length);
	}
	
	public void SetCostInfoStyles(int cost_index, int styles)
	{
		MarboApp_CostInfoStyles(mContextId, cost_index, styles);
	}
	
	public void SetBubbleStyle(int type, int color, float scale)
	{
		MarboApp_SetBubbleStyle(mContextId, type, ArgbToAbgr(color), scale);
	}
	
	public void SetIconStyle(int type, String icon_path, float scale, int pos, int ref_column)
	{
		MarboApp_SetIconStyle(mContextId, type, icon_path, scale, pos, ref_column);
	}
	
	public void SetDetailListType(int type)
	{
		MarboApp_SetDetailListType(mContextId, type);
	}

	public void SetVolumeColorRule(int rule)
	{
		MarboApp_SetVolumeColorRule(mContextId, rule);
	}
	
	public void SetDefaultCandlesInView(int num)
	{
		MarboApp_SetDefaultCandlesInView(mContextId, num);
	}
	
	public void SetTowerAvgDays(int num)
	{
		MarboApp_SetTowerAvgDays(mContextId, num);
	}
	
	public void SetBandInterval(String start_time, String end_time)
	{
		MarboApp_SetBandInterval(mContextId, start_time, end_time);
	}
	
	public void AuroraGroup(int type, int start_day, int end_day, int color)
	{
		MarboApp_AuroraGroup(mContextId, type, start_day, end_day, ArgbToAbgr(color));
	}
	
	public void SetAuroraTypes(int types)
	{
		MarboApp_SetAuroraTypes(mContextId, types);
	}
	
	public void SetBiasAvgDays(int num)
	{
		MarboApp_SetBiasAvgDays(mContextId, num);
	}
	
	public void SetStrengthMACandles(int ma_short, int ma_long)
	{
		MarboApp_SetStrengthMACandles(mContextId, ma_short, ma_long);
	}
	
	public void SetVisibleSlotInfoButtons(int flags)
	{
		MarboApp_SetVisibleSlotInfoButtons(mContextId, flags);
	}
	
	public void LoadReportData(String jsonMonthIncome, String jsonSeason, String jsonYearBonus, 
		String jsonSeasonBonus, String jsonMonthCandle, String lastCloseDate, float lastClosePrice)
	{
		MarboApp_ChartClear(mContextId, true);
		MarboApp_LoadReport(mContextId, jsonMonthIncome, jsonSeason, jsonYearBonus, jsonSeasonBonus, jsonMonthCandle, 
			lastCloseDate, lastClosePrice);
		requestRender();
	}
	
	public void LoadRiverData(String jsonKDays, String jsonSeason)
	{
		MarboApp_ChartClear(mContextId, true);
		MarboApp_LoadRiver(mContextId, jsonKDays, jsonSeason);
		requestRender();
	}
	
	public void LoadOptionData(String json, boolean reset)
	{
		if(reset) MarboApp_ChartClear(mContextId, json == null || json.isEmpty());
		MarboApp_LoadOptionData(mContextId, json);
		requestRender();
	}
	
	public void SetOptionParams(int candlesInView, boolean zoomable)
	{
		MarboApp_SetOptionParams(mContextId, candlesInView, zoomable);
	}
	
	public void LoadMajorShareHolderData(String jsonWeekData, String jsonShareholderDistrib, String jsonSupervisors,
		String jsonShareholderTopList)
	{
		MarboApp_ChartClear(mContextId, true);
		MarboApp_LoadMajorShareHolderData(mContextId, jsonWeekData, jsonShareholderDistrib, jsonSupervisors, jsonShareholderTopList);
		requestRender();
	}

	public void SetAutoBoldFont(boolean enabled, int minFontSize)
	{
		MarboApp_SetAutoBoldFont(enabled, minFontSize);
		requestRender();
	}
	
	public int GetClientScrollState()
	{
		return MarboApp_GetClientScrollState(mContextId);
	}
	
	public static int GetBuildNumber()
	{
		return MarboApp_GetBuildNumber();
	}
	
	public void SetShowInfoListener(ShowInfoListener listener)
	{
        mShowInfoListener = listener;
    }

	public void ShowInfo(String text)
	{
		if(mShowInfoListener != null) mShowInfoListener.onReceive(text);
	}

	public void SetSlotInfoButtonClickListener(SlotInfoButtonClickListener listener)
	{
		mSlotInfoButtonClickListener = listener;
	}

	public void SlotInfoButtonClick(int slot_index)
	{
		if(mSlotInfoButtonClickListener != null) mSlotInfoButtonClickListener.onClick(slot_index);
	}

	public void SetScrollStateListener(ScrollStateListener listener)
	{
		mScrollStateListener = listener;
	}

	private int ArgbToAbgr(int argb)
	{
		return (argb & 0xFF00FF00) |
			((argb >> 16) & 0xFF) | // Red  -> Blue
			((argb & 0xFF) << 16); // Blue -> Red
	}

	native long JNIInit(AssetManager mgr, String filesDir);
	native void MarboApp_init(long contextId, int width, int height);
	native void MarboApp_draw(long contextId);
	native void MarboApp_deinit(long contextId);
	native void MarboApp_DeviceReset(long contextId);
	native void MarboApp_DeviceRestore(long contextId);
	native void MarboApp_ChangeClientSize(long contextId, int width, int height);
	native void MarboApp_OnTouchBegin(long contextId, int touch_id, int x, int y);
	native void MarboApp_OnTouchEnd(long contextId, int touch_id, int x, int y);
	native void MarboApp_OnTouchMove(long contextId, int touch_id, int x, int y);
	native void MarboApp_OnLongPress(long contextId);
	native void MarboApp_ChartClear(long contextId, boolean resetView);
	native void MarboApp_ChartLoadAll(long contextId, String strJson);
	native void MarboApp_ChartLoadBroker(long contextId, String strJson);
	native void MarboApp_ChartLoadRainbow(long contextId, String strJson);
	native void MarboApp_ChartLoadTrend(long contextId, String strJson);
	native void MarboApp_ChartTypes(long contextId, int types);
	native void MarboApp_ChartSlotTypes(long contextId, int [] types);
	native void MarboApp_ChartSlotStyle(long contextId, int slot_index, int height, boolean show_info);
	native void MarboApp_ChartColor(long contextId, int type, int color);
	native void MarboApp_MAColor(long contextId, int type, int color);
	native void MarboApp_MAGroupTypes(long contextId, int group, int type1, int type2, int type3);
	native void MarboApp_MACandles(long contextId, int type, int count);
	native void MarboApp_DisplayDateFormat(long contextId, int major_format, int minor_format);
	native void MarboApp_AutoCrossLineType(long contextId, int type);
	native void MarboApp_DisplayOptions(long contextId, int options);
	native void MarboApp_RemapChartColumnIndex(long contextId, int column_index, int new_index);
	native void MarboApp_TrendYScale(long contextId, float scale);
	native void MarboApp_TrendAutoYScale(long contextId, boolean enabled, float limit_percentage);
	native void MarboApp_TrendTimeSpan(long contextId, int start_hour, int start_min, int total_minutes);
	native void MarboApp_BackgroundColor(long contextId, int color);
	native void MarboApp_CostTypes(long contextId, int types);
	native void MarboApp_SlotCostTypes(long contextId, int [] types);
	native void MarboApp_CostColor(long contextId, int cost_index, int color);
	native void MarboApp_CostFillColor(long contextId, int fill_id, int cost_index1, int cost_index2, int color);
	native void MarboApp_CostDashLength(long contextId, int cost_index, float length);
	native void MarboApp_CostInfoStyles(long contextId, int cost_index, int styles);
	native void MarboApp_SetBubbleStyle(long contextId, int type, int color, float scale);
	native void MarboApp_SetIconStyle(long contextId, int type, String icon_path, float scale, int pos, int ref_column);
	native void MarboApp_SetDetailListType(long contextId, int type);
	native void MarboApp_SetVolumeColorRule(long contextId, int rule);
	native void MarboApp_SetDefaultCandlesInView(long contextId, int num);
	native void MarboApp_SetTowerAvgDays(long contextId, int num);
	native void MarboApp_SetBandInterval(long contextId, String start_time, String end_time);
	native void MarboApp_AuroraGroup(long contextId, int type, int start_day, int end_day, int color);
	native void MarboApp_SetAuroraTypes(long contextId, int types);
	native void MarboApp_SetBiasAvgDays(long contextId, int num);
	native void MarboApp_SetStrengthMACandles(long contextId, int ma_short, int ma_long);
	native void MarboApp_SetVisibleSlotInfoButtons(long contextId, int flags);
	native void MarboApp_LoadReport(long contextId, String jsonMonthIncome, String jsonSeason, String jsonYearBonus,
		String jsonSeasonBonus, String jsonMonthCandle, String lastCloseDate, float lastClosePrice);
	native void MarboApp_LoadRiver(long contextId, String jsonKDays, String jsonSeason);
	native void MarboApp_LoadOptionData(long contextId, String strJson);
	native void MarboApp_SetOptionParams(long contextId, int candlesInView, boolean zoomable);
	native void MarboApp_LoadMajorShareHolderData(long contextId, String jsonWeekData, String jsonShareholderDistrib, 
		String jsonSupervisors, String jsonShareholderTopList);
	native void MarboApp_SetAutoBoldFont(boolean enabled, int minFontSize);
	native int MarboApp_GetClientScrollState(long contextId);
	static native int MarboApp_GetBuildNumber();
}

