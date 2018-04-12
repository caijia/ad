package com.caijia.ad.fetchdata.controller;


import com.alibaba.fastjson.JSON;
import com.caijia.ad.fetchdata.entities.*;
import com.caijia.ad.fetchdata.repository.AnalysisRepo;
import com.caijia.ad.fetchdata.repository.AnswerRepo;
import com.caijia.ad.fetchdata.repository.QuestionRepo;
import com.caijia.ad.fetchdata.repository.QuestionTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class FetchDataController {

    private final QuestionRepo questionRepo;

    private final AnalysisRepo analysisRepo;

    private final AnswerRepo answerRepo;
    private final QuestionTypeRepo questionTypeRepo;

    private static final String PREFIX = "http://mnks.jxedt.com/get_question?r=0.5811613514581165&&index=";
    private static final String IMAGE_PREFIX = "http://ww3.sinaimg.cn/mw600/";
    private static final String IMAGE_PREFIX1 = "http://img.58cdn.com.cn/jxedt/img/video/";

    @Autowired
    public FetchDataController(QuestionRepo questionRepo, AnalysisRepo analysisRepo,
                               AnswerRepo answerRepo,QuestionTypeRepo questionTypeRepo) {
        this.questionRepo = questionRepo;
        this.analysisRepo = analysisRepo;
        this.answerRepo = answerRepo;
        this.questionTypeRepo = questionTypeRepo;
    }

    @RequestMapping("/saveQuestionType")
    public @ResponseBody List<String> saveQuestionType() {
        List<String> s = new ArrayList<>();
        readFile("subject1.txt");
        return s;
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

    @RequestMapping("/fetchChapterType")
    public @ResponseBody List<String> fetchChapterType() {
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 365; i++) {
            QuestionTypeEntity typeEntity = new QuestionTypeEntity();
            typeEntity.setQuestionId((short) (i+1));
            typeEntity.setQuestionType((short) 30);
            questionTypeRepo.save(typeEntity);
        }

        for (int i = 2541; i < 2640; i++) {
            QuestionTypeEntity typeEntity = new QuestionTypeEntity();
            typeEntity.setQuestionId((short) (i+1));
            typeEntity.setQuestionType((short) 30);
            questionTypeRepo.save(typeEntity);
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
                insertDatabase(result,subject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error = " + index;
        }
        return "success = " + index;
    }

    private void insertDatabase(String result, int subject) {
        String s1 = result.replaceAll("\\\\", "");
        Driver driver = JSON.parseObject(s1, Driver.class);
        int type = driver.getType();

        QuestionEntity s = new QuestionEntity();
        s.setId(driver.getId());
        s.setQuestionText(driver.getQuestion());
        if (!StringUtils.isEmpty(driver.getSinaimg())) {
            s.setQuestionImg(IMAGE_PREFIX + driver.getSinaimg());

        }else if (!StringUtils.isEmpty(driver.getImageurl())){
            s.setQuestionImg(IMAGE_PREFIX1 + driver.getImageurl());
        }

        //分析
        AnalysisEntity analysisEntity = new AnalysisEntity();
        analysisEntity.setAnalysisText(driver.getBestanswer());
        analysisEntity.setId(driver.getId());
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

    private void readFile(String filePath) {
        try {
            InputStream i = new FileInputStream(filePath);
            String content = streamToString(i);
            String[] types = content.split("：");
            for (int j = 0; j < types.length; j++) {
                Pattern pattern = Pattern.compile("get_question\\?r=.*?&index=(\\d+)");
                Matcher matcher = pattern.matcher(types[j]);
                while (matcher.find()) {
                    int questionId = Integer.parseInt(matcher.group(1));
                    int type = j + 10;
                    System.out.println("questionId = " + questionId + "--type" + type);
                    QuestionTypeEntity typeEntity = new QuestionTypeEntity();
                    typeEntity.setQuestionId(questionId);
                    typeEntity.setQuestionType(type);
                    questionTypeRepo.save(typeEntity);
                }
                System.out.println("-----------------------------------------");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
