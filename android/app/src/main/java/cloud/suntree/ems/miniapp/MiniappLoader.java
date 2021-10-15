package cloud.suntree.ems.miniapp;


import android.app.Application;
import android.util.Log;

import com.alipay.mobile.framework.quinoxless.IInitCallback;
import com.alipay.mobile.framework.quinoxless.QuinoxlessFramework;
import com.alipay.mobile.nebula.provider.H5AppCenterPresetProvider;
import com.alipay.mobile.nebula.util.H5Utils;
import com.facebook.react.bridge.Promise;
import com.mpaas.tinyappcommonres.TinyAppCenterPresetProvider;

/**
 * 加载器，用于初始化项目
 */
public class MiniappLoader {

    public static  MiniappLoader INSTANCE = new MiniappLoader();

    /**
     * 添加事件
     * @param application
     */
    public void setup(Application application , Promise promise) {
        QuinoxlessFramework.setup(application, new IInitCallback() {
            @Override
            public void onPostInit() {
                H5Utils.setProvider(H5AppCenterPresetProvider.class.getName(), new TinyAppCenterPresetProvider());
                Log.i("WECHAT_MAPP","加载成功了");
                promise.resolve("ali_tinyapp_freamework_load_success");
                // 预置小程序离线包
                // 设置小程序离线包验签公钥，如需关闭验签，可在 custom_config.json 中，将 h5_shouldverifyapp 的 value 设置成 NO
//                MPNebula.enableAppVerification("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArrbiSmhH+oru/dzwyftJ" +
//                        "OXvXkSSYQ/f2K7kOMP6HN9cxvKQu/50LzjrpfusDQFZr2zMWayUPqSMftrpwfKGg" +
//                        "mxD6G8ldDzufjfAR7jI+AVlG3O6L9/pWjGYSjF7/IrPNzfSjG8zRRxoCeOOj0Y3n" +
//                        "tCEZ0h/+ndYN/BY3Ej4VySnTOHRJJyVgGMblN7Q4mBztN1RnoDldkx7nETrBX23S" +
//                        "I0Kp2GpggvmYcPx1Un2nF9UNvZDHIG/TnOg1GL4KP006kShqYD+2NPOJXPPhaDbf" +
//                        "F9SQLiteConnectiongrlxl8xRBGQ5/SQcs1r5gtpJp8NpiWxPcMjd8uA+SOsa0DmLMzuo4oMH3MOktI" +
//                        "tQIDAQAB");
            }
        });
    }

    public void onCreate() {
        QuinoxlessFramework.init();
    }



}
