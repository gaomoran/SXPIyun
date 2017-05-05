package com.mrg.sxpiyun.util;


import android.os.Environment;

import com.mrg.sxpiyun.modle.Results;
import com.mrg.sxpiyun.modle.Schedle;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by MrG on 2016-11-11.
 */
public class HttpHelper {
    private String loginurl1 = "http://jw.sxpi.edu.cn/default_sxgy.aspx";
    private String loginurl2="http://192.168.100.4/default_sxgy.aspx";
    private String loginurl3="http://jw.sxpi.edu.cn/login.aspx";

    /**
     * 内部网络登陆
     * @param username
     * @param pwd
     * @param loginurl
     * @param map
     * @return
     * @throws IOException
     */
    private Map<String,String> login(String username,String pwd,String loginurl,Map<String, String> map) throws IOException {

        Map<String, String> cookie = new HashMap<String, String>();

        Connection.Response response = Jsoup.connect(loginurl).data(map).method(
                    Connection.Method.POST)
                    .timeout(20000)
                    .followRedirects(false)// 禁止重定向
                    .execute();


        if (response.statusCode() == 302)// 登陆成功
        {
            // 保存cook信息
            cookie = response.cookies();
            System.out.println("成功" + cookie);



        }else {
            System.out.println("登陆失败__");
        }

        return cookie;
    }

    /**
     * 多路径登陆
     * @param username
     * @param pwd
     * @return
     */
    public Map<String,String> loginCon(String username,String pwd){
        Map<String, String> cookie=null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("__VIEWSTATE", "dDwtMTk1NDQ2ODI2NTt0PDtsPGk8MT47PjtsPHQ8O2w8aTwxMT47PjtsPHQ8cDw7cDxsPG9uY2xpY2s7PjtsPHdpbmRvdy5jbG9zZSgpXDs7Pj4+Ozs+Oz4+Oz4+O2w8SW1hZ2VCdXR0b24xO0ltYWdlYnV0dG9uMzs+PkXWptKHq7Z6WHqQX9M0ICZ2gyw4");
        map.put("yh", username);
        map.put("kl", pwd);
        map.put("RadioButtonList1","学生");
        map.put("ImageButton1.x", "33");
        map.put("ImageButton1.y", "7");

        Map<String, String> data = new HashMap<String, String>();
        data.put("__VIEWSTATE", "dDw4MTI3MTI0O3Q8O2w8aTwxPjs+O2w8dDw7bDxpPDc+Oz47bDx0PHA8O3A8bDxvbmNsaWNrOz47bDx3aW5kb3cuY2xvc2UoKVw7Oz4+Pjs7Pjs+Pjs+Pjs+o2v8lNTk6DCofPbLG8LPM8gvf3c=");
        data.put("tbYHM", username);
        data.put("tbPSW", pwd);
        data.put("Button1","登陆");

        try {
            //正常登陆
            System.out.println("正常登陆");
            cookie=login(username,pwd,loginurl1,map);
           /* if (cookie==null||cookie.size()==0){
                //内网登陆
                System.out.println("内网登陆");
               cookie=login(username,pwd,loginurl2,map);
           }
            if (cookie==null||cookie.size()==0){
                //特殊登陆
                System.out.println("特殊登陆");
                cookie=login(username,pwd,loginurl3,data);
            }*/



        } catch (IOException e) {
            e.printStackTrace();
        }

        return cookie;

    }

    /**
     * 从网络中获取一个课程表
     * @param cookie
     * @return
     * @throws IOException
     */
    public Document getSchedule(Map<String,String> cookie ,String user) throws IOException {
        String selectschedule = "http://jw.sxpi.edu.cn/xsgrkb.aspx?xh="+user+"&type=xs";
        Document document = Jsoup.connect(
                selectschedule).timeout(20000)
                .cookies(cookie) // 这个就是获取的cookies
                .get();

        sevakebiao(document);
       return document;
    }

    /**
     * 保存课程表本地
     * @param document
     */
    public void sevakebiao(Document document){


        String PATH = Environment.getExternalStorageDirectory().toString();
        File ff = new File( PATH+"/SXPIyun/kebiao/");
        FileOutputStream fOut = null;
        if (!ff.exists()) {
            //按照指定的路径创建文件夹
            ff.mkdirs();
        }
        File f = new File( PATH+"/SXPIyun/kebiao/"+UserAdmin.username+ ".html");
        if (f.exists()){
            f.delete();
        }
        try{
            fOut=new FileOutputStream(f);
            fOut.write(document.html().getBytes("gb2312"));
            System.out.println("保存课表！");
            fOut.close();

        }catch (IOException e){
            e.printStackTrace();
        }


    }



