package dash.xposedavicokkiller;
/*XposedAVICStupidButtonKiller is an Xposed Module which removes the OK button from AVIC units.
 *Copyright (C) 2015  Inspectifier Wrectifier
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/ .
 */

import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.util.ArrayList;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


/**
 * This Xposed module removes that stupid OK button on startup.
 * Created by Inspectifier Wrectifier on 8/22/15.
 */

public class XposedAVICStupidButtonKiller implements IXposedHookLoadPackage {


    /**
     * Creates a structure for quicker send beeps.
     */
    interface ISysFunction {
        public void sendBeep1();//high beep
        public void sendBeep2();//low beep
    }


    //Classloader used throughtout the class
    static XC_LoadPackage.LoadPackageParam lpparam1;

    //message
    final static String toastMessage = "This message should be displayed after the work is done!";

    //Storage of an instance of SysFunction class
    static ISysFunction sysFunction;


    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        //if this is the sysservice class
        if (lpparam.packageName.contains("jp.pioneer.ceam.sysservice")) {
            //store class loader for later
            lpparam1 = lpparam;
            XposedBridge.log("Loaded: " + lpparam.packageName);

            //set up the calls for the beep
            sysFunction = (ISysFunction) XposedHelpers.findClass("jp.pioneer.ceam.widget.common.SysFunction", lpparam1.classLoader).cast(ISysFunction.class);


            //for old models we use the PLCaution methods
            findAndHookMethod("jp.pioneer.ceam.view.plcaution.SYS_VIEW_PL_Caution_Main", lpparam.classLoader, "onShow", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("XposedAvicStupidButtonKiller is running on plcaution");
                    startTune();
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    //gather our objects
                    Object ok_ButtonListener = XposedHelpers.getObjectField(param.thisObject, "ok_ButtonListener");
                    Object view = XposedHelpers.getObjectField(param.thisObject, "view");

                    //check sanity
                    if (view == null || ok_ButtonListener == null) {
                        return;
                    } else {
                        XposedBridge.log("Pressing that PLCaution button!  ");
                        XposedHelpers.callMethod(ok_ButtonListener, "OnPush", view);
                        sendToast(toastMessage);

                    }
                }
            });

            //For newer devices use SmartPhone caution, instead of PLCaution.
            findAndHookMethod("jp.pioneer.ceam.view.smartphonecaution.SYS_VIEW_SmartPhone_Caution_Main", lpparam.classLoader, "onShow", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("XposedAvicStupidButtonKiller is running on SmartPhoneCaution");
                    startTune();
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    //gather objects
                    Object m_SmartPhoneCaution = XposedHelpers.getObjectField(param.thisObject, "m_SmartPhoneCaution");
                    ArrayList m_btnList = (ArrayList) XposedHelpers.getObjectField(m_SmartPhoneCaution, "m_btnList");

                    //find button position
                    int buttonPosition;
                    Object button = null;
                    for (buttonPosition = 0; buttonPosition < m_btnList.size(); buttonPosition++) {
                        if (m_btnList.get(buttonPosition).toString().contains("Button")) {
                            button = m_btnList.get(buttonPosition);
                            break;
                        }
                    }
                    //check sanity
                    if (button == null) {
                        return;
                    } else {
                        //press the button
                        XposedBridge.log("Pressing that SmartPhoneCaution button!  " + button.toString());
                        Object m_SmartPhoneCautionClickListner = XposedHelpers.getObjectField(m_SmartPhoneCaution, "m_OnClickListener");
                        XposedHelpers.callMethod(m_SmartPhoneCautionClickListner, "OnClick", buttonPosition);
                        sendToast(toastMessage);
                    }
                }
            });

        }
    }


    private void startTune(){
        new Thread(tune).start();
    }

    //Ready to go runnable to make a tune.  needs to be called in the background so as not to hog resources sleeping on the main thread
    Runnable tune = new Runnable() {
        @Override
        public void run() {
            try {
                sysFunction.sendBeep1();
                Thread.sleep(100);
                sysFunction.sendBeep1();
                Thread.sleep(100);
                sysFunction.sendBeep2();
                Thread.sleep(100);
                sysFunction.sendBeep1();
                Thread.sleep(100);
                sysFunction.sendBeep2();
                Thread.sleep(100);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                Thread.sleep(10);
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sysFunction.sendBeep1();
                sendToast(toastMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * sends a toast
     *
     * @param text
     */
    private void sendToast(String text) {
        try {
            Context moduleContext = AndroidAppHelper.currentApplication().createPackageContext("dash.XposedModule", Context.CONTEXT_IGNORE_SECURITY);
            Toast toast = Toast.makeText(moduleContext, text, Toast.LENGTH_LONG);
            toast.show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
