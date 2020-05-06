package com.BLE.johnny_wei.testtool.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.List;

public class AlertDialogUtil {
    public static void showAlertDialogList(Context context,
                                    String title,
                                    List<String> advertiseList)
    {
        String[] AdvArray = new String[advertiseList.size()];
        advertiseList.toArray(AdvArray);

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(AdvArray, new DialogInterface.OnClickListener(){
                    @Override
                    //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
//                            Toast.makeText(context, "你選的是" + dinner[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public static void simpleDialog(final Context context, String title,String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        builder.show();
    }
}
