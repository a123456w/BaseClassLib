package com.android.qizx.baselibrary.util.PopwindowUtils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.android.qizx.baselibrary.R;

import java.util.ArrayList;

/**
 * 375
 */

/**
 * @author yangyu
 *	功能描述：标题按钮上的弹窗（继承自PopupWindow）
 */
public class TitlePopup extends PopupWindow {
    private Context mContext;

    //列表弹窗的间隔
    protected final int LIST_PADDING = 10;

    //实例化一个矩形
    private Rect mRect = new Rect();

    //坐标的位置（x、y）
    private final int[] mLocation = new int[2];

    //屏幕的宽度和高度
    private int mScreenWidth, mScreenHeight;

    //判断是否需要添加或更新列表子类项
    private boolean mIsDirty;

    //位置不在中心
    private int popupGravity = Gravity.NO_GRAVITY;

    //弹窗子类项选中时的监听
    private OnItemOnClickListener mItemOnClickListener;

    //定义列表对象
    private ListView mListView;

    //定义弹窗子类项列表
    private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

    private int pos=0;

    public TitlePopup(Context context) {
        //设置布局的参数
        this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public TitlePopup(Context context, int width, int height) {
        this.mContext = context;

        //设置可以获得焦点
        setFocusable(true);
        //设置弹窗内可点击
        setTouchable(true);
        //设置弹窗外可点击
        setOutsideTouchable(true);

        //获得屏幕的宽度和高度
        mScreenWidth = Util.getScreenWidth(mContext);
        mScreenHeight = Util.getScreenHeight(mContext);

        //设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);

        setBackgroundDrawable(new BitmapDrawable());

        //设置弹窗的布局界面
        setContentView(LayoutInflater.from(mContext).inflate(R.layout.title_popup_lib, null));

        initUI();
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI() {
        mListView = (ListView) getContentView().findViewById(R.id.title_list);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                //点击子类项后，弹窗消失
                dismiss();
                pos=index;
                if (mItemOnClickListener != null)
                    mItemOnClickListener.onItemClick(mActionItems.get(index), index);
            }
        });
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view, int position) {
        //获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);

        //设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(), mLocation[1] + view.getHeight());

        //判断是否需要添加或更新列表子类项
        if (mIsDirty) {
            populateActions();
        }

        //显示弹窗的位置
//		showAtLocation(view, popupGravity, position, mRect.bottom);
        showAsDropDown(view);
    }


    public void show(View view) {
        view.getLocationOnScreen(mLocation);
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(), mLocation[1] + view.getHeight());
        if (mIsDirty) {
            populateActions();
        }
        showAsDropDown(view);
    }

    /**
     * 设置弹窗列表子项
     */
    private void populateActions() {
        mIsDirty = false;

        //设置列表的适配器
        mListView.setAdapter(new BaseAdapter() {

            class ViewHolder {

                ImageView image_image;
                TextView tv_number_range;

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                ViewHolder holder = null;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.exchange_list_activity_popwindow_listview_item_lib, null);
                    holder.tv_number_range = (TextView) convertView.findViewById(R.id.tv_number_range);

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }


                ActionItem item = mActionItems.get(position);

                //设置文本文字
                holder.tv_number_range.setText(item.mTitle);
                if (pos==position){

                    holder.tv_number_range.setTextColor(mContext.getResources().getColor(R.color.login_title_color));
                }else {

                    holder.tv_number_range.setTextColor(mContext.getResources().getColor(R.color.nwb_personal_setup_textcolor9));
                }

                return convertView;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public Object getItem(int position) {
                return mActionItems.get(position);
            }

            @Override
            public int getCount() {
                return mActionItems.size();
            }
        });
    }

    /**
     * 添加子类项
     */
    public void addAction(ActionItem action) {
        if (action != null) {
            mActionItems.add(action);
            mIsDirty = true;
        }
    }

    /**
     * 清除子类项
     */
    public void cleanAction() {
        if (!mActionItems.isEmpty()) {
            mActionItems.clear();
            mIsDirty = true;
        }
    }

    /**
     * 根据位置得到子类项
     */
    public ActionItem getAction(int position) {
        if (position < 0 || position > mActionItems.size())
            return null;
        return mActionItems.get(position);
    }

    /**
     * 设置监听事件
     */
    public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.mItemOnClickListener = onItemOnClickListener;
    }

    /**
     * @author yangyu
     *	功能描述：弹窗子类项按钮监听事件
     */
    public static interface OnItemOnClickListener {
        public void onItemClick(ActionItem item, int position);
    }


}