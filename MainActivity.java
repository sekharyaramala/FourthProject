package com.example.sharath.ongolefoodies;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    SwipeRefreshLayout swipeToRefresh;
    SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    final String PROD_URL = "https://ongolefoodies.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide toolbar
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        inflateViews();
        initializeListener();
        swipeToRefresh.setRefreshing(true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.loadUrl(PROD_URL);

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void inflateViews() {
        //inflate webView
        webView = findViewById(R.id.webView);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        webView.setWebViewClient(new MyWebViewClient());

        //enable JavaScript in WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    private void initializeListener() {

        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //reload page
                webView.reload();
            }
        };

        swipeToRefresh.setOnRefreshListener(onRefreshListener);
        swipeToRefresh.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_dark));

        //swipeToRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(android.R.color.darker_gray));
        swipeToRefresh.setSize(SwipeRefreshLayout.LARGE);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("tel:")){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }else if(url.startsWith("mailto:")){
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(url));
                //intent.putExtra(Intent.EXTRA_EMAIL,url.split(":")[1]);
                startActivity(intent);
                return true;
            }else if (Uri.parse(url).getHost().equals("ongolefoodies.com")) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }else{
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //show loader
            swipeToRefresh.setRefreshing(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //dismiss loader
            swipeToRefresh.setRefreshing(false);
        }

    }

}
