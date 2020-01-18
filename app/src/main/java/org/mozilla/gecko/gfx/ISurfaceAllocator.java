/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /builds/worker/workspace/build/src/mobile/android/geckoview/src/main/aidl/org/mozilla/gecko/gfx/ISurfaceAllocator.aidl
 */
package org.mozilla.gecko.gfx;
public interface ISurfaceAllocator extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mozilla.gecko.gfx.ISurfaceAllocator
{
private static final java.lang.String DESCRIPTOR = "org.mozilla.gecko.gfx.ISurfaceAllocator";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.mozilla.gecko.gfx.ISurfaceAllocator interface,
 * generating a proxy if needed.
 */
public static org.mozilla.gecko.gfx.ISurfaceAllocator asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mozilla.gecko.gfx.ISurfaceAllocator))) {
return ((org.mozilla.gecko.gfx.ISurfaceAllocator)iin);
}
return new org.mozilla.gecko.gfx.ISurfaceAllocator.Stub.Proxy(obj);
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
case TRANSACTION_acquireSurface:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
boolean _arg2;
_arg2 = (0!=data.readInt());
org.mozilla.gecko.gfx.GeckoSurface _result = this.acquireSurface(_arg0, _arg1, _arg2);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_releaseSurface:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.releaseSurface(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_configureSync:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.gfx.SyncConfig _arg0;
if ((0!=data.readInt())) {
_arg0 = org.mozilla.gecko.gfx.SyncConfig.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.configureSync(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sync:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.sync(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mozilla.gecko.gfx.ISurfaceAllocator
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
@Override public org.mozilla.gecko.gfx.GeckoSurface acquireSurface(int width, int height, boolean singleBufferMode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.mozilla.gecko.gfx.GeckoSurface _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(width);
_data.writeInt(height);
_data.writeInt(((singleBufferMode)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_acquireSurface, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = org.mozilla.gecko.gfx.GeckoSurface.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void releaseSurface(int handle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(handle);
mRemote.transact(Stub.TRANSACTION_releaseSurface, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void configureSync(org.mozilla.gecko.gfx.SyncConfig config) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((config!=null)) {
_data.writeInt(1);
config.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_configureSync, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sync(int handle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(handle);
mRemote.transact(Stub.TRANSACTION_sync, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_acquireSurface = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_releaseSurface = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_configureSync = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_sync = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public org.mozilla.gecko.gfx.GeckoSurface acquireSurface(int width, int height, boolean singleBufferMode) throws android.os.RemoteException;
public void releaseSurface(int handle) throws android.os.RemoteException;
public void configureSync(org.mozilla.gecko.gfx.SyncConfig config) throws android.os.RemoteException;
public void sync(int handle) throws android.os.RemoteException;
}
