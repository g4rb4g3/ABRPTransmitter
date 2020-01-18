/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /builds/worker/workspace/build/src/mobile/android/geckoview/src/main/aidl/org/mozilla/gecko/process/IChildProcess.aidl
 */
package org.mozilla.gecko.process;
public interface IChildProcess extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mozilla.gecko.process.IChildProcess
{
private static final java.lang.String DESCRIPTOR = "org.mozilla.gecko.process.IChildProcess";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.mozilla.gecko.process.IChildProcess interface,
 * generating a proxy if needed.
 */
public static org.mozilla.gecko.process.IChildProcess asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mozilla.gecko.process.IChildProcess))) {
return ((org.mozilla.gecko.process.IChildProcess)iin);
}
return new org.mozilla.gecko.process.IChildProcess.Stub.Proxy(obj);
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
case TRANSACTION_getPid:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPid();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_start:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.process.IProcessManager _arg0;
_arg0 = org.mozilla.gecko.process.IProcessManager.Stub.asInterface(data.readStrongBinder());
java.lang.String[] _arg1;
_arg1 = data.createStringArray();
android.os.Bundle _arg2;
if ((0!=data.readInt())) {
_arg2 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
int _arg3;
_arg3 = data.readInt();
java.lang.String _arg4;
_arg4 = data.readString();
android.os.ParcelFileDescriptor _arg5;
if ((0!=data.readInt())) {
_arg5 = android.os.ParcelFileDescriptor.CREATOR.createFromParcel(data);
}
else {
_arg5 = null;
}
android.os.ParcelFileDescriptor _arg6;
if ((0!=data.readInt())) {
_arg6 = android.os.ParcelFileDescriptor.CREATOR.createFromParcel(data);
}
else {
_arg6 = null;
}
android.os.ParcelFileDescriptor _arg7;
if ((0!=data.readInt())) {
_arg7 = android.os.ParcelFileDescriptor.CREATOR.createFromParcel(data);
}
else {
_arg7 = null;
}
android.os.ParcelFileDescriptor _arg8;
if ((0!=data.readInt())) {
_arg8 = android.os.ParcelFileDescriptor.CREATOR.createFromParcel(data);
}
else {
_arg8 = null;
}
android.os.ParcelFileDescriptor _arg9;
if ((0!=data.readInt())) {
_arg9 = android.os.ParcelFileDescriptor.CREATOR.createFromParcel(data);
}
else {
_arg9 = null;
}
boolean _result = this.start(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_crash:
{
data.enforceInterface(DESCRIPTOR);
this.crash();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mozilla.gecko.process.IChildProcess
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
@Override public int getPid() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPid, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean start(org.mozilla.gecko.process.IProcessManager procMan, java.lang.String[] args, android.os.Bundle extras, int flags, java.lang.String crashHandlerService, android.os.ParcelFileDescriptor prefsPfd, android.os.ParcelFileDescriptor prefMapPfd, android.os.ParcelFileDescriptor ipcPfd, android.os.ParcelFileDescriptor crashReporterPfd, android.os.ParcelFileDescriptor crashAnnotationPfd) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((procMan!=null))?(procMan.asBinder()):(null)));
_data.writeStringArray(args);
if ((extras!=null)) {
_data.writeInt(1);
extras.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(flags);
_data.writeString(crashHandlerService);
if ((prefsPfd!=null)) {
_data.writeInt(1);
prefsPfd.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((prefMapPfd!=null)) {
_data.writeInt(1);
prefMapPfd.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((ipcPfd!=null)) {
_data.writeInt(1);
ipcPfd.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((crashReporterPfd!=null)) {
_data.writeInt(1);
crashReporterPfd.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((crashAnnotationPfd!=null)) {
_data.writeInt(1);
crashAnnotationPfd.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_start, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void crash() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_crash, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getPid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_start = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_crash = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public int getPid() throws android.os.RemoteException;
public boolean start(org.mozilla.gecko.process.IProcessManager procMan, java.lang.String[] args, android.os.Bundle extras, int flags, java.lang.String crashHandlerService, android.os.ParcelFileDescriptor prefsPfd, android.os.ParcelFileDescriptor prefMapPfd, android.os.ParcelFileDescriptor ipcPfd, android.os.ParcelFileDescriptor crashReporterPfd, android.os.ParcelFileDescriptor crashAnnotationPfd) throws android.os.RemoteException;
public void crash() throws android.os.RemoteException;
}
