package com.matchday.matchdayserver.matchevent.service;

import com.matchday.matchdayserver.matchevent.mapper.MatchEventMapper;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchEventHistoryService {

    private final MatchEventRepository matchEventRepository;

    public List<MatchEventResponse> findAllHistoryByMatchId(Long matchId) {
        return matchEventRepository.findByMatchIdFetchMatchUserAndMatch(matchId)
            .stream()
            .map(MatchEventMapper::toResponse)
            .toList();
    }
}
