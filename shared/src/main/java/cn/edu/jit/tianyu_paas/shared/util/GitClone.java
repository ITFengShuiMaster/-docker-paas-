package cn.edu.jit.tianyu_paas.shared.util;


import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;

import java.io.File;


public class GitClone {
    public static String cloneRepository(String gitUrl) {
        String[] array = gitUrl.split("/");
        String[] split = array[array.length - 1].split("\\.");
        String localPath = "D:\\" + split[0]+System.currentTimeMillis();
        try {
            CloneCommand cc = Git.cloneRepository().setURI(gitUrl);
            cc.setDirectory(new File(localPath)).call();
            return localPath;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
