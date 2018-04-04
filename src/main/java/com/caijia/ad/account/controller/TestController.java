package com.caijia.ad.account.controller;

import com.caijia.ad.account.repository.TestRepository;
import com.caijia.ad.account.entities.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    private final TestRepository testRepository;

    @Autowired
    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @RequestMapping("/test")
    public TestEntity test(@RequestParam(value = "id", defaultValue = "1") int id,
                           @RequestParam(value = "name", defaultValue = "caijia") String name) {
        return new TestEntity(id, name);
    }

    @RequestMapping("/testName")
    public @ResponseBody
    TestEntity testName(@RequestParam(value = "name", defaultValue = "caijia") String name) {
        return testRepository.findByName(name);
    }

    @RequestMapping("/saveTest")
    public @ResponseBody
    String saveTest(@RequestParam(value = "id", defaultValue = "1") int id,
                    @RequestParam(value = "name", defaultValue = "caijia") String name) {
        TestEntity test = new TestEntity(id, name);
        testRepository.save(test);
        return "Saved";
    }

    @GetMapping("/all")
    public @ResponseBody
    Iterable<TestEntity> getAllUsers() {
        // This returns a JSON or XML with the users
        return testRepository.findAll();
    }
}
