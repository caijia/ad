package com.caijia.ad.fetchdata.controller;

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

    @Autowired
    public FetchDataController(QuestionRepo questionRepo, AnalysisRepo analysisRepo,
                               AnswerRepo answerRepo, QuestionTypeRepo questionTypeRepo) {
        this.questionRepo = questionRepo;
        this.analysisRepo = analysisRepo;
        this.answerRepo = answerRepo;
        this.questionTypeRepo = questionTypeRepo;
    }


    @RequestMapping("/fetchData")
    public @ResponseBody
    String fetchData(@RequestParam(value = "key") String key,
                     @RequestParam(value = "subject") int subject) {
        return get(key, subject);
    }

    @RequestMapping("/fetchAllData")
    public @ResponseBody
    List<String> fetchData(@RequestParam(value = "subject") int subject) {
        List<String> subject4 = readKeyList("subject4");
        List<String> list = new ArrayList<>();
        for (String s : subject4) {
            list.add(get(s, subject));
        }
        return list;
    }

    private String get(String key, int subject) {
        String result = "";
        try {
            URL url = new URL(String.format("http://tiba.jsyks.com/Post/%s.htm", key));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                result = streamToString(in).trim();
                Pattern p = Pattern.compile("[\t\r\n]");
                Matcher m = p.matcher(result);
                result = m.replaceAll("");
                insertDb(key,result, subject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error = " + key;
        }
        return "success = " + key;
    }

    private void insertDb(String key,String html, int subject) {
        Pattern p = Pattern.compile("试题分析</a></span></div><div class=\"mainL\" id=\"Content\">" +
                "<div id=\"question\" class=\"fcc\"><h1><strong>(.*?)</strong>(.*?)答案：<u>(.*?)</u>" +
                "</h1><p><i title=\"人气指数\" id=\"ReadCount\"></i></p><b class=\"bg\">" +
                "</b></div><style>.qxl a:hover \\{background:#36A803;color:#FFFFFF;}" +
                "</style><div id=\"answer\" class=\"fcc\"><em>最佳分析</em><h2>(.*?)</h2>");
        Matcher m = p.matcher(html);
        if (m.find()) {
            String questionText = m.group(1);
            String answers = m.group(2);
            String realAnswer = m.group(3);
            String analysis = m.group(4);
            String imgUrl = "";
            String[] answerList;
            if (realAnswer.equals("对") || realAnswer.equals("错")) {
                //判断题
                answerList = new String[2];
                answerList[0] = "正确";
                answerList[1] = "错误";

            } else {
                Pattern p1 = Pattern.compile("<span><img onclick=\"vExamTp\\(this\\);\" src='(.*?)'></span>");
                Matcher matcher = p1.matcher(answers);
                if (matcher.find()) {
                    imgUrl = matcher.group(1);
                    answers = answers.replace(matcher.group(0), "");
                }
                answers = answers.replaceAll("<b>|</b>|<br>|<br/>|A|B|C|D", "").replaceFirst("、", "");
                answerList = answers.split("、");
            }

            //问题
            Question q = new Question();
            q.setQuestionSubject(subject);
            q.setQuestionText(questionText);
            q.setQuestionUrl(imgUrl);

            //分析
            QuestionAnalysis a = new QuestionAnalysis();
            a.setAnalysisText(analysis);
            q.addAnalysis(a);

            //答案
            saveAnswer(q, realAnswer, answerList);
            questionRepo.save(q);
        }
    }

    private void saveAnswer(Question s, String ta, String[] answers) {
        String answer = ta.replaceAll("A", "0")
                .replaceAll("B", "1")
                .replaceAll("C", "2")
                .replaceAll("D", "3");
        for (int i = 0; i < answers.length; i++) {
            QuestionAnswer answerEntity = new QuestionAnswer();
            answerEntity.setAnswerText(answers[i]);
            answerEntity.setOk(answer.contains(String.valueOf(i)) ? 1 : 0);
            answerEntity.setAnswerOrder(i);
            s.addAnswer(answerEntity);
        }
    }

    private List<String> readKeyList(String filePath) {
        List<String> keyList = new ArrayList<>();
        try {
            InputStream i = new FileInputStream(filePath);
            String content = streamToString(i);
            Pattern pattern = Pattern.compile("(.*?).json\\?v=20171031154805.json");
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                String key = matcher.group(1);
                keyList.add(key);
            }

        } catch (FileNotFoundException e) {
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
