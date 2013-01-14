/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mappingdocumentation;

import java.io.IOException;

/**
 * Генерация массива из считанного из файла массива данных, преобразованного в
 * обычную строку
 *
 * @author dmitryrassakhatsky
 */
public
        class Table {

    OutputStreaming outputStreaming = new OutputStreaming();
    String messageNum = null; //Порядковый номер сообщения
    String prevMessageNum = "0"; //Порядковый номер предыдущей строки
    String sourceMessage = null; //Определяем исходное сообщение
    String targetMessage = null; //Определяем целевое сообщение

    public static
            void main(String[] args) throws IOException {

        String catalog = "/Users/dmitryrassakhatsky/Desktop/out.csv";
        String stringSource = "";
        Table table = new Table();
        table.createTable(stringSource, catalog);
        System.out.println("Пыщь Пыщь");
    }
    /**
     * Создание таблицы с результатами обработки
     *
     * @param stringSource - строка содержащая искходный файл
     * @param catalog      - полный путь к файлу, в который необходимо записать
     * результат преобразования
     *
     * @throws IOException
     */
    String[] path = new String[255];

    public
            void createTable(String stringSource, String catalog) throws IOException {
        String[] unformattedTable; //Определяем массив для построчного представления полученного файла
        int Lenght;//Определяем переменную размера массива
        String[][] table; //Определяем массив для таблицы с целевым и исходным сообщением
        String messageHeader; //Заголовок сообщения в таблице

        /**
         * Создается массив, в котором исходные данные разделены построчно
         */
        stringSource = stringSource.replaceAll("\\r", "");


        unformattedTable = stringSource.toString().split("\n");

        /**
         * Создается массив, в котором данные из предыдущего массива делятся на
         * исходное и целевое сообщение
         */
        Lenght = unformattedTable.length;
        table = new String[Lenght][2];

        /**
         * Создаем шапку таблицы
         */
        outputStreaming.createTable(catalog);

        /**
         * Построчно заполняем таблицу
         */
        for (int i = 0; i < Lenght; i++) {
            String temp = unformattedTable[i]; //Временная переменная для хранения значений
            int tempBorder = temp.indexOf("="); //Определяем место разделителя исходного и целевого сообщения
            int tempBorderNS; //Место константы "/ns" в исходном сообщении
            sourceMessage = temp.substring(tempBorder + 1); //Определяем исходное сообщение
            targetMessage = temp.substring(0, tempBorder); //Определяем целевое сообщение

            /**
             * Определяем номер сообщения
             */
            String number;
            MessageNum iMessageNum = new MessageNum();
            messageNum = iMessageNum.getMessageNumber(targetMessage);
            /**
             * Удаляем техническую информацию
             */
            Trash trash = new Trash();
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

    public
            String createTarget(String targetMessage) throws IOException {
        /**
         * Обработка целевого сообщения
         */
        String[] Levels;
        String target = "";
        String empty = "   ";
        Levels = targetMessage.split("/");

        /*
         * Проверяем был ли такой уровень до этого, если был, то проверяем был
         * ли он тот же
         *
         * Но - последний уровень нужно отображать всегда, поэтому для него
         * делаем исключение
         */
        for (int j = 0; j < Levels.length; j++) {
            if (j != (Levels.length - 1)) { //Это любой уровень кроме последнего
                if (path[j] != null) { //Уровень уже существует

                    if (j != 0) { //Исключение является имя самого сообщения
                        if (!path[j].equals(Levels[j])) {   //Уровень изменился
                            target = target + Levels[j].toString() + "/";
                            path[j] = Levels[j];

                            /*
                             * При изменении уровня, все послежующие должны
                             * обнуляться
                             */
                            for (int k = j + 1; k < Levels.length; k++) {
                                path[k] = null;
                            }

                        } else {  //Уровень не изменился
                            target = target + empty;
                        }
                    } else {
                        path[j] = Levels[j];
                        target = target + empty;
                    }

                } else { //Уровень не существовал ранее
                    if (j != 0) { //Исключение является имя самого сообщения
                        target = target + Levels[j].toString() + "/";
                    } else {
                        target = target + empty;

                        /*
                         * При изменении уровня, все послежующие должны
                         * обнуляться
                         */
                        for (int k = j + 1; k < Levels.length; k++) {
                            path[k] = null;
                        }
                    }
                    path[j] = Levels[j];

                }
            } else { //Это последний уровень, его всегда записываем в файл
                path[j] = Levels[j];
                target = target + Levels[j].toString() + "/";
                /*
                 * При изменении уровня, все послежующие должны
                 * обнуляться
                 */
                for (int k = j + 1; k < Levels.length; k++) {
                    path[k] = null;
                }
            }
        }
        target = target.substring(0, target.length() - 1);
        return target;
    }

    public
            String createSource(String source) throws IOException {
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
        // SourceCleaner sourceCleaner = new SourceCleaner();
        Constant constant = new Constant();
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
    public
            String createNewMessageHeader(String number, String target, int record) throws IOException {
        /**
         * Проверка, сложный ли маппинг (содержит в себе больше 1 сообщения,
         * т.е. имеет конструкцию /ns0:Messages/ns0:Message1/
         */
        int border = target.indexOf("/");
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
    public
            String deleteTechInformation(String target, String number) throws IOException {
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
