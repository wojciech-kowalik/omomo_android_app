package pl.visualnet.omomo.ui.fragment;

import android.os.Bundle;
import pl.visualnet.omomo.ui.activity.WebActivity;

public class AppConfirmPaymentFragment extends AppConfirmFragment {

    public static final String ERROR_DIALOG_TAG = "error_dialog_confirm_payment_tag";

    public static AppConfirmPaymentFragment getInstance(String title, String message, String stringify) {

        AppConfirmPaymentFragment appDialogFragment = new AppConfirmPaymentFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putString("title", title);
        args.putString("stringify", stringify);
        appDialogFragment.setArguments(args);

        return appDialogFragment;
    }

    public void doPositive(Bundle arguments) {
        ((WebActivity) getActivity()).doConfirmPositiveClick(arguments.getString("stringify"));
    }

    public void doNegative(Bundle arguments) {
        ((WebActivity) getActivity()).doConfirmNegativeClick();
    }


}