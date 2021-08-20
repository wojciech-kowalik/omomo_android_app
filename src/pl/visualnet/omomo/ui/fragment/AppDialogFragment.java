package pl.visualnet.omomo.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import pl.visualnet.omomo.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AppDialogFragment extends DialogFragment {

    public static final String ERROR_DIALOG_TAG = "error_dialog_tag";

    public static AppDialogFragment getInstance(String title, String message, boolean activateOnclickMethod) {

        AppDialogFragment appDialogFragment = new AppDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putString("title", title);
        args.putBoolean("activateOnclickMethod", activateOnclickMethod);
        appDialogFragment.setArguments(args);

        return appDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String message = getArguments().getString("message");
        String title = getArguments().getString("title");
        final Boolean activateOnclickMethod = getArguments().getBoolean("activateOnclickMethod");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getActivity().getString(R.string.omomo_close),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                if (!activateOnclickMethod) return;

                                Method methodToFind = null;

                                try {

                                    methodToFind = getActivity().getClass().getMethod("doDialogPositiveClick", (Class<?>[]) null);
                                    methodToFind.invoke(getActivity());

                                } catch (NoSuchMethodException e) {
                                } catch (InvocationTargetException e) {
                                } catch (IllegalAccessException e) {
                                }

                            }
                        }
                )

                .create();
    }

}