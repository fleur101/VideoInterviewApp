package com.mirka.app.naimi.data;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miras on 9/17/2017.
 */

public class AppData {


    public static List<String> getQuestions() {
        // filling questions list
        List<String> list = new ArrayList<>();
        list.add("Расскажите вкратце о себе");
        list.add("Расскажите о своих профессиональных навыках");
        list.add("Каков ваш опыт работы в данной сфере?");
        list.add("Каковые ваши недостатки и достоинства?");
        list.add("Почему Вы выбрали эту площадку?");
        return list;
    }

    public static int getNumberOfQuestions(){
        return 5;
    }

    public static String base_filename = "section_";
    public static String base_filename_subs = "subs";

    public static String base_path = Environment.getExternalStoragePublicDirectory(
    Environment.DIRECTORY_PICTURES)+ File.separator +"MyCameraApp" + File.separator;

}
