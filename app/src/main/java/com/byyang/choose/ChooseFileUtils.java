package com.byyang.choose;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.CallSuper;

import java.io.File;


public class ChooseFileUtils {
    @SuppressLint("StaticFieldLeak")
    private static final Context mContext = ContextUtils.getContext();
    private final ActivityResultLauncher activityResultLauncher;
    //private Intent mIntent;
    private ChooseListener chooseListener;
    //private ActivityResultLauncher<Intent> activityResult;

    public ChooseFileUtils(Activity activity) {
        this(new ActivityResultLauncher(activity));
    }

    private ChooseFileUtils(ActivityResultLauncher activityResultLauncher) {
        this.activityResultLauncher = activityResultLauncher.registerForActivityResult(result -> activityResult(chooseListener, result));
    }


    private static void activityResult(ChooseListener chooseListener, ActivityResult result) {
        if (result == null) {
            return;
        }
        int resultCode = result.getResultCode();
        chooseListener.onFinish();
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        Intent intent = result.getData();
        String filePath = getDocumentPath(intent.getData());
        if (filePath != null) {
            chooseListener.onSuccess(filePath, intent.getData(), intent);
        } else {
            chooseListener.onFailed();
        }

    }

    /**
     *
     * @return 路径解密，自己实现
     */
    public static String getDocumentPath(Uri uri) {
        try {
            String filePath;
            String[] pathList = uri.getPath().split(":");
            if (pathList[pathList.length - 1].equals("/document/primary")) {
                filePath = Environment.getExternalStorageDirectory() + "/";
            } else {
                filePath = Environment.getExternalStorageDirectory() + "/" + pathList[pathList.length - 1] + "/";
            }
            if (fileIsExists(filePath)) {
                return filePath;
            }
        } catch (Exception e) {
            return null;
        }
        return null;

    }

    private static boolean fileIsExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.canRead();

    }

    public void chooseFolder(ChooseListener chooseListener) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
        this.chooseListener = chooseListener;
        this.activityResultLauncher.launch(intent);
    }

    public static abstract class ChooseListener {
        public void onFinish() {


        }

        /**
         * 只有单选的时候才能用，其他时候为null！
         */
        public void onSuccess(String filePath, Uri uri, Intent intent) {


        }

        @CallSuper
        public void onFailed() {

        }

    }


}
