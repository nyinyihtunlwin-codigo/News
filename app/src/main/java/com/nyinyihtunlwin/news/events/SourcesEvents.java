package com.nyinyihtunlwin.news.events;

import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.data.vos.SourceVO;

import java.util.List;

public class SourcesEvents {

    public static class SourceLoadedEvent {
        private List<SourceVO> sources;

        public SourceLoadedEvent(List<SourceVO> sources) {
            this.sources = sources;
        }

        public List<SourceVO> getSources() {
            return sources;
        }

        public void setSources(List<SourceVO> sources) {
            this.sources = sources;
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
