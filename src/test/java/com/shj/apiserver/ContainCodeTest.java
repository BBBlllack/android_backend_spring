package com.shj.apiserver;

import com.shj.apiserver.entity.Paper;
import com.shj.apiserver.service.PaperService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class ContainCodeTest {
    @Resource
    private PaperService paperService;

    @Test
    void name() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ids.add(new Random().nextInt(1000)+1);
        }
        List<Paper> papers = paperService.listByIds(ids);
        for (Paper paper : papers) {
            String detailEn = paper.getDetailEn();
            System.out.println(detailEn);
            if (detailEn.contains("github")){
                System.out.println(true);
            }
            System.out.println("--------------------------");
        }
    }
}
