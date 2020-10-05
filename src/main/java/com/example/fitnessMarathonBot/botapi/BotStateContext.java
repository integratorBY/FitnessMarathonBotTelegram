package com.example.fitnessMarathonBot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines message handlers for each state.
 */
@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isFillingProfileState(currentState)) {
            return messageHandlers.get(BotState.ASK_PERSONAL_INFO);
        } else if (isSupplementProfileState(currentState)) {
            return messageHandlers.get(BotState.ASK_SUPPLEMENT_PERSONAL_INFO);

        } else if(isFillingReport(currentState)) {
            return messageHandlers.get(BotState.ASK_REPORT);
        } else if(isFillingGoals(currentState)) {
            return messageHandlers.get(BotState.ASK_ADMIN_GOALS);
        } else if (isFillingMarathonState(currentState)) {
            return messageHandlers.get(BotState.START_NEW_MARATHON);
        } else if (isSendMessageAllState(currentState)) {
            return messageHandlers.get(BotState.ASK_MESSAGE_FOR_USER);
        }
        return messageHandlers.get(currentState);
    }

        private boolean isFillingMarathonState(BotState currentState) {
            switch (currentState) {
                case START_NEW_MARATHON:
                case ASK_DATE_START_MARATHON:
                case ASK_DATE_FINISH_MARATHON:
                    return true;
                default:
                    return false;
            }
        }

        private boolean isFillingProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_NAME:
            case ASK_HEIGHT:
            case ASK_WEIGHT:
            case ASK_AGE:
            case ASK_PERSONAL_INFO:
            case PROFILE_FILLED:
                return true;
            default:
                return false;
        }
    }

    private boolean isFillingReport(BotState currentState){

        switch (currentState) {
            case ASK_REPORT:
            case ASK_GOALS:
            case ASK_PHOTO:
            case ASK_TASK_ONE:
            case ASK_TASK_TWO:
            case ASK_TASK_THREE:
            case ASK_TASK_FOUR:
            case ASK_TASK_FIVE:
            case ASK_TASK_SIX:
            case FILLING_REPORT:
                return true;
            default:
                return false;
        }
    }

    private boolean isSupplementProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_BELLY:
            case ASK_CHEST:
            case ASK_HIPS:
            case ASK_HIP:
            case ASK_ARM:
            case ASK_NECK:
            case ASK_SHIN:
            case ASK_DATE:
            case ASK_WAIST:
            case PERSONAL_INFO_FILLED:
            case ASK_SUPPLEMENT_PERSONAL_INFO:
                return true;
            default:
                return false;
        }
    }

    private boolean isFillingGoals(BotState currentState) {
        switch (currentState) {
            case FILLING_GOALS:
            case ASK_ADMIN_TASK_FIVE:
            case ASK_ADMIN_TASK_ONE:
            case ASK_ADMIN_TASK_FOUR:
            case ASK_ADMIN_TASK_THREE:
            case ASK_ADMIN_TASK_TWO:
            case ASK_ADMIN_TASK_SIX:
            case GOALS_FILLED:
                return true;
            default:
                return false;
        }
    }

    private boolean isSendMessageAllState(BotState currentState) {
        switch (currentState) {
            case INPUT_MESSAGE_OT_ALL:
            case MESSAGE_SENT:
//            case ASK_MESSAGE_FOR_USER:
                return true;
            default:
                return false;
        }
    }
}