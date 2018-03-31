package com.softatomos.apps.setmanagerlite;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        */

        /*
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */

        initWebview();
    }

    WebView webview;
    WebView menuWebview;

    private void initWebview() {
        webview = new WebView(this){
        @Override
        public void  postUrl(String  url, byte[] postData)
        {
            Log.d("webview","postUrl can modified here:" +url);
            super.postUrl(url, postData);
        }};
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.addView(webview);

        webview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://apps.softatomos.com/setmanager/?site=home&special=webview");
        webview.setHapticFeedbackEnabled(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        webview.getSettings().setAppCacheEnabled(true);
        webview.setWebViewClient(new myWebViewClient());


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        menuWebview = header.findViewById(R.id.menuWebview);
        menuWebview.setWebViewClient(new myMenuWebClient());
        menuWebview.getSettings().setJavaScriptEnabled(true);
        menuWebview.loadUrl("https://apps.softatomos.com/setmanager/?site=home&special=webmenu");
        menuWebview.addJavascriptInterface(this, "MyApp");
        //menuWebview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


    }

    @JavascriptInterface
    public void resize(final float height) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                menuWebview.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }
    MainActivity myActivity=this;

    public class myWebViewClient extends WebViewClient { //WebViewCLient

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.contains("get_file.php")) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url+"&part_hash="+Vars.getHash()));
                startActivity(i);
            } else {
                if(!url.contains("?"))
                    url+="?cf=mob";
                view.loadUrl(url+"&special=webview");
            }

            Log.d("webview","loading "+url);

            return true;
        }

    }



    public class myMenuWebClient extends WebViewClient { //WebViewCLient

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(!url.contains("?"))
                url+="?cf=mob";
            view.loadUrl(url+"&special=webmenu");

            Log.d("webview_menu","loading "+url);

            return true;
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            webview.loadUrl("javascript:toggleMenu()");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
