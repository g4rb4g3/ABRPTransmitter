/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /builds/worker/workspace/build/src/mobile/android/geckoview/src/main/aidl/org/mozilla/gecko/IGeckoEditableParent.aidl
 */
package org.mozilla.gecko;
// Interface for GeckoEditable calls from child to parent

public interface IGeckoEditableParent extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.mozilla.gecko.IGeckoEditableParent
{
private static final java.lang.String DESCRIPTOR = "org.mozilla.gecko.IGeckoEditableParent";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.mozilla.gecko.IGeckoEditableParent interface,
 * generating a proxy if needed.
 */
public static org.mozilla.gecko.IGeckoEditableParent asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.mozilla.gecko.IGeckoEditableParent))) {
return ((org.mozilla.gecko.IGeckoEditableParent)iin);
}
return new org.mozilla.gecko.IGeckoEditableParent.Stub.Proxy(obj);
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
case TRANSACTION_setDefaultChild:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.IGeckoEditableChild _arg0;
_arg0 = org.mozilla.gecko.IGeckoEditableChild.Stub.asInterface(data.readStrongBinder());
this.setDefaultChild(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_notifyIME:
{
data.enforceInterface(DESCRIPTOR);
org.mozilla.gecko.IGeckoEditableChild _arg0;
_arg0 = org.mozilla.gecko.IGeckoEditableChild.Stub.asInterface(data.readStrongBinder());
int _arg1;
_arg1 = data.readInt();
this.notifyIME(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_notifyIMEContext:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
int _arg1;
_arg1 = data.readInt();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
java.lang.String _arg4;
_arg4 = data.readString();
int _arg5;
_arg5 = data.readInt();
this.notifyIMEContext(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
return true;
}
case TRANSACTION_onSelectionChange:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
this.onSelectionChange(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onTextChange:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
java.lang.CharSequence _arg1;
if ((0!=data.readInt())) {
_arg1 = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
this.onTextChange(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_onDefaultKeyEvent:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
android.view.KeyEvent _arg1;
if ((0!=data.readInt())) {
_arg1 = android.view.KeyEvent.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.onDefaultKeyEvent(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_updateCompositionRects:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
android.graphics.RectF[] _arg1;
_arg1 = data.createTypedArray(android.graphics.RectF.CREATOR);
this.updateCompositionRects(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.mozilla.gecko.IGeckoEditableParent
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
// Set the default child to forward events to, when there is no focused child.

@Override public void setDefaultChild(org.mozilla.gecko.IGeckoEditableChild child) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((child!=null))?(child.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setDefaultChild, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Notify an IME event of a type defined in GeckoEditableListener.

@Override public void notifyIME(org.mozilla.gecko.IGeckoEditableChild child, int type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((child!=null))?(child.asBinder()):(null)));
_data.writeInt(type);
mRemote.transact(Stub.TRANSACTION_notifyIME, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Notify a change in editor state or type.

@Override public void notifyIMEContext(android.os.IBinder token, int state, java.lang.String typeHint, java.lang.String modeHint, java.lang.String actionHint, int flags) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(token);
_data.writeInt(state);
_data.writeString(typeHint);
_data.writeString(modeHint);
_data.writeString(actionHint);
_data.writeInt(flags);
mRemote.transact(Stub.TRANSACTION_notifyIMEContext, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Notify a change in editor selection.

@Override public void onSelectionChange(android.os.IBinder token, int start, int end) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(token);
_data.writeInt(start);
_data.writeInt(end);
mRemote.transact(Stub.TRANSACTION_onSelectionChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Notify a change in editor text.

@Override public void onTextChange(android.os.IBinder token, java.lang.CharSequence text, int start, int unboundedOldEnd) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(token);
if ((text!=null)) {
_data.writeInt(1);
android.text.TextUtils.writeToParcel(text, _data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(start);
_data.writeInt(unboundedOldEnd);
mRemote.transact(Stub.TRANSACTION_onTextChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Perform the default action associated with a key event.

@Override public void onDefaultKeyEvent(android.os.IBinder token, android.view.KeyEvent event) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(token);
if ((event!=null)) {
_data.writeInt(1);
event.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onDefaultKeyEvent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// Update the screen location of current composition.

@Override public void updateCompositionRects(android.os.IBinder token, android.graphics.RectF[] rects) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(token);
_data.writeTypedArray(rects, 0);
mRemote.transact(Stub.TRANSACTION_updateCompositionRects, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_setDefaultChild = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_notifyIME = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_notifyIMEContext = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onSelectionChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onTextChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onDefaultKeyEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_updateCompositionRects = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
// Set the default child to forward events to, when there is no focused child.

public void setDefaultChild(org.mozilla.gecko.IGeckoEditableChild child) throws android.os.RemoteException;
// Notify an IME event of a type defined in GeckoEditableListener.

public void notifyIME(org.mozilla.gecko.IGeckoEditableChild child, int type) throws android.os.RemoteException;
// Notify a change in editor state or type.

public void notifyIMEContext(android.os.IBinder token, int state, java.lang.String typeHint, java.lang.String modeHint, java.lang.String actionHint, int flags) throws android.os.RemoteException;
// Notify a change in editor selection.

public void onSelectionChange(android.os.IBinder token, int start, int end) throws android.os.RemoteException;
// Notify a change in editor text.

public void onTextChange(android.os.IBinder token, java.lang.CharSequence text, int start, int unboundedOldEnd) throws android.os.RemoteException;
// Perform the default action associated with a key event.

public void onDefaultKeyEvent(android.os.IBinder token, android.view.KeyEvent event) throws android.os.RemoteException;
// Update the screen location of current composition.

public void updateCompositionRects(android.os.IBinder token, android.graphics.RectF[] rects) throws android.os.RemoteException;
}
