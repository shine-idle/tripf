package com.shineidle.tripf.domain.chatbot.dictionary;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SynonymDictionary {
    private final Map<String, Set<String>> synonymMap = new HashMap<>();

    public SynonymDictionary() {
        synonymMap.put("안녕", new HashSet<>(Arrays.asList("ㅎㅇ", "반가워", "안뇽")));
        synonymMap.put("결제", new HashSet<>(Arrays.asList("카드", "현금", "취소", "비용", "거래")));
        synonymMap.put("문제 해결", new HashSet<>(Arrays.asList("문제", "안됨", "안돼", "에러", "도움", "이슈")));
        synonymMap.put("감사", new HashSet<>(Arrays.asList("감사", "수고", "고생", "고마워", "덕분에", "ㄱㅅ")));
    }

    /**
     * 입력 문장에 포함된 유사어가 있으면 해당 카테고리명 반환
     * @param input 사용자 질문
     * @return 카테고리명 또는 null
     */
    public String findCategoryBySynonym(String input) {
        for (Map.Entry<String, Set<String>> entry : synonymMap.entrySet()) {
            for (String synonym : entry.getValue()) {
                if (input.contains(synonym)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}
