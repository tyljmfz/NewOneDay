package com.example.newoneday.bomb.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by yongg on 2019/3/6.
 */

public class BmobPicture extends BmobObject {

    private BmobFile image;

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }
}
