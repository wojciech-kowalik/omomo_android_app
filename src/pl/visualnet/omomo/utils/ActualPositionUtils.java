package pl.visualnet.omomo.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.ui.fragment.AppConfirmFragment;

public class ActualPositionUtils {


    /**
     * Get coordinates for actual position
     *
     * @param activity
     */
    public static boolean setActualPositionCoordinates(Activity activity) {

        App.sActualPosition = MapUtils.getMyCoordinates(App.getContext());

        if (App.sActualPosition == null) {

            AppConfirmFragment appConfirm = AppConfirmFragment.getInstance(
                    App.getContext().getString(R.string.omomo_information),
                    App.getContext().getString(R.string.omomo_localization_information),
                    App.getContext().getString(R.string.omomo_preferences),
                    App.getContext().getString(R.string.omomo_no_thank_you));

            appConfirm.show(activity.getFragmentManager(), AppConfirmFragment.INFORMATION_DIALOG_TAG);
            return false;

        }

        return true;

    }

    /**
     * @param activity
     * @param actualPositonOn
     * @param actualPositonOff
     * @param actualPositionOnText
     */
    public static void enable(Activity activity, ImageButton actualPositonOn, ImageButton actualPositonOff, TextView actualPositionOnText) {

        if (ActualPositionUtils.setActualPositionCoordinates(activity)) {

            App.setActualPositionMode(true);
            actualPositonOff.setVisibility(View.GONE);
            actualPositonOn.setVisibility(View.VISIBLE);
            actualPositionOnText.setVisibility(View.VISIBLE);

        }

    }

    /**
     * @param actualPositonOn
     * @param actualPositonOff
     * @param actualPositionOnText
     */
    public static void disable(ImageButton actualPositonOn, ImageButton actualPositonOff, TextView actualPositionOnText) {

        App.sActualPosition = null;
        App.setActualPositionMode(false);
        actualPositonOff.setVisibility(View.VISIBLE);
        actualPositonOn.setVisibility(View.GONE);
        actualPositionOnText.setVisibility(View.GONE);

    }

}
