package com.nyinyihtunlwin.news.events;

import com.nyinyihtunlwin.news.data.vos.NewsVO;

import java.util.List;

public class NewsEvents {

    public static class NewsLoadedEvent {
        private List<NewsVO> news;

        public NewsLoadedEvent(List<NewsVO> news) {
            this.news = news;
        }

        public List<NewsVO> getNews() {
            return news;
        }

        public void setNews(List<NewsVO> news) {
            this.news = news;
        }
    }

    public static class RestAPIEvent {
        private String message;

        public RestAPIEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
