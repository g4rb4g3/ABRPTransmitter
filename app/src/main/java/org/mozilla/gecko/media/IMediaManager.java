/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /builds/worker/workspace/build/src/mobile/android/geckoview/src/main/aidl/org/mozilla/gecko/media/IMediaManager.aidl
 */
package org.mozilla.gecko.media;
public interface IMediaManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mozilla.gecko.media.IMediaManager
{
private static final java.lang.String DESCRIPTOR = "org.mozilla.gecko.media.IMediaManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.mozilla.gecko.media.IMediaManager interface,
 * generating a proxy if needed.
 */
public static org.mozilla.gecko.media.IMediaManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mozilla.gecko.media.IMediaManager))) {
return ((org.mozilla.gecko.media.IMediaManager)iin);
}
return new org.mozilla.gecko.media.IMediaManager.Stub.Proxy(obj);
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
case TRANSACTION_createCodec:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.media.ICodec _result = this.createCodec();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_createRemoteMediaDrmBridge:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
org.mozilla.gecko.media.IMediaDrmBridge _result = this.createRemoteMediaDrmBridge(_arg0, _arg1);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_endRequest:
{
data.enforceInterface(DESCRIPTOR);
this.endRequest();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mozilla.gecko.media.IMediaManager
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
/** Creates a remote ICodec object. */
@Override public org.mozilla.gecko.media.ICodec createCodec() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.mozilla.gecko.media.ICodec _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_createCodec, _data, _reply, 0);
_reply.readException();
_result = org.mozilla.gecko.media.ICodec.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/** Creates a remote IMediaDrmBridge object. */
@Override public org.mozilla.gecko.media.IMediaDrmBridge createRemoteMediaDrmBridge(java.lang.String keySystem, java.lang.String stubId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.mozilla.gecko.media.IMediaDrmBridge _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(keySystem);
_data.writeString(stubId);
mRemote.transact(Stub.TRANSACTION_createRemoteMediaDrmBridge, _data, _reply, 0);
_reply.readException();
_result = org.mozilla.gecko.media.IMediaDrmBridge.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/** Called by client to indicate it no longer needs a requested codec or DRM bridge. */
@Override public void endRequest() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_endRequest, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_createCodec = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_createRemoteMediaDrmBridge = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_endRequest = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
/** Creates a remote ICodec object. */
public org.mozilla.gecko.media.ICodec createCodec() throws android.os.RemoteException;
/** Creates a remote IMediaDrmBridge object. */
public org.mozilla.gecko.media.IMediaDrmBridge createRemoteMediaDrmBridge(java.lang.String keySystem, java.lang.String stubId) throws android.os.RemoteException;
/** Called by client to indicate it no longer needs a requested codec or DRM bridge. */
public void endRequest() throws android.os.RemoteException;
}
