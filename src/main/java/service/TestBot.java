package service;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class TestBot extends TelegramLongPollingBot {
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            handleMessage(update.getMessage());
            Message message = update.getMessage();
            Calendar calendar = Calendar.getInstance();
            //CurrentTime
            if (message.hasText()){
                Date currentDate = java.util.Calendar.getInstance().getTime();
                execute(
                        SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("Current Time is: " + currentDate)
                        .build());
            }

            if (update.hasMessage() && message.getText().equals("Year") ){
                int currentYear = calendar.getWeekYear();
                 execute(
                         SendMessage.builder()
                                 .chatId(message.getChatId().toString())
                                 .text("Current Year: " + currentYear)
                                 .build());
            }

            if (update.hasMessage() && message.getText().equals("Month") ){
                int currentMonth = calendar.get(Calendar.MONTH);
                execute(
                        SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text("Current Month: " + currentMonth)
                                .build());
            }

            if (update.hasMessage() && message.getText().equals("Day") ){
                int currentDay = calendar.get(Calendar.DATE);
                execute(
                        SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text("Current Day: " + currentDay)
                                .build());
            }

//            if (message.equals("pic")){
//                try {
//                    SendPhoto sendPhoto = new SendPhoto().setPhoto(new FileInputStream(new File("/Users/khlopinant/Downloads/11FGbFaSBEI.jpg")));
//                    this.execute(message);
//                }catch (FileNotFoundException){
//                    e.printStackTrace();
//                }
//            }
        }

    }

    private void execute(Message message) {
    }

    @SneakyThrows
    private void handleMessage(Message message) {
        //handle command
        if (message.hasText() && message.hasEntities()){
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()){
                String command =
                        message
                                .getText()
                                .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command){
                    case "/set_currency":
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        for (Currency currency : Currency.getAvailableCurrencies()){
                            buttons.add(
                                    Arrays.asList(
                                            InlineKeyboardButton.builder()
                                                    .text(currency.getDisplayName())
                                                    .callbackData("ORIGINAL:" + currency)
                                                    .build(),
                                            InlineKeyboardButton.builder()
                                                    .text(currency.getDisplayName())
                                                    .callbackData("TARGET" + currency)
                                                    .build()));
                        }
                        execute(
                                SendMessage.builder()
                                        .text("Please choose Original and Target currencies")
                                        .chatId(message.getChatId().toString())
                                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                        .build());
                        return;
                }
            }

        }
    }

    @Override
    public String getBotUsername() {
        return "@KhlopinAntBot";
    }

    @Override
    public String getBotToken() {
        return "5096980089:AAHg8uPEaps1oRPawi3ELU98fjb2yDz-uXI";
    }
    @SneakyThrows
    public static void main(String[] args) {
        TestBot bot = new TestBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
}
