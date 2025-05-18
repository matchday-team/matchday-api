package com.matchday.matchdayserver.match.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Schema(description = "페이징용 매치 리스트 응답 객체")
public class MatchListPageResponse {

    @Schema(description = "전체 매치 개수", example = "5")
    private final long totalCount;

    @Schema(description = "현재 페이지 매치 리스트")
    private final List<MatchListResponse> matches;

    @Builder
    public MatchListPageResponse(long totalCount, List<MatchListResponse> matches) {
        this.totalCount = totalCount;
        this.matches = matches;
    }
}
