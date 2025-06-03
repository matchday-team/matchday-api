package com.matchday.matchdayserver.team.model.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class MatchResultSummary {
    private int win;
    private int loss;
    private int draw;

    public MatchResultSummary() {
        this.win = 0;
        this.loss = 0;
        this.draw = 0;
    }

    public void incrementWin(int win) {
        this.win += win;
    }
    public void incrementLoss(int loss) {
        this.loss += loss;
    }
    public void incrementDraw(int draw) {
        this.draw += draw;
    }
}
