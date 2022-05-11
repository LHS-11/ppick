package com.warrenverr.ppick.crawling;

import com.warrenverr.ppick.dto.CrawlingDto;
import com.warrenverr.ppick.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class Crawling {


    private final CrawlingService crawlingService;

    @EventListener(ContextRefreshedEvent.class)
    public void CrawlingLogic() throws IOException {

        // 공모분야가 IT인 페이지의 수를 측정하기 위해 처음 페이지
        final String camUrl = "https://allforyoung.com/posts/category/2/?contypes=14";
        Connection conn1 = Jsoup.connect(camUrl);
        Document document1 = conn1.get();
        Elements pages = document1.select("div.pagination>button.pages>span");

        for (int j = 1; j <= Integer.parseInt(pages.get(1).text()); j++) {

            CrawlingDto crawlingDto = new CrawlingDto();

            final String campuspickUrl = "https://allforyoung.com/posts/category/2/?contypes=14&page=" + j;
            Connection conn = Jsoup.connect(campuspickUrl);

            System.out.println("--------------------");
            try {
                Document document = conn.get();
                Elements imageUrlElements = document.select("div.poster__box > a > picture > img");
                Elements linkElements = document.select("div.poster__box > a");

                for (int i = 0; i < imageUrlElements.size(); i++) {


                    String url = linkElements.get(i).attr("abs:href");

                    Connection innerConn = Jsoup.connect(url);
                    Document innerDocument = innerConn.get();

                    // 이미지
                    Elements imageUrlElements1 = innerDocument.select("div.poster_imgBox > a > picture > img");
                    // 지원하기 버튼 링크
                    Elements applyLink = innerDocument.select("div.detail_actionBox > button.info_button_apply.applyCount");

                    // 제목
                    Elements title = innerDocument.select("div.post_title>h1");

                    //제목 (title)
                    crawlingDto.setTitle(title.text());


                    // 주최측,지원기간,공모분야,자격요건,상금 크롤링 (host, apply_date,field,condition,reward)
                    Elements tables = innerDocument.select("div.detail_info > table");
                    for (Element table : tables) {
                        int cnt=0;
                        for(Element row: table.select("tr")){
                            Elements tds = row.select("td");
                            System.out.println(tds.get(0).text());
                            if(cnt==0) crawlingDto.setHost(tds.get(0).text());
                            else if(cnt==1) crawlingDto.setApply_date(tds.get(0).text());
                            else if(cnt==2) crawlingDto.setField(tds.get(0).text());
                            else if(cnt==3) crawlingDto.setCondition(tds.get(0).text());
                            else crawlingDto.setReward(tds.get(0).text());
                            cnt++;
                        }
                    }


                    //상세 내용 (content)
                    Elements content = innerDocument.select("div.container > div.descriptionBox > p");
                    crawlingDto.setContent(content.html());


                    //신청하기 링크 (link)
                    String temp = applyLink.get(0).attr("abs:onclick");
                    if (temp.equals("")) {
                        System.out.println("No link");
                        continue;
                    }
                    String apply = temp.substring(temp.indexOf('\'') + 1, temp.lastIndexOf('\''));
                    System.out.println("신청 링크 : " + apply);
                    crawlingDto.setLink(apply);


                    // 이미지 (img)
                    System.out.println("포스팅 이미지 : " + imageUrlElements1.get(0).attr("abs:src"));
                    crawlingDto.setImg(imageUrlElements1.get(0).attr("abs:src"));

                    crawlingService.createCrawling(crawlingDto);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("—————————");
        }
    }
}

