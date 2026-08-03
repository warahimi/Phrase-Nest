package com.phrasenest.expression.dialogue.application;


import com.phrasenest.expression.dialogue.api.ExpressionDialogueResponse;
import com.phrasenest.expression.dialogue.domain.ExpressionDialogue;
import org.springframework.stereotype.Component;

@Component
public class ExpressionDialogueMapper {

    public ExpressionDialogueResponse toResponse(ExpressionDialogue dialogue)
    {
        return new ExpressionDialogueResponse(
                dialogue.getId(),
                dialogue.getExpression().getId(),
                dialogue.getSpeakerAName(),
                dialogue.getSpeakerAText(),
                dialogue.getSpeakerBName(),
                dialogue.getSpeakerBText(),
                dialogue.getContextText(),
                dialogue.getDifficultyLevel(),
                dialogue.getDisplayOrder(),
                dialogue.getCreatedAt(),
                dialogue.getUpdatedAt()
        );
    }
}