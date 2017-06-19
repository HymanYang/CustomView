package com.example.yzhou.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


/**
 * 
 * 云集价格显示view，
 * 
 * @author Yangzhou
 * 
 *         2016-7-18下午2:43:27
 */
public class YJMoneyView extends View {

	/** 价格前缀文本 */
	private String mPreText;
	private int mPreTextColor;
	private int mPreTextSize;
	private Rect mPreBound;
	private Paint mPrePaint;

	/** 文本 */
	private String mText;
	/** 文本的颜色 */
	private int mTextColor;
	/** 文本的大小 */
	private int mTextSize;
	/** 绘制时控制文本绘制的范围 */
	private Rect mBound;
	private Paint mPaint;

	/** 小数点后的字大小 */
	private int mPointSize;
	private Rect mPointBound;
	private Paint mPointPaint;
	private String mPointTxt;

	/** 金钱单位的符号 */
	private int mUnitColor;
	private int mUnitSize;
	private Rect mUnitBound;
	private Paint mUnitPaint;
	private String mUnitTxt;

	private int mUnitToMoneyPadding;

	private Context mContext;
	private boolean mEndStatue = false;

	public YJMoneyView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public YJMoneyView(Context context) {
		this(context, null);
	}

	/**
	 * 获得我自定义的样式属性
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public YJMoneyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.mContext = context;
		/**
		 * 获得我们所定义的自定义样式属性
		 */
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.YJMoneyView, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.YJMoneyView_preText:
				mPreText = a.getString(attr);
				break;
			case R.styleable.YJMoneyView_preTextColor:
				mPreTextColor = a.getColor(attr, Color.BLACK);
				break;
			case R.styleable.YJMoneyView_preTextSize:
				mPreTextSize = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
								getResources().getDisplayMetrics()));
				break;
			case R.styleable.YJMoneyView_moneyText:
				mText = a.getString(attr);
				break;
			case R.styleable.YJMoneyView_moneyTextColor:
				// 默认颜色设置为黑色
				mTextColor = a.getColor(attr, Color.BLACK);
				break;
			case R.styleable.YJMoneyView_moneyTextSize:
				// 默认设置为16sp，TypeValue也可以把sp转化为px
				mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
								getResources().getDisplayMetrics()));
				break;
			case R.styleable.YJMoneyView_ponitTextSize:
				mPointSize = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
								getResources().getDisplayMetrics()));
				break;
			case R.styleable.YJMoneyView_unitTextColor:
				// 默认颜色设置为黑色
				mUnitColor = a.getColor(attr, Color.BLACK);
				break;
			case R.styleable.YJMoneyView_unitTextSize:
				// 默认设置为16sp，TypeValue也可以把sp转化为px
				mUnitSize = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
								getResources().getDisplayMetrics()));
				break;
			}

		}
		a.recycle();

		init();

	}

	private void init() {

		mUnitToMoneyPadding = Utils.dip2px(mContext, 3);

		mPrePaint = new Paint();
		mPrePaint.setTextSize(mPreTextSize);
		mPrePaint.setColor(mPreTextColor);
		mPrePaint.setAntiAlias(true);
		mPreBound = new Rect();
		if (mPreText == null)
			mPreText = "";
		mPrePaint.getTextBounds(mPreText, 0, mPreText.length(), mPreBound);

		mPaint = new Paint();
		mPaint.setTextSize(mTextSize);
		mPaint.setColor(mTextColor);
		mPaint.setAntiAlias(true);
		mBound = new Rect();
		if (mText == null)
			mText = "";
		mPaint.getTextBounds(mText, 0, mText.length(), mBound);

		mUnitPaint = new Paint();
		mUnitPaint.setTextSize(mUnitSize);
		mUnitPaint.setColor(mUnitColor);
		mUnitPaint.setAntiAlias(true);
		mUnitBound = new Rect();
		mUnitTxt = "￥";
		mUnitPaint.getTextBounds(mUnitTxt, 0, mUnitTxt.length(), mUnitBound);

		mPointPaint = new Paint();
		mPointPaint.setTextSize(mPointSize);
		mPointPaint.setColor(mTextColor);
		mPointPaint.setAntiAlias(true);
		mPointBound = new Rect();
		if (mPointTxt == null)
			mPointTxt = "";
		mPointPaint
				.getTextBounds(mPointTxt, 0, mPointTxt.length(), mPointBound);

	}

	
	/**小数点后两位是否动态去掉.00*/
	public void setPointEndStatue(boolean statue) {
		this.mEndStatue = statue;
	}

	/**
	 * 
	 * @param money
	 *            金钱
	 */
	public void setText(double money) {
		String txt = Utils.doubleToString(2, money);

		mText = txt.substring(0, txt.length() - 2);
		mPointTxt = txt.substring(txt.length() - 2, txt.length());

		if (mEndStatue && "00".equals(mPointTxt)) {
			mText = txt.substring(0, txt.length() - 3);
			mPointTxt = "";
		}
		mPaint.getTextBounds(mText, 0, mText.length(), mBound);
		mPointPaint
				.getTextBounds(mPointTxt, 0, mPointTxt.length(), mPointBound);

		postInvalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = 0;
		int height = 0;

		/**
		 * 设置宽度
		 */
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		switch (specMode) {
		case MeasureSpec.EXACTLY:// 明确指定了
			width = getPaddingLeft() + getPaddingRight() + specSize;
//			width = specSize;
			break;
		case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
			int w = mPreBound.width() + mUnitBound.width() + mBound.width()
					+ mPointBound.width();
			width = getPaddingLeft() + getPaddingRight() + w
					+ mUnitToMoneyPadding * 2;
//			width = Math.max(100,specSize);
			break;
		}

		/**
		 * 设置高度
		 */
		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		switch (specMode) {
		case MeasureSpec.EXACTLY:// 明确指定了
			height = getPaddingTop() + getPaddingBottom() + specSize;
//			height =  specSize;
			break;
		case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
			int d1 = Math.max(mPreBound.height(), mUnitBound.height());
			int d2 = Math.max(mBound.height(), mPointBound.height());
			int mh = Math.max(d1, d2);
			height = getPaddingTop() + getPaddingBottom() + mh;

//			height = Math.max(100,specSize);
			break;
		}

		setMeasuredDimension(width, height);

	}

	@Override
	protected void onDraw(Canvas canvas) {

//		canvas.drawColor(getResources().getColor(R.color.text_green_01));

		int x0 = getPaddingLeft();
		canvas.drawText(mPreText, x0, getHeight() - getPaddingBottom(),
				mPrePaint);
		int x1 = x0 + mPreBound.width();
		canvas.drawText(mUnitTxt, x1, getHeight() - getPaddingBottom(),
				mUnitPaint);
		int x2 = x1 + mUnitBound.width() + mUnitToMoneyPadding;
		canvas.drawText(mText, x2, getHeight() - getPaddingBottom(), mPaint);
		int x3 = x2 + mBound.width() + mUnitToMoneyPadding;
		canvas.drawText(mPointTxt, x3, getHeight() - getPaddingBottom(),
				mPointPaint);

	}
}
