package com.caijia.ad.fetchdata.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.caijia.ad.fetchdata.entities.*;
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

    @RequestMapping("/setQuestionType")
    public @ResponseBody
    List<String> setQuestionType(@RequestParam(value = "subject") int subject) {
        String filePath = String.format("subject%dspecial.json", subject);
        List<String> list = new ArrayList<>();
        try {
            InputStream i = new FileInputStream(filePath);
            String content = streamToString(i);
            JSONArray array = JSON.parseArray(content);
            int size = array.size();
            for (int j = 0; j < size; j++) {//parent type
                JSONArray jsonArray = array.getJSONArray(j);
                int size1 = jsonArray.size();
                for (int k = 0; k < size1; k++) { //child type
                    JSONArray jsonArray1 = jsonArray.getJSONArray(k);
                    int size2 = jsonArray1.size();
                    for (int l = 0; l < size2; l++) {
                        int questionId = jsonArray1.getIntValue(l);
                        QuestionType questionType = new QuestionType();
                        questionType.setType((short) (subject * 1000 + j));
                        questionType.setQuestionId(questionId + "");
                        questionType.setChildType(subject * 1000 + j * 100 + k);
                        questionTypeRepo.save(questionType);
                        list.add("success questionId = " + questionId);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            list.add("error questionId = ");
        }
        return list;
    }

    @RequestMapping("/setQuestionChapter")
    public @ResponseBody
    List<String> setQuestionChapter(@RequestParam(value = "subject") int subject) {
        String filePath = String.format("subject%dchapter.json", subject);
        List<String> list = new ArrayList<>();
        try {
            InputStream i = new FileInputStream(filePath);
            String content = streamToString(i);
            JSONArray array = JSON.parseArray(content);
            int size = array.size();
            for (int j = 0; j < size; j++) {
                JSONArray jsonArray = array.getJSONArray(j);
                int size1 = jsonArray.size();
                for (int k = 0; k < size1; k++) {
                    int questionId = jsonArray.getIntValue(k);
                    questionRepo.updateQuestionChapter(subject * 100 + j, questionId + "", subject);
                    list.add("success questionId = " + questionId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            list.add("error questionId = ");
        }
        return list;
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
