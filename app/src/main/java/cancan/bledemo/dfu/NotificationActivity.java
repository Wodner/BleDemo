//package cancan.bledemo.dfu;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//
///**
// * 描述：
// * 作者：Wu on 2017/4/23 13:32
// * 邮箱：wuwende@live.cn
// */
//
//public class NotificationActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // If this activity is the root activity of the task, the app is not running
//        if (isTaskRoot()) {
//            // Start the app before finishing
//            final Intent parentIntent = new Intent(this, DfuActivity.class);
//            parentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            final Intent startAppIntent = new Intent(this, DfuActivity.class);
//            startAppIntent.putExtras(getIntent().getExtras());
//            startActivities(new Intent[] { parentIntent, startAppIntent });
//        }
//
//        // Now finish, which will drop the user in to the activity that was at the top
//        //  of the task stack
//        finish();
//    }
//}
