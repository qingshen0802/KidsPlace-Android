package www.kidsplace.at.fcm;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class KPJobService extends JobService {

    private static final String TAG = "KPJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
