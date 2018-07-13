package cn.edu.jit.tianyu_paas.shared.util;

import java.io.File;

/**
 * 检测代码类型
 *
 * @author 倪龙康
 */
public class CheckWord {
    private static String language = "null";

    public static String existsCheck(String dirPath) {
        File pathFile = new File(dirPath);
        if (!pathFile.exists() || pathFile.isFile()) {
            return null;
        }
        for (File file : pathFile.listFiles()) {
            if (file.isFile()) {
                if ("pom.xml".equals(file.getName())) {
                    language = "java";
                    break;
                } else if ("index.html".equals(file.getName())) {
                    language = "html";
                    break;
                } else if ("package.json".equals(file.getName())) {
                    language = "nodejs";
                    break;
                }
            } else if (file.isDirectory()) {
                existsCheck(file.getPath());
            }
        }
        pathFile.delete();
        return language;
    }
}


