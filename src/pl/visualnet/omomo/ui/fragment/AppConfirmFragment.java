package pl.visualnet.omomo.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import pl.visualnet.omomo.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AppConfirmFragment extends DialogFragment {

    public static final String ERROR_DIALOG_TAG = "error_dialog_confirm_tag";
    public static final String INFORMATION_DIALOG_TAG = "information_dialog_confirm_tag";

    public static AppConfirmFragment getInstance(String title, String message, String positiveButtonTitle, String negativeButtonTitle) {

        AppConfirmFragment appDialogFragment = new AppConfirmFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putString("title", title);
        args.putString("positiveButtonTitle", positiveButtonTitle);
        args.putString("negativeButtonTitle", negativeButtonTitle);

        appDialogFragment.setArguments(args);

        return appDialogFragment;
    }

    protected void doPositive(Bundle arguments) {

        Method methodToFind = null;

        try {

            methodToFind = getActivity().getClass().getMethod("doConfirmPositiveClick", (Class<?>[]) null);
            methodToFind.invoke(getActivity());

        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        }
    }

    protected void doNegative(Bundle arguments) {

        Method methodToFind = null;

        try {

            methodToFind = getActivity().getClass().getMethod("doConfirmNegativeClick", (Class<?>[]) null);
            methodToFind.invoke(getActivity());

        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String positiveTitle = (getArguments().getString("positiveButtonTitle") == null)
                ? getActivity().getString(R.string.omomo_yes)
                : getArguments().getString("positiveButtonTitle");

        String negativeTitle = (getArguments().getString("negativeButtonTitle") == null)
                ? getActivity().getString(R.string.omomo_no)
                : getArguments().getString("negativeButtonTitle");

        return new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("message"))
                .setPositiveButton(positiveTitle,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                doPositive(getArguments());
                            }
                        }
                )
                .setNegativeButton(negativeTitle,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                doNegative(getArguments());
                            }
                        })

                .create();
    }

}