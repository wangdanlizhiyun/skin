package skin.lzy.com.skin;

import android.app.Application;

import com.skin_library.SkinManager;

/**
 * Created by lizhiyun on 2018/3/22.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
