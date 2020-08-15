package cn.dapan.router.api;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;

public class ClassUtils {

    /**
     * 拿到 Application 中，所有 pkgName 包下的类
     * @param context
     * @param pkgName
     * @return
     */
    public static Set<String> getFileNameByPackageName(Application context, final String pkgName) throws PackageManager.NameNotFoundException {
        Set<String> classNames = new HashSet<>();
        List<String> paths = getSourcePaths(context);

        for (String path : paths) {
            DexFile dexFile = null;
            try {
                dexFile = new DexFile(path);
                Enumeration<String> entries = dexFile.entries();
                while (entries.hasMoreElements()) {
                    String className = entries.nextElement();
                    if (className.startsWith(pkgName)) {
                        classNames.add(className);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != dexFile) {
                    try {
                        dexFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return classNames;
    }

    private static List<String> getSourcePaths(Application context) throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        List<String> sourcePaths = new ArrayList<>();

        sourcePaths.add(applicationInfo.sourceDir);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (null != applicationInfo.splitSourceDirs) {
                sourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
            }
        }
        return sourcePaths;
    }


}
