/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /builds/worker/workspace/build/src/mobile/android/geckoview/src/main/aidl/org/mozilla/gecko/media/ICodec.aidl
 */
package org.mozilla.gecko.media;
public interface ICodec extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mozilla.gecko.media.ICodec
{
private static final java.lang.String DESCRIPTOR = "org.mozilla.gecko.media.ICodec";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.mozilla.gecko.media.ICodec interface,
 * generating a proxy if needed.
 */
public static org.mozilla.gecko.media.ICodec asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mozilla.gecko.media.ICodec))) {
return ((org.mozilla.gecko.media.ICodec)iin);
}
return new org.mozilla.gecko.media.ICodec.Stub.Proxy(obj);
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
org.mozilla.gecko.media.ICodecCallbacks _arg0;
_arg0 = org.mozilla.gecko.media.ICodecCallbacks.Stub.asInterface(data.readStrongBinder());
this.setCallbacks(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_configure:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.media.FormatParam _arg0;
if ((0!=data.readInt())) {
_arg0 = org.mozilla.gecko.media.FormatParam.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
org.mozilla.gecko.gfx.GeckoSurface _arg1;
if ((0!=data.readInt())) {
_arg1 = org.mozilla.gecko.gfx.GeckoSurface.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
int _arg2;
_arg2 = data.readInt();
java.lang.String _arg3;
_arg3 = data.readString();
boolean _result = this.configure(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isAdaptivePlaybackSupported:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isAdaptivePlaybackSupported();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isHardwareAccelerated:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isHardwareAccelerated();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isTunneledPlaybackSupported:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isTunneledPlaybackSupported();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_start:
{
data.enforceInterface(DESCRIPTOR);
this.start();
reply.writeNoException();
return true;
}
case TRANSACTION_stop:
{
data.enforceInterface(DESCRIPTOR);
this.stop();
reply.writeNoException();
return true;
}
case TRANSACTION_flush:
{
data.enforceInterface(DESCRIPTOR);
this.flush();
reply.writeNoException();
return true;
}
case TRANSACTION_release:
{
data.enforceInterface(DESCRIPTOR);
this.release();
reply.writeNoException();
return true;
}
case TRANSACTION_dequeueInput:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
org.mozilla.gecko.media.Sample _result = this.dequeueInput(_arg0);
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
case TRANSACTION_queueInput:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.media.Sample _arg0;
if ((0!=data.readInt())) {
_arg0 = org.mozilla.gecko.media.Sample.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.queueInput(_arg0);
return true;
}
case TRANSACTION_getInputBuffer:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
org.mozilla.gecko.media.SampleBuffer _result = this.getInputBuffer(_arg0);
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
case TRANSACTION_getOutputBuffer:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
org.mozilla.gecko.media.SampleBuffer _result = this.getOutputBuffer(_arg0);
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
case TRANSACTION_releaseOutput:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.media.Sample _arg0;
if ((0!=data.readInt())) {
_arg0 = org.mozilla.gecko.media.Sample.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
boolean _arg1;
_arg1 = (0!=data.readInt());
this.releaseOutput(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_setRates:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setRates(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mozilla.gecko.media.ICodec
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
@Override public void setCallbacks(org.mozilla.gecko.media.ICodecCallbacks callbacks) throws android.os.RemoteException
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
@Override public boolean configure(org.mozilla.gecko.media.FormatParam format, org.mozilla.gecko.gfx.GeckoSurface surface, int flags, java.lang.String drmStubId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((format!=null)) {
_data.writeInt(1);
format.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((surface!=null)) {
_data.writeInt(1);
surface.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(flags);
_data.writeString(drmStubId);
mRemote.transact(Stub.TRANSACTION_configure, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isAdaptivePlaybackSupported() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isAdaptivePlaybackSupported, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isHardwareAccelerated() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isHardwareAccelerated, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isTunneledPlaybackSupported() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isTunneledPlaybackSupported, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void start() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_start, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void flush() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_flush, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void release() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_release, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public org.mozilla.gecko.media.Sample dequeueInput(int size) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.mozilla.gecko.media.Sample _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(size);
mRemote.transact(Stub.TRANSACTION_dequeueInput, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = org.mozilla.gecko.media.Sample.CREATOR.createFromParcel(_reply);
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
@Override public void queueInput(org.mozilla.gecko.media.Sample sample) throws android.os.RemoteException
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
mRemote.transact(Stub.TRANSACTION_queueInput, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public org.mozilla.gecko.media.SampleBuffer getInputBuffer(int id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.mozilla.gecko.media.SampleBuffer _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
mRemote.transact(Stub.TRANSACTION_getInputBuffer, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = org.mozilla.gecko.media.SampleBuffer.CREATOR.createFromParcel(_reply);
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
@Override public org.mozilla.gecko.media.SampleBuffer getOutputBuffer(int id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
org.mozilla.gecko.media.SampleBuffer _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
mRemote.transact(Stub.TRANSACTION_getOutputBuffer, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = org.mozilla.gecko.media.SampleBuffer.CREATOR.createFromParcel(_reply);
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
@Override public void releaseOutput(org.mozilla.gecko.media.Sample sample, boolean render) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((sample!=null)) {
_data.writeInt(1);
sample.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(((render)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_releaseOutput, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setRates(int newBitRate) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(newBitRate);
mRemote.transact(Stub.TRANSACTION_setRates, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_setCallbacks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_configure = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isAdaptivePlaybackSupported = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_isHardwareAccelerated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_isTunneledPlaybackSupported = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_start = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_stop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_flush = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_release = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_dequeueInput = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_queueInput = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_getInputBuffer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getOutputBuffer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_releaseOutput = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_setRates = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
}
public void setCallbacks(org.mozilla.gecko.media.ICodecCallbacks callbacks) throws android.os.RemoteException;
public boolean configure(org.mozilla.gecko.media.FormatParam format, org.mozilla.gecko.gfx.GeckoSurface surface, int flags, java.lang.String drmStubId) throws android.os.RemoteException;
public boolean isAdaptivePlaybackSupported() throws android.os.RemoteException;
public boolean isHardwareAccelerated() throws android.os.RemoteException;
public boolean isTunneledPlaybackSupported() throws android.os.RemoteException;
public void start() throws android.os.RemoteException;
public void stop() throws android.os.RemoteException;
public void flush() throws android.os.RemoteException;
public void release() throws android.os.RemoteException;
public org.mozilla.gecko.media.Sample dequeueInput(int size) throws android.os.RemoteException;
public void queueInput(org.mozilla.gecko.media.Sample sample) throws android.os.RemoteException;
public org.mozilla.gecko.media.SampleBuffer getInputBuffer(int id) throws android.os.RemoteException;
public org.mozilla.gecko.media.SampleBuffer getOutputBuffer(int id) throws android.os.RemoteException;
public void releaseOutput(org.mozilla.gecko.media.Sample sample, boolean render) throws android.os.RemoteException;
public void setRates(int newBitRate) throws android.os.RemoteException;
}
