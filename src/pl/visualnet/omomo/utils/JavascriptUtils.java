package pl.visualnet.omomo.utils;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import com.google.gson.Gson;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.ui.activity.WebActivity;
import pl.visualnet.omomo.ui.fragment.AppConfirmFragment;
import pl.visualnet.omomo.ui.fragment.AppConfirmPaymentFragment;
import pl.visualnet.omomo.ui.fragment.AppDialogFragment;

public class JavascriptUtils {

    Context context;

    public JavascriptUtils(Context c) {
        context = c;
    }

    @JavascriptInterface
    public void androidShowToast(String toast, boolean isLonger) {

        final int toastLength = (isLonger) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(context, toast, toastLength).show();
    }

    @JavascriptInterface
    public void androidSetCartData(String count, String worth) {
        ((WebActivity) context).androidSetCartData(count, Double.parseDouble(worth));
    }

    @JavascriptInterface
    public void androidShowCartData() {
        ((WebActivity) context).androidShowCartData();
    }

    @JavascriptInterface
    public void androidSetEventData(String name, String location, String date) {
        ((WebActivity) context).androidSetEventData(name, location, date);
    }

    @JavascriptInterface
    public String androidGetAddressData() {
        return new Gson().toJson(App.getSettingsData().getAddress());
    }

    @JavascriptInterface
    public void androidShowDialog(String message, boolean isFinishActivity) {

        DialogFragment errorDialog = AppDialogFragment.getInstance(context.getString(R.string.omomo_error_occur), message, isFinishActivity);
        errorDialog.show(((Activity) context).getFragmentManager(), AppDialogFragment.ERROR_DIALOG_TAG);
    }

    @JavascriptInterface
    public boolean androidShowConfirm(String message, String stringify) {

        if (App.getSettingsData().getAddress().isFullFilled()) {
            return false;
        }

        AppConfirmFragment appConfirm = AppConfirmPaymentFragment.getInstance(context.getString(R.string.omomo_question), message, stringify);
        appConfirm.show(((Activity) context).getFragmentManager(), AppConfirmFragment.ERROR_DIALOG_TAG);

        return true;
    }

    @JavascriptInterface
    public void androidRealizeOrder() {
        ((WebActivity) context).androidRealizeOrder();
    }

    @JavascriptInterface
    public void androidPay() {
        ((WebActivity) context).androidPay();
    }

}
