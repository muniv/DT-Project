package com.muni.examples.aibrilcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

    private static int pState = TelephonyManager.CALL_STATE_IDLE;
    private static boolean isReceived = false;
    private static boolean isCalling = false;
    private static Intent serviceIntent;
    private static String TAG = "Logcat";
    @Override

    public void onReceive(final Context context, Intent intent) {


        TelephonyManager callManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        callManager.listen(new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state != pState) {
                    serviceIntent = new Intent(context, CallRecoderService.class);
                    switch (state) {
                        case TelephonyManager.CALL_STATE_IDLE:
                            if(!isReceived&&!isCalling){
                                Log.i(TAG, "IDLE_NO_RECEIVE,CALL");
                            }
                            else if(isReceived&&isCalling) {
                                Log.i(TAG, "IDLE_YES_RECEIVE,CALL");
                                isReceived = false;
                                isCalling = false;
                                context.stopService(serviceIntent);
                                CallList.adapter.notifyDataSetChanged();
                            }
                            else if(!isReceived&&isCalling) {
                                Log.i(TAG, "IDLE_YES_CALL");
                              //isReceived = false;
                                isCalling = false;
                            }
                            Log.i(TAG, "IDLE");
                            context.stopService(serviceIntent);
                           /* VoiceFileListView Activity = (VoiceFileListView)VoiceFileListView.act;
                            Activity.finish();
                            ActivityCompat.finishAffinity(Activity);
                            android.os.Process.killProcess(android.os.Process.myPid());*/
                            break;

                        case TelephonyManager.CALL_STATE_RINGING:
                            Log.i(TAG, "RINGING");
                            isCalling = true;
                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            Log.i(TAG, "OFFHOOK");
                            isReceived = true;
                            context.startService(serviceIntent);
                            break;
                        default:
                            Log.i(TAG, "DEFAULT");
                            break;
                    }
                    pState = state;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);


        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Log.i(TAG, "OUT");
        }

        callManager.listen(new PhoneStateListener() {
            public void onServiceStateChanged(ServiceState serviceState) {
                switch (serviceState.getState()) {
                    case ServiceState.STATE_IN_SERVICE:
                        Log.i(TAG, "STATE_IN_SERVICE");
                        break;
                    case ServiceState.STATE_OUT_OF_SERVICE:
                        Log.i(TAG, "STATE_OUT_SERVICE");
                        break;
                    case ServiceState.STATE_EMERGENCY_ONLY:
                        Log.i(TAG, "STATE_EMERGENCY_ONLY");
                        break;
                    case ServiceState.STATE_POWER_OFF:
                        Log.i(TAG, "STATE_POWER_OFF");
                        break;
                    default:
                        Log.i(TAG, "DEFAULT");
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_SERVICE_STATE);

    }
}