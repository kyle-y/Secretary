package com.example.yxb.secretary.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.bean.MyUser;
import com.example.yxb.secretary.common.Codes;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.interfaces.IchangeIcon;
import com.example.yxb.secretary.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/24
 * MODIFY_BY:
 */
public class PersonalSettingActivity extends BaseActivity implements View.OnClickListener{
    private TextView text_personal_nickname,text_logout;
    private ImageView image_personal_icon,image_back_personal_setting;
    private TextView text_photo,text_picture,text_cancel;
    private View popupView;
    private LinearLayout layout_personal_setting,text_change_icon,text_change_nickname,text_change_password;
    private File targetFile;
    private BmobFile avatarFile;
    private PopupWindow popupWindow;
    private MyUser user;
    private String url,name;
    private static IchangeIcon ichangeIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        text_personal_nickname = (TextView) findViewById(R.id.text_personal_nickname);
        image_personal_icon = (ImageView) findViewById(R.id.image_personal_icon);
        layout_personal_setting = (LinearLayout) findViewById(R.id.layout_personal_setting);
        image_back_personal_setting = (ImageView) findViewById(R.id.image_back_personal_setting);

        text_change_icon = (LinearLayout) findViewById(R.id.text_change_icon);
        text_change_password = (LinearLayout) findViewById(R.id.text_change_password);
        text_change_nickname = (LinearLayout) findViewById(R.id.text_change_nickname);
        text_logout = (TextView) findViewById(R.id.text_logout);

        text_change_icon.setOnClickListener(this);
        text_change_password.setOnClickListener(this);
        text_change_nickname.setOnClickListener(this);
        text_logout.setOnClickListener(this);
        image_back_personal_setting.setOnClickListener(this);

        //加载图片的popupWindow
        popupView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.popupwindow_photo, null);
        text_photo = (TextView) popupView.findViewById(R.id.text_photo);
        text_picture = (TextView) popupView.findViewById(R.id.text_picture);
        text_cancel = (TextView) popupView.findViewById(R.id.text_cancel);

        text_photo.setOnClickListener(this);
        text_picture.setOnClickListener(this);
        text_cancel.setOnClickListener(this);

        user = BmobUser.getCurrentUser(MyUser.class);
        //更新头像和昵称
        if (MyApplication.isLoaded()) {
            String nickName = (String) BmobUser.getObjectByKey("nickName");
            String iconUrl = (String) BmobUser.getObjectByKey("iconUrl");
            if(nickName != null){
                text_personal_nickname.setText(nickName);
            }else{
                text_personal_nickname.setText("昵称");
            }
            if(nickName != null){
                Picasso.with(MyApplication.getContext()).load(iconUrl).into(image_personal_icon);
            }else{
                image_personal_icon.setImageResource(R.drawable.menu_4);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_back_personal_setting:
                PreferencesUtils.putString(MyApplication.getContext(),"nickname",name);
                PreferencesUtils.putString(MyApplication.getContext(),"iconUrl",url);
                if (ichangeIcon != null) {
                    ichangeIcon.changeIcon();
                }
                finish();
                break;
            case R.id.text_change_icon:
                showPopupWindow(popupView, layout_personal_setting);
                break;
            case R.id.text_change_password:
                startActivity(new Intent(MyApplication.getContext(),ChangePasswordActivity.class));
                break;
            case R.id.text_change_nickname:
                startActivityForResult(new Intent(MyApplication.getContext(),ChangeNikenameActivity.class), 50);
                break;
            case R.id.text_logout:
                user.logOut();
                MyApplication.setIsLoaded(false);
                PreferencesUtils.putBoolean(MyApplication.getContext(),"isLogin",false);
                if (ichangeIcon != null) {
                    ichangeIcon.changeIcon();
                }
                startActivity(new Intent(MyApplication.getContext(),LoginActivity.class));
                finish();
                break;
            case R.id.text_photo:
                Intent popintent = new Intent(Intent.ACTION_GET_CONTENT);
                popintent.addCategory(Intent.CATEGORY_OPENABLE);
                popintent.setType("image/*");
                popintent.putExtra("crop", "true");
                popintent.putExtra("aspectX", 1);
                popintent.putExtra("aspectY", 1);
                popintent.putExtra("outputX", 80);
                popintent.putExtra("outputY", 80);
                popintent.putExtra("data", true);
                startActivityForResult(popintent, 30);
                break;
            case R.id.text_picture:
                Intent popintentpictures = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //调取系统摄像头界面
                String state = Environment.getExternalStorageState();
                targetFile = null;
                File dir;
                //判断外部存储设备是否挂载
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    dir = Environment.getExternalStorageDirectory();
                    //文件夹
                    dir = new File(dir, "MediaRecorder/Images");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                } else {
                    dir = getFilesDir();
                }

                targetFile = new File(dir, "Image-" + System.currentTimeMillis() + ".jpg");

                Uri uri = Uri.fromFile(targetFile);
                popintentpictures.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(popintentpictures, 40);
                break;
            case R.id.text_cancel:
                popupWindow.dismiss();
                break;

        }
    }

    private void showPopupWindow(View popupView, View parent) {
        popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 30) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                if (bitmap != null) {
                    image_personal_icon.setScaleType(ImageView.ScaleType.FIT_XY);
                    image_personal_icon.setImageBitmap(bitmap);
                    popupWindow.dismiss();
                    //将图片存到本地，并上传的服务器
                    File dir = this.getExternalCacheDir();
                    File file = new File(dir, "Image-" + System.currentTimeMillis() + ".jpg");
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
                        out.flush();
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final String[] iconUrl = {null};
                    avatarFile = new BmobFile(new File(file.getAbsolutePath()));
                    avatarFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                iconUrl[0] = avatarFile.getFileUrl();
                                MyUser newUser = new MyUser();
                                url = iconUrl[0];
                                newUser.setIconUrl(iconUrl[0]);
                                newUser.setGoldBeanNum(user.getGoldBeanNum());
                                addSubscription(newUser.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {

                                    }
                                }));
                            } else {
                            }
                        }
                    });
                }
            }
        }
        if (requestCode == 40) {
            if (resultCode == RESULT_OK) {
                if (targetFile != null && targetFile.exists()) {
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inSampleSize = 4;//压缩加载
                    Bitmap bitmap = BitmapFactory.decodeFile(targetFile.getAbsolutePath(), opt);
                    image_personal_icon.setImageBitmap(bitmap);
                    popupWindow.dismiss();
                    //上传数据到服务器
                    final String[] iconUrl = {null};
                    avatarFile = new BmobFile(new File(targetFile.getAbsolutePath()));
                    avatarFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                iconUrl[0] = avatarFile.getFileUrl();
                                MyUser newUser = new MyUser();
                                url = iconUrl[0];
                                newUser.setIconUrl(iconUrl[0]);
                                newUser.setGoldBeanNum(user.getGoldBeanNum());
                                addSubscription(newUser.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {

                                    }
                                }));
                            } else {
                            }
                        }
                    });

                }
            }
        }
        if (requestCode == 50 && resultCode == Codes.RESULT_CODE_WEATHER){
            String name = data.getStringExtra("nickname");
            text_personal_nickname.setText(name);
            MyUser newUser = new MyUser();
            this.name = name;
            newUser.setNickName(name);
            newUser.setGoldBeanNum(user.getGoldBeanNum());
            addSubscription(newUser.update(user.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {

                }
            }));
        }
    }

    public static void setIchangeIcon(IchangeIcon ichangeIcon) {
        PersonalSettingActivity.ichangeIcon = ichangeIcon;
    }
}
