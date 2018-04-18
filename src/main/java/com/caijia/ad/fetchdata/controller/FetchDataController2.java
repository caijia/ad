package com.caijia.ad.fetchdata.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.caijia.ad.fetchdata.entities.FetchAnswer;
import com.caijia.ad.fetchdata.entities.Question;
import com.caijia.ad.fetchdata.entities.QuestionAnalysis;
import com.caijia.ad.fetchdata.entities.QuestionAnswer;
import com.caijia.ad.fetchdata.repository.AnalysisRepo;
import com.caijia.ad.fetchdata.repository.AnswerRepo;
import com.caijia.ad.fetchdata.repository.QuestionRepo;
import com.caijia.ad.fetchdata.repository.QuestionTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class FetchDataController2 {

    private final QuestionRepo questionRepo;
    private final AnalysisRepo analysisRepo;
    private final AnswerRepo answerRepo;
    private final QuestionTypeRepo questionTypeRepo;

    @Autowired
    public FetchDataController2(QuestionRepo questionRepo, AnalysisRepo analysisRepo,
                                AnswerRepo answerRepo, QuestionTypeRepo questionTypeRepo) {
        this.questionRepo = questionRepo;
        this.analysisRepo = analysisRepo;
        this.answerRepo = answerRepo;
        this.questionTypeRepo = questionTypeRepo;
    }

    public static void main(String[] args) {
        String html1 = "{\\\"answer\\\":128,\\\"chapterId\\\":124,\\\"difficulty\\\":3,\\\"explain\\\":\\\"<p>电喷车是指装备电子控制燃油喷射系统的车辆。对于电喷车，下坡挂空挡并不能使油耗下降，反而会失去发动机制动能力。</p>\\\",\\\"id\\\":32957,\\\"label\\\":\\\"4.1.1.115\\\",\\\"mediaHeight\\\":0,\\\"mediaType\\\":0,\\\"mediaWidth\\\":0,\\\"optionType\\\":1,\\\"question\\\":\\\"长下坡禁止挂空挡，下列原因错误的是？\\\",\\\"questionId\\\":1160900,\\\"falseCount\\\":41181910,\\\"trueCount\\\":144828486,\\\"wrongRate\\\":0.22139574392390413,\\\"options\\\":[\\\"长下坡挂低速挡可以借助发动机控制车速\\\",\\\"避免因刹车失灵发生危险\\\",\\\"长下坡空挡滑行导致车速过高时，难以抢挂低速档控制车速\\\",\\\"下坡挂空挡，油耗容易增多\\\"],\\\"factAnswer\\\":[128],\\\"shuffleOptions\\\":[{\\\"answer\\\":64,\\\"label\\\":\\\"长下坡空挡滑行导致车速过高时，难以抢挂低速档控制车速\\\",\\\"isRight\\\":false},{\\\"answer\\\":32,\\\"label\\\":\\\"避免因刹车失灵发生危险\\\",\\\"isRight\\\":false},{\\\"answer\\\":128,\\\"label\\\":\\\"下坡挂空挡，油耗容易增多\\\",\\\"isRight\\\":true},{\\\"answer\\\":16,\\\"label\\\":\\\"长下坡挂低速挡可以借助发动机控制车速\\\",\\\"isRight\\\":false}],\\\"shuffleOptionsMap\\\":\\\"64,32,128,16\\\"";
        String html = "\\\"difficulty\\\":3,\\\"explain\\\":\\\"<p>有3种违法行为：<br/>1、打电话<br/>2、不系安全带<br/>3、遇前方拥堵走应急车道</p>\\\",\\\"id\\\":18752,\\\"label\\\":\\\"7.1.1.6\\\",\\\"mediaHeight\\\":285,\\\"mediaType\\\":2,\\\"mediaWidth\\\":350,\\\"optionType\\\":1,\\\"question\\\":\\\"动画6中有几种违法行为？\\\",\\\"questionId\\\":910300,\\\"mediaContent\\\":\\\"http://file.open.jiakaobaodian.com/tiku/res/910300.mp4\\\",\\\"falseCount\\\":46722986,\\\"trueCount\\\":241210631,\\\"wrongRate\\\":0.16226999294771474,\\\"options\\\":[\\\"一种违法行为\\\",\\\"二种违法行为\\\",\\\"三种违法行为\\\",\\\"四种违法行为\\\"],\\\"parseWidth\\\":299,\\\"parseHeight\\\":244,\\\"factAnswer\\\":[64],\\\"shuffleOptions\\\":[{\\\"answer\\\":64,\\\"label\\\":\\\"三种违法行为\\\",\\\"isRight\\\":true},{\\\"answer\\\":128,\\\"label\\\":\\\"四种违法行为\\\",\\\"isRight\\\":false},{\\\"answer\\\":32,\\\"label\\\":\\\"二种违法行为\\\",\\\"isRight\\\":false},{\\\"answer\\\":16,\\\"label\\\":\\\"一种违法行为\\\",\\\"isRight\\\":false}],\\\"shuffleOptionsMap\\\":\\\"64,128,32,16\\\",\\\"factAnswerLabel\\\":[\\\"A\\\"]},\\\"errorCode\\\":0,\\\"message\\\":null,\\\"success\\\":true}";
        Pattern p = Pattern.compile("\\\\\"explain\\\\\":\\\\\"(.*?)\\\\\",.*?" +
                "\\\\\"question\\\\\":\\\\\"(.*?)\\\\\"," +
                "(.*?\\\\\"mediaContent\\\\\":\\\\\"(.*?)\\\\\",.*?|.*?)" +
                "\\\\\"shuffleOptions\\\\\":(.*),\\\\\"shuffleOptionsMap");
        Matcher m = p.matcher(html1);
        if (m.find()) {
            System.out.println(m.group(3));
            System.out.println(m.group(4));


        } else {
            System.out.println("no find");
        }
    }

    @RequestMapping("/jkfetchAllData4")
    public @ResponseBody
    List<String> fetchAllData4() {
        List<String> subject1 = readFileSubject("jkbd_subject4.json");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < subject1.size(); i++) {
            list.add(get((i + 1) + "", subject1.get(i), 4));
        }
        return list;
    }

    @RequestMapping("/jkfetchAllData1")
    public @ResponseBody
    List<String> fetchAllData1() {
        List<String> subject1 = readFileSubject("jkbd_subject1.json");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < subject1.size(); i++) {
            list.add(get((i + 1) + "", subject1.get(i), 1));
        }
        return list;
    }

    @RequestMapping("/jkfetchData")
    public @ResponseBody
    String fetchData(@RequestParam(value = "key") String key) {
        return get("", key, 1);
    }

    private String get(String index, String key, int subject) {
        String result = "";
        String msg = "error = " + key + "--index = " + index;
        try {
            URL url = new URL(String.format("http://www.jiakaobaodian.com/mnks/exercise/0-car-kemu1.html?id=%s", key));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                result = streamToString(in).trim();
                boolean b = insertDb(key, result, subject);
                msg = (b ? "success = " : "error = ") + key + "--index = " + index;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    //shuffleOptions, question ,explain //www.jiakaobaodian.com
    private boolean insertDb(String key, String html, int subject) {
        Pattern p = Pattern.compile("\\\\\"explain\\\\\":\\\\\"(.*?)\\\\\",.*?" +
                "\\\\\"question\\\\\":\\\\\"(.*?)\\\\\"," +
                "(.*?\\\\\"mediaContent\\\\\":\\\\\"(.*?)\\\\\",.*?|.*?)" +
                "\\\\\"shuffleOptions\\\\\":(.*),\\\\\"shuffleOptionsMap");
        Matcher m = p.matcher(html);
        if (m.find()) {
            String analysis = m.group(1);
//            analysis = analysis.replaceAll("<p>|</p>|</span>|<span style=.*?>", "");
            String questionText = m.group(2);
            String imgUrl = m.group(4);
            String json = m.group(5);
            json = json.replaceAll("\\\\\"", "\"").replaceAll("[\t\r\n]", "");
            List<FetchAnswer> answerList = JSON.parseArray(json, FetchAnswer.class);

            //问题
            Question q = new Question();
            q.setQuestionId(key);
            q.setQuestionSubject((short) subject);
            q.setQuestionText(questionText);
            q.setQuestionUrl(imgUrl);

            //分析
            QuestionAnalysis a = new QuestionAnalysis();
            a.setAnalysisText(analysis);
            q.addAnalysis(a);

            //答案
            saveAnswer(q, answerList);
            questionRepo.save(q);
            return true;
        }
        return false;
    }

    private void saveAnswer(Question s, List<FetchAnswer> answerList) {
        for (FetchAnswer fetchAnswer : answerList) {
            QuestionAnswer answerEntity = new QuestionAnswer();
            answerEntity.setAnswerText(fetchAnswer.getLabel());
            answerEntity.setAnswerOk((short) (fetchAnswer.isRight() ? 1 : 0));
            answerEntity.setAnswerOrder((short) fetchAnswer.getAnswer());
            s.addAnswer(answerEntity);
        }
    }

    private List<String> readFileSubject(String filePath) {
        List<String> keyList = new ArrayList<>();
        try {
            InputStream i = new FileInputStream(filePath);
            String content = streamToString(i);
            JSONArray array = JSONArray.parseArray(content);
            int size = array.size();
            for (int j = 0; j < size; j++) {
                keyList.add(array.getString(j));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyList;
    }

    private static String streamToString(InputStream stream) {
        ByteArrayOutputStream out = null;
        BufferedInputStream in = null;
        String s = "";
        try {

            in = new BufferedInputStream(stream);
            out = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024 * 8];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            s = new String(out.toByteArray());

        } catch (Exception e) {

        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {

            }
        }
        return s;
    }
}
