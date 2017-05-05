package com.mrg.sxpiyun.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxg.sxpi.ui.R;
import com.mrg.sxpiyun.db.LianXiRenBD;
import com.mrg.sxpiyun.util.BlurImageView;
import com.mrg.sxpiyun.util.FriendInfo;
import com.mrg.sxpiyun.util.TouXiangGet;
import com.mrg.sxpiyun.util.UserAdmin;

import java.io.FileNotFoundException;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public class MyFragment extends Fragment {

    private TouXiangGet touXiangGet;
    private FriendInfo friendInfo;
    public final int NICHENGSET = 100;
    public final int TOUXIANGSET = 200;
    public final int TOUXIANGREMOVEOLD = 300;
    public final int TOUXIANGREFLAS = 400;
    private TextView nichen;
    private final int RESULT_LOAD_IMAGE = 2;
    private ImageView mytouxiang;
    private RelativeLayout txbg;
    private Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TOUXIANGSET:
                    txbg.setBackground(txgbddrawable);
                    mytouxiang.setImageBitmap(touxiang);

                    break;
                case NICHENGSET:
                    nichen.setText(niCheng);
                    break;
                case TOUXIANGREMOVEOLD:
                    touXiangGet.removeTouXiang(UserAdmin.username);
                    Toast.makeText(getActivity(),"修改头像成功",Toast.LENGTH_SHORT).show();
                    break;
                case TOUXIANGREFLAS:
                    Toast.makeText(getActivity(),"修改头像失败",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    private Bitmap touxiang;
    private Drawable txgbddrawable;
    private String niCheng;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        LianXiRenBD lianXiRenBD = new LianXiRenBD(getActivity());
        touXiangGet = new TouXiangGet();
        friendInfo = new FriendInfo();
        txbg = (RelativeLayout) view.findViewById(R.id.rl_My_touxiangbg);
        mytouxiang = (ImageView) view.findViewById(R.id.img_My_touxiang);
        nichen = (TextView) view.findViewById(R.id.tv_My_nichen);

        //耗时操作
        new Thread() {

            @Override
            public void run() {
                super.run();

                touxiang = touXiangGet.getTouxiang(UserAdmin.username, getActivity());
                txgbddrawable = BlurImageView.BlurImages(touxiang, getActivity());
                niCheng =UserAdmin.nichen;
                Message message = handler.obtainMessage();
                Message message2 = handler.obtainMessage();
                message.what=TOUXIANGSET;
                message2.what=NICHENGSET;
                handler.sendMessage(message);
                handler.sendMessage(message2);
            }

        }.start();


        nichen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetValueActivity.class);
                intent.putExtra("type", "昵称");
                startActivityForResult(intent, NICHENGSET);
            }
        });
        mytouxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开手机图片库
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        return view;
    }

    @TargetApi(JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //修改昵称
        if (requestCode == NICHENGSET && resultCode == NICHENGSET) {
            new Thread(){
                @Override
                public void run() {

                    niCheng=friendInfo.getNiCheng(UserAdmin.username);
                    UserAdmin.nichen=friendInfo.getNiCheng(UserAdmin.username);
                    Message message = handler.obtainMessage();
                    message.what=NICHENGSET;
                    handler.sendMessage(message);
                }
            }.start();

            //修改头像
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1) {
            System.out.println("修改头像");
            final Uri selectedImage = data.getData();
                new Thread(){
                    @Override
                    public void run() {
                        Bitmap  bitmap = null;
                        try {
                            //根据url生成bitmap图片
                            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage));
                            //压缩图片
                            touxiang = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);
                            txgbddrawable = BlurImageView.BlurImages(touxiang, getActivity());
                            if (friendInfo.setTouXiang(touxiang)) {
                                Message message = handler.obtainMessage();
                                Message message2 = handler.obtainMessage();
                                message.what = TOUXIANGSET;
                                message2.what = TOUXIANGREMOVEOLD;
                                handler.sendMessage(message);
                                handler.sendMessage(message2);
                            }else{
                                Message message = handler.obtainMessage();
                                message.what = TOUXIANGREFLAS;
                                handler.sendMessage(message);

                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }



                    }
                }.start();

        }
        System.out.println("数字" + requestCode + "vv" + resultCode);


    }
}