    /**
     * 由一个Document对象获得一个课程表
     * @param doc
     * @return
     */
    public List<Schedle> getSchedule(Document doc){
        List<Schedle> schedles = new ArrayList<>();
        Element table = doc.getElementById("table6");

        Elements tr = table.select("tr");

        int dijijie=0;
        for (int i=0;i<18;i++){

            if (i==0||i==1||i==6||i==11||i==14||i==15||i==16){

                continue;

            }else {
                dijijie++;

            }
            int xinqi= 0;
            Elements value = tr.get(i).getElementsByAttributeValue("align", "Center");
            for (Element td:value){
                xinqi++;
                if (td.text().length()>8) {
                    Schedle schedle = new Schedle();
                    schedle.setCTL(td.html());
                    schedle.setWeek(xinqi);
                    schedle.setNo(dijijie);
                    schedles.add(schedle);
                }

            }

        }
        return schedles;


    }

    /**
     * 从网络获取个人信息
     * @param cookie
     * @return
     * @throws IOException
     */
    public HashMap<String,String> getTureInfo(Map<String,String> cookie) throws IOException {
        String selectschedule = "http://jw.sxpi.edu.cn/xstop.aspx";

        Document document = Jsoup.connect(
                selectschedule).timeout(20000)
                .cookies(cookie) // 这个就是获取的cookies
                .get();
        //名字
        String name = document.getElementById("lbXM").text();
        //专业
        String professional = document.getElementById("lbZYMC").text();
        //班级
        String classs = document.getElementById("lbBJMC").text();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name",name);
        map.put("professional",professional);
        map.put("class",classs);

        return map;

    }

    /**
     * 从网络上获取成绩
     * @param cookie
     * @param user
     * @return
     * @throws IOException
     */
    public Document selectrseults(Map<String,String> cookie ,String user) throws IOException {
        String url="http://jw.sxpi.edu.cn/xscj.aspx?xh="+user;
        Document document = Jsoup.connect(
                url).timeout(20000)
                .cookies(cookie)
                .get();
        //保存成绩
        sevachenji(document);
        return document;
    }
    /**
     * 保存成绩本地
     * @param document
     */
    public void sevachenji(Document document){


        String PATH = Environment.getExternalStorageDirectory().toString();
        File ff = new File( PATH+"/SXPIyun/chenji/");
        FileOutputStream fOut = null;
        if (!ff.exists()) {
            //按照指定的路径创建文件夹
            ff.mkdirs();
        }
        File f = new File( PATH+"/SXPIyun/chenji/"+UserAdmin.username+ ".html");
        if (f.exists()){
            f.delete();
        }
        try{
            fOut=new FileOutputStream(f);
            fOut.write(document.html().getBytes("gb2312"));
            System.out.println("保存成绩！");
            fOut.close();

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    /**
     * 根据document 获取成绩数据
     * @param document
     * @return
     */
    public List<Results> getResults(Document document){
        Element element = document.getElementById("DataGrid1");
        List<Results> resultses = new ArrayList<Results>();



        Elements tr = element.select("tr");
        for (Element tr1:tr){


            Elements td = tr1.select("td");
            Results results = new Results();
            int flag=0;
            for (Element td1:td){

                if (flag==0){
                    results.setXuenian(td1.text());
                }else if (flag==1){
                    results.setXueqi(td1.text());
                }else if (flag==2){
                    results.setKechengming(td1.text());
                }else if (flag==3){
                    results.setKechengtype(td1.text());
                }else if (flag==4){
                    results.setTeacher(td1.text());
                }else if (flag==5){
                    results.setKaohefangshi(td1.text());
                }else if (flag==6){
                    results.setZongping(td1.text());
                }else if (flag==7){
                    results.setBukao(td1.text());
                }else if (flag==8){
                    results.setChongxiu(td1.text());
                }else if (flag==9){
                    results.setYingdexuefeng(td1.text());
                }else if (flag==10){
                    results.setXuefengjidian(td1.text());
                }
                flag++;

            }
            resultses.add(results);

        }
        //删除第一行
        resultses.remove(0);

        return resultses;

    }

}
