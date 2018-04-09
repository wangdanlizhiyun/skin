package skin.lzy.com.skin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skin_library.SkinManager;

import java.util.ArrayList;
import java.util.List;

import skin.lzy.com.skin.fragment.MusicFragment;
import skin.lzy.com.skin.fragment.RadioFragment;
import skin.lzy.com.skin.fragment.VideoFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        List<Fragment> list = new ArrayList<>();
        list.add(new MusicFragment());
        list.add(new VideoFragment());
        list.add(new RadioFragment());
        List<String> listTitle = new ArrayList<>();
        listTitle.add("音乐");
        listTitle.add("视频");
        listTitle.add("电台");
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter
                (getSupportFragmentManager(), list, listTitle);
        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //有第三方view的内部类需要换肤时使用
        SkinManager.getInstance().updateSkin(this);
        TextView tv = new TextView(this);
        tv.setText("这个view不是通过xml生成的！！！");
        tv.setBackgroundColor(Color.GREEN);
        tv.setTextColor(getResources().getColor(R.color.colorAccent));
        tv.setTag(R.id.tag_textColor,R.color.colorAccent);
        tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.text_drawable_left,0,0,0);
        tv.setTag(R.id.tag_drawableLeft,R.drawable.text_drawable_left);
        LinearLayout linearLayout = findViewById(R.id.ll_container);
        linearLayout.addView(tv);
        SkinManager.getInstance().updateSkinForNewView(this,tv);
    }
    public void skinSelect(View view) {
        startActivity(new Intent(this, SkinActivity.class));
    }
}
