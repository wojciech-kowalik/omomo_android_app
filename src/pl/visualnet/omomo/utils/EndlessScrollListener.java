package pl.visualnet.omomo.utils;

import android.util.Log;
import android.widget.AbsListView;

public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 20;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
            //new LoadGigsTask().execute(currentPage + 1);
            loading = true;

            Log.e("", currentPage + "");
            Log.e("", visibleItemCount + "");
            Log.e("", visibleThreshold + "");

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}