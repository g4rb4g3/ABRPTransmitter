/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /builds/worker/workspace/build/src/mobile/android/geckoview/src/main/aidl/org/mozilla/gecko/media/IMediaDrmBridgeCallbacks.aidl
 */
package org.mozilla.gecko.media;
public interface IMediaDrmBridgeCallbacks extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mozilla.gecko.media.IMediaDrmBridgeCallbacks
{
private static final java.lang.String DESCRIPTOR = "org.mozilla.gecko.media.IMediaDrmBridgeCallbacks";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.mozilla.gecko.media.IMediaDrmBridgeCallbacks interface,
 * generating a proxy if needed.
 */
public static org.mozilla.gecko.media.IMediaDrmBridgeCallbacks asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mozilla.gecko.media.IMediaDrmBridgeCallbacks))) {
return ((org.mozilla.gecko.media.IMediaDrmBridgeCallbacks)iin);
}
return new org.mozilla.gecko.media.IMediaDrmBridgeCallbacks.Stub.Proxy(obj);
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
case TRANSACTION_onSessionCreated:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
byte[] _arg2;
_arg2 = data.createByteArray();
byte[] _arg3;
_arg3 = data.createByteArray();
this.onSessionCreated(_arg0, _arg1, _arg2, _arg3);
return true;
}
case TRANSACTION_onSessionUpdated:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
byte[] _arg1;
_arg1 = data.createByteArray();
this.onSessionUpdated(_arg0, _arg1);
return true;
}
case TRANSACTION_onSessionClosed:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
byte[] _arg1;
_arg1 = data.createByteArray();
this.onSessionClosed(_arg0, _arg1);
return true;
}
case TRANSACTION_onSessionMessage:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
int _arg1;
_arg1 = data.readInt();
byte[] _arg2;
_arg2 = data.createByteArray();
this.onSessionMessage(_arg0, _arg1, _arg2);
return true;
}
case TRANSACTION_onSessionError:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
java.lang.String _arg1;
_arg1 = data.readString();
this.onSessionError(_arg0, _arg1);
return true;
}
case TRANSACTION_onSessionBatchedKeyChanged:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
org.mozilla.gecko.media.SessionKeyInfo[] _arg1;
_arg1 = data.createTypedArray(org.mozilla.gecko.media.SessionKeyInfo.CREATOR);
this.onSessionBatchedKeyChanged(_arg0, _arg1);
return true;
}
case TRANSACTION_onRejectPromise:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
this.onRejectPromise(_arg0, _arg1);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mozilla.gecko.media.IMediaDrmBridgeCallbacks
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
@Override public void onSessionCreated(int createSessionToken, int promiseId, byte[] sessionId, byte[] request) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(createSessionToken);
_data.writeInt(promiseId);
_data.writeByteArray(sessionId);
_data.writeByteArray(request);
mRemote.transact(Stub.TRANSACTION_onSessionCreated, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onSessionUpdated(int promiseId, byte[] sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(promiseId);
_data.writeByteArray(sessionId);
mRemote.transact(Stub.TRANSACTION_onSessionUpdated, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onSessionClosed(int promiseId, byte[] sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(promiseId);
_data.writeByteArray(sessionId);
mRemote.transact(Stub.TRANSACTION_onSessionClosed, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onSessionMessage(byte[] sessionId, int sessionMessageType, byte[] request) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(sessionId);
_data.writeInt(sessionMessageType);
_data.writeByteArray(request);
mRemote.transact(Stub.TRANSACTION_onSessionMessage, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onSessionError(byte[] sessionId, java.lang.String message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(sessionId);
_data.writeString(message);
mRemote.transact(Stub.TRANSACTION_onSessionError, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onSessionBatchedKeyChanged(byte[] sessionId, org.mozilla.gecko.media.SessionKeyInfo[] keyInfos) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(sessionId);
_data.writeTypedArray(keyInfos, 0);
mRemote.transact(Stub.TRANSACTION_onSessionBatchedKeyChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onRejectPromise(int promiseId, java.lang.String message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(promiseId);
_data.writeString(message);
mRemote.transact(Stub.TRANSACTION_onRejectPromise, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_onSessionCreated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onSessionUpdated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onSessionClosed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onSessionMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onSessionError = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onSessionBatchedKeyChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_onRejectPromise = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
public void onSessionCreated(int createSessionToken, int promiseId, byte[] sessionId, byte[] request) throws android.os.RemoteException;
public void onSessionUpdated(int promiseId, byte[] sessionId) throws android.os.RemoteException;
public void onSessionClosed(int promiseId, byte[] sessionId) throws android.os.RemoteException;
public void onSessionMessage(byte[] sessionId, int sessionMessageType, byte[] request) throws android.os.RemoteException;
public void onSessionError(byte[] sessionId, java.lang.String message) throws android.os.RemoteException;
public void onSessionBatchedKeyChanged(byte[] sessionId, org.mozilla.gecko.media.SessionKeyInfo[] keyInfos) throws android.os.RemoteException;
public void onRejectPromise(int promiseId, java.lang.String message) throws android.os.RemoteException;
}
