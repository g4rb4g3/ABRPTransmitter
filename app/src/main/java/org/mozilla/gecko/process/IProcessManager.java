/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /builds/worker/workspace/build/src/mobile/android/geckoview/src/main/aidl/org/mozilla/gecko/process/IProcessManager.aidl
 */
package org.mozilla.gecko.process;
public interface IProcessManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mozilla.gecko.process.IProcessManager
{
private static final java.lang.String DESCRIPTOR = "org.mozilla.gecko.process.IProcessManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.mozilla.gecko.process.IProcessManager interface,
 * generating a proxy if needed.
 */
public static org.mozilla.gecko.process.IProcessManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mozilla.gecko.process.IProcessManager))) {
return ((org.mozilla.gecko.process.IProcessManager)iin);
}
return new org.mozilla.gecko.process.IProcessManager.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getEditableParent:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.IGeckoEditableChild _arg0;
_arg0 = org.mozilla.gecko.IGeckoEditableChild.Stub.asInterface(data.readStrongBinder());
long _arg1;
_arg1 = data.readLong();
long _arg2;
_arg2 = data.readLong();
this.getEditableParent(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mozilla.gecko.process.IProcessManager
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void getEditableParent(org.mozilla.gecko.IGeckoEditableChild child, long contentId, long tabId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((child!=null))?(child.asBinder()):(null)));
_data.writeLong(contentId);
_data.writeLong(tabId);
mRemote.transact(Stub.TRANSACTION_getEditableParent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getEditableParent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void getEditableParent(org.mozilla.gecko.IGeckoEditableChild child, long contentId, long tabId) throws android.os.RemoteException;
}
