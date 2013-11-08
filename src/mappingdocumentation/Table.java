package mappingdocumentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Генерация массива из считанного из файла массива данных, преобразованного в
 * обычную строку
 *
 * @author dmitryrassakhatsky
 */
public class Table {

    private OutputStreaming outputStreaming;
    private String messageNum; //Порядковый номер сообщения
    private String prevMessageNum; //Порядковый номер предыдущей строки
    private String sourceMessage; //Определяем исходное сообщение
    private String targetMessage; //Определяем целевое сообщение
    private ArrayList<String> path;

    /**
     * Создание таблицы с результатами обработки
     *
     * @param stringSource - строка содержащая искходный файл
     * @param catalog - полный путь к файлу, в который необходимо записать
     * результат преобразования
     *
     * @throws IOException
     */
    public void createTable(String stringSource, String catalog) throws IOException {
        ArrayList<String> unformattedTable; //Определяем массив для построчного представления полученного файла
        int Lenght;//Определяем переменную размера массива
        String messageHeader; //Заголовок сообщения в таблице

        outputStreaming = new OutputStreaming();
        messageNum = null;
        prevMessageNum = "0";
        sourceMessage = null;
        targetMessage = null;
        path = new ArrayList<String>();

        /**
         * Создается массив, в котором исходные данные разделены построчно
         */
        stringSource = stringSource.replaceAll("\\r", "");
        unformattedTable= new ArrayList(Arrays.asList(stringSource.split("\n")));

        /**
         * Создается массив, в котором данные из предыдущего массива делятся на
         * исходное и целевое сообщение
         */
        Lenght = unformattedTable.size();
        /**
         * Создаем шапку таблицы
         */
        outputStreaming.createTable(catalog);

        /**
         * Построчно заполняем таблицу
         */
        for (int i = 0; i < Lenght; i++) {
            String temp, number;
            int tempBorder, tempBorderNS;//Место константы "/ns" в исходном сообщении
            MessageNum iMessageNum;
            Trash trash;

            temp = unformattedTable.get(i); //Временная переменная для хранения значений
            tempBorder = temp.indexOf("="); //Определяем место разделителя исходного и целевого сообщения
            sourceMessage = temp.substring(tempBorder + 1); //Определяем исходное сообщение
            targetMessage = temp.substring(0, tempBorder); //Определяем целевое сообщение

            /**
             * Определяем номер сообщения
             */
            iMessageNum = new MessageNum();
            messageNum = iMessageNum.getMessageNumber(targetMessage);
            /**
             * Удаляем техническую информацию
             */
            trash = new Trash();
            targetMessage = trash.deleteTechInformation(targetMessage, messageNum);
            targetMessage = trash.deletePrefix(targetMessage);
            //sourceMessage = trash.deletePrefix(sourceMessage);

            /**
             * Выделяем смену сообщений (у нас возможны 3 ситуации:
             * 1) Это 1я строка - тогда выделяем название сообщения
             * 2) Это не 1я строка и сообщение сменилось - тогда вначале делаем
             * пустую строку, затем вставляем название сообщения
             * 3) Это не 1я строка и сообщение не менялось - ничего не
             * предпринимаем
             */
            if (i == 0) {
                messageHeader = createNewMessageHeader(messageNum, targetMessage, i);
                outputStreaming.createRow(messageHeader, "", "");
            } else {
                if (!prevMessageNum.equals(messageNum)) {
                    messageHeader = createNewMessageHeader(messageNum, targetMessage, i);
                    outputStreaming.createRow("", "", "");
                    outputStreaming.createRow(messageHeader, "", "");
                }
            }

            targetMessage = createTarget(targetMessage);
            sourceMessage = createSource(sourceMessage);

            /**
             * Построчная запись в файл
             */
            outputStreaming.createRow(targetMessage, sourceMessage, "");
            prevMessageNum = messageNum;
        }
        outputStreaming.close();
    }

