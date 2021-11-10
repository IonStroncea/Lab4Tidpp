package com.fampay.assignment.videosretrievalserviceserver.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fampay.assignment.videosretrievalserviceserver.config.YoutubeApiConfiguration;
import com.fampay.assignment.videosretrievalserviceserver.db.Thumbnail;
import com.fampay.assignment.videosretrievalserviceserver.db.VideoEntity;
import com.fampay.assignment.videosretrievalserviceserver.db.VideoRepository;
import com.fampay.assignment.videosretrievalserviceserver.utils.ConversionUtils;
import com.fampay.assignment.videosretrievalserviceserver.utils.DateTimeUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class VideosRetrievalWorker {

    @Autowired
    private YoutubeApiConfiguration youtubeApiConfiguration;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private WebClientService webClientService;

    @Autowired
    private ConversionUtils conversionUtils;

    @Autowired
    private DateTimeUtils dateTimeUtils;

    private final Set<String> pageTokens = new LinkedHashSet<>();

    public void schedule() {

        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                Date prevLatestPublishedDate = videoRepository.getLatestPublishedDate();
                if(Objects.isNull(prevLatestPublishedDate)) {
                    prevLatestPublishedDate = new Date();
                }
                getAndSaveApiResults(null, prevLatestPublishedDate);
            }
        }, 0, youtubeApiConfiguration.getFrequencyMillis());
    }

    public void getNextApiPageAndSaveApiResults() {
        String[] pageTokensArray = new String[pageTokens.size()];
        String pageToken = pageTokens.toArray(pageTokensArray)[pageTokens.size() - 1];
        getAndSaveApiResults(pageToken, null);
    }

    public void getAndSaveApiResults(String pageToken, Date latestPublishedDate) {
        log.info("Request:: pageToken: {}", pageToken);

        Map<?,?> response = webClientService.sendGetRequest(Optional.ofNullable(pageToken), dateTimeUtils.convertDateToIsoDateStr(latestPublishedDate));
//        Map<?,?> response = conversionUtils.stringToPojo("{\"kind\":\"youtube#searchListResponse\",\"etag\":\"GiIMQBmzRCVyLqnbzi1YybWJ-DE\",\"nextPageToken\":\"CBQQAA\",\"regionCode\":\"IN\",\"pageInfo\":{\"totalResults\":1000000,\"resultsPerPage\":20},\"items\":[{\"kind\":\"youtube#searchResult\",\"etag\":\"lh1UPqr0xTj2MmLe2uCggOZkHQ8\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"I5DZJ9A9_O8\"},\"snippet\":{\"publishedAt\":\"2021-11-07T13:25:03Z\",\"channelId\":\"UCJkMa25_J1VeUITBSuy05Lw\",\"title\":\"\uD83D\uDD34LIVE: NEW ZEALAND vs AFGHANISTAN | NZ vs AFG Live Scores | Live Cricket Match Today\",\"description\":\"No Commentary New visitors plz SUBSCRIBE everyone Hit the LIKE button #NZvsAFG #LIVECRICKET #t20worldcup #cricket #t20 #livecricket #live LIVE: NEW ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/I5DZJ9A9_O8/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/I5DZJ9A9_O8/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/I5DZJ9A9_O8/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"KM PUNK\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T13:25:03Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"wVTzBzX8FcEQdgm63mqVHTteQ-8\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"F0az9yc-A-w\"},\"snippet\":{\"publishedAt\":\"2021-11-07T13:14:39Z\",\"channelId\":\"UCyfow0cQUnSQYJNGmlkXyLA\",\"title\":\"NZ vs Afghan 40th Match | ICC T20 Cricket World Cup 2021 | live streaming | score\",\"description\":\"NZ vs Afghan 40th Match | ICC T20 Cricket World Cup 2021 | live streaming | score.\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/F0az9yc-A-w/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/F0az9yc-A-w/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/F0az9yc-A-w/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"mi sports tv\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T13:14:39Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"-uUdPU0foDW5i0tH7Na22aNOT2k\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"Pfnp535Nv-A\"},\"snippet\":{\"publishedAt\":\"2021-11-07T12:19:45Z\",\"channelId\":\"UCoMdktPbSTixAyNGwb-UYkQ\",\"title\":\"Cricket controversy: &#39;Racism has been rife at Headingley&#39;\",\"description\":\"Human rights lawyer Mohammed Patel says he is 'not at all surprised' by allegations of racism at Yorkshire County Cricket Club (CCC), which emerged after star ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/Pfnp535Nv-A/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/Pfnp535Nv-A/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/Pfnp535Nv-A/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Sky News\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T12:19:45Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"PxVD2oJbftzk-3009NOt8Rth8pQ\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"qy-6janBDu0\"},\"snippet\":{\"publishedAt\":\"2021-11-07T11:46:10Z\",\"channelId\":\"UC8Wd_RVw8T1O1_IWEbICkIg\",\"title\":\"\uD83D\uDD34LIVE: NEW ZEALAND vs AFGHANISTAN | NZ vs AFG Live Scores &amp; Commentary |  Live Cricket Match Today\",\"description\":\"live #livecricket #t20live #NZvsAFG LIVE: NEW ZEALAND vs AFGHANISTAN | NZ vs AFG Live Scores & Commentary | Live Cricket Match Today Topics to be ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/qy-6janBDu0/default_live.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/qy-6janBDu0/mqdefault_live.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/qy-6janBDu0/hqdefault_live.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Mr Play\",\"liveBroadcastContent\":\"live\",\"publishTime\":\"2021-11-07T11:46:10Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"FZahCa6HivfPru7WR16u98epmdI\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"gkNE35zwJJc\"},\"snippet\":{\"publishedAt\":\"2021-11-07T11:36:36Z\",\"channelId\":\"UCKvwTsezozI7skPGsejA_-Q\",\"title\":\"\uD83D\uDD34LIVE: NEW ZEALAND vs AFGHANISTAN | NZ vs AFG Live Scores &amp; Commentary |  Live Cricket Match Today\",\"description\":\"LIVE: NEW ZEALAND vs AFGHANISTAN | NZ vs AFG Live Scores & Commentary | Live Cricket Match Today #cricket #t20 #livecricket #live #NZvsAFG Topics to ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/gkNE35zwJJc/default_live.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/gkNE35zwJJc/mqdefault_live.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/gkNE35zwJJc/hqdefault_live.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Mr. Prakash\",\"liveBroadcastContent\":\"live\",\"publishTime\":\"2021-11-07T11:36:36Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"iaWXFK0DPYfacah1WvCRdLpY4Qc\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"wcvMu59eSOI\"},\"snippet\":{\"publishedAt\":\"2021-11-07T10:52:35Z\",\"channelId\":\"UCEMMIOFWBbMunw5oAIVz3UQ\",\"title\":\"\uD83D\uDD34LIVE: New Zealand vs Afghanistan | NZ Vs AFG Live Cricket Scores | NZ vs AFG Live Cricket Match\",\"description\":\"LIVE: NEW ZEALAND vs AFGHANISTAN | ENG vs SA Live Cricket Scores | ENG vs SA Live Cricket Match Today LIVE: New Zealand vs Afghanistan nz vs afg ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/wcvMu59eSOI/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/wcvMu59eSOI/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/wcvMu59eSOI/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"All In One Gaming\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T10:52:35Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"ANbRyPeDqlnds8UjCjnXeVQ_9I8\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"LpG-osFUijY\"},\"snippet\":{\"publishedAt\":\"2021-11-07T10:12:19Z\",\"channelId\":\"UCVffBLVWOJpX3WeJF-gKB-A\",\"title\":\"\uD83D\uDD34#LIVE New Zealand vs Afghanistan 40th Match #ICC_T20_2021 Cricket Score | Fan Chat | #NZvsAFG\",\"description\":\"AFGvsNZ #ICC_T20 #Live_Cricket Signup Link: https://bit.ly/2XeKp7A Telegram Link: https://t.me/playinexch !!! PlayinExchange !\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/LpG-osFUijY/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/LpG-osFUijY/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/LpG-osFUijY/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"M 36\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T10:12:19Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"scLsICwHRYcpbY8pKKOxY24rQp8\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"gZr8_-t15Lg\"},\"snippet\":{\"publishedAt\":\"2021-11-07T10:02:13Z\",\"channelId\":\"UCSRQXk5yErn4e14vN76upOw\",\"title\":\"Cricbuzz LIVE हिन्दी: न्यूज़ीलैंड v अफ़ग़ानिस्तान, मैच 40, प्री-मैच शो\",\"description\":\"NewZealand के लिए जीत है सेमीफ़ाइनल की चाबी, #Afghanistan की जीत से #India की उम्मीदें रहेगी बाकि। करेंगे #T20WorldCup 2021 ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/gZr8_-t15Lg/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/gZr8_-t15Lg/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/gZr8_-t15Lg/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Cricbuzz\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T10:02:13Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"B1WtORXQ1K1L0uLXIxYwvqjm7oY\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"2XSvHN9zqEU\"},\"snippet\":{\"publishedAt\":\"2021-11-07T09:41:13Z\",\"channelId\":\"UCFoMTYk_nJfpxckD8MKrdZA\",\"title\":\"LIVE–AFG vs NZ T20 World Cup Match Live Score, Afghanistan vs New Zealand Live match highlights\",\"description\":\"LIVE – NZ vs AFG T20 World Cup Match Live Score, New Zealand vs Afghanistan Live Cricket match highlights today New Zealand vs Afghanistan Live Cricket ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/2XSvHN9zqEU/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/2XSvHN9zqEU/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/2XSvHN9zqEU/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"IFC knowledge\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T09:41:13Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"F166xS8qdXFV2nhusYNzZ3W0Bmc\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"AVTEvurp6Og\"},\"snippet\":{\"publishedAt\":\"2021-11-07T09:04:06Z\",\"channelId\":\"UClto1RLH1sDlYN5n-EfLNhQ\",\"title\":\"Ptv Sports Live | Ptv Sports\",\"description\":\"Ptv Sports Live | Ptv Sports PTV Sports is a 24-hour Pakistani sports channel owned by PTV Network. PTV Sports was launched on 14 January 2012. Its test ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/AVTEvurp6Og/default_live.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/AVTEvurp6Og/mqdefault_live.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/AVTEvurp6Og/hqdefault_live.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Opn Cricket\",\"liveBroadcastContent\":\"live\",\"publishTime\":\"2021-11-07T09:04:06Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"xxIIuf_3PK-AOxVZUhENYN9r8B4\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"u0omABV9seo\"},\"snippet\":{\"publishedAt\":\"2021-11-07T08:29:37Z\",\"channelId\":\"UCjFKMoAk3qhRkW4eOqNm6dw\",\"title\":\"Zee Hindustan live news | न्यूज़ीलैंड बनाम अफगानिस्तान | T20 World Cup | nz vs afg | live score\",\"description\":\"Zee Hindustan Live News| T20 World Cup | NZ VS AFG | PM Narendra Modi | Sameer Wankhede शुरु हुआ अफगानिस्तान और न्यूजीलैंड के बीच ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/u0omABV9seo/default_live.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/u0omABV9seo/mqdefault_live.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/u0omABV9seo/hqdefault_live.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Zee Hindustan\",\"liveBroadcastContent\":\"live\",\"publishTime\":\"2021-11-07T08:29:37Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"ui3XdFhSkg28qBtknnE73zL4fh8\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"7VToJJOyOWo\"},\"snippet\":{\"publishedAt\":\"2021-11-07T07:47:58Z\",\"channelId\":\"UC8Wd_RVw8T1O1_IWEbICkIg\",\"title\":\"\uD83D\uDD34LIVE: NEW ZEALAND vs AFGHANISTAN | NZ vs AFG Live Scores &amp; Commentary |  Live Cricket Match Today\",\"description\":\"live #livecricket #t20live #NZvsAFG LIVE: NEW ZEALAND vs AFGHANISTAN | NZ vs AFG Live Scores & Commentary | Live Cricket Match Today Topics to be ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/7VToJJOyOWo/default_live.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/7VToJJOyOWo/mqdefault_live.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/7VToJJOyOWo/hqdefault_live.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Mr Play\",\"liveBroadcastContent\":\"live\",\"publishTime\":\"2021-11-07T07:47:58Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"Oa7t4PBkd1AiwzG2E7T8TvediJQ\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"zrKMih_klCk\"},\"snippet\":{\"publishedAt\":\"2021-11-07T07:32:42Z\",\"channelId\":\"UCi4LcKTGpsuS9kE2XSGXOJA\",\"title\":\"\uD83D\uDD34LIVE: NEW ZEALAND vs AFGHANISTAN | NZ vs AFG Live Cricket Match Today | हिंदी Cricket Song Special\",\"description\":\"Hindi Podcast (हिंदी पॉडकास्ट) -Cricket Song Special | T20 World Cup 2021 NEW ZEALAND vs AFGHANISTAN Songs - Cricket Song by Axl Hazarika ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/zrKMih_klCk/default_live.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/zrKMih_klCk/mqdefault_live.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/zrKMih_klCk/hqdefault_live.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Axl Hazarika\",\"liveBroadcastContent\":\"live\",\"publishTime\":\"2021-11-07T07:32:42Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"_FrdTdbWfTj0F7XjETaGdYf48xo\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"mpzGg-iaLik\"},\"snippet\":{\"publishedAt\":\"2021-11-07T07:29:44Z\",\"channelId\":\"UCKvwTsezozI7skPGsejA_-Q\",\"title\":\"\uD83D\uDD34LIVE: NEW ZEALAND vs AFGHANISTAN | NZ vs AFG Live Scores &amp; Commentary |  Live Cricket Match Today\",\"description\":\"LIVE: NEW ZEALAND vs AFGHANISTAN | NZ vs AFG Live Scores & Commentary | Live Cricket Match Today #cricket #t20 #livecricket #live #NZvsAFG Topics to ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/mpzGg-iaLik/default_live.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/mpzGg-iaLik/mqdefault_live.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/mpzGg-iaLik/hqdefault_live.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Mr. Prakash\",\"liveBroadcastContent\":\"live\",\"publishTime\":\"2021-11-07T07:29:44Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"pkDQD5uHQiKPMermNeymIT4V344\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"tLPkQ--reLc\"},\"snippet\":{\"publishedAt\":\"2021-11-07T05:34:30Z\",\"channelId\":\"UC5OY44lUSti_QD78IEn0qew\",\"title\":\"LIVE T20 World Cup 2021 PAK vs SCO Dream11, Pakistan vs Scotland fantasy Prediction GL H2H Today\",\"description\":\"PAK vs SCO T20 World Cup 2021 Dream 11 Team Prediction | PAK vs SCO T20 World Cup 2021 Dream 11 Team Analysis || Pitch Report, Pakistan vs Scotland ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/tLPkQ--reLc/default_live.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/tLPkQ--reLc/mqdefault_live.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/tLPkQ--reLc/hqdefault_live.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Cross Border Cricket - Total Cricket Entertainment\",\"liveBroadcastContent\":\"live\",\"publishTime\":\"2021-11-07T05:34:30Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"ys93HOldb2eVxUGBjtSojPfWjiw\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"KjvBneo5rPc\"},\"snippet\":{\"publishedAt\":\"2021-11-07T05:30:03Z\",\"channelId\":\"UC0bTwqMpF70YH9OrlFOt_qQ\",\"title\":\"cricket | 50K Tournament  Semifinal |super thriller| Maravamangalam Vs Keezhaiyur | nz vs afganistan\",\"description\":\"In this match between Maravamangalam Vs Keezhaiyur this is 6 over Semifinal match Maravamangalam batted first they are started slowly at the end of ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/KjvBneo5rPc/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/KjvBneo5rPc/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/KjvBneo5rPc/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Ulloor Cricket\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T05:30:03Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"39Cw97A28Q3rBzq5Zilir3CnMfk\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"0CK_0LoDSVg\"},\"snippet\":{\"publishedAt\":\"2021-11-07T05:30:11Z\",\"channelId\":\"UCZSNzBgFub_WWil6TOTYwAg\",\"title\":\"Cricket vs. Dance Battle ft. Yuzvendra Chahal &amp; @Dhanashree Verma | Meenakshi Sundareshwar\",\"description\":\"Sach ko record hone do aaj! Watch a long distance couple like Yuzvendra Chahal and Dhanashree Verma, on Meenakshi Sundareshwar, now streaming only ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/0CK_0LoDSVg/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/0CK_0LoDSVg/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/0CK_0LoDSVg/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Netflix India\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T05:30:11Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"Ud_ucLigfomUrLP2qmQx6xIBxpc\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"4ZT-wZJeNlc\"},\"snippet\":{\"publishedAt\":\"2021-11-07T05:06:20Z\",\"channelId\":\"UCffbe58dztBIyE8rmaGIBlg\",\"title\":\"Charith Asalanka In IPL 2022 ? - Sri Lanka Cricket\",\"description\":\"Charith Asalanka In IPL 2021 ? - Sri Lanka Cricket #charith_asalanka #cricket_news #ipl_2022 SL vs WI 2021 කොදෙව් සංචාරයට නම් කල අපේ සිංහයන් ...\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/4ZT-wZJeNlc/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/4ZT-wZJeNlc/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/4ZT-wZJeNlc/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Sports Counter\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T05:06:20Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"iJgZHnzw2yqTiOWQ44SK4Jptj5g\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"MwCNqrOjcyk\"},\"snippet\":{\"publishedAt\":\"2021-11-07T04:58:36Z\",\"channelId\":\"UCQYRGzYaVa7aWd8TsbjdJ6Q\",\"title\":\"Can AFGHANISTAN help INDIA make it to the SEMIS? | Betway Cricket Chaupaal | Aakash Chopra\",\"description\":\"Checkout Betway here: https://betway.onelink.me/B7UR/2547f0f0 2X your winnings on every India win this T20 Worldcup. Get a chance to win 1 crore!\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/MwCNqrOjcyk/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/MwCNqrOjcyk/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/MwCNqrOjcyk/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"Aakash Chopra\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T04:58:36Z\"}},{\"kind\":\"youtube#searchResult\",\"etag\":\"22zYpxdfWWmOluBdaXTHg3d_wpM\",\"id\":{\"kind\":\"youtube#video\",\"videoId\":\"vs8b0GRGcp0\"},\"snippet\":{\"publishedAt\":\"2021-11-07T04:41:50Z\",\"channelId\":\"UCdcTiloe8VYoypIIHSF3l0A\",\"title\":\"අයිසීසිය ශ්\u200Dරී ලංකා ක්\u200Dරිකට් කණ්ඩායමට කරල තියෙන වැඩේ මෙන්න | Sri lanka cricket | today match\",\"description\":\"අයිසීසිය ශ්\u200Dරී ලංකා ක්\u200Dරිකට් කණ්ඩායමට කරල තියෙන වැඩේ මෙන්න | Sri lanka cricket | today match.\",\"thumbnails\":{\"default\":{\"url\":\"https://i.ytimg.com/vi/vs8b0GRGcp0/default.jpg\",\"width\":120,\"height\":90},\"medium\":{\"url\":\"https://i.ytimg.com/vi/vs8b0GRGcp0/mqdefault.jpg\",\"width\":320,\"height\":180},\"high\":{\"url\":\"https://i.ytimg.com/vi/vs8b0GRGcp0/hqdefault.jpg\",\"width\":480,\"height\":360}},\"channelTitle\":\"NEWS LANKA\",\"liveBroadcastContent\":\"none\",\"publishTime\":\"2021-11-07T04:41:50Z\"}}]}", Map.class);
        log.info("Response:: {}", conversionUtils.pojoToString(response));

        videoRepository.saveAll(getVideoEntityList(response));
    }

    private List<VideoEntity> getVideoEntityList(Map<?,?> response) {
        pageTokens.add((String) response.get("nextPageToken"));
        List<Map<?,?>> items = (List<Map<?, ?>>) response.get("items");
        return items.stream().map(this::buildVideoEntity)
                .collect(Collectors.toList());
    }

    private VideoEntity buildVideoEntity(Map<?,?> item) {
        VideoEntity videoEntity = new VideoEntity();
        Map<String, String> idMap = (Map<String, String>) item.get("id");
        videoEntity.setId(idMap.get("videoId"));

        Map<?,?> snippetMap = (Map<?, ?>) item.get("snippet");
        videoEntity.setTitle((String) snippetMap.get("title"));
        videoEntity.setDescription((String) snippetMap.get("description"));
        videoEntity.setChannel((String) snippetMap.get("channelTitle"));
        videoEntity.setThumbnail(getThumbnail(snippetMap));

        videoEntity.setPublishedAt((dateTimeUtils.convertIsoDateStrToDate((String) snippetMap.get("publishedAt"))));
        return videoEntity;
    }

    private Thumbnail getThumbnail(Map<?,?> snippetMap) {
        Map<?,?> thumbnailMap = (Map<?, ?>) snippetMap.get("thumbnails");
        return conversionUtils.convertToPojo(thumbnailMap.get("medium"), Thumbnail.class);
    }
}
