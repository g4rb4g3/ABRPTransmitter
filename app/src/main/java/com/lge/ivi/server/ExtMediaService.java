package com.lge.ivi.server;

import android.os.RemoteException;

import com.lge.ivi.media.IExtMediaManager;

import java.util.List;

public class ExtMediaService extends IExtMediaManager.Stub {

  private static ExtMediaService mInstance;

  static {
    System.loadLibrary("extmedia_jni");
  }

  private static native int native_chmod(String paramString, int paramInt);

  private static native int native_chown(String paramString, int paramInt1, int paramInt2);

  private static native int native_copyfile(String paramString1, String paramString2);

  private static native int native_excute(String paramString1, String paramString2);

  private static native int native_isdirexist(String paramString);

  private static native int native_isexist(String paramString);

  private static native int native_mkdir(String paramString, int paramInt);

  private static native String native_readfile(String paramString);

  private static native int native_remount(String paramString1, String paramString2);

  private static native int native_rename(String paramString1, String paramString2);

  private static native int native_rmfile(String paramString);

  private static native long native_vcrm_availableSpace();

  private static native int native_writestr(String paramString1, String paramString2);

  public static ExtMediaService getInstance() {
    if (mInstance == null) {
      mInstance = new ExtMediaService();
    }
    return mInstance;
  }

  @Override
  public int Rename(String s, String s1) throws RemoteException {
    return native_rename(s, s1);
  }

  @Override
  public boolean WriteVerToSD(String s) throws RemoteException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean checkExternalMediaFile(String s) throws RemoteException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String checkSDVerFile(String s) throws RemoteException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int chmod(String s, int i) throws RemoteException {
    return native_chmod(s, i);
  }

  @Override
  public int chown(String s, int i, int i1) throws RemoteException {
    return native_chown(s, i, i1);
  }

  @Override
  public boolean copyToUsb(String s, String s1) throws RemoteException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int copyfile(String s, String s1) throws RemoteException {
    return native_copyfile(s, s1);
  }

  @Override
  public int excute(String s, String s1) throws RemoteException {
    return native_excute(s, s1);
  }

  @Override
  public long getAvailableVcrmSpace() throws RemoteException {
    return native_vcrm_availableSpace();
  }

  @Override
  public List<String> getFileList(String s) throws RemoteException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getFileSystemType(String s) throws RemoteException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int isDirExist(String s) throws RemoteException {
    return native_isdirexist(s);
  }

  @Override
  public boolean isUsedUsb() throws RemoteException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int mkdir(String s, int i) throws RemoteException {
    return native_mkdir(s, i);
  }

  @Override
  public boolean mkdirUsb(String s) throws RemoteException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String readfile(String s) throws RemoteException {
    return native_readfile(s);
  }

  @Override
  public int remount(String s, String s1) throws RemoteException {
    return native_remount(s, s1);
  }

  @Override
  public boolean removeFileInSd(String s) throws RemoteException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean writeFileInSd(String s, String s1) throws RemoteException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int writefile(String s, String s1) throws RemoteException {
    throw new UnsupportedOperationException();
  }

  public int writestr(String paramString1, String paramString2) {
    return native_writestr(paramString1, paramString2);
  }

  public int rmfile(String paramString) {
    return native_rmfile(paramString);
  }

  public int isExist(String paramString) {
    return native_isexist(paramString);
  }
}
