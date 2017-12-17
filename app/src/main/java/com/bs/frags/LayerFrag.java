package com.bs.frags;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.R;
import com.bs.adapter.AudienceAdapter;
import com.bs.adapter.MessageAdapter;
import com.bs.utils.DisplayUtil;
import com.bs.utils.HorizontalListView;
import com.bs.utils.SoftKeyBoardListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 用户交互页
 *
 * @author
 * @date 2017-5-3
 */
public class LayerFrag extends BaseFrag {

    private NumberAnim giftNumberAnim;
    private List<String> messageData = new LinkedList<>();
    private MessageAdapter messageAdapter;
    private ListView lv_message;
    private HorizontalListView hlv_audience;
    private LinearLayout ll_gift_group;
    private TranslateAnimation outAnim;
    private TranslateAnimation inAnim;
    private LinearLayout ll_inputparent;
    private Button tv_chat;
    private EditText et_chat;
    private LinearLayout ll_anchor;
    private RelativeLayout rl_num;

    @Override
    public int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.frag_layer;
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        super.initView();

        lv_message = (ListView) view.findViewById(R.id.lv_message);
        hlv_audience = (HorizontalListView) view.findViewById(R.id.hlv_audience);
        ll_gift_group = (LinearLayout) view.findViewById(R.id.ll_gift_group);
        ll_inputparent = (LinearLayout) view.findViewById(R.id.ll_inputparent);
        tv_chat = (Button) view.findViewById(R.id.tv_chat);
        et_chat = (EditText) view.findViewById(R.id.et_chat);
        ll_anchor = (LinearLayout) view.findViewById(R.id.ll_anchor);
        rl_num = (RelativeLayout) view.findViewById(R.id.rl_num);
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        super.initData();

