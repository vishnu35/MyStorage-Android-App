package com.vishnu.www.mystorage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StatFs;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnu.www.mystorage.Fragments.ExtFragment;
import com.vishnu.www.mystorage.Fragments.FileFragment;
import com.vishnu.www.mystorage.Helpers.ExtMaxHeap;
import com.vishnu.www.mystorage.Helpers.FileMaxHeap;
import com.vishnu.www.mystorage.Helpers.Helper;
import com.vishnu.www.mystorage.Model.MyExt;
import com.vishnu.www.mystorage.Model.MyFile;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    HashMap<String,MyExt> fileExts;
    FileMaxHeap fileMaxHeap;
    ExtMaxHeap extMaxHeap;
    long numberOfFiles = 0;
    long allFilesSize = 0;
    long storageUsedSize = 0;
    int scanned=0;

    File externalFile = null;

    Button btn_startScan, btn_stopScan;
    TextView tv_avg,tv_scanned,tv_totalfiles;

    ArrayList<MyFile> myFiles;
    ArrayList<MyExt> myExts;

    LinearLayout lay_results;
    ProgressBar progressBar;
    AsyncGetFiles asyncGetFiles;
    Toolbar toolbar;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fileExts = new HashMap<String, MyExt>();
        fileMaxHeap = new FileMaxHeap(10);
        extMaxHeap = new ExtMaxHeap(5);

        btn_startScan = (Button)findViewById(R.id.btn_startScan);
        btn_stopScan = (Button)findViewById(R.id.btn_stopScan);
        btn_stopScan.setEnabled(false);

        tv_avg = (TextView)findViewById(R.id.tv_avg);
        tv_scanned= (TextView)findViewById(R.id.tv_scanned);
        tv_totalfiles= (TextView)findViewById(R.id.tv_totalfiles);
        lay_results = (LinearLayout)findViewById(R.id.lay_results);
        lay_results.setVisibility(View.GONE);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        myFiles = new ArrayList<MyFile>();
        myExts = new ArrayList<MyExt>();

        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void StartScan(View v){
        if(toolbar.getMenu()!=null && toolbar.getMenu().getItem(0)!=null){
            toolbar.getMenu().getItem(0).setVisible(false);
        }
        btn_startScan.setEnabled(false);
        btn_stopScan.setEnabled(true);
        asyncGetFiles = new AsyncGetFiles();
        asyncGetFiles.execute();
        Notify("Scanning Started", "External Strorage Scan Started");
    }

    public void StopScan(View v){
        btn_startScan.setEnabled(true);
        btn_stopScan.setEnabled(false);
        asyncGetFiles.cancel(false);
    }


    private class AsyncGetFiles extends AsyncTask<Void, Integer, Void>{

        @Override
        protected void onPreExecute() {
            numberOfFiles = 0;
            allFilesSize = 0;
            storageUsedSize = 0;
            scanned=0;
            lay_results.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            scanFiles();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            postScan();
        }

        @Override
        protected void onCancelled() {
            postScan();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        private void scanFiles() {
            System.out.println("Scanning Started!!!");
            File home = getRootFile();
            storageUsedSize = getUsedSize();

            if (home != null) {
                File[] listFiles = home.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    scanDirectory(home);
                }else{
                    Toast.makeText(HomeActivity.this,"No Data found in the Storage",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(HomeActivity.this,"No External Storage Found",Toast.LENGTH_LONG).show();
            }
            System.out.println("Scanning Ended!!!");
        }

        private void scanDirectory(File directory) {
            if(isCancelled()){
                return;
            }
            if(storageUsedSize!=0)
                scanned=(int)(allFilesSize*100/storageUsedSize);
                if(scanned>100)
                    scanned = 95;
                publishProgress(scanned);
            if (directory != null) {
                File[] listFiles = directory.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    for (File file : listFiles) {
                        if (file.isDirectory()) {
                             scanDirectory(file);
                        } else {
                            addFileToList(file);
                        }
                    }
                }
            }
        }

        private void addFileToList(File file) {
            numberOfFiles++;
            MyFile myFile = new MyFile(file);
            fileMaxHeap.add(myFile);

            allFilesSize += myFile.getSize();

            MyExt ext = fileExts.get(myFile.getExt());
            if(ext!=null){
                ext.incrementCount();
            }else{
                ext = new MyExt(myFile.getExt());
                fileExts.put(myFile.getExt(),ext);
            }
            extMaxHeap.add(ext);
        }

        private void postScan() {
            publishProgress(100);
            btn_startScan.setEnabled(true);
            btn_stopScan.setEnabled(false);
            fileExts.clear();
            if(scanned == 95) scanned=100;
            tv_avg.setText("Average File Size : " + Helper.getSize(getAvgSize()));
            tv_scanned.setText("Percentage Scanned : " + scanned + "%");
            tv_totalfiles.setText("Total Files Scanned : " + numberOfFiles);

            myFiles.clear();
            myFiles.addAll(fileMaxHeap.getList());

            myExts.clear();
            myExts.addAll(extMaxHeap.getList());

            mSectionsPagerAdapter.notifyDataSetChanged();

            lay_results.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            toolbar.getMenu().getItem(0).setVisible(true);

            Notify("Scanning Stopped", "External Strorage Scan Stopped");
        }
    }

    private File getRootFile(){
        String strSDCardPath = System.getenv("SECONDARY_STORAGE");

        if ((strSDCardPath == null) || (strSDCardPath.length() == 0)) {
            strSDCardPath = System.getenv("EXTERNAL_STORAGE");
        }

        if (strSDCardPath.contains(":")) {
            strSDCardPath = strSDCardPath.substring(0, strSDCardPath.indexOf(":"));
        }
        externalFile = new File(strSDCardPath);
        if (externalFile.exists() && externalFile.canRead()){
            return externalFile;
        }else{
            strSDCardPath = System.getenv("EXTERNAL_STORAGE");
        }

        if(strSDCardPath != null) {
            if (strSDCardPath.contains(":")) {
                strSDCardPath = strSDCardPath.substring(0, strSDCardPath.indexOf(":"));
            }
            externalFile = new File(strSDCardPath);

            if (externalFile.exists() && externalFile.canRead()){
                //do what you need here
                return externalFile;
            }
        }
        return null;
    }

    private long getAvgSize(){
        try {
            return allFilesSize/ numberOfFiles;
        }catch (Exception ex){
            return 0;
        }
    }

    private long getUsedSize() {
        StatFs statFs = null;
        try {
            statFs = new StatFs(externalFile.getAbsolutePath());
        }catch (Exception ex){
            return 0;
        }
        long   Busy   = (long) ((statFs.getTotalBytes()-statFs.getAvailableBytes())/2.1);
        return Busy;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_share:
                ShareStats();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ShareStats(){
        String shareBody = "Average File Size : \n" + Helper.getSize(getAvgSize()) + "\n\n" +
                            "Top File Size : \nFile: " + myFiles.get(0).getName() +
                            "\nSize: " + Helper.getSize(myFiles.get(0).getSize()) + "\n\n" +
                            "Top Frequency Ext : \nExtension: " + myExts.get(0).getExtension() +
                            "\nCount: " +  myExts.get(0).getCount();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MY STORAGE STATS");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "SHARE STATS"));
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FileFragment.newInstance(myFiles);
                case 1:
                    return ExtFragment.newInstance(myExts);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "TOP 10 FILES";
                case 1:
                    return "TOP 5 EXTS";
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private void Notify(String notificationTitle, String notificationMessage) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        @SuppressWarnings("deprecation")
        Notification notification;
        Intent notificationIntent = new Intent(this,HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher).setTicker(notificationTitle)
                .setAutoCancel(true).setContentTitle(notificationTitle)
                .setContentText(notificationMessage).build();

        notificationManager.notify(9999, notification);


    }
}
