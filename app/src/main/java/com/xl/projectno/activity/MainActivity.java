package com.xl.projectno.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xl.projectno.ICountInterface;
import com.xl.projectno.IKeepLiveInterface;
import com.xl.projectno.R;
import com.xl.projectno.adapter.RecyclerViewAdapter;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.data.imageload.CustomNetworkImageView;
import com.xl.projectno.data.protocol.ProtocolManager;
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.dialog.ChooseInventoryDialog;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.InventoryBean;
import com.xl.projectno.model.ItemBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.DrawCircle;
import com.xl.projectno.utils.FileUtils;
import com.xl.projectno.utils.ImageCompress;
import com.xl.projectno.utils.ImageTools;
import com.xl.projectno.utils.InputSoftUtils;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.utils.StatusBarUtils;
import com.xl.projectno.utils.TimeUtils;
import com.xl.projectno.utils.ValidateUtils;
import com.xl.projectno.volley.ResponseListener;
import com.xl.projectno.widget.BottomInput;
import com.xl.projectno.widget.MyEditText;
import com.xl.projectno.widget.WhItemTouchCallback;
import com.xl.projectno.widget.onDragStartListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DataManager.IDataChangeListener, View.OnClickListener, onDragStartListener {
    ViewHolder viewHolder;
    RecyclerViewAdapter dragListAdapter;
    ArrayList<BaseBean> data;

    DataManager dataManager;

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int CROP_PHOTO = 3;

    private ProgressDialog waitingDialog;

    private Uri uri = null;
    private String imagePath = null;

    private ItemTouchHelper itemtouchhelper;
    WhItemTouchCallback callback;

    private boolean deleteEnable = false;
    private boolean sendEnable = false;

    ServiceConnection sc;

    //    CountService.CountBinder countBinder =null;
    IKeepLiveInterface binder = null;

    Handler handler;

    ThemeBean theme;

    private class KeepLiveTask implements Runnable {
        public void run() {
            invoke();
            try {
                if (binder.keepLive() == 200)
                    handler.postDelayed(myTask, 2000);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private KeepLiveTask myTask = new KeepLiveTask();

    public void invoke() {
        if (sc != null && binder != null) {
            try {
                switch (binder.keepLive()) {
                    case 403:
                        binder.removeCallBack();
                        handler.removeCallbacks(myTask);
                        //Toast.makeText(MainActivity.this,"用户已退出",Toast.LENGTH_SHORT).show();
                        break;
                    case 404:
                        DataManager.getInstance().delUser();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        binder.removeCallBack();
                        handler.removeCallbacks(myTask);
                        Toast.makeText(MainActivity.this, getString(R.string.loginanother), Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    UserBean user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme = DataManager.getInstance().getCurrentTheme();
        setTheme(theme.style);
        LanguageUtils.ChangeLanguage(this, LanguageUtils.current_language);
        setContentView(R.layout.activity_main);
        viewHolder = new ViewHolder(getWindow().getDecorView());
        initItem();
        initData();
        if (user!=null){
            startService();
        }
        IntentFilter intentFilter = new IntentFilter(AEInventory.action);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void startService() {
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                binder = IKeepLiveInterface.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
        startService(new Intent(this, KeepLiveService.class));
        bindService(new Intent(this, KeepLiveService.class), sc, Context.BIND_AUTO_CREATE);
        handler = new Handler();
        handler.postDelayed(myTask, 5000);
    }

    private void initData() {
        data = dataManager.getListDirect(DataManager.TYPE_DATA_ITEM);
        dragListAdapter = new RecyclerViewAdapter(this, data);
        dragListAdapter.setStartdraglistener(this);
        dragListAdapter.setDeleteEnable(false);
        viewHolder.recyclerView.setAdapter(dragListAdapter);
        viewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager gridlayoutmanager = new GridLayoutManager(this, 1, OrientationHelper.VERTICAL, false);
        viewHolder.recyclerView.setLayoutManager(gridlayoutmanager);

        callback = new WhItemTouchCallback(dragListAdapter);//初始化ItemTouchHelper.
        itemtouchhelper = new ItemTouchHelper(callback);
        itemtouchhelper.attachToRecyclerView(viewHolder.recyclerView);//这个必须加上.让辅助类和recycleview关联上
        callback.setDragEnable(true);
        callback.setXdirection(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    private void initItem() {
        dataManager = DataManager.getInstance();
        dataManager.updateListFromDb();
        dataManager.registerDataChangeListener(DataManager.TYPE_DATA_ITEM, this);
        viewHolder.toolbar.setTitle(getString(R.string.today));
        StatusBarUtils.setWindowStatusBarColor(this, theme.color);
        setSupportActionBar(viewHolder.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, viewHolder.drawer, viewHolder.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        viewHolder.drawer.setDrawerListener(toggle);
        toggle.syncState();

        viewHolder.navigationView.setNavigationItemSelectedListener(this);
        viewHolder.input.setBackListener(new MyEditText.BackListener() {
            @Override
            public void back(TextView textView) {
                sendEnable = false;
                toggleView();
            }
        });
        viewHolder.sound.setOnClickListener(this);
        viewHolder.input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    sendMessage(textView);
                    return true;
                }
                return false;
            }
        });

        viewHolder.bottomInput.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldleft, int oldtop, int oldright, int oldbottom) {
//                int heightDiff = view.getRootView().getHeight() -view.getHeight();
                int heightDiff = oldbottom - bottom;
                //大于100可看做软键盘弹出
                if (heightDiff > 100) {
                    sendEnable = true;
                    toggleView();
                } else if (heightDiff < 0) {
                    sendEnable = false;
                    toggleView();
                }
            }
        });
        String name = null;
        for (BaseBean baseBean : DataManager.getInstance().getListDirect(DataManager.TYPE_DATA_INVENTORY)) {
            if (((InventoryBean) baseBean).isUsing) {
                name = ((InventoryBean) baseBean).name;
            }
        }
        viewHolder.navigationView.getMenu().findItem(R.id.collect).setTitle(name);
        initUser();
    }

    void initUser() {
        user = DataManager.getInstance().getCurrentUser();
        viewHolder.navigationView.getHeaderView(0).findViewById(R.id.username).setVisibility(View.VISIBLE);
        viewHolder.navigationView.getHeaderView(0).findViewById(R.id.loginorregister).setVisibility(View.GONE);
        viewHolder.navigationView.getHeaderView(0).findViewById(R.id.set).setOnClickListener(this);
        viewHolder.navigationView.getHeaderView(0).findViewById(R.id.image_user).setOnClickListener(this);
        if (user!=null){
            ((TextView) viewHolder.navigationView.getHeaderView(0).findViewById(R.id.username)).setText(user.name);
            if (user.imageUrl != null && !user.imageUrl.equals("null")) {
                DataManager.getInstance().setImage(user.imageUrl, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Bitmap circleBitmap = DrawCircle.getCircleBitmap(bitmap);
                        ((ImageView) viewHolder.navigationView.getHeaderView(0).findViewById(R.id.image_user)).setImageBitmap(circleBitmap);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
            }
        }else{
            viewHolder.navigationView.getHeaderView(0).findViewById(R.id.username).setVisibility(View.GONE);
            viewHolder.navigationView.getHeaderView(0).findViewById(R.id.loginorregister).setVisibility(View.VISIBLE);
            viewHolder.navigationView.getHeaderView(0).findViewById(R.id.loginorregister).setOnClickListener(this);
            ((TextView) viewHolder.navigationView.getHeaderView(0).findViewById(R.id.username)).setText(getString(R.string.username));
            ((ImageView) viewHolder.navigationView.getHeaderView(0).findViewById(R.id.image_user)).setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.icon_user));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUser();
    }

    private void toggleView() {
        if (sendEnable) {
            viewHolder.sound.setBackground(getResources().getDrawable(R.drawable.send));
        } else {
            viewHolder.sound.setBackground(getResources().getDrawable(R.drawable.sound));
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
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_delete:
                if (deleteEnable) {
                    dragListAdapter.setDeleteEnable(false);
                    item.setTitle(getString(R.string.action_delete));
                } else {
                    dragListAdapter.setDeleteEnable(true);
                    item.setTitle(getString(R.string.hidedelete));
                }
                deleteEnable = !deleteEnable;
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();
        if (id == R.id.today) {
            // Handle the camera action
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.collect) {
            ChooseInventoryDialog dialog = new ChooseInventoryDialog(this, R.style.add_dialog);
            dialog.setOnChooseListener(new ChooseInventoryDialog.OnChooseListener() {
                @Override
                public void onChoose(String name) {
                    item.setTitle(name);
                    for (BaseBean baseBean : DataManager.getInstance().getListDirect(DataManager.TYPE_DATA_INVENTORY)) {
                        InventoryBean bean = (InventoryBean) baseBean;
                        bean.isUsing = false;
                        if (name.equals(bean.name)) {
                            bean.isUsing = true;
                        }
                        DataManager.getInstance().updateItem(bean, bean);
                    }
                }
            });
            dialog.show();
        } else if (id == R.id.calendar) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.add) {
            intent.setClass(this, AEInventory.class);
            startActivity(intent);
        } else if (id == R.id.manager) {
            intent.setClass(this, EditInventory.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onDataChange(int msgId, int arg) {
        if (msgId > 0) {
            dragListAdapter.setData(dataManager.getListDirect(DataManager.TYPE_DATA_ITEM));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sound:
                sendMessage(viewHolder.input);
                break;
            case R.id.set:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.image_user:
                if (user!=null){
                    final String[] mItems = {getString(R.string.takephoto), getString(R.string.photo)};
                    new AlertDialog.Builder(this).setTitle(getString(R.string.pleasechoose)).setItems(mItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    doTakePhoto();
                                    break;
                                case 1:
                                    OpenGallery();
                                    break;
                            }
                        }
                    }).show();
                }else{
                    Toast.makeText(this,getString(R.string.loginfirst),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.loginorregister:
                Intent intent1 = new Intent(this,LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }

    private void sendMessage(TextView textView) {
        if (sendEnable) {
            String name = viewHolder.input.getText().toString();
            if (!ValidateUtils.getInstance(MainActivity.this).validate("", name, DataManager.TYPE_DATA_ITEM)) {
                return;
            }
            ItemBean itemBean = new ItemBean(1, TimeUtils.getNowTime(), TimeUtils.getNowTime(), name
                    , "", 1, false, 0, TimeUtils.getNowTime());
            dataManager.addItem(itemBean);
            viewHolder.input.setText("");
            InputSoftUtils.closeInputSoft(textView);
            viewHolder.recyclerView.scrollToPosition(dragListAdapter.ndatasize);
        }
    }

    public void OpenGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    private void doPickPhotoFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");  // 开启Pictures画面Type设定为image
        intent.setAction(Intent.ACTION_GET_CONTENT); //使用Intent.ACTION_GET_CONTENT这个Action
        startActivityForResult(intent, CHOOSE_PHOTO); //取得相片后返回到本画面
    }

    /**
     * 拍照获取相片
     **/
    private void doTakePhoto() {
        Uri imageUri = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用系统相机
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        //直接使用，没有缩小
        startActivityForResult(intent, TAKE_PHOTO);  //用户点击了从相机获取
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        uri = data.getData();
                        String realPath = FileUtils.getPath(this, uri);
                        if (realPath != null) {
                            new HandleImage().execute(realPath);
                        } else {
                            Toast.makeText(this, getString(R.string.getfail), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                break;
            case TAKE_PHOTO:  //拍照获取图片
                new HandleImage().execute(Environment.getExternalStorageDirectory()+"/temp.jpg");
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class HandleImage extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            showWaitingDialog(getString(R.string.zipping));
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Bitmap bitmap = new ImageCompress().compressImage(params[0], 400, 400, 512);
            return FileUtils.saveBitmap(bitmap);
        }

        @Override
        protected void onPostExecute(String path) {
            waitingDialog.cancel();

            if (path != null) {
                imagePath = path;
                setBitmapToView(path);
                UserBean userBean = DataManager.getInstance().getCurrentUser();
                uploadImage(userBean.name, "test.png", path);
            } else {
                System.out.println(getString(R.string.getfail));
            }

            super.onPostExecute(path);
        }
    }

    void setBitmapToView(String path) {
        Bitmap bitmap = getLocalBitmap(path);
        if (bitmap != null) {
            Bitmap circleBitmap = DrawCircle.getCircleBitmap(bitmap);
            ((ImageView) viewHolder.navigationView.getHeaderView(0).findViewById(R.id.image_user)).setImageBitmap(circleBitmap);
        }
    }

    public void uploadImage(String user, String imageName, String path) {
        Bitmap bitmap = getLocalBitmap(path);
        showWaitingDialog(getString(R.string.uploading));
        DataManager.getInstance().uploadImage(user, imageName, bitmap, new ResponseListener<String>() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, getString(R.string.uploadfail), Toast.LENGTH_SHORT).show();
                waitingDialog.cancel();
            }

            @Override
            public void onResponse(String response) {
//                response = response.substring(response.indexOf("img src="));
//                response = response.substring(8,response.indexOf("/>")) ;
                Log.v("xingling", "===========onResponse=========" + response);
                try {
                    ResponseHeaderBean bean = ProtocolManager.parseUploadImageJSON(response);
                    String url = bean.getUrl();
                    UserBean user = DataManager.getInstance().getCurrentUser();
                    user.imageUrl = url;
                    DataManager.getInstance().updateItem(user, user);
                } catch (ParseError parseError) {
                    parseError.printStackTrace();
                }
                waitingDialog.cancel();
                Toast.makeText(MainActivity.this, getString(R.string.uploadsuccess), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Bitmap getLocalBitmap(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showWaitingDialog(String message) {
        if (waitingDialog == null)
            waitingDialog = new ProgressDialog(this);

        waitingDialog.setTitle(getString(R.string.waitsecond));
        waitingDialog.setMessage(message + "...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }

    class ViewHolder {
        Toolbar toolbar;
        DrawerLayout drawer;
        NavigationView navigationView;
        BottomInput bottomInput;
        LinearLayout downInput;
        MyEditText input;
        ImageView sound;

        RecyclerView recyclerView;

        public ViewHolder(View view) {
            toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) view.findViewById(R.id.nav_view);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

            bottomInput = (BottomInput) view.findViewById(R.id.bottominput);
            downInput = (LinearLayout) bottomInput.findViewById(R.id.downinput);
            input = (MyEditText) downInput.findViewById(R.id.input);
            sound = (ImageView) downInput.findViewById(R.id.sound);
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemtouchhelper.startDrag(viewHolder);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewHolder.navigationView.getMenu().findItem(R.id.collect).setTitle(intent.getStringExtra("name"));
        }
    };

}
