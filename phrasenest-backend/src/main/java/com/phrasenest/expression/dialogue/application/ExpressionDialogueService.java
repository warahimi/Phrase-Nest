package com.phrasenest.expression.dialogue.application;


import com.phrasenest.expression.dialogue.api.*;
import com.phrasenest.expression.dialogue.domain.ExpressionDialogue;
import com.phrasenest.expression.dialogue.infrastructure.ExpressionDialogueRepository;
import com.phrasenest.expression.domain.Expression;
import com.phrasenest.expression.infrastructure.ExpressionRepository;
import com.phrasenest.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExpressionDialogueService {

    private final ExpressionRepository expressionRepository;
    private final ExpressionDialogueRepository dialogueRepository;
    private final ExpressionDialogueMapper dialogueMapper;

    // constructor injection
    public ExpressionDialogueService(
            ExpressionRepository expressionRepository,
            ExpressionDialogueRepository dialogueRepository,
            ExpressionDialogueMapper dialogueMapper)
    {
        this.expressionRepository = expressionRepository;
        this.dialogueRepository = dialogueRepository;
        this.dialogueMapper = dialogueMapper;
    }

    public ExpressionDialogueResponse create(UUID expressionId, CreateExpressionDialogueRequest request)
    {
        System.out.println("Dialogue service called");
        Expression expression = findExpression(expressionId);

        ExpressionDialogue dialogue = new ExpressionDialogue(
                expression,
                trimToNull(request.speakerAName()),
                request.speakerAText().trim(),
                trimToNull(request.speakerBName()),
                request.speakerBText().trim(),
                trimToNull(request.contextText()),
                request.difficultyLevel(),
                request.displayOrder()
        );

        return dialogueMapper.toResponse(
                dialogueRepository.save(dialogue)
        );
    }

    @Transactional(readOnly = true)
    public List<ExpressionDialogueResponse> getAllForExpression(
            UUID expressionId
    ) {
        findExpression(expressionId);

        return dialogueRepository
                .findAllByExpressionIdOrderByDisplayOrderAscCreatedAtAsc(
                        expressionId
                )
                .stream()
                .map(dialogueMapper::toResponse)
                .toList();
    }

    public ExpressionDialogueResponse update(
            UUID dialogueId,
            UpdateExpressionDialogueRequest request
    ) {
        ExpressionDialogue dialogue = findDialogue(dialogueId);

        dialogue.update(
                trimToNull(request.speakerAName()),
                request.speakerAText().trim(),
                trimToNull(request.speakerBName()),
                request.speakerBText().trim(),
                trimToNull(request.contextText()),
                request.difficultyLevel(),
                request.displayOrder()
        );

        return dialogueMapper.toResponse(dialogue);
    }

    public void delete(UUID dialogueId) {
        dialogueRepository.delete(
                findDialogue(dialogueId)
        );
    }

    private Expression findExpression(UUID expressionId) {
        return expressionRepository
                .findById(expressionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expression with ID '" +
                                        expressionId +
                                        "' was not found."
                        )
                );
    }

    private ExpressionDialogue findDialogue(UUID dialogueId) {
        return dialogueRepository
                .findById(dialogueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Dialogue with ID '" +
                                        dialogueId +
                                        "' was not found."
                        )
                );
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
