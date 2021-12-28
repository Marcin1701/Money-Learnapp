package polsl.moneysandbox.api.question.mapper;

import org.springframework.stereotype.Component;
import polsl.moneysandbox.model.question.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class QuestionMapper {

    public SingleChoice createSingleChoice(LinkedHashMap<?, ?> question) {
        @SuppressWarnings("unchecked")
        var singleChoiceOptions = (List<String>) ((LinkedHashMap<?, ?>)question.get("value")).get("singleChoiceOptions");
        return SingleChoice.builder()
                .answerTime((String) question.get("answerTime"))
                .name((String) question.get("name"))
                .question((String) question.get("question"))
                .correctSingleChoiceIndex((Integer) ((LinkedHashMap<?, ?>)question.get("value")).get("correctSingleChoiceOptionIndex"))
                .singleChoiceOptions(singleChoiceOptions)
                .build();

    }

    public MultipleChoice createMultipleChoice(LinkedHashMap<?, ?> question) {
        @SuppressWarnings("unchecked")
        var multipleChoiceOptions = (List<String>) ((LinkedHashMap<?, ?>)question.get("value")).get("multipleChoiceOptions");
        @SuppressWarnings("unchecked")
        var multipleChoiceCorrectIndices = (List<Integer>) ((LinkedHashMap<?, ?>)question.get("value")).get("correctMultipleChoiceOptionIndices");
        return MultipleChoice.builder()
                .answerTime((String) question.get("answerTime"))
                .name((String) question.get("name"))
                .question((String) question.get("question"))
                .multipleChoiceOptions(multipleChoiceOptions)
                .correctMultipleChoiceOptionIndices(multipleChoiceCorrectIndices)
                .build();
    }

    public OrderedList createOrderedList(LinkedHashMap<?, ?> question) {
        @SuppressWarnings("unchecked")
        var orderedListOptions = (List<String>) ((LinkedHashMap<?, ?>)question.get("value")).get("orderedListOptions");
        return OrderedList.builder()
                .answerTime((String) question.get("answerTime"))
                .name((String) question.get("name"))
                .question((String) question.get("question"))
                .orderedListOptions(orderedListOptions)
                .build();
    }

    public DragAndDrop createDragAndDrop(LinkedHashMap<?, ?> question) {
        @SuppressWarnings("unchecked")
        var dragAndDropOptions = (List<LinkedHashMap<?, ?>>) ((LinkedHashMap<?, ?>) question.get("value")).get("allDragAndDropOptions");
        List<String> optionNames = new ArrayList<>();
        List<Integer> optionCosts = new ArrayList<>();
        dragAndDropOptions.forEach(option -> {
            optionNames.add((String) option.get("optionName"));
            optionCosts.add((Integer) option.get("optionCost"));
        });
        return DragAndDrop.builder()
                .answerTime((String) question.get("answerTime"))
                .name((String) question.get("name"))
                .question((String) question.get("question"))
                .optionCost(optionCosts)
                .optionName(optionNames)
                .balance((Integer) ((LinkedHashMap<?, ?>) question.get("value")).get("balance"))
                .build();
    }
}
