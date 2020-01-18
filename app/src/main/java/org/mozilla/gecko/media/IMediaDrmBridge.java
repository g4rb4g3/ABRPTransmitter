/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /builds/worker/workspace/build/src/mobile/android/geckoview/src/main/aidl/org/mozilla/gecko/media/IMediaDrmBridge.aidl
 */
package org.mozilla.gecko.media;
public interface IMediaDrmBridge extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mozilla.gecko.media.IMediaDrmBridge
{
private static final java.lang.String DESCRIPTOR = "org.mozilla.gecko.media.IMediaDrmBridge";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.mozilla.gecko.media.IMediaDrmBridge interface,
 * generating a proxy if needed.
 */
public static org.mozilla.gecko.media.IMediaDrmBridge asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mozilla.gecko.media.IMediaDrmBridge))) {
return ((org.mozilla.gecko.media.IMediaDrmBridge)iin);
}
return new org.mozilla.gecko.media.IMediaDrmBridge.Stub.Proxy(obj);
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
case TRANSACTION_setCallbacks:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.media.IMediaDrmBridgeCallbacks _arg0;
_arg0 = org.mozilla.gecko.media.IMediaDrmBridgeCallbacks.Stub.asInterface(data.readStrongBinder());
this.setCallbacks(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_createSession:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
byte[] _arg3;
_arg3 = data.createByteArray();
this.createSession(_arg0, _arg1, _arg2, _arg3);
return true;
}
case TRANSACTION_updateSession:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
byte[] _arg2;
_arg2 = data.createByteArray();
this.updateSession(_arg0, _arg1, _arg2);
return true;
}
case TRANSACTION_closeSession:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
this.closeSession(_arg0, _arg1);
return true;
}
case TRANSACTION_release:
{
data.enforceInterface(DESCRIPTOR);
this.release();
return true;
}
case TRANSACTION_setServerCertificate:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
this.setServerCertificate(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mozilla.gecko.media.IMediaDrmBridge
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
@Override public void setCallbacks(org.mozilla.gecko.media.IMediaDrmBridgeCallbacks callbacks) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callbacks!=null))?(callbacks.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setCallbacks, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void createSession(int createSessionToken, int promiseId, java.lang.String initDataType, byte[] initData) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(createSessionToken);
_data.writeInt(promiseId);
_data.writeString(initDataType);
_data.writeByteArray(initData);
mRemote.transact(Stub.TRANSACTION_createSession, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void updateSession(int promiseId, java.lang.String sessionId, byte[] response) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(promiseId);
_data.writeString(sessionId);
_data.writeByteArray(response);
mRemote.transact(Stub.TRANSACTION_updateSession, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void closeSession(int promiseId, java.lang.String sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(promiseId);
_data.writeString(sessionId);
mRemote.transact(Stub.TRANSACTION_closeSession, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void release() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_release, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void setServerCertificate(byte[] cert) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(cert);
mRemote.transact(Stub.TRANSACTION_setServerCertificate, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_setCallbacks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_createSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_updateSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_closeSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_release = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_setServerCertificate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void setCallbacks(org.mozilla.gecko.media.IMediaDrmBridgeCallbacks callbacks) throws android.os.RemoteException;
public void createSession(int createSessionToken, int promiseId, java.lang.String initDataType, byte[] initData) throws android.os.RemoteException;
public void updateSession(int promiseId, java.lang.String sessionId, byte[] response) throws android.os.RemoteException;
public void closeSession(int promiseId, java.lang.String sessionId) throws android.os.RemoteException;
public void release() throws android.os.RemoteException;
public void setServerCertificate(byte[] cert) throws android.os.RemoteException;
}