    private String createTarget(String targetMessage) throws IOException {
        ArrayList<String> Levels;
        String target, empty;

        /**
         * Обработка целевого сообщения
         */
        target = "";
        empty = "   ";
        Levels = new ArrayList(Arrays.asList(targetMessage.split("/")));

        /*
         * Проверяем был ли такой уровень до этого, если был, то проверяем был
         * ли он тот же
         *
         * Но - последний уровень нужно отображать всегда, поэтому для него
         * делаем исключение
         */
        for (int index = 0; index < Levels.size(); index++) {
            if (index != (Levels.size() - 1)) { //Это любой уровень кроме последнего
                if (path.get(index) != null) { //Уровень уже существует

                    if (index != 0) { //Исключение является имя самого сообщения
                        if (!path.get(index).equals(Levels.get(index))) {   //Уровень изменился
                            target += Levels.get(index).toString() + "/";
                            path.add(index, Levels.get(index));

                            /*
                             * При изменении уровня, все послежующие должны
                             * обнуляться
                             */
                            for (int k = index + 1; k < Levels.size(); k++) {
                                path.add(k, null);
                            }

                        } else {  //Уровень не изменился
                            target += empty;
                        }
                    } else {
                        path.add(index, Levels.get(index));
                        target += empty;
                    }

                } else { //Уровень не существовал ранее
                    if (index != 0) { //Исключение является имя самого сообщения
                        target += Levels.get(index).toString() + "/";
                    } else {
                        target += empty;

                        /*
                         * При изменении уровня, все послежующие должны
                         * обнуляться
                         */
                        for (int k = index + 1; k < Levels.size(); k++) {
                            path.add(k, null);
                        }
                    }
                    path.add(index, Levels.get(index));
                }
            } else { //Это последний уровень, его всегда записываем в файл
                path.add(index, Levels.get(index));
                target += Levels.get(index).toString() + "/";
                /*
                 * При изменении уровня, все послежующие должны
                 * обнуляться
                 */
                for (int k = index + 1; k < Levels.size(); k++) {
                    path.add(k, null);
                }
            }
        }
        target = target.substring(0, target.length() - 1);
        return target;
    }

    private String createSource(String source) throws IOException {
        /*
         * String sourceReturn=""; //Результат обработки
         * source = source.replace("const(value=", "const(");//Улучшаем
         * отображаемость констант: const(value=1) заменяется на const(1)
         * source = source.replace(", result)", ")"); // Убираем поле результата
         * в различных функциях
         * if (source.indexOf("mapWithDefault") != -1)
         * source = source.replace("default_", ""); // Улучшаем внешний вид
         * функции mapWithDefault
         * String[] Source;
         * Source = source.split(",");
         * for(int j=0;j<Source.length;j++){
         * int i1_source = Source[j].indexOf("/Messages/Message");
         * int i2_source =
         * Source[j].indexOf("/",(i1_source+"/Messages/Message".length()));
         * if (i1_source!=-1&&i2_source!=-1)
         * Source[j] =
         * Source[j].substring(0,i1_source)+Source[j].substring(i2_source+1);
         * sourceReturn = sourceReturn+Source[j];
         * }
         *
         */
        Constant constant;

        constant = new Constant();
        source = constant.checkConstant(source);
        return source;
    }

    /**
     * Выделяет смену сообщения
     *
     * @param number
     * param
     * targetMessage
     * param
     * record
     *
     * @return
     *
     * @throws IOException
     */
    private String createNewMessageHeader(String number, String target, int record) throws IOException {
        /**
         * Проверка, сложный ли маппинг (содержит в себе больше 1 сообщения,
         * т.е. имеет конструкцию /ns0:Messages/ns0:Message1/
         */
        int border;

        border = target.indexOf("/");
        if (border != -1) {
            target = target.substring(0, border);
        }
        return target;
    }

    /**
     * Удаляем техническую инфомрацию из сообщения
     *
     * @param target Исходное сообщение
     * @param number Номер сообщения
     *
     * @return Обработанное соообщение
     *
     * @throws IOException
     */
    private String deleteTechInformation(String target, String number) throws IOException {
        /*
         * Возможные варианты targetMessage
         *
         * /ns0:BAPI_VENDOR_GETINTNUMBER/ACCOUNTGROUP
         *
         * /ns0:Messages/ns0:Message1/CREMAS04/IDOC/@BEGIN
         *
         * /ns0:Messages/ns0:Message1/ns1:BAPI_ADDRESSORG_SAVEREPLICA/CONTEXT
         *
         */
        if (!number.equals("0")) { //Сложный маппинг
            target = target.substring(target.indexOf("Messages") + 9);
            target = target.substring(target.indexOf("/") + 1);
            if (target.indexOf("/ns") == 0);
            target = target.substring(target.indexOf(":") + 1);
        } else { //Простой маппинг
            target = target.substring(target.indexOf(":") + 1);
        }
        return target;
    }
}
