/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /builds/worker/workspace/build/src/mobile/android/geckoview/src/main/aidl/org/mozilla/gecko/media/ICodecCallbacks.aidl
 */
package org.mozilla.gecko.media;
public interface ICodecCallbacks extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mozilla.gecko.media.ICodecCallbacks
{
private static final java.lang.String DESCRIPTOR = "org.mozilla.gecko.media.ICodecCallbacks";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.mozilla.gecko.media.ICodecCallbacks interface,
 * generating a proxy if needed.
 */
public static org.mozilla.gecko.media.ICodecCallbacks asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mozilla.gecko.media.ICodecCallbacks))) {
return ((org.mozilla.gecko.media.ICodecCallbacks)iin);
}
return new org.mozilla.gecko.media.ICodecCallbacks.Stub.Proxy(obj);
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
case TRANSACTION_onInputQueued:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
this.onInputQueued(_arg0);
return true;
}
case TRANSACTION_onInputPending:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
this.onInputPending(_arg0);
return true;
}
case TRANSACTION_onOutputFormatChanged:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.media.FormatParam _arg0;
if ((0!=data.readInt())) {
_arg0 = org.mozilla.gecko.media.FormatParam.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onOutputFormatChanged(_arg0);
return true;
}
case TRANSACTION_onOutput:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.media.Sample _arg0;
if ((0!=data.readInt())) {
_arg0 = org.mozilla.gecko.media.Sample.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onOutput(_arg0);
return true;
}
case TRANSACTION_onError:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.onError(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mozilla.gecko.media.ICodecCallbacks
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
@Override public void onInputQueued(long timestamp) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(timestamp);
mRemote.transact(Stub.TRANSACTION_onInputQueued, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onInputPending(long timestamp) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(timestamp);
mRemote.transact(Stub.TRANSACTION_onInputPending, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onOutputFormatChanged(org.mozilla.gecko.media.FormatParam format) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((format!=null)) {
_data.writeInt(1);
format.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onOutputFormatChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onOutput(org.mozilla.gecko.media.Sample sample) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((sample!=null)) {
_data.writeInt(1);
sample.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onOutput, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void onError(boolean fatal) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((fatal)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_onError, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_onInputQueued = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onInputPending = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onOutputFormatChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onOutput = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onError = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
public void onInputQueued(long timestamp) throws android.os.RemoteException;
public void onInputPending(long timestamp) throws android.os.RemoteException;
public void onOutputFormatChanged(org.mozilla.gecko.media.FormatParam format) throws android.os.RemoteException;
public void onOutput(org.mozilla.gecko.media.Sample sample) throws android.os.RemoteException;
public void onError(boolean fatal) throws android.os.RemoteException;
}
