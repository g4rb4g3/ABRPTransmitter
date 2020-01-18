/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /builds/worker/workspace/build/src/mobile/android/geckoview/src/main/aidl/org/mozilla/gecko/IGeckoEditableChild.aidl
 */
package org.mozilla.gecko;
// Interface for GeckoEditable calls from parent to child

public interface IGeckoEditableChild extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mozilla.gecko.IGeckoEditableChild
{
private static final java.lang.String DESCRIPTOR = "org.mozilla.gecko.IGeckoEditableChild";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.mozilla.gecko.IGeckoEditableChild interface,
 * generating a proxy if needed.
 */
public static org.mozilla.gecko.IGeckoEditableChild asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mozilla.gecko.IGeckoEditableChild))) {
return ((org.mozilla.gecko.IGeckoEditableChild)iin);
}
return new org.mozilla.gecko.IGeckoEditableChild.Stub.Proxy(obj);
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
case TRANSACTION_transferParent:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.IGeckoEditableParent _arg0;
_arg0 = org.mozilla.gecko.IGeckoEditableParent.Stub.asInterface(data.readStrongBinder());
this.transferParent(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onKeyEvent:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
long _arg5;
_arg5 = data.readLong();
int _arg6;
_arg6 = data.readInt();
int _arg7;
_arg7 = data.readInt();
int _arg8;
_arg8 = data.readInt();
boolean _arg9;
_arg9 = (0!=data.readInt());
android.view.KeyEvent _arg10;
if ((0!=data.readInt())) {
_arg10 = android.view.KeyEvent.CREATOR.createFromParcel(data);
}
else {
_arg10 = null;
}
this.onKeyEvent(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10);
reply.writeNoException();
return true;
}
case TRANSACTION_onImeSynchronize:
{
data.enforceInterface(DESCRIPTOR);
this.onImeSynchronize();
reply.writeNoException();
return true;
}
case TRANSACTION_onImeReplaceText:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
this.onImeReplaceText(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onImeAddCompositionRange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
boolean _arg5;
_arg5 = (0!=data.readInt());
int _arg6;
_arg6 = data.readInt();
int _arg7;
_arg7 = data.readInt();
int _arg8;
_arg8 = data.readInt();
this.onImeAddCompositionRange(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
reply.writeNoException();
return true;
}
case TRANSACTION_onImeUpdateComposition:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
this.onImeUpdateComposition(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onImeRequestCursorUpdates:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onImeRequestCursorUpdates(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mozilla.gecko.IGeckoEditableChild
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
// Transfer this child to a new parent.

@Override public void transferParent(org.mozilla.gecko.IGeckoEditableParent parent) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((parent!=null))?(parent.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_transferParent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Process a key event.

@Override public void onKeyEvent(int action, int keyCode, int scanCode, int metaState, int keyPressMetaState, long time, int domPrintableKeyValue, int repeatCount, int flags, boolean isSynthesizedImeKey, android.view.KeyEvent event) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(action);
_data.writeInt(keyCode);
_data.writeInt(scanCode);
_data.writeInt(metaState);
_data.writeInt(keyPressMetaState);
_data.writeLong(time);
_data.writeInt(domPrintableKeyValue);
_data.writeInt(repeatCount);
_data.writeInt(flags);
_data.writeInt(((isSynthesizedImeKey)?(1):(0)));
if ((event!=null)) {
_data.writeInt(1);
event.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onKeyEvent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Request a callback to parent after performing any pending operations.

@Override public void onImeSynchronize() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onImeSynchronize, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Replace part of current text.

@Override public void onImeReplaceText(int start, int end, java.lang.String text) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(start);
_data.writeInt(end);
_data.writeString(text);
mRemote.transact(Stub.TRANSACTION_onImeReplaceText, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Store a composition range.

@Override public void onImeAddCompositionRange(int start, int end, int rangeType, int rangeStyles, int rangeLineStyle, boolean rangeBoldLine, int rangeForeColor, int rangeBackColor, int rangeLineColor) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(start);
_data.writeInt(end);
_data.writeInt(rangeType);
_data.writeInt(rangeStyles);
_data.writeInt(rangeLineStyle);
_data.writeInt(((rangeBoldLine)?(1):(0)));
_data.writeInt(rangeForeColor);
_data.writeInt(rangeBackColor);
_data.writeInt(rangeLineColor);
mRemote.transact(Stub.TRANSACTION_onImeAddCompositionRange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Change to a new composition using previously added ranges.

@Override public void onImeUpdateComposition(int start, int end, int flags) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(start);
_data.writeInt(end);
_data.writeInt(flags);
mRemote.transact(Stub.TRANSACTION_onImeUpdateComposition, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Request cursor updates from the child.

@Override public void onImeRequestCursorUpdates(int requestMode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(requestMode);
mRemote.transact(Stub.TRANSACTION_onImeRequestCursorUpdates, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_transferParent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onKeyEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onImeSynchronize = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onImeReplaceText = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onImeAddCompositionRange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onImeUpdateComposition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_onImeRequestCursorUpdates = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
// Transfer this child to a new parent.

public void transferParent(org.mozilla.gecko.IGeckoEditableParent parent) throws android.os.RemoteException;
// Process a key event.

public void onKeyEvent(int action, int keyCode, int scanCode, int metaState, int keyPressMetaState, long time, int domPrintableKeyValue, int repeatCount, int flags, boolean isSynthesizedImeKey, android.view.KeyEvent event) throws android.os.RemoteException;
// Request a callback to parent after performing any pending operations.

public void onImeSynchronize() throws android.os.RemoteException;
// Replace part of current text.

public void onImeReplaceText(int start, int end, java.lang.String text) throws android.os.RemoteException;
// Store a composition range.

public void onImeAddCompositionRange(int start, int end, int rangeType, int rangeStyles, int rangeLineStyle, boolean rangeBoldLine, int rangeForeColor, int rangeBackColor, int rangeLineColor) throws android.os.RemoteException;
// Change to a new composition using previously added ranges.

public void onImeUpdateComposition(int start, int end, int flags) throws android.os.RemoteException;
// Request cursor updates from the child.

public void onImeRequestCursorUpdates(int requestMode) throws android.os.RemoteException;
}
