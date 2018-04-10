package com.caijia.ad.fetchdata.controller;


import com.alibaba.fastjson.JSON;
import com.caijia.ad.fetchdata.entities.AnalysisEntity;
import com.caijia.ad.fetchdata.entities.AnswerEntity;
import com.caijia.ad.fetchdata.entities.Driver;
import com.caijia.ad.fetchdata.entities.QuestionEntity;
import com.caijia.ad.fetchdata.repository.AnalysisRepo;
import com.caijia.ad.fetchdata.repository.AnswerRepo;
import com.caijia.ad.fetchdata.repository.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FetchDataController {

    private final QuestionRepo questionRepo;

    private final AnalysisRepo analysisRepo;

    private final AnswerRepo answerRepo;

    private static final String PREFIX = "http://mnks.jxedt.com/get_question?r=0.5811656514581165&&index=";
    private static final String IMAGE_PREFIX = "http://ww3.sinaimg.cn/mw600/";
    private static final String IMAGE_PREFIX1 = "http://img.58cdn.com.cn/jxedt/img/video/";

    @Autowired
    public FetchDataController(QuestionRepo questionRepo, AnalysisRepo analysisRepo, AnswerRepo answerRepo) {
        this.questionRepo = questionRepo;
        this.analysisRepo = analysisRepo;
        this.answerRepo = answerRepo;
    }

    @RequestMapping("/fetchAllData")
    public @ResponseBody List<String> fetchAllData() {
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 1330; i++) {
            s.add(fetch(i + 1,1));
        }
        return s;
    }

    @RequestMapping("/fetchSubject4Data")
    public @ResponseBody List<String> fetchSubject4Data() {
        List<String> s = new ArrayList<>();
        for (int i = 1537; i < 1537 + 1131; i++) {
            s.add(fetch(i + 1,4));
        }
        return s;
    }

    @RequestMapping("/fetchData")
    public @ResponseBody String fetchData(@RequestParam(value = "index") int index,
                                          @RequestParam(value = "subject") int subject) {
        return fetch(index,subject);
    }

    private String fetch(int index,int subject) {
        try {
            URL url = new URL(PREFIX + index);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                String result = streamToString(in);
                System.out.println(result);
                insertDatabase(result,subject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error = " + index;
        }
        return "success = " + index;
    }

    private void insertDatabase(String result, int subject) {
        Driver driver = JSON.parseObject(result, Driver.class);
        int type = driver.getType();

        QuestionEntity s = new QuestionEntity();
        s.setId((short) driver.getId());
        s.setQuestionText(driver.getQuestion());
        if (!StringUtils.isEmpty(driver.getSinaimg())) {
            s.setQuestionImg(IMAGE_PREFIX + driver.getSinaimg());

        }else if (!StringUtils.isEmpty(driver.getImageurl())){
            s.setQuestionImg(IMAGE_PREFIX1 + driver.getImageurl());
        }

        s.setQuestionSubject((short) subject);

        //分析
        AnalysisEntity analysisEntity = new AnalysisEntity();
        analysisEntity.setAnalysisText(driver.getBestanswer());
        analysisEntity.setId((short) driver.getId());
        s.addAnalysis(analysisEntity);

        switch (type) {
            case 3:
            case 2: {
                String a = driver.getA();
                String b = driver.getB();
                String c = driver.getC();
                String d = driver.getD();
                String ta = driver.getTa();
                String[] array = {a, b, c, d};
                saveAnswer(s, ta, array);
                break;
            }

            case 1:{ //判断题
                String a = "正确";
                String b = "错误";
                String ta = driver.getTa();
                String[] array = {a, b};
                saveAnswer(s, ta, array);
                break;
            }
        }
        questionRepo.save(s);
    }

    private void saveAnswer(QuestionEntity s, String ta, String[] array) {
        for (int i = 0; i < array.length; i++) {
            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setAnswerText(array[i]);
            answerEntity.setIsOk((short) (ta.contains(String.valueOf(i+1)) ? 1 : 0));
            s.addAnswer(answerEntity);
        }
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
