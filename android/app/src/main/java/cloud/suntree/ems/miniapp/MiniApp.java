package cloud.suntree.ems.miniapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alipay.android.phone.scancode.export.ScanCallback;
import com.alipay.android.phone.scancode.export.ScanRequest;
import com.alipay.android.phone.scancode.export.ScanService;
import com.alipay.android.phone.scancode.export.adapter.MPScan;
import com.alipay.mobile.framework.LauncherApplicationAgent;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.mpaas.mas.adapter.api.MPLogger;
import com.mpaas.nebula.adapter.api.MPNebula;
import com.mpaas.nebula.adapter.api.MPTinyHelper;


public class MiniApp extends ReactContextBaseJavaModule {

  private static final String TAG = "MiniApp" ;


  private static ReactApplicationContext reactContext;


  public MiniApp(ReactApplicationContext context) {
    super(context);
    reactContext = context;
  }


  @Override
  public String getName() {
    return "MiniApp";
  }

  /**
   * 初始化代码
   */
  @ReactMethod
  public  void init(Promise promise){
    MiniappLoader.INSTANCE.setup(getCurrentActivity().getApplication(), promise);
    try {
      MiniappLoader.INSTANCE.onCreate();
    }catch (IllegalStateException e){
      promise.reject(e);
    }




  }


  /**
   * 启动小程序
   * @param appId
   */
  @ReactMethod
  public void startUp(String appId) {
    Log.i(TAG, "准备开始了: startUp：" + appId);

    MPNebula.startApp(appId);
  }
  /**
   * 开发者模式
   * @param qrcode
   */
  @ReactMethod
  public void startUpDev(String qrcode) {

  }

  /**
   * 动画
   * @param anim
   */
  @ReactMethod
  public  void configAnim(String anim){


  }

  @ReactMethod
  public void checkPermission() {
    if (ContextCompat.checkSelfPermission(reactContext, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(reactContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(reactContext, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
    ) {
      ActivityCompat.requestPermissions(reactContext.getCurrentActivity(),
              new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                      Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
              0);
    }
  }


  /**
   * appVHost
   * @param vhost
   */
  @ReactMethod
  public  void appVHost(String vhost) {
    Log.i(TAG, "准备开始了: appVHost：" + vhost);
    MPTinyHelper mpTinyHelper = MPTinyHelper.getInstance();
    mpTinyHelper.setTinyAppVHost(vhost);
  }
  /**
   * 配置当前的用户ID【白名单的手机号码】
   * @param mobile
   */
  @ReactMethod
  public  void updateUserId(String mobile) {

    Log.i(TAG, "准备开始了:  updateUserId：" + mobile);

    MPLogger.setUserId(mobile);
  }


  /**
   * 扫码开发
   */
  @ReactMethod
  public void startByScan(){
//    scanPreviewOrDebugQRCode();
    ScanRequest request = new ScanRequest();
    request. setScanType(ScanRequest.ScanType.QRCODE);
    MPScan. startMPaasScanActivity(  reactContext.getCurrentActivity(), request, new ScanCallback() {
      @Override
      public void onScanResult(boolean success, Intent result) {
        if (result == null || !success) {
          showScanError();
          return;
        }
        Uri uri = result.getData();
        if (uri == null) {
          showScanError();
          return;
        }
        // 启动预览或调试小程序，第二个参数为小程序启动参数
        MPTinyHelper.getInstance().launchIdeQRCode(uri, new Bundle());
      }
    });
  }


  private void scanPreviewOrDebugQRCode() {
    ScanService service = LauncherApplicationAgent.getInstance().getMicroApplicationContext()
            .findServiceByInterface(ScanService.class.getName());
    ScanRequest scanRequest = new ScanRequest();
    scanRequest.setScanType(ScanRequest.ScanType.QRCODE);
    service.scan(reactContext.getCurrentActivity(), scanRequest, new ScanCallback() {
      @Override
      public void onScanResult(boolean success, Intent result) {
        if (result == null || !success) {
          showScanError();
          return;
        }
        Uri uri = result.getData();
        if (uri == null) {
          showScanError();
          return;
        }
        // 启动预览或调试小程序，第二个参数为小程序启动参数
        MPTinyHelper.getInstance().launchIdeQRCode(uri, new Bundle());
      }
    });
  }

  private void showScanError() {
    Toast.makeText(reactContext.getCurrentActivity(), "扫码失败了", Toast.LENGTH_SHORT).show();
  }




}
