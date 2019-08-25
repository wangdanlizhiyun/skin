package skin.lzy.com.skin;

import android.app.Application;

import com.skin_library.SkinManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
