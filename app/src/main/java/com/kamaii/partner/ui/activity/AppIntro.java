package com.kamaii.partner.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.ActivityAppIntro2Binding;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.adapter.AppIntroPagerAdapter;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.ProjectUtils;


public class AppIntro extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    ViewPager mViewPager;
    private AppIntroPagerAdapter mAdapter;
    private LinearLayout viewPagerCountDots;
    private int dotsCount;
    private ImageView[] dots;
    public SharedPrefrence preference;
    private Context mContext;
    private LinearLayout llSignin, llSignup;
    private CustomTextView btnSkip, btnStart;
    int[] mResources = {R.drawable.intro_1, R.drawable.intro_2};
    private ActivityAppIntro2Binding binding;
    ImageView btnnext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(AppIntro.this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_app_intro2);
        mContext = AppIntro.this;
        preference = SharedPrefrence.getInstance(mContext);


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        btnnext =  findViewById(R.id.btnnext);
        viewPagerCountDots = (LinearLayout) findViewById(R.id.viewPagerCountDots);



        mAdapter = new AppIntroPagerAdapter(AppIntro.this, mContext, mResources);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(true, new StackTransformer());
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(this);

        btnnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(binding.viewpager.getCurrentItem()==0)
                {
                    binding.viewpager.setCurrentItem(binding.viewpager.getCurrentItem()+1);
                }
                else if(binding.viewpager.getCurrentItem()==1) {
                    Intent intent=new Intent(AppIntro.this,SignInActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void scrollPage(int position) {
        binding.viewpager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.close_msg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}