        initAudience(); // 初始化观众
        initMessage(); // 初始化评论
        clearTiming(); // 开启定时清理礼物列表
        initAnim(); // 初始化动画
    }

    /**
     * 初始化动画
     */
    private void initAnim() {

        giftNumberAnim = new NumberAnim(); // 初始化数字动画
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_in); // 礼物进入时动画
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_out); // 礼物退出时动画
    }

    @Override
    public void initListener() {
        // TODO Auto-generated method stub
        super.initListener();

        view.findViewById(R.id.btn_gift01).setOnClickListener(this);
        view.findViewById(R.id.btn_gift02).setOnClickListener(this);
        view.findViewById(R.id.btn_gift03).setOnClickListener(this);
        view.findViewById(R.id.btn_gift04).setOnClickListener(this);
        tv_chat.setOnClickListener(this);
        view.findViewById(R.id.tv_send).setOnClickListener(this);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ll_inputparent.getVisibility() == View.VISIBLE) {

                    tv_chat.setVisibility(View.VISIBLE);
                    ll_inputparent.setVisibility(View.GONE);
                    hideKeyboard();
                }
            }
        });

        // 软键盘监听
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/

                // 输入文字时的界面退出动画
                AnimatorSet animatorSetHide = new AnimatorSet();
                ObjectAnimator leftOutAnim = ObjectAnimator.ofFloat(rl_num, "translationX", 0, -rl_num.getWidth());
                ObjectAnimator topOutAnim = ObjectAnimator.ofFloat(ll_anchor, "translationY", 0, -ll_anchor.getHeight());
                animatorSetHide.playTogether(leftOutAnim, topOutAnim);
                animatorSetHide.setDuration(300);
                animatorSetHide.start();

                // 改变listview的高度
                dynamicChangeListviewH(90);
                dynamicChangeGiftParentH(true);
            }

            @Override
            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/

                tv_chat.setVisibility(View.VISIBLE);
                ll_inputparent.setVisibility(View.GONE);

                // 输入文字时的界面进入时的动画
                AnimatorSet animatorSetShow = new AnimatorSet();
                ObjectAnimator leftInAnim = ObjectAnimator.ofFloat(rl_num, "translationX", -rl_num.getWidth(), 0);
                ObjectAnimator topInAnim = ObjectAnimator.ofFloat(ll_anchor, "translationY", -ll_anchor.getHeight(), 0);
                animatorSetShow.playTogether(leftInAnim, topInAnim);
                animatorSetShow.setDuration(300);
                animatorSetShow.start();

                // 改变listview的高度
                dynamicChangeListviewH(150);
                dynamicChangeGiftParentH(false);
            }
        });
    }

    /**
     * 初始化观众列表
     */
    private void initAudience() {

        hlv_audience.setAdapter(new AudienceAdapter(myContext));
    }

    /**
     * 初始化评论列表
     */
    private void initMessage() {

        for (int x = 0; x < 20; x++) {

            messageData.add(": 主播好漂亮啊" + x);
        }
        messageAdapter = new MessageAdapter(getActivity(), messageData);
        lv_message.setAdapter(messageAdapter);
        lv_message.setSelection(messageData.size());
    }

    /**
     * 定时清理礼物列表信息
     */
    private void clearTiming() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                int childCount = ll_gift_group.getChildCount();
                long nowTime = System.currentTimeMillis();
                if(childCount>=1){
                    View childView = ll_gift_group.getChildAt(0);
                    long lastUpdateTime = (long) childView.getTag();
                    // 更新超过3秒就刷新
                    if (nowTime - lastUpdateTime >= 2000) {
                        removeGiftView(childView);
                    }
                }
            }
        }, 0, 2000);
    }

    /**
     * 送的礼物后面的数字动画
     */
    public class NumberAnim {

        private Animator lastAnimator;

        public void showAnimator(View v) {

            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.cancel();
                lastAnimator.end();
            }
            ObjectAnimator animScaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.6f, 1.0f);
            ObjectAnimator animScaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.6f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(animScaleX, animScaleY);
            animSet.setDuration(400);
            lastAnimator = animSet;
            animSet.start();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_chat:// 聊天
                tv_chat.setVisibility(View.GONE);
                ll_inputparent.setVisibility(View.VISIBLE);
                ll_inputparent.requestFocus(); // 获取焦点
                showKeyboard();
                break;
            case R.id.tv_send:// 发送消息
                String chatMsg = et_chat.getText().toString();
                if (!TextUtils.isEmpty(chatMsg)) {

                    messageData.add(": " + chatMsg);
                    et_chat.setText("");
                    messageAdapter.NotifyAdapter(messageData);
                    lv_message.setSelection(messageData.size());
                }
                hideKeyboard();
                break;
            case R.id.btn_gift01: // 礼物1,送香皂
                showGift("gift01");
                break;
            case R.id.btn_gift02: // 礼物2,送玫瑰
                showGift("gift02");
                break;
            case R.id.btn_gift03: // 礼物3,送爱心
                showGift("gift03");
                break;
            case R.id.btn_gift04: // 礼物4,送蛋糕
                showGift("gift04");
                break;
        }
    }

    /**
     * 刷礼物
     */
    private void showGift(String tag) {

        if(ll_gift_group.getChildCount() > 5){
            ll_gift_group.removeViewAt(0);
        }

        // 获取礼物
        final View newGiftView = getNewGiftView();
        ll_gift_group.addView(newGiftView);
        final ImageView iv_gift = (ImageView) newGiftView.findViewById(R.id.iv_gift);
        inAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                giftNumberAnim.showAnimator(iv_gift);


            }
        });
        // 播放动画
        newGiftView.startAnimation(inAnim);
    }

    /**
     * 当前动画runnable
     */
    private Runnable mCurrentAnimRunnable;

    // 礼物
    private int[] GiftIcon = new int[]{R.mipmap.zem68,
            R.mipmap.zem72,
            R.mipmap.zem70,
            R.mipmap.zem63};
    /**
     * 礼物展示时间
     */
    public static final int GIFT_DISMISS_TIME = 3000;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };//连击handler
    /**
     * 获取礼物
     */
    private View getNewGiftView() {
        // 添加标识, 该view若在layout中存在，就不在生成（用于findViewWithTag判断是否存在）
        View giftView = LayoutInflater.from(myContext).inflate(R.layout.item_gift, null);
        giftView.setTag(System.currentTimeMillis());

        // 添加标识, 记录生成时间，回收时用于判断是否是最新的，回收最老的
        ImageView iv_gift = (ImageView) giftView.findViewById(R.id.iv_gift);
        iv_gift.setImageResource(GiftIcon[2]);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        giftView.setLayoutParams(lp);

        return giftView;
    }

    /**
     * 移除礼物列表里的giftView
     */
    private void removeGiftView(final View removeGiftView) {

        // 移除列表，外加退出动画
        outAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                ll_gift_group.removeView(removeGiftView);
            }
        });

        // 开启动画，因为定时原因，所以可能是在子线程
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                removeGiftView.startAnimation(outAnim);
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_chat.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     */
    private void showKeyboard() {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_chat, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 动态的修改listview的高度
     */
    private void dynamicChangeListviewH(int heightPX) {

        ViewGroup.LayoutParams layoutParams = lv_message.getLayoutParams();
        layoutParams.height = DisplayUtil.dip2px(getActivity(), heightPX);
        lv_message.setLayoutParams(layoutParams);
    }

    /**
     * 动态修改礼物父布局的高度
     */
    private void dynamicChangeGiftParentH(boolean showhide) {

        if (showhide) {// 如果软键盘显示中

            if (ll_gift_group.getChildCount() != 0) {

                // 判断是否有礼物显示，如果有就修改父布局高度，如果没有就不作任何操作
                ViewGroup.LayoutParams layoutParams = ll_gift_group.getLayoutParams();
                layoutParams.height = ll_gift_group.getChildAt(0).getHeight();
                ll_gift_group.setLayoutParams(layoutParams);
            }
        } else {
            // 如果软键盘隐藏中
            // 就将装载礼物的容器的高度设置为包裹内容
            ViewGroup.LayoutParams layoutParams = ll_gift_group.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ll_gift_group.setLayoutParams(layoutParams);
        }
    }
}