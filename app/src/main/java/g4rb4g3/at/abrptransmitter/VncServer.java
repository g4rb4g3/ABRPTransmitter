package g4rb4g3.at.abrptransmitter;

import android.content.Context;
import android.util.Log;

import com.lge.ivi.media.ExtMediaManager;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class VncServer {
  private static final String exe_name = "libreversevncserver.so";

  public static void starStopServer(Context context) {
    String serverPid = checkIfRunning();
    extractHttpStuff(context);
    ExtMediaManager extMediaManager = ExtMediaManager.getInstance(context);

    if (serverPid != null) {
      extMediaManager.excute("kill -SIGTERM " + serverPid, null);
    } else {
      String reversevncserver_cmd = "busybox nohup " + context.getFilesDir().getParent() + "/lib/" + exe_name + " > /dev/null &";
      String reversevncserver_chmod = "chmod a+rwx " + reversevncserver_cmd;

      extMediaManager.excute(reversevncserver_chmod, null);
      extMediaManager.excute(reversevncserver_cmd, null);
    }
  }

  public static String checkIfRunning() {
    String pid = null;
    try {
      Process proc = Runtime.getRuntime().exec("ps");
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

      String s;
      while ((s = stdInput.readLine()) != null) {
        if (s.indexOf(exe_name) >= 0) {
          Pattern p = Pattern.compile("^([a-z]*) *([1-9][0-9]*)");
          Matcher m = p.matcher(s);
          if (!m.find()) {
            break;
          }
          s = m.group();

          p = Pattern.compile("([1-9][0-9]*)");
          m = p.matcher(s);
          if (!m.find()) {
            break;
          }

          pid = m.group();
        }
      }
      stdInput.close();
    } catch (Exception e) {
      Log.e(MainActivity.TAG, "", e);
    }

    return pid;
  }

  private static void extractHttpStuff(Context context) {
    String filesdir = context.getFilesDir().getAbsolutePath() + "/";
    String filepath = filesdir + "/webclients.zip";

    if(new File(filepath).exists()) {
      return;
    }

    copyRawFile(context, R.raw.webclients, filepath);

    try {
      unpackZip(R.raw.webclients, context, filesdir);
    } catch (Exception e) {
      Log.e(MainActivity.TAG, "", e);
    }
  }

  private static void copyRawFile(Context context, int id, String path) {
    try {
      InputStream ins = context.getResources().openRawResource(id);
      int size = ins.available();

      byte[] buffer = new byte[size];
      ins.read(buffer);
      ins.close();

      FileOutputStream fos = new FileOutputStream(path);
      fos.write(buffer);
      fos.close();
    } catch (Exception e) {
      Log.e(MainActivity.TAG, "", e);
    }
  }

  private static void unpackZip(int id, Context C, String destFolder) {
    ZipInputStream inputStream = new ZipInputStream(C.getResources().openRawResource(id));

    try {
      for (ZipEntry entry = inputStream.getNextEntry();
           entry != null;
           entry = inputStream.getNextEntry()) {
        String innerFileName = destFolder + File.separator + entry.getName();
        File innerFile = new File(innerFileName);
        if (innerFile.exists())
          innerFile.delete();

        if (entry.isDirectory())
          innerFile.mkdirs();
        else {
          FileOutputStream outputStream = new FileOutputStream(innerFileName);
          final int BUFFER = 2048;

          BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BUFFER);

          int count = 0;
          byte[] data = new byte[BUFFER];
          while ((count = inputStream.read(data, 0, BUFFER)) != -1)
            bufferedOutputStream.write(data, 0, count);

          bufferedOutputStream.flush();
          bufferedOutputStream.close();
        }

        inputStream.closeEntry();
      }
      inputStream.close();
    } catch (Exception e) {
      Log.e(MainActivity.TAG, "", e);
    }
  }
}
