package com.phrasenest.expression.dialogue.infrastructure;


import com.phrasenest.expression.dialogue.domain.ExpressionDialogue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpressionDialogueRepository
        extends JpaRepository<ExpressionDialogue, UUID> {

    List<ExpressionDialogue>
    findAllByExpressionIdOrderByDisplayOrderAscCreatedAtAsc(
            UUID expressionId
    );
}